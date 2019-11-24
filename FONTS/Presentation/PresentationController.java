package Presentation;

import Domain.DomainController;

public class PresentationController {
    // Singleton instance
    private static final PresentationController instance = new PresentationController();

    // Private to avoid external use of the constructor
    private PresentationController() {}

    // Singleton getter
    public static PresentationController getInstance() {
        return instance;
    }
}