package service;

public class ClearDatabaseService extends BaseService {

    public static void clearDatabase() {
        USER_DAO.clear();
        GAME_DAO.clear();
        AUTH_DAO.clear();
    }
}
