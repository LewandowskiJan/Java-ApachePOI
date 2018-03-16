package excelapp;


import excelapp.configuration.ReportConfiguration;
import excelapp.model.OrderToReportExcel;
import excelapp.model.WorkbookToOrderMapper;
import excelapp.entity.Order;
import excelapp.entity.OrderDate;
import excelapp.converter.CsvReader;
import excelapp.converter.TextConverter;
import excelapp.util.FolderUtils;
import excelapp.util.FolderUtilsImpl;
import excelapp.util.WorkbookHeader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public class Main {


    public static void main(String[] args) throws InvalidFormatException, IOException {

        /*
        Reading + coping from excels to  list of Orders
        */

        String password;
        String configurationPath;
        String csvFilePath;
        String csvCorrectSku;
        String reportPath;
        String brandsPath;

        String[] paths = ReportConfiguration.getPaths();

        configurationPath = paths[0];
        password = paths[1];
        csvFilePath = paths[2];
        csvCorrectSku = paths[3];
        reportPath = paths[4];
        brandsPath = paths[5];


        CsvReader csvReader = new CsvReader();


        Map<String, OrderDate> orderDateMap = csvReader.getOrderDateFromCsvFile(csvFilePath);
        //System.out.println(orderDateMap.size());

        TextConverter textConverter = new TextConverter();

        //absolute path to the folder
        File folder = new File(configurationPath);

        // list of file absolute paths
        FolderUtils folderUtils = new FolderUtilsImpl();


        List<String> list = folderUtils.listFilesFromFolder(folder);

        List<String> listWithModificationDate = folderUtils.listFilesFromFolderWithModifiedDate(folder);
        /*
        System.out.println("Path list:");
        for (String path : list) {
            System.out.print(textConverter.convertUrlToOrderNumber(path, configurationPath) + "*");
        }
        */

        // ConsoleImg.drawReportPicture();

        System.out.println("\n-------------GENERATING------------- \n");

        // open excel files and copy data to Order List
        //  System.out.print("\nCoping data from orders ");
        WorkbookToOrderMapper workbookDao = new WorkbookToOrderMapper();
        //  System.out.println("");
        List<Order> orderList = workbookDao.getOrder(list, password);


        //Create Blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        //Create file system using specific name
        //  System.out.println("\nCreating report XLSX file \n");


        FileOutputStream out = new FileOutputStream(reportPath);

        //Create sheet
        //  System.out.println("Creating sheet \n");
        Sheet sheet = workbook.createSheet("Report");

        /*
        Writing form list of Orders to one excel file
        */

        // Adding headers
        WorkbookHeader workbookHeader = new WorkbookHeader();
        workbookHeader.createHeaderRow(workbook, sheet);

        //  System.out.println("Adding headers \n");

        Map<String, String> correctSkuMap = csvReader.getCorrectSku(csvCorrectSku);

        Map<String, String> allBrandsMap = csvReader.getNameOfBrand(brandsPath);

        // Write into excel
        OrderToReportExcel orderUtils = new OrderToReportExcel();
        //  System.out.println("Pasting data into Order report.xlsm file \n");
        orderUtils.writeIntoExcelReport(orderList, sheet, configurationPath, 1, orderDateMap, correctSkuMap, allBrandsMap);

        XSSFSheet controlSheet = workbook.createSheet("control");

        orderUtils.writeControlVersion(listWithModificationDate, controlSheet);

        // PivotTable pivotTable = new PivotTable();
        // pivotTable.createPivotTable(workbook);

        //write operation workbook using file out object
        workbook.write(out);
        out.close();
        System.out.println("Order report.xlsx written successfully");


    }

}

