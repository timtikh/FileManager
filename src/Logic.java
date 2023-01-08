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

    // Here is the variable to put the base directory
    final public static String baseDirString = "D:/codeprojects/FileManager";
    // Here is the variable to put the base directory

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
        boolean isCycle = false;
        for (RequireFileClass i : requireFileNames) {
            try {
                if (i.checkIfFileCyclic(i.getRequiredFilesList())) {
                    i.setIsCycle(true);
                    isCycle = true;
                } else {
                    i.setIsCycle(false);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return isCycle;
    }

    public static List<RequireFileClass> generateRequireSortedList() {
        var requireSortedList = new ArrayList<RequireFileClass>();
        for (RequireFileClass i : requireFileNames) {
            if (!i.getIsCycle()) {
                requireSortedList.add(i);
            }
        }
        requireSortedList.sort(RequireFileClass::compareTo);
        return requireSortedList;
    }

    public static void printFileList(List<RequireFileClass> fileList) {
        System.out.println("\nSorted list of files:\n");
        for (var i : fileList) {
            System.out.println(i.getName().replace('\\', '/'));
        }
    }

    public static void WriteResultToFile() {
        var requireSortedList = generateRequireSortedList();
        var path = Paths.get(baseDirString + "/output.txt");
        try (var writer = Files.newBufferedWriter(path)) {
            for (var i : requireSortedList) {
                // write from file i to output.txt
                var path2 = Paths.get(baseDirString + "/" + i.getName());
                var reader = Files.newBufferedReader(path2);
                String line = null;
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.newLine();
                }
                writer.newLine();
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
