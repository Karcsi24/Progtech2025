package game;

import game.data.DataHandler;
import game.logic.GameEngine;
import game.model.Board;
import game.model.Coordinate;
import game.model.Symbol;
import game.ui.ConsoleInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public final class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final int BOARD_SIZE = 10;
    private static final String SAVE_FILE = "saved_game.txt";

    private Main() {
    }

    public static void main(final String[] args) {
        logger.info("Az alkalmazás elindult.");

        ConsoleInterface ui = new ConsoleInterface();
        DataHandler dataHandler = new DataHandler();
        Scanner inputScanner = new Scanner(System.in);

        dataHandler.initDatabase();
        System.out.println("Üdvözölek az Amőba játékban!");
        ui.printHighScores(dataHandler.getHighScores());

        String playerName = ui.askPlayerName();

        boolean playAgain = true;
        while (playAgain) {
            GameEngine game = null;

            try {
                game = dataHandler.loadGame(SAVE_FILE);
                if (game != null) {
                    if (game.isGameOver()) {
                        game = null;
                        new File(SAVE_FILE).delete();
                    } else {
                        System.out.println("Mentett játék sikeresen betöltve!");
                        logger.info("Mentett játék betöltve.");
                    }
                }
            } catch (IOException e) {
                logger.warn("Hiba a betöltésnél vagy nem létezik mentés.", e);
                System.out.println("Hiba a betöltésnél, új játék indul.");
            }

            if (game == null) {
                System.out.println("\n--- ÚJ JÁTÉK INDUL (10x10) ---");
                game = new GameEngine(BOARD_SIZE, BOARD_SIZE);
            }

            while (!game.isGameOver()) {
                ui.printBoard(game);

                if (game.getCurrentPlayer() == Symbol.X) {
                    System.out.println(playerName + " (X) következik.");

                    boolean isFirstMove = isBoardEmpty(game.getBoard());
                    Coordinate move = ui.askMove(
                            game.getBoard().getRows(),
                            game.getBoard().getCols(),
                            isFirstMove
                    );

                    if (move == null) {
                        try {
                            dataHandler.saveGame(game, SAVE_FILE);
                            System.out.println("Játék elmentve! "
                                    + "A kilépéshez nyomj Entert...");
                            logger.info("Játék elmentve és kilépés.");
                            return;
                        } catch (IOException e) {
                            logger.error("Hiba mentéskor", e);
                            System.err.println("Hiba mentéskor: " + e.getMessage());
                        }
                    } else {
                        if (game.isValidMove(move)) {
                            game.makeMove(move);
                        } else {
                            System.out.println("Érvénytelen lépés! "
                                    + "(Foglalt, nem érintkezik, vagy táblán kívüli).");
                        }
                    }
                } else {
                    System.out.println("A gép (O) gondolkodik...");
                    game.aiMove();
                }
            }

            ui.printBoard(game);
            Symbol winner = game.getCurrentPlayer();

            if (winner == Symbol.X) {
                System.out.println("\nGRATULÁLOK " + playerName + "! NYERTÉL!");
                dataHandler.saveWin(playerName);
                logger.info("A játékos nyert: {}", playerName);
            } else if (winner == Symbol.O) {
                System.out.println("\nA gép nyert.!");
                logger.info("A gép nyert.");
            } else {
                System.out.println("\nDöntetlen!");
                logger.info("Döntetlen.");
            }

            File saveFile = new File(SAVE_FILE);
            if (saveFile.exists()) {
                saveFile.delete();
            }

            ui.printHighScores(dataHandler.getHighScores());

            while (true) {
                System.out.print("Szeretnél új játékot kezdeni? ( igen / nem ): ");
                String answer = inputScanner.nextLine().trim().toLowerCase();

                if ("igen".equals(answer) || "i".equals(answer)) {
                    playAgain = true;
                    break;
                } else if ("nem".equals(answer) || "n".equals(answer)) {
                    playAgain = false;
                    System.out.println("Találkozunk legközelebbi körben!");
                    break;
                } else {
                    System.out.println("Nem értelmezhető válasz!");
                }
            }
        }
        logger.info("Az alkalmazás bezárult.");
    }

    private static boolean isBoardEmpty(final Board board) {
        for (int r = 0; r < board.getRows(); r++) {
            for (int c = 0; c < board.getCols(); c++) {
                if (board.getSymbolAt(r, c) != Symbol.NONE) {
                    return false;
                }
            }
        }
        return true;
    }
}