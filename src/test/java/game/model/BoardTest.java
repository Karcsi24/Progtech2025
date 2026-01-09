package game.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BoardTest {

    @Test
    void testBoardCreation() {
        Board board = new Board(10, 10);
        assertNotNull(board);

        assertThrows(IllegalArgumentException.class, () -> new Board(4, 4));
    }
}