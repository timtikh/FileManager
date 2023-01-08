import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// class to clean main from logic code)
public class Logic {

    private static List<File> fileNames = new ArrayList<File>();
    private static List<RequireFileClass> requireFileNames = new ArrayList<RequireFileClass>();
    public static String baseDirString;

    /**
     * class to read all !txt! files in base dir and print readable to console
     * 
     * @param baseDirFile - getting base dir in JavaFile to read all files in it
     */
    public static void readFiles(File baseDirFile) {
        for (File i : baseDirFile.listFiles()) {
            if (i.isDirectory()) {
                readFiles(i);
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

    // to start ny class i need to translate from file to RequireFileClass
    public static void initRequireFileNames() {
        for (File i : fileNames) {
            requireFileNames.add(new RequireFileClass(i));
        }
    }

    // get file extension (made for marking txt files)
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

    // method to init cycle checking in RequireFileClass
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

    /**
     * method to generate sorted list of files in terms of their dependencies
     * 
     * @return sorted list of files of RequireFileClass type
     */
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

    /**
     * method to print sorted list of files in console
     * 
     * @param fileList - list of files to print in
     */
    public static void printFileList(List<RequireFileClass> fileList) {
        System.out.println("\nSorted list of files:\n");
        for (var i : fileList) {
            System.out.println(i.getName().replace('\\', '/'));
        }
    }

    /**
     * method to write sorted list of files to output.txt
     * 
     * @param requireSortedList - list of files to write by one to output.txt
     * 
     */
    public static void writeResultToFile() {
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
