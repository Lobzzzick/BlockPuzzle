package sk.tuke.kpi.kp.blockpuzzle;

import sk.tuke.kpi.kp.blockpuzzle.gamestudio.entity.Rating;
import sk.tuke.kpi.kp.blockpuzzle.gamestudio.service.RatingService;
import sk.tuke.kpi.kp.blockpuzzle.gamestudio.service.RatingServiceJDBC;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class RatingServiceJDBCTest {
    private RatingService service;

    @BeforeEach
    void setUp() {
        service = new RatingServiceJDBC();
        service.reset();
    }

    @Test
    void testSetRatingAndGetRating() {
        Rating r1 = new Rating("Greg", "BlockPuzzle", 3, new Date());
        service.setRating(r1);

        int ratingFromDb = service.getRating("BlockPuzzle", "Greg");
        assertEquals(3, ratingFromDb);

        // Обновляем рейтинг
        r1.setRating(5);
        service.setRating(r1);

        ratingFromDb = service.getRating("BlockPuzzle", "Greg");
        assertEquals(5, ratingFromDb);
    }

    @Test
    void testGetAverageRating() {
        service.setRating(new Rating("Helen", "BlockPuzzle", 4, new Date()));
        service.setRating(new Rating("Ivan", "BlockPuzzle", 2, new Date()));

        int avg = service.getAverageRating("BlockPuzzle");
        assertEquals(3, avg); // (4 + 2) / 2 = 3
    }

    @Test
    void testReset() {
        service.setRating(new Rating("John", "BlockPuzzle", 5, new Date()));
        assertEquals(5, service.getRating("BlockPuzzle", "John"));

        service.reset();
        assertEquals(0, service.getRating("BlockPuzzle", "John"));
        assertEquals(0, service.getAverageRating("BlockPuzzle"));
    }
}