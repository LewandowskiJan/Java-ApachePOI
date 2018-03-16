package excelapp.converter;

import excelapp.entity.OrderDate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;


public class CsvReader {

    public Map<String, String> getNameOfBrand(String path) {

        Map<String, String> nameOfBrands = new TreeMap<>();

        TextConverter textConverter = new TextConverter();

        String line;
        String csvSplitBy = ";";

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            br.readLine(); //headers
            br.readLine(); //empty field
            while ((line = br.readLine()) != null) {
                String[] orderLine = line.split(csvSplitBy);

                if (orderLine.length > 6) {
                    String sku;
                    String brand;

                    sku = orderLine[0].replace("\"", "");
                    if (orderLine[0].contains("LC")) {
                        sku = textConverter.convertSku(sku);
                    }

                    brand = orderLine[5].replace("\"", "");

                    nameOfBrands.put(sku, brand);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return nameOfBrands;
    }

    public Map<String, OrderDate> getOrderDateFromCsvFile(String path) {

        Map<String, OrderDate> orderDateMap = new TreeMap<>();

        String line;
        String csvSplitBy = ";";

        TextConverter textConverter = new TextConverter();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            br.readLine(); //headers
            br.readLine(); //empty field
            while ((line = br.readLine()) != null) {
                String[] orderLine = line.split(csvSplitBy);

                if (orderLine.length > 8) {

                    if (!orderLine[1].contains("/M03/") || !orderLine[1].contains("/BIG/")) {
                        if (!orderDateMap.containsKey(textConverter.convertZdName(orderLine[1]))) {
                            OrderDate orderDate = new OrderDate();

                            orderDate.setDateOfRealization(orderLine[8]);
                            orderDate.setSupplierName(orderLine[2]);
                            orderDate.setZdName(textConverter.convertZdName(orderLine[1]));
                            String orderNum = textConverter.convertZdName(orderLine[1]);

                            orderDateMap.put(orderNum, orderDate);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return orderDateMap;
    }

    public Map<String, String> getCorrectSku(String path) {

        Map<String, String> correctSku = new TreeMap<>();

        String line;
        String csvSplitBy = ";";

        TextConverter textConverter = new TextConverter();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            br.readLine(); //headers
            while ((line = br.readLine()) != null) {
                String[] orderLine = line.split(csvSplitBy);

                correctSku.put(orderLine[0], orderLine[1]);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return correctSku;
    }


    public boolean isCorrectOrderNumber(String[] orderLine) {


        return false;
    }

}
