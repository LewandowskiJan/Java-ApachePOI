package excelapp.model;

import excelapp.entity.Order;
import excelapp.entity.OrderDate;
import excelapp.entity.Product;
import excelapp.converter.TextConverter;
import org.apache.poi.sl.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class OrderToReportExcel {

    public OrderToReportExcel() {

    }

    public void writeIntoExcelReport(List<Order> orderList,
                                     Sheet sheet,
                                     String folderPath,
                                     int startRow,
                                     Map<String, OrderDate> orderDateMap,
                                     Map<String, String> correctSku,
                                     Map<String, String> allBrands) {


        TextConverter nameConverter = new TextConverter();
        String sku, name, orderNum, supplier, currency;
        double price, quantityFv, quantityOrdered, orderT, realizedT, stockValue, available;

        for (Order order : orderList) {

            for (Map.Entry<Integer, Product> entry : order.getProductList().entrySet()) {

                Row row = sheet.createRow(startRow);
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

                if (allBrands.containsKey(sku)) {
                    row.createCell(9).setCellValue(allBrands.get(sku));
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
                startRow++;
            }

        }


    }

    public void writeControlVersion(List<String> listWithModificationDate, XSSFSheet sheet) {
        int i = 0;

        for (String pathWithModifiedDate : listWithModificationDate) {
            Row row = sheet.createRow(i);
            row.createCell(0).setCellValue(pathWithModifiedDate);
            i++;
        }

    }

    public void writeControlVersion(List<String> listWithModificationDate, String reportPath) {
        int i = 0;

        try {


            FileInputStream file = new FileInputStream(reportPath);
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            XSSFSheet sheet = workbook.getSheet("control");
            Row row;
            int startRow = 0;
            int lastRow = sheet.getLastRowNum();
            for (int j = startRow; j < lastRow + 1; j++) {
                row = sheet.getRow(j);
                if (row != null) {
                    sheet.removeRow(row);
                }
            }

            for (String pathWithModifiedDate : listWithModificationDate) {

                row = sheet.createRow(i);
                row.createCell(0).setCellValue(pathWithModifiedDate);
                i++;
            }

            file.close();

            String[] xxx = new String[123];

            FileOutputStream outputStream = new FileOutputStream(new File(reportPath));
            workbook.write(outputStream);
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}





