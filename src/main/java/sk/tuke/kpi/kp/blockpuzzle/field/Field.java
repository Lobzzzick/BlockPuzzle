package sk.tuke.kpi.kp.blockpuzzle.field;

public class Field {
    private int rows;
    private int cols;
    private FieldBlock[][] blocks;
    private int selected;

    public Field(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.blocks = new FieldBlock[rows][cols];
        initField();
    }

    private void initField() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                blocks[r][c] = new FieldBlock();
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public FieldBlock getBlock(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return null;
        }
        return blocks[row][col];
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    /**
     * Проверка, что вся строка row заполнена (stone != null).
     */
    public boolean checkLine(int row) {
        if (row < 0 || row >= rows) return false;
        for (int c = 0; c < cols; c++) {
            if (blocks[row][c].getStone() == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Удалить все камни в строке row (установить stone = null).
     */
    public void removeLine(int row) {
        if (row < 0 || row >= rows) return;
        for (int c = 0; c < cols; c++) {
            blocks[row][c].setStone(null);
        }
    }



    /**
     * Проверить, что ячейка (row, col) пуста (stone == null).
     */
    public boolean checkEmpty(int row, int col) {
        FieldBlock fb = getBlock(row, col);
        if (fb == null) return false; // за пределами поля
        return (fb.getStone() == null);
    }
}