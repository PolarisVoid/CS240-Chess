package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.ResultSet;
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

    private String createGameQuery() {
        return "INSERT INTO GAME (GAMENAME, WHITEUSERNAME, BLACKUSERNAME, GAME) VALUES (?, ?, ?, ?)";
    }

    private String getGameQuery() {
        return "SELECT * FROM GAME WHERE GAMEID = ?";
    }

    private String listGamesQuery() {
        return "SELECT * FROM GAME";
    }

    private String updateGameQuery() {
        return "UPDATE GAME SET GAMENAME = ?, WHITEUSERNAME = ?, BLACKUSERNAME = ?, GAME = ? WHERE GAMEID = ?";
    }

    private GameData processGetGame(ResultSet rs) {
        try {
            if (rs.next()) {
                return new GameData(
                        rs.getInt("GAMEID"),
                        rs.getString("GAMENAME"),
                        rs.getString("WHITEUSERNAME"),
                        rs.getString("BLACKUSERNAME"),
                        new Gson().fromJson(rs.getString("GAME"), ChessGame.class)
                );
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<GameData> processGetGames(ResultSet rs) {
        try {
            ArrayList<GameData> games = new ArrayList<>();
            while (rs.next()) {
                games.add(new GameData(
                        rs.getInt("GAMEID"),
                        rs.getString("GAMENAME"),
                        rs.getString("WHITEUSERNAME"),
                        rs.getString("BLACKUSERNAME"),
                        new Gson().fromJson(rs.getString("GAME"), ChessGame.class)
                ));
            }
            return games;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        String boardEncoding = new Gson().toJson(new ChessGame());
        int gameID = DatabaseManager.executeInsert(createGameQuery(), gameName, null, null, boardEncoding);
        return (gameID > -1) ? getGame(gameID) : null;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return DatabaseManager.executeQuery(getGameQuery(), this::processGetGame, gameID);
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        return DatabaseManager.executeQuery(listGamesQuery(), this::processGetGames);
    }

    @Override
    public void updateGame(int gameID, String gameName, String whiteUsername, String blackUsername, ChessGame game) throws DataAccessException {
        String boardEncoding = new Gson().toJson(game);
        DatabaseManager.executeUpdate(updateGameQuery(), gameName, whiteUsername, blackUsername, boardEncoding, gameID);
    }
}
