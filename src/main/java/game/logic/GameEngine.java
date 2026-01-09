package game.logic;

import game.model.Board;
import game.model.Coordinate;
import game.model.Symbol;

import java.util.Random;

public class GameEngine {
    private final Board board;
    private Symbol currentPlayer;
    private boolean isGameOver;
    private Symbol winner;
    private final Random random = new Random();

    public GameEngine(int rows, int cols) {
        this.board = new Board(rows, cols);
        this.currentPlayer = Symbol.X;
        this.isGameOver = false;
    }

    public GameEngine(Board board, Symbol nextPlayer) {
        this.board = board;
        this.currentPlayer = nextPlayer;
        this.isGameOver = false;
    }

    public Board getBoard() {
        return board;
    }

    public Symbol getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public Symbol getWinner() {
        return this.winner;
    }


    public boolean isValidMove(Coordinate c) {
        if (!board.isWithinBounds(c.row(), c.col())) return false;

        if (board.getSymbolAt(c.row(), c.col()) != Symbol.NONE) return false;

        boolean isBoardEmpty = true;
        checkLoop:
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                if (board.getSymbolAt(i, j) != Symbol.NONE) {
                    isBoardEmpty = false;
                    break checkLoop;
                }
            }
        }

        if (isBoardEmpty) {
            int centerRow = board.getRows() / 2;
            int centerCol = board.getCols() / 2;
            return Math.abs(c.row() - centerRow) <= 1 && Math.abs(c.col() - centerCol) <= 1;
        } else {
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (dr == 0 && dc == 0) continue;

                    if (board.getSymbolAt(c.row() + dr, c.col() + dc) != Symbol.NONE) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public boolean makeMove(Coordinate c) {
        if (isGameOver || !isValidMove(c)) {
            return false;
        }

        board.setSymbolAt(c.row(), c.col(), currentPlayer);

        if (checkWin(c, currentPlayer)) {
            isGameOver = true;
            winner = currentPlayer;
        } else if (board.isFull()) {
            isGameOver = true;
            winner = Symbol.NONE;
        } else {
            currentPlayer = (currentPlayer == Symbol.X) ? Symbol.O : Symbol.X;
        }

        return true;
    }

    public boolean makeMove(int row, int col) {
        return makeMove(new Coordinate(row, col));
    }


    public void aiMove() {
        if (isGameOver) return;

        int maxAttempts = 2000;
        for (int i = 0; i < maxAttempts; i++) {
            int r = random.nextInt(board.getRows());
            int c = random.nextInt(board.getCols());
            Coordinate candidate = new Coordinate(r, c);

            if (isValidMove(candidate)) {
                makeMove(candidate);
                return;
            }
        }
    }


    private boolean checkWin(Coordinate c, Symbol s) {
        int r = c.row();
        int col = c.col();
        int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};

        for (int[] dir : directions) {
            int count = 1;

            for (int i = 1; i < 4; i++) {
                if (board.getSymbolAt(r + dir[0] * i, col + dir[1] * i) == s) count++;
                else break;
            }

            for (int i = 1; i < 4; i++) {
                if (board.getSymbolAt(r - dir[0] * i, col - dir[1] * i) == s) count++;
                else break;
            }

            if (count >= 4) return true;
        }
        return false;
    }
}