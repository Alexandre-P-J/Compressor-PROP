package Presentation;

import Domain.DomainController;
import javax.swing.SwingUtilities;

public class PresentationController {
    /**
     * Navegation panel
     */
    private static NavigationPanel navigator;
    /**
     * Indicates if it is compressed
     */
    private static Boolean isCompressed = null;
    /**
     * Singleton instance
     */
    private static final PresentationController instance = new PresentationController();

    /**
     * Default constructor 
     * private to avoid external use of the constructor
     */
    private PresentationController() {
    }

    /**
     * Singleton getter
     * @return the instance
     */
    public static PresentationController getInstance() {
        return instance;
    }

    /**
     * Display the UI
     */
    public static void DisplayUI() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame();
            }
        });
    }

    /**
     * Return if the realtive path is compressed
     * @param path realtive path to a file
     * @return if is ti compressed
     * @throws Exception if path is invalid or filetree not initialized
     */
    public static boolean readFileTree(String path) throws Exception {
        isCompressed = DomainController.readFileTree(path);
        navigator.refresh("");
        return isCompressed;
    }

    /**
     * Return filenames from the given relative path
     * @param pathToParentFolder either "." or "foo/bar.." (replacing ".." with the rest of the path)
     * @return an array of filenames contained in the folder with path equal to path argument
     * @throws Exception if path is invalid or filetree not initialized
     */
    public static String[] getFileNames(String path) throws Exception {
        return DomainController.getFileNames(path);
    }

    /**
     * Return folder names from the given relative path
     * @param pathToParentFolder either "." or "foo/bar.." (replacing ".." with the rest of the path)
     * @return an array of folder names contained in the folder with path equal to path argument
     * @throws Exception if path is invalid or filetree not initialized
     */
    public static String[] getFolderNames(String path) throws Exception {
        return DomainController.getFolderNames(path);
    }

    /**
     * Returns an array of all implemented compression algorithms for the given file in the path
     * @param path path of the file
     * @return a string array containing valid compression types for the file in the path
     * @throws Exception if the path does not point a file
     */
    public static String[] getValidCompressionTypes(String path) throws Exception {
        return DomainController.getValidCompressionTypes(path);
    }

    /**
     * Returns an array of all implemented parameters for the given algorithm
     * @param compressionType the compression type, either "LZW", "LZ78", "LZSS" or "JPEG"
     * @return a string array containing valid parameters types for the file in the path
     * @throws Exceptionif the path does not point a file
     */
    public static String[] getValidCompressionParameters(String compressionType) throws Exception {
        return DomainController.getValidCompressionParameters(compressionType);
    }

    /**
     * Returns true if the file in the path is a ppm image
     * @param path relative path to a file
     * @return true if the file in the path is a ppm image, false otherwise
     * @throws Exception if the path does not point a file
     */
    public static boolean isFileImage(String path) throws Exception {
        return DomainController.isFileImage(path);
    }

    /**
     * Sets the compression type for the file in the given path
     * @param path relative path to a file
     * @param Type the compression type, either "LZW", "LZ78", "LZSS" or "JPEG"
     * @throws Exception if the file does not exist, or the compression type does not support the file 
     * or if changing the compression of an already compressed file, or the compression type does not exist
     */
    public static void setCompressionType(String path, String Type) throws Exception {
        DomainController.setCompressionType(path, Type);
    }

    /**
     * Returns the current compression type for the file in the given path
     * @param path relative path to a file
     * @return the compression type, either "LZW", "LZ78", "LZSS" or "JPEG"
     * @throws Exception if the file does not exist
     */
    public static String getCompressionType(String path) throws Exception {
        return DomainController.getCompressionType(path);
    }

    /**
     * Returns the current parameter of the algorithm for the file in the given path
     * @param path relative path to a file
     * @return the paramater selected
     * @throws Exception if the file does not exist
     */
    public static String getCompressionParameter(String path) throws Exception {
        return DomainController.getCompressionParameter(path);
    }

    /**
     * Returns the current compression type for the file in the given path
     * @param path relative path to a file
     * @return the compression type, either "LZW", "LZ78", "LZSS" or "JPEG"
     * @throws Exception if the file does not exist
     */
    public static void setCompressionParameter(String path, String arg) throws Exception {
        DomainController.setCompressionParameter(path, arg);
    }

    /**
     * Compresses the entire file tree to the given file path
     * @param OutputFilePath Path to the resulting compressed file
     * @throws Exception if any of the compressions fails or invalid OutputFilePath
     */
    public static void compressTo(String OutputFilePath) throws Exception {
        DomainController.compressTo(OutputFilePath);
        navigator.refresh();
    }

    /**
     * Decompresses the entire file tree into the given folder path
     * @param OutputFolderPath Path to the folder to save the decompressed data
     * @throws Exception if any of the decompressions fails, or invalid OutputFolderPath
     */
    public static void decompressTo(String OutputFolderPath) throws Exception {
        DomainController.decompressTo(OutputFolderPath);
        navigator.refresh();
    }

    /**
     * Returns a decompressed document from the compressed or not compressed filetree
     * @param Path relative path to the file
     * @return String that contains the entire document in UTF-8
     * @throws Exception if i/o error, or the decompression fails
     */
    public static String getDocument(String path) throws Exception {
        return DomainController.getDocument(path);
    }

    /**
     * 
     * @param path
     * @return
     * @throws Exception
     */
    public static byte[] getImage(String path) throws Exception {
        return DomainController.getImage(path);
    }

    /**
     * 
     * @param path
     * @return
     * @throws Exception
     */
    public static byte[] getImageAfterLossyCompression(String path) throws Exception {
        return DomainController.getImageAfterLossyCompression(path);
    }

    // Ojo! si no esta comprimiendo/descomprimiendo por que no se ha seleccionado archivo, retorna null!
    public static Boolean isCompressed() {
        return isCompressed;
    }

    /**
     * Sets the navigator
     * @param np the navigation panel
     */
    public static void setNavigator(NavigationPanel np) {
        navigator = np;
    }

    /**
     * Returns the total time of the last compress/decompress operation
     * @return time in miliseconds
     */
    public static long getTotalTimeStat() {
        return DomainController.getTotalTimeStat();
    }

    /**
     * Returns the total size of the input data from the last compress/decompress operation
     * @return size in bytes
     */
    public static long getTotalInputSizeStat() {
        return DomainController.getTotalInputSizeStat();
    }

    /**
     * Returns the total size of the output data from the last compress/decompress operation
     * @return size in bytes
     */
    public static long getTotalOutputSizeStat() {
        return DomainController.getTotalOutputSizeStat();
    }

    /**
     * Returns the time of the last compress/decompress operation for the file in the given path
     * @param path relative path to a file
     * @return time in miliseconds
     * @throws Exception if the file does not exist
     */
    public static long getFileTimeStat(String path) throws Exception {
        return DomainController.getFileTimeStat(path);
    }

    /**
     * Returns the input size of the last compress/decompress operation for the file in the given path
     * @param path relative path to a file
     * @return size in bytes
     * @throws Exception if the file does not exist
     */
    public static long getFileInputSizeStat(String path) throws Exception {
        return DomainController.getFileInputSizeStat(path);
    }

    /**
     * Returns the output size of the last compress/decompress operation for the file in the given path
     * @param path relative path to a file
     * @return size in bytes
     * @throws Exception if the file does not exist
     */
    public static long getFileOutputSizeStat(String path) throws Exception {
        return DomainController.getFileOutputSizeStat(path);
    }







}