package sk.tuke.kpi.kp.blockpuzzle;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import sk.tuke.kpi.kp.blockpuzzle.field.Field;
import sk.tuke.kpi.kp.blockpuzzle.field.FieldBlock;
import sk.tuke.kpi.kp.blockpuzzle.field.Stone;

public class FieldTest {

    @Test
    public void testCheckEmptyAndSetStone() {
        Field field = new Field(5, 5);
        assertTrue(field.checkEmpty(2, 2));
        FieldBlock fb = field.getBlock(2, 2);
        fb.setStone(new Stone(3));
        assertFalse(field.checkEmpty(2, 2));
    }

    @Test
    public void testCheckLine() {
        Field field = new Field(3, 3);
        assertFalse(field.checkLine(1));
        for (int c = 0; c < field.getCols(); c++) {
            field.getBlock(1, c).setStone(new Stone(2));
        }
        assertTrue(field.checkLine(1));
    }

    @Test
    public void testRemoveLine() {
        Field field = new Field(3, 3);
        for (int c = 0; c < field.getCols(); c++) {
            field.getBlock(0, c).setStone(new Stone(4));
        }
        field.removeLine(0);
        for (int c = 0; c < field.getCols(); c++) {
            assertNull(field.getBlock(0, c).getStone());
        }
    }
}