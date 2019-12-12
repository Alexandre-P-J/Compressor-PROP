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
     * @param pathToParentFolder either "." or "foo/bar.." (replacing ".." with the rest of the path)
     * @return an array of filenames contained in the folder with path equal to path argument
     * @throws Exception if path is invalid or filetree not initialized
     */
    public static String[] getFileNames(String pathToParentFolder) throws Exception {
        if (pathToParentFolder.length() == 0)
            pathToParentFolder = ".";
        return PersistenceController.getFileNames(pathToParentFolder);
    }

    /**
     * Return folder names from the given relative path
     * @param pathToParentFolder either "." or "foo/bar.." (replacing ".." with the rest of the path)
     * @return an array of folder names contained in the folder with path equal to path argument
     * @throws Exception if path is invalid or filetree not initialized
     */
    public static String[] getFolderNames(String pathToParentFolder) throws Exception {
        if (pathToParentFolder.length() == 0)
            pathToParentFolder = ".";
        return PersistenceController.getFolderNames(pathToParentFolder);
    }

    /**
     * Returns an array of all implemented compression algorithms
     * @return a String[] containing the identifier of all implemented compression algorithms
     */
    public static String[] getValidCompressionTypes() {
        return new String[] {"LZW", "LZ78", "LZSS", "JPEG"};
    }

    public static String[] getValidCompressionParameters(String compressionType) throws Exception {
        switch (compressionType) {
            case "LZW":
                return new String[] {"8", "9", "10", "11", "12", "13", "14", "15", 
                    "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26",
                    "27", "28", "29", "30", "31"};
            case "LZ78":
                return new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", 
                    "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", 
                    "24", "25", "26", "27", "28", "29", "30", "31"};
            case "LZSS":
                return new String[0];
            case "JPEG":
                return new String[] {"Q0", "Q1", "Q2", "Q3", "Q4", "Q5", "Q6", "Q7", "Q8", "Q9", 
                    "Q10", "Q11", "Q12", "MAX", "MIN", "DEFAULT", "JPEGStandard"};
            default:
                throw new Exception("Invalid Compression Type!");
        }
    }

    /**
     * Returns true if the file in the path is a ppm image
     * @param path relative path to a file
     * @return true if the file in the path is a ppm image, false otherwise
     * @throws Exception if the path does not point a file
     */
    public static boolean isFileImage(String path) throws Exception {
        return PersistenceController.isFileImage(path);
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

    public static String getCompressionParameter(String path) throws Exception {
        return PersistenceController.getCompressionParameter(path);
    }

    public static void setCompressionParameter(String path, String arg) throws Exception {
        PersistenceController.setCompressionParameter(path, arg);
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
        PPMTranslator ppmt = new PPMTranslator(PersistenceController.getImage(Path));
        int w = ppmt.getWidth();
        byte[] wa = toArray(w);
        int h = ppmt.getHeight();
        byte[] ha = toArray(h);
        byte[] result = new byte[8 + (w*h*3)];
        System.arraycopy(wa, 0, result, 0, 4);
        System.arraycopy(ha, 0, result, 4, 4);
        for (int i = 8; i < result.length; ++i)
            result[i] = (byte)ppmt.getNextComponent(); // unsigned encoding
        return result;
    }

    private static byte[] toArray(int value) {
        byte[] result = new byte[4];
        result[0] = (byte)((value >> 24) & 0x000000FF);
        result[1] = (byte)((value >> 16) & 0x000000FF);
        result[2] = (byte)((value >> 8) & 0x000000FF);
        result[3] = (byte)(value & 0x000000FF);
        return result;
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
    public static void chainCompress(InputStream is, OutputStream os, String compressionType, String arg0) throws Exception {
        Algorithm alg;
        switch (compressionType) {
            case "LZW":
                LZW lzw = new LZW();
                lzw.setDictionarySize(Integer.valueOf(arg0));
                alg = lzw;
                break;
            case "LZ78":
                LZ78 lz78 = new LZ78();
                lz78.setDictionarySize(Integer.valueOf(arg0));
                alg = lz78;
                break;
            case "LZSS":
                alg = new LZSS();
                break;
            case "JPEG":
                JPEG jpeg = new JPEG(new Huffman());
                jpeg.setQuantizationTables(PersistenceController.getLuminanceTable(arg0), PersistenceController.getChrominanceTable(arg0));
                alg = jpeg;
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