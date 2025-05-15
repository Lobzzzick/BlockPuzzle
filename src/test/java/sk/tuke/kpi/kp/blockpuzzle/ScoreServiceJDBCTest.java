package sk.tuke.kpi.kp.blockpuzzle;

import blockpuzzle.game.entity.Score;
import blockpuzzle.game.service.ScoreServiceJDBC;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;

public class ScoreServiceJDBCTest {
    private ScoreServiceJDBC service;

    @BeforeEach
    void setUp() {
        service = new ScoreServiceJDBC();
        service.reset();
    }

    @Test
    void testAddScoreAndGetTopScores() {
        Score s1 = new Score("BlockPuzzle", "Alice", 120, new Date());
        Score s2 = new Score("BlockPuzzle", "Bob", 200, new Date());

        service.addScore(s1);
        service.addScore(s2);

        List<Score> scores = service.getTopScores("BlockPuzzle");
        assertEquals(2, scores.size(), "Должны получить 2 результата");

        assertEquals("Bob", scores.get(0).getPlayer());
        assertEquals(200, scores.get(0).getPoints());
        assertEquals("Alice", scores.get(1).getPlayer());
        assertEquals(120, scores.get(1).getPoints());
    }

    @Test
    void testReset() {
        service.addScore(new Score("BlockPuzzle", "Charlie", 300, new Date()));
        assertFalse(service.getTopScores("BlockPuzzle").isEmpty());

        service.reset();
        assertTrue(service.getTopScores("BlockPuzzle").isEmpty());
    }
}