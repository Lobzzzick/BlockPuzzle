package blockpuzzle.game.game.field;

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

    public boolean checkLine(int row) {
        if (row < 0 || row >= rows) return false;
        for (int c = 0; c < cols; c++) {
            if (blocks[row][c].getStone() == null) {
                return false;
            }
        }
        return true;
    }

    public void removeLine(int row) {
        if (row < 0 || row >= rows) return;
        for (int c = 0; c < cols; c++) {
            blocks[row][c].setStone(null);
        }
    }

    /**
     * Проверяет, что столбец col полностью заполнен (stone != null).
     */
    public boolean checkColumn(int col) {
        if (col < 0 || col >= cols) return false;
        for (int r = 0; r < rows; r++) {
            if (blocks[r][col].getStone() == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Удаляет все камни в столбце col (устанавливает stone = null).
     */
    public void removeColumn(int col) {
        if (col < 0 || col >= cols) return;
        for (int r = 0; r < rows; r++) {
            blocks[r][col].setStone(null);
        }
    }

    public boolean checkEmpty(int row, int col) {
        FieldBlock fb = getBlock(row, col);
        if (fb == null) return false;
        return (fb.getStone() == null);
    }
}