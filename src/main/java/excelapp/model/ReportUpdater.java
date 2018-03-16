package excelapp.model;

import excelapp.entity.Order;
import excelapp.entity.OrderDate;
import excelapp.entity.Product;
import excelapp.converter.TextConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ReportUpdater {

    // ------------------------------

    public List<String> getListOfOrderNameWithDateOfModified(String path) {

        List<String> listOfOrderWithDateOfModified = new LinkedList<>();
        Workbook workbook;

        try {
            workbook = WorkbookFactory.create(new File(path));
            XSSFSheet sheet = (XSSFSheet) workbook.getSheet("control");


            for (Row row : sheet) {
                for (Cell cell : row) {
                    listOfOrderWithDateOfModified.add(cell.getStringCellValue());
                }
            }

        } catch (InvalidFormatException | IOException e) {
            e.printStackTrace();
        }


        return listOfOrderWithDateOfModified;
    }

// ------------------------------


    public List<Integer> getDeleteNotActualRowList(String path, List<String> deleteList) {


        Workbook workbook;
        List<Integer> deleteRowList = new ArrayList<>();

        try {

            workbook = WorkbookFactory.create(new File(path));
            XSSFSheet sheet = (XSSFSheet) workbook.getSheet("Report");


            for (Row row : sheet) {
                if (deleteList.contains(row.getCell(8).getStringCellValue())) {
                    deleteRowList.add(row.getRowNum());
                }
            }


        } catch (InvalidFormatException | IOException e) {
            e.printStackTrace();
        }
        return deleteRowList;
    }

    public void deleteNotActualRow(List<Integer> deleteList, String reportPath) {

        try {
            FileInputStream file = new FileInputStream(reportPath);

            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheet("Report");


            for (Integer rowNum : deleteList) {
                Row row = sheet.getRow(rowNum);
                sheet.removeRow(row);
            }
            int lastRow = sheet.getLastRowNum();

            for (int i = 1; i < lastRow; i++) {
                if (isEmpty(sheet.getRow(i))) {
                    sheet.shiftRows(i + 1, lastRow, -1);
                    i--;//Adjusts the sweep in accordance to a row removal
                    lastRow = lastRow - 1;
                }
            }

            file.close();

            FileOutputStream outputStream = new FileOutputStream(new File(reportPath));
            workbook.write(outputStream);
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isEmpty(Row row) {

        if (row == null) {
            return true;
        }
        if (row.getLastCellNum() <= 0) {
            return true;
        }
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellTypeEnum() != CellType.BLANK && StringUtils.isNotBlank(cell.toString())) {
                return false;
            }
        }
        return true;

    }

    public void addActualRowIntoExcelReport(List<Order> orderList,
                                            String folderPath,
                                            Map<String, String> correctSku,
                                            Map<String, OrderDate> orderDateMap,
                                            Map<String, String> allBarands,
                                            String reportPath) {


        try {

            FileInputStream file = new FileInputStream(reportPath);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheet("Report");


            int lastRow = sheet.getLastRowNum();

            TextConverter nameConverter = new TextConverter();
            String sku, name, orderNum, supplier, currency;
            double price, quantityFv, quantityOrdered, orderT, realizedT, stockValue, available;

            for (Order order : orderList) {

                for (Map.Entry<Integer, Product> entry : order.getProductList().entrySet()) {

                    Row row = sheet.createRow(lastRow+1);

                    sku = nameConverter.convertSku(entry.getValue().getSku());

                    if (correctSku.get(sku) != null) {
                        sku = correctSku.get(sku);
                    }
                    name = entry.getValue().getName();
                    price = entry.getValue().getPrice();
                    currency = order.getCurrency();
                    quantityFv = entry.getValue().getQuantityFv();
                    quantityOrdered = entry.getValue().getQuantityOrdered();
                    orderT = entry.getValue().getOrderT();
                    realizedT = entry.getValue().getRealizedT();

                    stockValue = entry.getValue().getStock();
                    available = entry.getValue().getAvailable();

                    String orderName = order.getOrderNum();
                    orderNum = nameConverter.convertUrlToOrderNumber(orderName, folderPath);
                    supplier = order.getSupplier();

                    row.createCell(0).setCellValue(sku);
                    row.createCell(1).setCellValue(name);
                    row.createCell(2).setCellValue(price);
                    row.createCell(3).setCellValue(quantityFv);
                    row.createCell(4).setCellValue(quantityOrdered);
                    row.createCell(5).setCellValue(supplier);

                    if (orderNum.contains("FV")) {
                        row.createCell(6).setCellValue(realizedT);
                        row.createCell(7).setCellValue(available);
                    } else {
                        row.createCell(6).setCellValue(orderT);
                        row.createCell(7).setCellValue(stockValue);
                    }

                    row.createCell(8).setCellValue(orderNum);

                    if (allBarands.containsKey(sku)) {
                        row.createCell(9).setCellValue(allBarands.get(sku));
                    }

                    row.createCell(10).setCellValue(currency);


                    orderNum = orderNum.replace("FV_", "");

                    if (orderDateMap.containsKey(orderNum)) {
                        String realizationDate = orderDateMap.get(orderNum).getDateOfRealization();
                        row.createCell(12).setCellValue(realizationDate.replace("\"", ""));
                    } else {
                        row.createCell(12).setCellValue("");
                    }

                    orderNum = orderNum.replace("ZD", "");
                    row.createCell(11).setCellValue(Integer.valueOf(orderNum));
                    lastRow++;
                }

            }


            file.close();

            FileOutputStream outputStream = new FileOutputStream(new File(reportPath));
            workbook.write(outputStream);
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
