package excelapp.util;

import excelapp.converter.TextConverter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class FolderUtilsImpl implements FolderUtils {



    @Override
    public List<String> listFilesFromFolder(File folder) {

        List<String> pathList = new LinkedList<>();
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (fileEntry.isDirectory()) {
                //listFilesFromFolder(fileEntry);
            } else {
                pathList.add(fileEntry.getAbsolutePath());
            }
        }
        return pathList;
    }

    @Override
    public List<String> listFilesFromFolderWithModifiedDate(File folder) {

        TextConverter textConverter = new TextConverter();
        List<String> pathListWithModification = new LinkedList<>();
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (fileEntry.isDirectory()) {
                //listFilesFromFolder(fileEntry);
            } else {
                Date modifiedDate = new Date(fileEntry.lastModified());
                String dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(modifiedDate);
                String orderName = textConverter.convertUrlToOrderNumber(fileEntry.getAbsolutePath(), folder.getAbsolutePath());
                pathListWithModification.add(orderName + " " + dateFormat);
            }
        }
        return pathListWithModification;
    }
}
