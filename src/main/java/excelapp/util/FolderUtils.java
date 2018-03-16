package excelapp.util;

import java.io.File;
import java.util.List;

public interface FolderUtils {

   List<String> listFilesFromFolder(File folderPath);
   List<String> listFilesFromFolderWithModifiedDate(File folderPath);
}
