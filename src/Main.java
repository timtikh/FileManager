import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to the file manager!");
        // make a global variable to store the current directory
        final String baseDirString = "D:/codeprojects/FileManager"; // or whatever the default is

        File baseDirFile = new File(baseDirString);
        if (!baseDirFile.exists()) {
            System.out.println("The base directory does not exist. Exiting...");
            System.exit(1);
        } else {
            System.out.println("The base directory exists.");

            Logic.ReadFiles(baseDirFile);
            Logic.initRequireFileNames();
            if (Logic.checkIfBaseDirCyclic()) {
                System.out.println("The base directory is cyclic. Exiting...");
                System.exit(1);
            }
            // Logic.CountFileRequire();
            // Logic.CheckFileRequireCycle();
            // Logic.AnalyzeScheme();
            // Logic.WriteOutput();
            System.out.println("The final text will be in file with name: " + baseDirString + "/output.txt");
        }
    }
}