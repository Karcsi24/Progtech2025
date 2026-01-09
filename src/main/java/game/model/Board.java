package game.model;

import java.util.Arrays;

public class Board {
    private final int rows;
    private final int cols;
    private final Symbol[][] grid;

    public Board(int rows, int cols) {
        if (cols < 5 || rows < cols || rows > 25) {
            throw new IllegalArgumentException("Hibás méret! Szabály: 5 <= M <= N <= 25");
        }
        this.rows = rows;
        this.cols = cols;
        this.grid = new Symbol[rows][cols];

        for (Symbol[] row : grid) {
            Arrays.fill(row, Symbol.NONE);
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Symbol getSymbolAt(int r, int c) {
        if (isWithinBounds(r, c)) return grid[r][c];
        return Symbol.NONE;
    }

    public void setSymbolAt(int r, int c, Symbol s) {
        if (isWithinBounds(r, c)) grid[r][c] = s;
    }

    public boolean isWithinBounds(int r, int c) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }

    public boolean isFull() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == Symbol.NONE) return false;
            }
        }
        return true;
    }
}