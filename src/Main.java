import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to the file manager!");
        // Here is the variable to put the base directory
        final String baseDirString = "D:/codeprojects/FileManager";
        // Here is the variable to put the base directory
        Logic.baseDirString = baseDirString;

        File baseDirFile = new File(baseDirString);
        if (!baseDirFile.exists()) {
            System.out.println("The base directory does not exist. Exiting...");
            System.exit(1);
        } else {
            System.out.println("The base directory exists.");
            System.out.println("Files found in base auditory:");
            Logic.readFiles(baseDirFile);
            Logic.initRequireFileNames();

            // cycle checking
            if (Logic.checkIfBaseDirCyclic()) {
                System.out.println("The base directory is cyclic. Exiting...");
                System.exit(1);
            }

            Logic.printFileList(Logic.generateRequireSortedList());
            Logic.writeResultToFile();
            System.out.println("\nThe final text will be in file with name: " + baseDirString + "/output.txt");
        }
    }
}