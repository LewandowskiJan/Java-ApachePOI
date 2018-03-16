package excelapp.converter;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TextConverter {


    public TextConverter() {
    }

    public String convertUrlToOrderNumber(String absolutePath, String folderPath) {

        String orderNumber;
        orderNumber = absolutePath.replace(folderPath, "");
        orderNumber = orderNumber.replace(".xlsm", "");
        orderNumber = orderNumber.replace("\\", "");

        return orderNumber;
    }

    public String convertSku(String sku) {
        sku = sku.replace(".0", "");
        String newSku;
        StringBuilder skuBuilder = new StringBuilder("0000000000" + sku);

            newSku = skuBuilder.substring(skuBuilder.length() - 10);


        return newSku;
    }

    public String convertZdName(String zdFullName) {
        String convertedZdName;
        int beginingIndex = 0;
        int endIndex = zdFullName.indexOf("/");

        convertedZdName = zdFullName.substring(beginingIndex, endIndex);
        convertedZdName = convertedZdName.replace(" ", "");
        convertedZdName = convertedZdName.replace("\"", "");

        return convertedZdName;
    }

    public Date convertStringToDate(String dateAsString) {

        dateAsString = dateAsString.substring(1, dateAsString.length() - 1);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");

        try {

            //System.out.println(date);
            //System.out.println(formatter.format(date));

            return formatter.parse(dateAsString);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return null;
    }

    public String convertFromOrderNameToPath(String orderName, String folderPath){

        String path;

        path = folderPath + orderName + ".xlsm" ;

        return path;

    }


}
