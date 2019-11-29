package Domain;

import Presentation.PresentationController;
import Persistence.PersistenceController;
import java.io.IOException;
import java.io.OutputStream;

public class DomainController {
    // Singleton instance
    private static final DomainController instance = new DomainController();

    // Private to avoid external use of the constructor
    private DomainController() {}

    // Singleton getter
    public static DomainController getInstance() {
        return instance;
    }
    
    // lo llama presentation controller con el path obtenido despues de pulsar SELECT
    public static void readFileTree(String path) throws IOException {
        PersistenceController.readFileTree(path);
    }

    /**
     * Return filenames from the given relative path
     * @param pathToParentFolder either "/" or "foo/bar.." (replacing ".." with the rest of the path)
     * @return an array of filenames contained in the folder with path equal to path argument
     * @throws Exception if path is invalid or filetree not initialized
     */
    public static String[] getFileNames(String pathToParentFolder) throws Exception {
        return PersistenceController.getFileNames(pathToParentFolder);
    }

    /**
     * Return folder names from the given relative path
     * @param pathToParentFolder either "/" or "foo/bar.." (replacing ".." with the rest of the path)
     * @return an array of folder names contained in the folder with path equal to path argument
     * @throws Exception if path is invalid or filetree not initialized
     */
    public static String[] getFolderNames(String pathToParentFolder) throws Exception {
        return PersistenceController.getFolderNames(pathToParentFolder);
    }

    public static void setCompressionType(String path, String Type) throws Exception {
        PersistenceController.setCompressionType(path, Type);
    }

    public static String getCompressionType(String path) throws Exception {
        return PersistenceController.getCompressionType(path);
    }

    // escribe la cabecera (con la jerarquia de FileTree) y comprime todos los archivos
    public static void compress(String OutputFilePath) throws Exception {
        OutputStream os = PersistenceController.reserveHeader(OutputFilePath);
    }

    // descomprime todos los archivos
    public static void decompress(String OutputFolderPath) throws Exception {
        
    }
    
}