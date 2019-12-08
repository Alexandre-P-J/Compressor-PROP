package Presentation;

import javax.swing.SwingUtilities;

public class PresentationController {
    // Singleton instance
    private static final PresentationController instance = new PresentationController();

    // Private to avoid external use of the constructor
    private PresentationController() {}

    public static void DisplayUI () {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame();
            }
        });
    }

    // Singleton getter
    public static PresentationController getInstance() {
        return instance;
    }
}