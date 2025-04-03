package sk.tuke.kpi.kp.blockpuzzle;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import sk.tuke.kpi.kp.blockpuzzle.gamestudio.game.field.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BlockTest {

    @Test
    public void testBlockRotation() {
        List<int[]> shape = new ArrayList<>();
        shape.add(new int[]{1, 0});
        shape.add(new int[]{0, 1});
        Block block = new Block(shape);
        block.rotatingBlock();
        List<int[]> rotatedShape = block.getShape();
        boolean foundFirst = false;
        boolean foundSecond = false;
        for (int[] coord : rotatedShape) {
            if (coord[0] == 0 && coord[1] == 1) {
                foundFirst = true;
            }
            if (coord[0] == -1 && coord[1] == 0) {
                foundSecond = true;
            }
        }
        assertTrue(foundFirst && foundSecond, "Rotation did not produce expected coordinates.");
    }

    @Test
    public void testBlockMove() {
        List<int[]> shape = new ArrayList<>();
        shape.add(new int[]{0, 0});
        shape.add(new int[]{1, 1});
        Block block = new Block(shape);
        block.moveBlock(2, 3);
        List<int[]> movedShape = block.getShape();
        boolean foundFirst = false;
        boolean foundSecond = false;
        for (int[] coord : movedShape) {
            if (coord[0] == 2 && coord[1] == 3) {
                foundFirst = true;
            }
            if (coord[0] == 3 && coord[1] == 4) {
                foundSecond = true;
            }
        }
        assertTrue(foundFirst && foundSecond, "Move did not shift coordinates as expected.");
    }

    @Test
    public void testBlockCopy() {
        List<int[]> shape = new ArrayList<>();
        shape.add(new int[]{0, 0});
        Block block = new Block(shape);
        block.setColor(5);
        Block copy = block.copy();
        assertEquals(block.getColor(), copy.getColor(), "Block color in copy does not match.");
        block.moveBlock(1, 1);
        int[] originalCoord = block.getShape().get(0);
        int[] copyCoord = copy.getShape().get(0);
        assertNotEquals(originalCoord[0], copyCoord[0], "Coordinates in the copy should remain unchanged after move.");
        assertNotEquals(originalCoord[1], copyCoord[1], "Coordinates in the copy should remain unchanged after move.");
    }
}