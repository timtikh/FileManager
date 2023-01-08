import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RequireFileClass implements Comparable {
    private String name = "";
    private File file;
    private int requireCount = -1;
    private boolean isCycle = true;
    private String canonicalPath = "";

    public RequireFileClass(File file) {
        String baseDir = Logic.baseDirString;
        this.file = file;
        this.canonicalPath = file.getAbsolutePath();
        this.name = canonicalPath.substring(baseDir.length() + 1);
    }

    // standard getters, setters and toString

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getRequireCount() {
        return requireCount;
    }

    public void setRequireCount(int requireCount) {
        this.requireCount = requireCount;
    }

    public boolean getIsCycle() {
        return isCycle;
    }

    public void setIsCycle(boolean isCycle) {
        this.isCycle = isCycle;
    }

    public String getCanonicalPath() {
        return canonicalPath;
    }

    @Override
    public boolean equals(Object obj) {
        return ((RequireFileClass) obj).getName().equals(getName());
    }

    @Override
    public int compareTo(Object o) {
        RequireFileClass e = (RequireFileClass) o;
        try {
            if (this.getRequiredFilesList().contains(e)) {
                return 1;
            } else if (e.getRequiredFilesList().contains(this)) {
                return -1;
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return this.getRequireCount() - e.getRequireCount();
    }

    // comparator for class
    public static Comparator<RequireFileClass> RequireFileClassComparator = new Comparator<RequireFileClass>() {
        @Override
        public int compare(RequireFileClass e1, RequireFileClass e2) {
            return e1.compareTo(e2);
        }
    };

    public boolean checkIfFileCyclic(List<RequireFileClass> requires) {
        for (RequireFileClass i : requires) {
            if (this.equals(i)) {
                System.out.println("Cyclic file: " + this.getName());
                this.setIsCycle(true);
                return true;
            }
        }
        var inRequires = new ArrayList<RequireFileClass>();
        for (RequireFileClass i : requires) {
            try {
                inRequires.addAll(i.getRequiredFilesList());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public List<RequireFileClass> getRequiredFilesList() throws IOException {
        // print plain string

        String expected_value = "require ‘";
        List<RequireFileClass> requiredFiles = new ArrayList<RequireFileClass>();
        Path path = Paths.get(this.getCanonicalPath());
        BufferedReader reader = Files.newBufferedReader(path);
        String line = null;

        int count = 0;
        while ((line = reader.readLine()) != null) {
            if (line.contains(expected_value)) {
                String requiredFileName = line.substring(line.indexOf(expected_value) + expected_value.length(),
                        line.indexOf("’"));
                requiredFileName += ".txt";
                requiredFileName = requiredFileName.replace('/', '\\');
                for (RequireFileClass i : Logic.getRequireFileNames()) {
                    if (i.getName().equals(requiredFileName)) {
                        requiredFiles.add(i);
                        count++;
                    }
                }
            }
        }
        var oldRequiredFiles = new ArrayList<RequireFileClass>();
        oldRequiredFiles.addAll(requiredFiles);
        for (RequireFileClass i : requiredFiles) {
            oldRequiredFiles.addAll(i.getRequiredFilesList(requiredFiles));
        }

        this.setRequireCount(count);
        reader.close();
        return oldRequiredFiles;
    }

    public List<RequireFileClass> getRequiredFilesList(List<RequireFileClass> requires) throws IOException {
        // print plain string

        String expected_value = "require ‘";
        List<RequireFileClass> requiredFiles = new ArrayList<RequireFileClass>();
        Path path = Paths.get(this.getCanonicalPath());
        BufferedReader reader = Files.newBufferedReader(path);
        String line = null;

        int count = 0;
        while ((line = reader.readLine()) != null) {
            if (line.contains(expected_value)) {
                String requiredFileName = line.substring(line.indexOf(expected_value) + expected_value.length(),
                        line.indexOf("’"));
                requiredFileName += ".txt";
                requiredFileName = requiredFileName.replace('/', '\\');
                for (RequireFileClass i : Logic.getRequireFileNames()) {
                    if (i.getName().equals(requiredFileName)) {
                        requiredFiles.add(i);
                        count++;
                    }
                }
            }
        }
        this.setRequireCount(count);
        reader.close();
        return requiredFiles;
    }

    public static void CountFileRequire() throws IOException {
        // print plain string
        System.out.println("\n");
        for (File i : Logic.getFileNames()) {
            String expected_value = "require";

            Path path = Paths.get(i.getAbsolutePath());
            BufferedReader reader = Files.newBufferedReader(path);
            var fileStrings = reader.lines();
            int countFileRequire = 0;
            for (String line : (Iterable<String>) fileStrings::iterator) {
                if (line.contains(expected_value)) {
                    countFileRequire++;
                }
            }
            reader.close();
            System.out.println(i.getName() + " has " + countFileRequire + " require(s)");

        }

    }

}
