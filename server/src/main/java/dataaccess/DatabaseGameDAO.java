package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public class DatabaseGameDAO implements GameDAO {
    @Override
    public void clear() {
        try {
            DatabaseManager.truncateTable("GAME");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        return null;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(int gameID, String gameName, String whiteUsername, String blackUsername, ChessGame game) throws DataAccessException {

    }
}
