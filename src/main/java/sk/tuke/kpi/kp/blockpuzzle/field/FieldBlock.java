package sk.tuke.kpi.kp.blockpuzzle.field;

/**
 * Ячейка игрового поля. Хранит "камень" (stone), если ячейка занята.
 */
public class FieldBlock {
    private Stone stone;

    public FieldBlock() {
        this.stone = null;
    }

    public Stone getStone() {
        return stone;
    }

    public void setStone(Stone stone) {
        this.stone = stone;
    }
}