package sk.tuke.kpi.kp.blockpuzzle;

import sk.tuke.kpi.kp.blockpuzzle.gamestudio.entity.Comment;
import sk.tuke.kpi.kp.blockpuzzle.gamestudio.service.CommentService;
import sk.tuke.kpi.kp.blockpuzzle.gamestudio.service.CommentServiceJDBC;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;

public class CommentServiceJDBCTest {
    private CommentService service;

    @BeforeEach
    void setUp() {
        service = new CommentServiceJDBC();
        service.reset();
    }

    @Test
    void testAddCommentWithoutOrder() {
        Comment c1 = new Comment("Dave", "BlockPuzzle", "Nice game!", new Date());
        Comment c2 = new Comment("Eve", "BlockPuzzle", "Challenging but fun", new Date());

        service.addComment(c1);
        service.addComment(c2);

        List<Comment> comments = service.getComments("BlockPuzzle");
        assertEquals(2, comments.size(), "waiting for comments to be added");

        boolean hasDave = false;
        boolean hasEve = false;
        for (Comment c : comments) {
            if ("Dave".equals(c.getPlayer())) {
                hasDave = true;
            }
            if ("Eve".equals(c.getPlayer())) {
                hasEve = true;
            }
        }
        assertTrue(hasDave, "may contain Dave comment");
        assertTrue(hasEve, "may contain Eve comment");
    }

    @Test
    void testReset() {
        service.addComment(new Comment("Frank", "BlockPuzzle", "Hello!", new Date()));
        assertFalse(service.getComments("BlockPuzzle").isEmpty());

        service.reset();
        assertTrue(service.getComments("BlockPuzzle").isEmpty());
    }
}