package game.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest {

    @Test
    void testCoordinate() {
        Coordinate c1 = new Coordinate(5, 10);
        Coordinate c2 = new Coordinate(5, 10);
        Coordinate c3 = new Coordinate(0, 0);

        assertEquals(5, c1.row());
        assertEquals(10, c1.col());

        assertEquals(c1, c2);
        assertNotEquals(c1, c3);
        assertNotEquals(c1, null);
        assertNotEquals(c1, "String");
        assertEquals(c1, c1);

        assertEquals(c1.hashCode(), c2.hashCode());

        assertNotNull(c1.toString());
    }

    @Test
    void testSymbolEnum() {
        assertEquals(Symbol.X, Symbol.valueOf("X"));
        assertEquals(Symbol.O, Symbol.valueOf("O"));
        assertNotNull(Symbol.values());
    }
}