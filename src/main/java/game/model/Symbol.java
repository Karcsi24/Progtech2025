package game.model;

public enum Symbol {
    X,
    O,
    NONE;

    @Override
    public String toString() {
        return switch (this) {
            case X -> "x";
            case O -> "o";
            case NONE -> ".";
        };
    }
}