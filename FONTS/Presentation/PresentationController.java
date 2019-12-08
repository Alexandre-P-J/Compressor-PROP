package Presentation;

import Domain.DomainController;

import javax.swing.SwingUtilities;
import java.io.File;
import java.io.IOException;

public class PresentationController {
    private static String s = new String();
    // Singleton instance
    private static final PresentationController instance = new PresentationController();
    // Private to avoid external use of the constructor
    private PresentationController() {}
    private static DomainController dc = DomainController.getInstance();
    
    public static void DisplayUI () {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame();
            }
        });
    }

    public static String[] obtenerArchivos(File f, boolean carpetas) {
        try {
            String[] ficheros;
            s = f.getAbsolutePath();
            dc.readFileTree(s);
            s = f.getName();
            if (carpetas) {
                ficheros = dc.getFolderNames(s);
            }
            else {
                ficheros = dc.getFileNames(s);
            }
            dc.getFileNames(s);
            return ficheros;
        } 
        catch (Exception exc) {
            return null;
        }
    }

    // Singleton getter
    public static PresentationController getInstance() {
        return instance;
    }

}