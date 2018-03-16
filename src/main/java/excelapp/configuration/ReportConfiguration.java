package excelapp.configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReportConfiguration {

    public static String[] getPaths() throws IOException {


        String password;
        String configurationPath;
        String csvFilePath;
        String csvCorrectSku;
        String reportPath;
        String brandsPath;

        String[] arrayOfPaths = new String[10];
        /*
        try {
        */
        BufferedReader bufferedReader = new BufferedReader(new FileReader("configuration\\conf.txt"));
        configurationPath = bufferedReader.readLine().trim();
        password = bufferedReader.readLine().trim();
        csvFilePath = bufferedReader.readLine().trim();
        csvCorrectSku = bufferedReader.readLine().trim();
        reportPath = bufferedReader.readLine().trim();
        brandsPath = bufferedReader.readLine().trim();
        bufferedReader.close();
            /*
        } catch (IOException e) {
            System.out.println("Nie znaleziono sciezki do konfiguracji");
            return;
        }

        */
        configurationPath = configurationPath.substring(configurationPath.indexOf('=') + 1);
        //System.out.println(configurationPath);
        password = password.substring(password.indexOf('=') + 1);
        //System.out.println(password);
        csvFilePath = csvFilePath.substring(csvFilePath.indexOf('=') + 1);
        //System.out.println(csvFilePath);
        csvCorrectSku = csvCorrectSku.substring(csvCorrectSku.indexOf('=') + 1);
        //System.out.println(csvCorrectSku);
        reportPath = reportPath.substring(reportPath.indexOf('=') + 1);

        brandsPath = brandsPath.substring(brandsPath.indexOf('=') + 1);

        arrayOfPaths[0] = configurationPath;
        arrayOfPaths[1] = password;
        arrayOfPaths[2] = csvFilePath;
        arrayOfPaths[3] = csvCorrectSku;
        arrayOfPaths[4] = reportPath;
        arrayOfPaths[5] = brandsPath;

        return arrayOfPaths;
    }

}
