package game.data;

import game.logic.GameEngine;
import game.model.Board;
import game.model.Symbol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataHandler {

    private static final Logger logger = LoggerFactory.getLogger(DataHandler.class);
    private static final String DB_URL = "jdbc:h2:./amoba_db";
    private static final String DB_USER = "sa";
    private static final String DB_PASS = "";

    public void saveGame(GameEngine game, String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            Board b = game.getBoard();
            writer.println(b.getRows() + " " + b.getCols());
            writer.println(game.getCurrentPlayer().toString());

            for (int r = 0; r < b.getRows(); r++) {
                for (int c = 0; c < b.getCols(); c++) {
                    writer.print(b.getSymbolAt(r, c).toString());
                }
                writer.println();
            }
            logger.info("Játék sikeresen elmentve ide: {}", filename);
        } catch (IOException e) {
            logger.error("Hiba történt a játék mentésekor!", e);
            throw e;
        }
    }

    public GameEngine loadGame(String filename) throws IOException {
        File f = new File(filename);
        if (!f.exists()) {
            logger.info("Nincs mentett játék ezen a néven: {}", filename);
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String[] dims = reader.readLine().split(" ");
            int rows = Integer.parseInt(dims[0]);
            int cols = Integer.parseInt(dims[1]);

            String playerStr = reader.readLine().trim();
            Symbol nextPlayer = "X".equalsIgnoreCase(playerStr) ? Symbol.X : Symbol.O;

            Board board = new Board(rows, cols);
            for (int r = 0; r < rows; r++) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                for (int c = 0; c < cols && c < line.length(); c++) {
                    char ch = line.charAt(c);
                    if (ch == 'X' || ch == 'x') {
                        board.setSymbolAt(r, c, Symbol.X);
                    } else if (ch == 'O' || ch == 'o') {
                        board.setSymbolAt(r, c, Symbol.O);
                    }
                }
            }
            logger.info("Játék sikeresen betöltve: {}", filename);
            return new GameEngine(board, nextPlayer);
        } catch (IOException | NumberFormatException e) {
            logger.error("Hiba a játék betöltésekor!", e);
            throw new IOException("A mentés fájl sérült vagy olvashatatlan.", e);
        }
    }

    public void initDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "CREATE TABLE IF NOT EXISTS highscores ("
                    + "name VARCHAR(255) PRIMARY KEY, "
                    + "wins INT DEFAULT 0)";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                logger.info("Adatbázis (tábla) ellenőrizve/létrehozva.");
            }
        } catch (SQLException e) {
            logger.error("Adatbázis inicializálási hiba!", e);
        }
    }

    public void saveWin(String playerName) {
        String queryCheck = "SELECT wins FROM highscores WHERE name = ?";
        String queryUpdate = "UPDATE highscores SET wins = wins + 1 WHERE name = ?";
        String queryInsert = "INSERT INTO highscores (name, wins) VALUES (?, 1)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            boolean exists = false;
            try (PreparedStatement pstmt = conn.prepareStatement(queryCheck)) {
                pstmt.setString(1, playerName);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    exists = true;
                }
            }

            if (exists) {
                try (PreparedStatement pstmt = conn.prepareStatement(queryUpdate)) {
                    pstmt.setString(1, playerName);
                    pstmt.executeUpdate();
                    logger.info("Pontszám frissítve a játékosnak: {}", playerName);
                }
            } else {
                try (PreparedStatement pstmt = conn.prepareStatement(queryInsert)) {
                    pstmt.setString(1, playerName);
                    pstmt.executeUpdate();
                    logger.info("Új játékos bejegyezve a ranglistára: {}", playerName);
                }
            }
        } catch (SQLException e) {
            logger.error("Hiba az adatbázis mentésnél (saveWin)!", e);
        }
    }

    public List<String> getHighScores() {
        List<String> results = new ArrayList<>();
        String query = "SELECT name, wins FROM highscores ORDER BY wins DESC";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                results.add(rs.getString("name") + ": " + rs.getInt("wins") + " győzelem");
            }
        } catch (SQLException e) {
            logger.error("Hiba a ranglista lekérdezésénél!", e);
        }
        return results;
    }
}