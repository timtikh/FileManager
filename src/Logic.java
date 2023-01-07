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

    public static void CountFileRequire() throws IOException {
        // print plain string
        System.out.println("\n");
        for (File i : fileNames) {
            String expected_value = "require";

            Path path = Paths.get(i.getAbsolutePath());
            BufferedReader reader = Files.newBufferedReader(path);
            String line = null;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                if (line.contains(expected_value)) {
                    count++;
                }
            }
            System.out.println(i.getName() + " has " + count + " require statements.");
        }
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
