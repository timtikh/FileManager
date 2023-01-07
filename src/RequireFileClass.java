import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class RequireFileClass implements Comparable {
    private String name = "";
    private File file;
    private int requireCount = -1;
    private boolean isCycle = true;
    private String canonicalPath = "";

    public RequireFileClass(File file) {
        this.file = file;
        this.name = file.getName();
        this.canonicalPath = file.getAbsolutePath();
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
        return this.getRequireCount() - e.getRequireCount();
    }

    // comparator for class
    public static Comparator<RequireFileClass> RequireFileClassComparator = new Comparator<RequireFileClass>() {
        @Override
        public int compare(RequireFileClass e1, RequireFileClass e2) {
            return e1.compareTo(e2);
        }
    };

    public static void CheckFileRequireCycle() {
        // check if the file requires a cycle
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
