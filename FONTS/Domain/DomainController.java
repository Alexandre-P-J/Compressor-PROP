package Domain;

import Presentation.PresentationController;
import Persistence.PersistenceController;
import java.nio.file.Paths;

public class DomainController {
    // Singleton instance
    private static final DomainController instance = new DomainController();
    private static Folder FileTree = new Folder(".", null);

    // Private to avoid external use of the constructor
    private DomainController() {}

    // Singleton getter
    public static DomainController getInstance() {
        return instance;
    }

    public static void openCompressed(String absPath) {}

    public static void openNotCompressed(String absPath) {}

    public static String[] getFilenames(String absPath) {
        String steps[] = Paths.get(absPath).toString().split("\\.");
        Folder aux = FileTree;
        for (int i = 0; i < steps.length; ++i) {
            Folder[] tmp = aux.getFolders();
            boolean found = false;
            for (int j = 0; j < tmp.length; ++j) {
                if (tmp[j].getName() == steps[i]) {
                    aux = tmp[j];
                    found = true;
                    break;
                }
            }
            if (!found)
                return null;
        }
        Folder[] tmp = aux.getFolders();
        String[] result = new String[tmp.length];
        for (int i = 0; i < tmp.length; ++i)
            result[i] = tmp[i].getName();
        return result;
    }

    public static void compress(String OutputFilePath) {}

    public static void decompress(String OutputFolderPath) {}
    
}