package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public interface GameDAO {

    void clear();
    GameData createGame(String gameName);
    GameData getGame(int gameID);
    ArrayList<GameData> listGames();
    void updateGame(int gameID, String gameName, String whiteUsername, String blackUsername, ChessGame game);
}
