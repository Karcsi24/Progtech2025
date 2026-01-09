package game.data;

import game.logic.GameEngine;
import game.model.Board;
import game.model.Coordinate;
import game.model.Symbol;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataHandlerTest {

    @TempDir
    Path tempDir;

    @Mock
    GameEngine mockGameEngine;

    @Test
    void testSaveAndLoadGame() throws IOException {
        DataHandler dataHandler = new DataHandler();
        GameEngine originalGame = new GameEngine(10, 10);
        originalGame.makeMove(new Coordinate(5, 5));

        File testFile = tempDir.resolve("valos_mentes_teszt.txt").toFile();
        String filePath = testFile.getAbsolutePath();

        dataHandler.saveGame(originalGame, filePath);
        assertTrue(testFile.exists(), "A fájlnak léteznie kell");

        GameEngine loadedGame = dataHandler.loadGame(filePath);
        assertNotNull(loadedGame, "A betöltött játék nem lehet null");
    }

    @Test
    void testLoadNonExistentFile() throws IOException {
        DataHandler handler = new DataHandler();
        GameEngine result = handler.loadGame("nincs_ilyen_fajl.txt");
        assertNull(result, "Nem létező fájl esetén null-t várunk");
    }

    @Test
    void testSaveGameWithMock() throws IOException {
        File tempFile = tempDir.resolve("mockito_teszt.txt").toFile();
        String path = tempFile.getAbsolutePath();
        DataHandler dataHandler = new DataHandler();

        Board realBoard = new Board(10, 10);
        realBoard.setSymbolAt(0, 0, Symbol.O);

        when(mockGameEngine.getBoard()).thenReturn(realBoard);
        when(mockGameEngine.getCurrentPlayer()).thenReturn(Symbol.X);

        dataHandler.saveGame(mockGameEngine, path);

        List<String> lines = Files.readAllLines(tempFile.toPath());
        assertTrue(lines.size() > 2, "A fájl túl rövid");

        String playerLine = lines.get(1).toUpperCase();
        assertTrue(playerLine.contains("X"),
                "A játékos jelének (X vagy x) ott kell lennie a 2. sorban. Kapott: " + lines.get(1));

        String boardLine = lines.get(2).toUpperCase();
        assertTrue(boardLine.contains("O"),
                "A tábla első sorában ott kell lennie az 'O' vagy 'o' jelnek.");

        verify(mockGameEngine, atLeastOnce()).getBoard();
    }
}