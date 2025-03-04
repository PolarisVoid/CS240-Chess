package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO {
    public static final ArrayList<GameData> DATABASE = new ArrayList<>();
    public static int counter = 1;

    @Override
    public void clear() {
        DATABASE.clear();
        counter = 1;
    }

    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        try {
            GameData game = new GameData(counter, gameName, null, null, new ChessGame());
            DATABASE.add(game);
            counter += 1;
            return game;
        } catch (Exception e) {
            throw new DataAccessException("Could not Create Game");
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try {
            for (GameData row : DATABASE) {
                if (Objects.equals(row.gameID(), gameID)) {
                    return row;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Game not found");
        }
        return null;
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        try {
            return DATABASE;
        } catch (Exception e) {
            throw new DataAccessException("Could not retrive games.");
        }
    }

    @Override
    public void updateGame(int gameID, String gameName, String whiteUsername, String blackUsername, ChessGame game) throws DataAccessException {
        try {
            DATABASE.removeIf(row -> Objects.equals(row.gameID(), gameID));
            DATABASE.add(new GameData(gameID, gameName, whiteUsername, blackUsername, game));
        } catch (Exception e) {
            throw new DataAccessException("Could not update Game");
        }
    }
}
