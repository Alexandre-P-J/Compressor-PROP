package Domain;

import Presentation.PresentationController;
import Persistence.PersistenceController;
import java.io.IOException;
import java.io.InputStream;
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
    
    // lo llama presentation controller con el path obtenido despues de pulsar SELECT, retorna true si se ha seleccionado un archivo comprimido
    public static boolean readFileTree(String path) throws IOException {
        PersistenceController.readFileTree(path);
        return PersistenceController.isFileTreeCompressed();
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

    //public static byte[] openFile(String path) {}

    // escribe la cabecera (con la jerarquia de FileTree) y comprime todos los archivos
    public static void compressTo(String OutputFilePath) throws Exception {
        PersistenceController.compressFiletree(OutputFilePath); // traverses the filetree calling chainCompress on each file
    }

    // descomprime todos los archivos
    public static void decompressTo(String OutputFolderPath) throws Exception {
        PersistenceController.decompressFiletree(OutputFolderPath); // traverses the filetree calling chainDecompress on each file
    }

    public static String getDocument(String Path) throws Exception {
        return PersistenceController.getDocument(Path);
    }

    public static byte[] getImage(String Path) throws Exception {
        return PersistenceController.getImage(Path);
    }

    public static long getTotalTimeStat() {
        return PersistenceController.getTotalTimeStat();
    }

    public static long getTotalInputSizeStat() {
        return PersistenceController.getTotalInputSizeStat();
    }

    public static long getTotalOutputSizeStat() {
        return PersistenceController.getTotalOutputSizeStat();
    }

    public static long getFileTimeStat(String path) throws Exception {
        return PersistenceController.getFileTimeStat(path);
    }

    public static long getFileInputSizeStat(String path) throws Exception {
        return PersistenceController.getFileInputSizeStat(path);
    }

    public static long getFileOutputSizeStat(String path) throws Exception {
        return PersistenceController.getFileOutputSizeStat(path);
    }

    //called by persistance, returns a long[3] containing statistics
    public static void chainCompress(InputStream is, OutputStream os, String compressionType) throws Exception {
        Algorithm alg;
        switch (compressionType) {
            case "LZW":
                alg = new LZW();
                break;
            case "LZ78":
                alg = new LZ78();
                break;
            case "LZSS":
                alg = new LZSS();
                break;
            case "JPEG":
                alg = new JPEG(new Huffman());
                break;
            default:
                throw new Exception("Invalid Compression Type!");
        }
        alg.compress(is, os);
    }

    //called by persistance, returns a long[3] containing statistics
    public static void chainDecompress(InputStream is, OutputStream os, String compressionType) throws Exception {
        Algorithm alg;
        switch (compressionType) {
            case "LZW":
                alg = new LZW();
                break;
            case "LZ78":
                alg = new LZ78();
                break;
            case "LZSS":
                alg = new LZSS();
                break;
            case "JPEG":
                alg = new JPEG(new Huffman());
                break;
            default:
                throw new Exception("Invalid Compression Type!");
        }
        alg.decompress(is, os);
    }
    
}