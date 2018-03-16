package excelapp.model;

import excelapp.entity.Order;
import excelapp.entity.Product;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class WorkbookToOrderMapper {


    private static final int MY_MINIMUM_COLUMN_COUNT = 10;
    private Integer count = 0;

    public List<Order> getOrder(List<String> pathList, String password) throws InvalidFormatException {


        List<Order> orderList = new LinkedList<>();
        List<String> errorMesagesList = new LinkedList<>();


        Workbook workbook;

        Double number = 1.0;

        int pathIndex = 0;
        long startTime = System.currentTimeMillis();

        for (String path : pathList) {

            Map<Integer, Product> mapOfProduct = new TreeMap<>();
            Order order = new Order();
            String currency;


            try {
                workbook = WorkbookFactory.create(new File(path), password, true);

                // Decide which rows to process
                XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
                CellReference cellReference = new CellReference("B1");
                XSSFRow row = sheet.getRow(cellReference.getRow());
                XSSFCell supplierNameCell = row.getCell(cellReference.getCol());

                order.setOrderNum(pathList.get(pathIndex));

                order.setSupplier(supplierNameCell.getStringCellValue());


                currency = getCurrencyFromFormat(sheet);

                order.setCurrency(currency);

                //System.out.println(currency.getDataFormatString());
                //System.out.println(currencyCell.getNumericCellValue());
                //order.setCurrency(String.valueOf(currencyCell.getNumericCellValue()));
                //System.out.println(order);

                mapOfProduct = mappedXlsxToProduct(mapOfProduct, sheet);

                order.setProductList(mapOfProduct);
                orderList.add(order);

                System.out.printf("\rProcessed: %.0f%%", number / pathList.size() * 100);

                number++;
            } catch (IOException e) {

                String errorMessage = "File was added to archive: " + path;
                errorMesagesList.add(errorMessage);
            }
            count = 0;
            pathIndex++;
        }

        System.out.println("");
        long endTime = System.currentTimeMillis() - startTime;
        System.out.println("Completion time in sec: "
                + TimeUnit.MILLISECONDS.toSeconds(endTime));

        for (String errorMessage : errorMesagesList){
            System.out.println(errorMessage);
        }

        System.out.println("Coping success!");

        return orderList;

    }

    private String getCurrencyFromFormat(XSSFSheet sheet) {
        CellReference cellReference;
        XSSFRow row;
        cellReference = new CellReference("C5");
        row = sheet.getRow(cellReference.getRow());
        XSSFCell currencyCell = row.getCell(cellReference.getCol());

        XSSFCellStyle currency = currencyCell.getCellStyle();
        String currencyFormat = currency.getDataFormatString();

        String currencySign;

        if (currencyFormat.contains("$PLN") || currencyFormat.contains("$zł") || currencyFormat.contains("zł")) {
            currencySign = "PLN";
        } else if (currencyFormat.contains("$EUR") || currencyFormat.contains("$€") || currencyFormat.contains("€")) {
            currencySign = "EUR";
        } else if (currencyFormat.contains("$GBP") || currencyFormat.contains("$£") || currencyFormat.contains("£")) {
            currencySign = "GBP";
        } else if (currencyFormat.contains("$$") || currencyFormat.contains("$USD")) {
            currencySign = "USD";
        } else if (currencyFormat.contains("$kr-sv-SE") || currencyFormat.contains("$SEK")) {
            currencySign = "SEK";
        } else {
            currencySign = "NONE";
        }

        return currencySign;
    }


    private Map<Integer, Product> mappedXlsxToProduct(Map<Integer, Product> mapOfProduct, Sheet sheet) {

        int rowNumber = 4;


        while (sheet.getRow(rowNumber) != null) {
            Row currentRow = sheet.getRow(rowNumber);

            List<String> cellContent = new ArrayList<>(10);
            for (int cn = 0; cn < MY_MINIMUM_COLUMN_COUNT; cn++) {

                Cell c = currentRow.getCell(cn, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

                //Cell c = currentRow.getCell(cn);
                if (c == null) {
                    cellContent.add("0.0");
                } else {
                    if (c.getCellTypeEnum() == CellType.NUMERIC) {
                        cellContent.add(String.valueOf(c.getNumericCellValue()));
                    }
                    if (c.getCellTypeEnum() == CellType.STRING) {
                        cellContent.add(c.getStringCellValue());
                    }
                    if (c.getCellTypeEnum() == CellType.FORMULA) {
                        cellContent.add(String.valueOf(c.getNumericCellValue()));
                    }
                }
            }

            Product product = createProductFromExcellCell(cellContent);

            if (!cellContent.get(0).equals("0.0") && !cellContent.get(1).equals("0.0")) {
                mapOfProduct.put(count, product);
                count++;
            }
//            stringList.clear();
            rowNumber++;
        }

        return mapOfProduct;
    }

    private Product createProductFromExcellCell(List<String> cellContent) {
        Product product = new Product();

        product.setSku(String.valueOf(cellContent.get(0)));
        product.setName(String.valueOf(cellContent.get(1)));
        product.setPrice(Double.parseDouble(cellContent.get(2)));
        product.setQuantityFv(Double.valueOf(cellContent.get(3)));
        product.setQuantityOrdered(Double.valueOf(cellContent.get(4)));
        product.setAvailable(Double.valueOf(cellContent.get(5)));
        product.setStock(Double.valueOf(cellContent.get(6)));
        product.setOrderT(Double.valueOf(cellContent.get(8)));
        product.setRealizedT(Double.valueOf(cellContent.get(9)));

        return product;
    }


    // ------------------------------
    /*
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

        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
*/
}



