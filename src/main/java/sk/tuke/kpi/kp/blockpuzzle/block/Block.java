package sk.tuke.kpi.kp.blockpuzzle.block;

import java.util.ArrayList;
import java.util.List;

public class Block {
    private BlockState blockState;  // FIXED / IDLE / SELECTED
    private List<int[]> shape;      // Список координат (x, y) блока
    private int color;              // Цвет блока

    public Block(List<int[]> shape) {
        this.shape = shape;
        this.blockState = BlockState.IDLE;
        this.color = 1; // Значение по умолчанию, будет переопределено при генерации
    }

    public BlockState getBlockState() {
        return blockState;
    }

    public void setBlockState(BlockState blockState) {
        this.blockState = blockState;
    }

    public List<int[]> getShape() {
        return shape;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    /**
     * Поворот блока на 90 градусов (x, y) -> (-y, x).
     */
    public void rotatingBlock() {
        List<int[]> rotated = new ArrayList<>();
        for (int[] coord : shape) {
            int x = coord[0];
            int y = coord[1];
            rotated.add(new int[]{-y, x});
        }
        this.shape = rotated;
    }

    /**
     * Сдвиг блока на dx по горизонтали и dy по вертикали.
     */
    public void moveBlock(int dx, int dy) {
        for (int[] coord : shape) {
            coord[0] += dx;
            coord[1] += dy;
        }
    }

    /**
     * Создаёт копию блока (с копированием цвета).
     */
    public Block copy() {
        List<int[]> copyShape = new ArrayList<>();
        for (int[] coord : shape) {
            copyShape.add(new int[]{coord[0], coord[1]});
        }
        Block copy = new Block(copyShape);
        copy.setBlockState(this.blockState);
        copy.setColor(this.color);
        return copy;
    }
}