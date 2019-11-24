package Persistence;

import Domain.DomainController;

public class PersistenceController {
    // Singleton instance
    private static final PersistenceController instance = new PersistenceController();

    // Private to avoid external use of the constructor
    private PersistenceController() {}

    // Singleton getter
    public static PersistenceController getInstance() {
        return instance;
    }
}