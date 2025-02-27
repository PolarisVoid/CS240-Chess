package responses;

import model.GameData;

import java.util.ArrayList;

public class GetGameResponse {

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("{ \"games\": [");
        for (GameData game : games) {
            string.append("{\"gameID\":").append(game.gameID());
            string.append(", \"whiteUsername\":\"").append(game.whiteUsername()).append("\"");
            string.append(", \"blackUsername\":\"").append(game.blackUsername()).append("\"");
            string.append(", \"gameName\": \"").append(game.gameName()).append("\"}");
        }
        string.append(" ]}");
        return string.toString();
    }

    ArrayList<GameData> games;
    public GetGameResponse() {
        this.games = new ArrayList<>();
    }

    public void insertGame(GameData game) {
        games.add(game);
    }
}
