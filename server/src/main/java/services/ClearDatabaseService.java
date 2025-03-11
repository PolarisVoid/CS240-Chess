package services;

public class ClearDatabaseService extends BaseService {

    public static void clearDatabase() {
        userDAO.clear();
        gameDAO.clear();
        authDAO.clear();
    }
}
