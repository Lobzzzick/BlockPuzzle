package sk.tuke.kpi.kp.blockpuzzle;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import blockpuzzle.game.game.field.block.Block;
import blockpuzzle.game.game.field.block.BlockState;
import blockpuzzle.game.game.field.Field;
import blockpuzzle.game.game.field.Stone;
import blockpuzzle.game.game.gamecore.BlockPuzzle;
import blockpuzzle.game.game.gamecore.GameState;
import java.util.ArrayList;
import java.util.List;

public class BlockPuzzleTest {

    @Test
    public void testMoveBlockSuccess() {
        BlockPuzzle puzzle = new BlockPuzzle();
        List<int[]> shape = new ArrayList<>();
        shape.add(new int[]{0, 0});
        Block block = new Block(shape);
        block.setColor(1);
        boolean placed = puzzle.moveBlock(block, 5, 5);
        assertTrue(placed);
        Stone stone = puzzle.getField().getBlock(5, 5).getStone();
        assertNotNull(stone);
        assertEquals(1, stone.getColor());
        assertEquals(BlockState.FIXED, block.getBlockState());
    }

    @Test
    public void testMoveBlockFailureOutOfBounds() {
        BlockPuzzle puzzle = new BlockPuzzle();
        List<int[]> shape = new ArrayList<>();
        shape.add(new int[]{0, 0});
        Block block = new Block(shape);
        boolean placed = puzzle.moveBlock(block, -1, 0);
        assertFalse(placed);
    }

    @Test
    public void testPlaceBlockAtIndex() {
        BlockPuzzle puzzle = new BlockPuzzle();
        Block block = puzzle.getCurrentBlocks()[0];
        boolean placed = puzzle.placeBlockAtIndex(0, 0, 0);
        assertTrue(placed);
        assertNull(puzzle.getCurrentBlocks()[0]);
    }

    @Test
    public void testRotateBlockLeftAndRight() {
        BlockPuzzle puzzle = new BlockPuzzle();
        int initialRotations = puzzle.getRotationsLeft();
        boolean rotatedLeft = puzzle.rotateBlockLeftAtIndex(0);
        assertTrue(rotatedLeft);
        assertEquals(initialRotations - 1, puzzle.getRotationsLeft());
        if (puzzle.getCurrentBlocks()[1] != null) {
            boolean rotatedRight = puzzle.rotateBlockRightAtIndex(1);
            assertTrue(rotatedRight);
            assertEquals(initialRotations - 2, puzzle.getRotationsLeft());
        }
    }

    @Test
    public void testScoreAndRotationsAfterClearingLine() {
        BlockPuzzle puzzle = new BlockPuzzle();
        Field field = puzzle.getField();
        int rowToFill = 0;
        for (int c = 0; c < field.getCols(); c++) {
            field.getBlock(rowToFill, c).setStone(new Stone(1));
        }
        int initialScore = puzzle.getScore().getPoints();
        int initialRotations = puzzle.getRotationsLeft();
        List<int[]> shape = new ArrayList<>();
        shape.add(new int[]{0, 0});
        Block block = new Block(shape);
        block.setColor(2);
        boolean placed = puzzle.moveBlock(block, 1, 1);
        assertTrue(placed);
        assertEquals(initialScore + 100, puzzle.getScore().getPoints());
        assertEquals(initialRotations + 2, puzzle.getRotationsLeft());
    }

    @Test
    public void testUpdateGameStateGameOver() {
        BlockPuzzle puzzle = new BlockPuzzle();
        Field field = puzzle.getField();
        for (int r = 0; r < field.getRows(); r++) {
            for (int c = 0; c < field.getCols(); c++) {
                field.getBlock(r, c).setStone(new Stone(1));
            }
        }
        puzzle.updateGameState();
        assertEquals(GameState.ENDEND, puzzle.getState());
    }
}