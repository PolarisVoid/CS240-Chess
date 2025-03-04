package responses;

import model.GameData;

import java.util.ArrayList;

public class GetGameResponse {

    @Override
    public String toString() {
        if (games.isEmpty()) {
            return "{ \"games\": []}";
        } else {
            StringBuilder string = new StringBuilder("{ \"games\": [");
            for (GameData game : games) {
                string.append("{\"gameID\":").append(game.gameID());
                string.append(", \"whiteUsername\":").append(game.whiteUsername() != null ? "\"" + game.whiteUsername() + "\"" : null);
                string.append(", \"blackUsername\":").append(game.blackUsername() != null ? "\"" + game.blackUsername() + "\"" : null);
                string.append(", \"gameName\": \"").append(game.gameName()).append("\"},");
            }
            string.deleteCharAt(string.length() - 1);
            string.append(" ]}");
            return string.toString();
        }
    }

    final ArrayList<GameData> games;
    public GetGameResponse() {
        this.games = new ArrayList<>();
    }

    public void insertGame(GameData game) {
        games.add(game);
    }

    public ArrayList<GameData> getGames() {
        return games;
    }
}
