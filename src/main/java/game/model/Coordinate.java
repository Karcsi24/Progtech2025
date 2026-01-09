package game.model;

public record Coordinate(int row, int col) {
    @Override
    public String toString() {
        return (char) ('a' + col) + String.valueOf(row + 1);
    }
}