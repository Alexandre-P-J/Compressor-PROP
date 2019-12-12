package Presentation;

import Domain.DomainController;
import javax.swing.SwingUtilities;

public class PresentationController {
    private static NavigationPanel navigator;
    private static Boolean isCompressed = null;
    // Singleton instance
    private static final PresentationController instance = new PresentationController();

    // Private to avoid external use of the constructor
    private PresentationController() {
    }

    // Singleton getter
    public static PresentationController getInstance() {
        return instance;
    }

    public static void DisplayUI() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame();
            }
        });
    }

    public static boolean readFileTree(String path) throws Exception {
        isCompressed = DomainController.readFileTree(path);
        navigator.refresh("");
        return isCompressed;
    }

    // Ojo! si no esta comprimiendo/descomprimiendo por que no se ha seleccionado archivo, retorna null!
    public static Boolean isCompressed() {
        return isCompressed;
    }

    public static void compressTo(String OutputFilePath) throws Exception {
        DomainController.compressTo(OutputFilePath);
    }

    public static void decompressTo(String OutputFolderPath) throws Exception {
        DomainController.decompressTo(OutputFolderPath);
    }

    public static void setNavigator(NavigationPanel np) {
        navigator = np;
    }

    public static String[] getFolderNames(String path) throws Exception {
        return DomainController.getFolderNames(path);
    }

    public static String[] getFileNames(String path) throws Exception {
        return DomainController.getFileNames(path);
    }

    public static String getDocument(String path) throws Exception {
        return DomainController.getDocument(path);
    }

    public static byte[] getImage(String path) throws Exception {
        return DomainController.getImage(path);
    }
    
    public static boolean isFileImage(String path) throws Exception {
        return DomainController.isFileImage(path);
    }

    public static long getTotalTimeStat() {
        return DomainController.getTotalTimeStat();
    }

    public static long getTotalInputSizeStat() {
        return DomainController.getTotalInputSizeStat();
    }

    public static long getTotalOutputSizeStat() {
        return DomainController.getTotalOutputSizeStat();
    }

    public static long getFileTimeStat(String path) throws Exception {
        return DomainController.getFileTimeStat(path);
    }

    public static long getFileInputSizeStat(String path) throws Exception {
        return DomainController.getFileInputSizeStat(path);
    }

    public static long getFileOutputSizeStat(String path) throws Exception {
        return DomainController.getFileOutputSizeStat(path);
    }

    public static String[] getValidCompressionTypes() {
        return DomainController.getValidCompressionTypes();
    }

    public static String[] getValidCompressionParameters(String compressionType) throws Exception {
        return DomainController.getValidCompressionParameters(compressionType);
    }
}