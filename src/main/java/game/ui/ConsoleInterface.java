package game.ui;

import game.logic.GameEngine;
import game.model.Coordinate;

import java.util.List;
import java.util.Scanner;

public class ConsoleInterface {
    private final Scanner scanner = new Scanner(System.in);

    public void printBoard(GameEngine game) {
        System.out.print("   ");
        for (int c = 0; c < game.getBoard().getCols(); c++) {
            System.out.print((char) ('a' + c) + " ");
        }
        System.out.println();

        for (int r = 0; r < game.getBoard().getRows(); r++) {
            System.out.printf("%2d ", (r + 1));
            for (int c = 0; c < game.getBoard().getCols(); c++) {
                System.out.print(game.getBoard().getSymbolAt(r, c) + " ");
            }
            System.out.println();
        }
    }

    public Coordinate askMove(int maxRows, int maxCols, boolean isFirstMove) {
        while (true) {
            String prompt = isFirstMove ? "Adja meg az első lépést (e5 vagy f5)" : "Adja meg a következő lépést";

            System.out.print("\n" + prompt + " vagy 'save' a játékállás mentéséhez: ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("save")) return null;

            if (input.length() < 2) continue;

            char colChar = input.charAt(0);
            String rowStr = input.substring(1);

            if (Character.isLetter(colChar) && rowStr.matches("\\d+")) {
                int col = colChar - 'a';
                int row = Integer.parseInt(rowStr) - 1;
                return new Coordinate(row, col);
            } else {
                System.out.println("Hibás formátum! Helyes példa: a1, b10");
            }
        }
    }

    public String askPlayerName() {
        System.out.print("Kérlek, add meg a neved: ");
        return scanner.nextLine();
    }

    public void printHighScores(List<String> scores) {
        System.out.println("\n=== RANGLISTA ===");
        if (scores.isEmpty()) {
            System.out.println("(Még nincs mentett eredmény)");
        } else {
            for (String s : scores) System.out.println(s);
        }
        System.out.println("=================\n");
    }
}