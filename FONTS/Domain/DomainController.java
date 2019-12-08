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
        if (pathToParentFolder.length() == 0)
            pathToParentFolder = "/";
        return PersistenceController.getFileNames(pathToParentFolder);
    }

    /**
     * Return folder names from the given relative path
     * @param pathToParentFolder either "/" or "foo/bar.." (replacing ".." with the rest of the path)
     * @return an array of folder names contained in the folder with path equal to path argument
     * @throws Exception if path is invalid or filetree not initialized
     */
    public static String[] getFolderNames(String pathToParentFolder) throws Exception {
        if (pathToParentFolder.length() == 0)
            pathToParentFolder = "/";
        return PersistenceController.getFolderNames(pathToParentFolder);
    }

    /**
     * Sets the compression type for the file in the given path
     * @param path relative path to a file
     * @param Type the compression type, either "LZW", "LZ78", "LZSS" or "JPEG"
     * @throws Exception if the file does not exist, or the compression type does not support the file 
     * or if changing the compression of an already compressed file, or the compression type does not exist
     */
    public static void setCompressionType(String path, String Type) throws Exception {
        PersistenceController.setCompressionType(path, Type);
    }

    /**
     * Returns the current compression type for the file in the given path
     * @param path relative path to a file
     * @return the compression type, either "LZW", "LZ78", "LZSS" or "JPEG"
     * @throws Exception if the file does not exist
     */
    public static String getCompressionType(String path) throws Exception {
        return PersistenceController.getCompressionType(path);
    }


    /**
     * Compresses the entire file tree to the given file path
     * @param OutputFilePath Path to the resulting compressed file
     * @throws Exception if any of the compressions fails or invalid OutputFilePath
     */
    public static void compressTo(String OutputFilePath) throws Exception {
        // traverses the filetree calling chainCompress on each file
        PersistenceController.compressFiletree(OutputFilePath);
    }

    /**
     * Decompresses the entire file tree into the given folder path
     * @param OutputFolderPath Path to the folder to save the decompressed data
     * @throws Exception if any of the decompressions fails, or invalid OutputFolderPath
     */
    public static void decompressTo(String OutputFolderPath) throws Exception {
        // traverses the filetree calling chainDecompress on each file
        PersistenceController.decompressFiletree(OutputFolderPath);
    }

    /**
     * Returns a decompressed document from the compressed or not compressed filetree
     * @param Path relative path to the file
     * @return String that contains the entire document in UTF-8
     * @throws Exception if i/o error, or the decompression fails
     */
    public static String getDocument(String Path) throws Exception {
        return PersistenceController.getDocument(Path);
    }

    /**
     * Returns a decompressed image from the compressed or not compressed filetree
     * @param Path relative path to the image
     * @return byte[] where 4 first bytes codify the width (int), the 4 next the height (int) and the rest of bytes
     * is a sequence of RGBRGBRGB... values
     * @throws Exception if i/o error, or the decompression fails
     */
    public static byte[] getImage(String Path) throws Exception {
        return PersistenceController.getImage(Path);
    }

    /**
     * Returns the total time of the last compress/decompress operation
     * @return time in miliseconds
     */
    public static long getTotalTimeStat() {
        return PersistenceController.getTotalTimeStat();
    }

    /**
     * Returns the total size of the input data from the last compress/decompress operation
     * @return size in bytes
     */
    public static long getTotalInputSizeStat() {
        return PersistenceController.getTotalInputSizeStat();
    }

    /**
     * Returns the total size of the output data from the last compress/decompress operation
     * @return size in bytes
     */
    public static long getTotalOutputSizeStat() {
        return PersistenceController.getTotalOutputSizeStat();
    }

    /**
     * Returns the time of the last compress/decompress operation for the file in the given path
     * @param path relative path to a file
     * @return time in miliseconds
     * @throws Exception if the file does not exist
     */
    public static long getFileTimeStat(String path) throws Exception {
        return PersistenceController.getFileTimeStat(path);
    }

    /**
     * Returns the input size of the last compress/decompress operation for the file in the given path
     * @param path relative path to a file
     * @return size in bytes
     * @throws Exception if the file does not exist
     */
    public static long getFileInputSizeStat(String path) throws Exception {
        return PersistenceController.getFileInputSizeStat(path);
    }

    /**
     * Returns the output size of the last compress/decompress operation for the file in the given path
     * @param path relative path to a file
     * @return size in bytes
     * @throws Exception if the file does not exist
     */
    public static long getFileOutputSizeStat(String path) throws Exception {
        return PersistenceController.getFileOutputSizeStat(path);
    }

    /**
     * Compresses an InputStream to an OutputStream with the compression algorithm given by compressionType
     * @param is InputStream containing valid uncompressed data
     * @param os OutputStream will contain the compressed data from InputStream
     * @param compressionType String that specifies the compression algorithm, either "LZW", "LZ78", "LZSS" or "JPEG"
     * @throws Exception if the compression fails or i/o error
     */
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

    /**
     * Decompresses an InputStream to an OutputStream with the compression algorithm given by compressionType
     * @param is InputStream containing valid compressed data
     * @param os OutputStream will contain the decompressed data from InputStream
     * @param compressionType String that specifies the compression algorithm, either "LZW", "LZ78", "LZSS" or "JPEG"
     * @throws Exception if the decompression fails or i/o error
     */
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