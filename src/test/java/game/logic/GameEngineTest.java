package game.logic;

import game.model.Coordinate;
import game.model.Symbol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameEngineTest {

    private GameEngine gameEngine;

    @BeforeEach
    void setUp() {
        gameEngine = new GameEngine(10, 10);
    }


    @Test
    void testFirstMoveMustBeCentral() {
        Coordinate corner = new Coordinate(0, 0);
        assertFalse(gameEngine.isValidMove(corner));
        assertFalse(gameEngine.makeMove(corner));
    }

    @Test
    void testValidCentralMove() {
        assertTrue(gameEngine.makeMove(new Coordinate(5, 5)));
        assertEquals(Symbol.X, gameEngine.getBoard().getSymbolAt(5, 5));
    }

    @Test
    void testMoveOnOccupiedPosition() {
        gameEngine.makeMove(5, 5);
        boolean result = gameEngine.makeMove(5, 5);
        assertFalse(result, "Nem léphetünk foglalt mezőre");
    }

    @Test
    void testMoveDisconnected() {
        gameEngine.makeMove(5, 5);
        boolean result = gameEngine.makeMove(0, 0);
        assertFalse(result, "Csak szomszédos mezőre szabad lépni");
    }


    @Test
    void testVerticalWin() {
        int col = 5;
        gameEngine.makeMove(new Coordinate(4, 5)); // X
        gameEngine.makeMove(new Coordinate(4, 6)); // O
        gameEngine.makeMove(new Coordinate(5, 5)); // X
        gameEngine.makeMove(new Coordinate(5, 6)); // O
        gameEngine.makeMove(new Coordinate(6, 5)); // X
        gameEngine.makeMove(new Coordinate(6, 6)); // O
        gameEngine.makeMove(new Coordinate(7, 5)); // X - NYERT

        assertTrue(gameEngine.isGameOver());
        assertEquals(Symbol.X, gameEngine.getWinner());
    }

    @Test
    void testHorizontalWin() {
        int row = 5;
        gameEngine.makeMove(new Coordinate(row, 5)); // X
        gameEngine.makeMove(new Coordinate(row + 1, 5)); // O
        gameEngine.makeMove(new Coordinate(row, 6)); // X
        gameEngine.makeMove(new Coordinate(row + 1, 6)); // O
        gameEngine.makeMove(new Coordinate(row, 7)); // X
        gameEngine.makeMove(new Coordinate(row + 1, 7)); // O
        gameEngine.makeMove(new Coordinate(row, 8)); // X - NYERT

        assertTrue(gameEngine.isGameOver());
        assertEquals(Symbol.X, gameEngine.getWinner());
    }

    @Test
    void testDiagonalWin() {
        gameEngine.makeMove(new Coordinate(5, 5)); // X
        gameEngine.makeMove(new Coordinate(5, 6)); // O
        gameEngine.makeMove(new Coordinate(6, 6)); // X
        gameEngine.makeMove(new Coordinate(6, 5)); // O
        gameEngine.makeMove(new Coordinate(7, 7)); // X
        gameEngine.makeMove(new Coordinate(7, 6)); // O
        gameEngine.makeMove(new Coordinate(8, 8)); // X - NYERT

        assertTrue(gameEngine.isGameOver());
    }


    @Test
    void testAiMove() {
        gameEngine.makeMove(5, 5);

        gameEngine.aiMove();

        boolean foundO = false;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (gameEngine.getBoard().getSymbolAt(i, j) == Symbol.O) {
                    foundO = true;
                    break;
                }
            }
        }
        assertTrue(foundO, "A gépnek lépnie kellett volna egyet");
        assertEquals(Symbol.X, gameEngine.getCurrentPlayer());
    }
}