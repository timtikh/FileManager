import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Logic {

    private static List<File> fileNames = new ArrayList<File>();
    private static List<RequireFileClass> requireFileNames = new ArrayList<RequireFileClass>();
    final public static String baseDirString = "D:/codeprojects/FileManager";

    public static void ReadFiles(File baseDirFile) {

        for (File i : baseDirFile.listFiles()) {
            if (i.isDirectory()) {
                ReadFiles(i);
            } else {
                if (getFileExtension(i.getName()).equals("txt")) {
                    String parentDir = baseDirFile.getParentFile().getName();

                    if (parentDir.equals("FileManager")) {
                        System.out.println(baseDirFile.getName() + "/" + i.getName());
                    } else {
                        System.out.println(
                                baseDirFile.getParentFile().getName() + "/" + baseDirFile.getName() + "/"
                                        + i.getName());
                    }
                    fileNames.add(i);
                }
            }
        }

    }

    public static void initRequireFileNames() {
        for (File i : fileNames) {
            requireFileNames.add(new RequireFileClass(i));
        }
    }

    public static String getFileExtension(String fullName) {
        if (fullName == null)
            throw new IllegalArgumentException("fileName cannot be null");
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    // get fileNames list
    public static List<File> getFileNames() {
        return fileNames;
    }

    // get requireFileNames list
    public static List<RequireFileClass> getRequireFileNames() {
        return requireFileNames;
    }

    public static boolean checkIfBaseDirCyclic() {
        // check if the file has a cycle
        // if it has a cycle, set the isCycle variable to true and return true
        // else set the isCycle variable to false and continue

        for (RequireFileClass i : requireFileNames) {
            try {
                if (i.checkIfFileCyclic(i.getRequiredFilesList())) {
                    i.setIsCycle(true);
                    return true;
                } else {
                    i.setIsCycle(false);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return false;
    }

    public static void AnalyzeScheme() {
        // analyze the file names and file extensions
        // and store the file names in a list
        // and store the file extensions in a list
        // and store the file paths in a list
    }

    public static void WriteOutput() {
        // write the output to a file
    }

}
