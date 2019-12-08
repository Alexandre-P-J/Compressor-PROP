package Presentation;

import Domain.DomainController;
import javax.swing.SwingUtilities;

public class PresentationController {
    private static NavigationPanel navigator;
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
        boolean compressed = DomainController.readFileTree(path);
        navigator.refresh("");
        return compressed;
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

    public static String[] getFolderNames(String Path) throws Exception {
        return DomainController.getFolderNames(Path);
    }

    public static String[] getFileNames(String Path) throws Exception {
        return DomainController.getFileNames(Path);
    }
}