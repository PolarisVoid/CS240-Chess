package service;

import dataaccess.*;
import org.junit.jupiter.api.*;
import services.ClearDatabaseService;

public class ClearDatabaseServiceTests extends TestUtils {
    @Test
    @Order(1)
    @DisplayName("Clear Database")
    public void clearDatabase() {
        try {
            ClearDatabaseService.clearDatabase();
        } catch (Exception e) {
            assert false;
        }
        assert MemoryGameDAO.DATABASE.isEmpty();
        assert MemoryGameDAO.counter == 1;
        assert MemoryAuthDAO.DATABASE.isEmpty();
        assert MemoryUserDAO.DATABASE.isEmpty();
    }
}
