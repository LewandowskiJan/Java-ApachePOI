package excelapp;

import excelapp.configuration.ReportConfiguration;
import excelapp.model.OrderToReportExcel;
import excelapp.model.ReportUpdater;
import excelapp.model.WorkbookToOrderMapper;
import excelapp.entity.Order;
import excelapp.entity.OrderDate;
import excelapp.converter.CsvReader;
import excelapp.converter.TextConverter;
import excelapp.util.FolderUtilsImpl;
import excelapp.util.WorkbookHeader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.util.ZipSecureFile;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Update {

    public static void main(String[] args) throws InvalidFormatException {


        String configurationPath;
        String password;
        String csvFilePath;
        String csvCorrectSku;
        String reportPath;

        String brandsPath;


        try {
            String[] paths = ReportConfiguration.getPaths();
            configurationPath = paths[0];
            password = paths[1];
            csvFilePath = paths[2];
            csvCorrectSku = paths[3];
            reportPath = paths[4];
            brandsPath = paths[5];
        } catch (IOException e) {
            System.out.println("Nie znaleziono sciezki do konfiguracji");
            return;
        }

        //Taking all order name with date of modified from report file

        ZipSecureFile.setMinInflateRatio(0.0);
        ReportUpdater reportUpdater = new ReportUpdater();
        WorkbookToOrderMapper workbookToOrderMapper = new WorkbookToOrderMapper();

        List<String> listOfOrderNameWithDateOfModifiedBefore = reportUpdater.getListOfOrderNameWithDateOfModified(reportPath);


        //Taking actual order name with date of modified from dictionary

        FolderUtilsImpl folderUtils = new FolderUtilsImpl();

        File folder = new File(configurationPath);

        List<String> listOfOrderNameWithDateOfModifiedActual = folderUtils.listFilesFromFolderWithModifiedDate(folder);


        // Equated two Lists of order name and date of modification
        // There are 3 options - add / delete / do nothing

        WorkbookHeader workbookHeader = new WorkbookHeader();

        List<String> updateList = workbookHeader.getUpdateList(listOfOrderNameWithDateOfModifiedBefore, listOfOrderNameWithDateOfModifiedActual);

        TextConverter textConverter = new TextConverter();
        List<String> pathList = new LinkedList<>();
        for (String orderName : updateList) {
            String path = textConverter.convertFromOrderNameToPath(orderName, configurationPath);
            pathList.add(path);
        }

        //delete from excel file this Row with order name
        // todo
        List<Integer> deleteList = reportUpdater.getDeleteNotActualRowList(reportPath, updateList);

        reportUpdater.deleteNotActualRow(deleteList, reportPath);

        CsvReader csvReader = new CsvReader();

        Map<String, OrderDate> orderDateMap = csvReader.getOrderDateFromCsvFile(csvFilePath);
        Map<String, String> correctSkuMap = csvReader.getCorrectSku(csvCorrectSku);
        Map<String, String> allBrandsMap = csvReader.getNameOfBrand(brandsPath);
        List<Order> orderList = workbookToOrderMapper.getOrder(pathList, password);

        OrderToReportExcel orderToReportExcel = new OrderToReportExcel();

        reportUpdater.addActualRowIntoExcelReport(orderList, configurationPath, correctSkuMap, orderDateMap, allBrandsMap, reportPath);

        orderToReportExcel.writeControlVersion(listOfOrderNameWithDateOfModifiedActual, reportPath);


        //open excel and copy to report file
        // todo


    }


}
