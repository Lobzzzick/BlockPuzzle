package sk.tuke.kpi.kp.blockpuzzle;

import sk.tuke.kpi.kp.blockpuzzle.block.Block;
import sk.tuke.kpi.kp.blockpuzzle.block.BlockState;
import sk.tuke.kpi.kp.blockpuzzle.field.Field;
import sk.tuke.kpi.kp.blockpuzzle.field.Stone;
import sk.tuke.kpi.kp.blockpuzzle.game.GameState;
import sk.tuke.kpi.kp.blockpuzzle.game.Score;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockPuzzle {
    private static final int DEFAULT_ROWS = 10;
    private static final int DEFAULT_COLS = 10;

    private GameState state;       // PLAYING / ENDEND
    private Field field;
    private Score score;

    // Список шаблонов фигур (7 классических тетрис-форм + блок 1x1)
    private List<Block> blockList;

    // 3 текущих (активных) блока
    private Block[] currentBlocks = new Block[3];

    // Сколько блоков из текущей тройки уже «поставлено»
    private int usedCount = 0;

    // Количество доступных поворотов
    private int rotationsLeft;

    public BlockPuzzle() {
        this.state = GameState.PLAYING;
        this.field = new Field(DEFAULT_ROWS, DEFAULT_COLS);
        this.score = new Score();
        // Изначально даём 7 поворотов
        this.rotationsLeft = 7;
        initBlockList();
        generateNextBlocks();
    }

    /**
     * Инициализация списка базовых фигур.
     */
    private void initBlockList() {
        this.blockList = new ArrayList<>();

        // 1) O (2x2) – уже был
        List<int[]> square = new ArrayList<>();
        square.add(new int[]{0, 0});
        square.add(new int[]{1, 0});
        square.add(new int[]{0, 1});
        square.add(new int[]{1, 1});
        blockList.add(new Block(square));

        // 2) L-образная (4-блочная) – уже была
        List<int[]> lShape4 = new ArrayList<>();
        lShape4.add(new int[]{0, 0});
        lShape4.add(new int[]{0, 1});
        lShape4.add(new int[]{0, 2});
        lShape4.add(new int[]{1, 2});
        blockList.add(new Block(lShape4));

        // 3) T-образная (4-блочная) – уже была
        List<int[]> tShape = new ArrayList<>();
        tShape.add(new int[]{0, 0});
        tShape.add(new int[]{1, 0});
        tShape.add(new int[]{2, 0});
        tShape.add(new int[]{1, 1});
        blockList.add(new Block(tShape));

        // 4) I-образная (линия из 4) – уже была
        List<int[]> iShape = new ArrayList<>();
        iShape.add(new int[]{0, 0});
        iShape.add(new int[]{1, 0});
        iShape.add(new int[]{2, 0});
        iShape.add(new int[]{3, 0});
        blockList.add(new Block(iShape));

        // 5) J-образная (4-блочная) – уже была
        List<int[]> jShape = new ArrayList<>();
        jShape.add(new int[]{0, 0});
        jShape.add(new int[]{0, 1});
        jShape.add(new int[]{0, 2});
        jShape.add(new int[]{-1, 2}); // или (1,2) в зависимости от «зеркала»
        blockList.add(new Block(jShape));

        // 6) S-образная (4-блочная) – уже была
        List<int[]> sShape = new ArrayList<>();
        sShape.add(new int[]{0, 1});
        sShape.add(new int[]{1, 1});
        sShape.add(new int[]{1, 0});
        sShape.add(new int[]{2, 0});
        blockList.add(new Block(sShape));

        // 7) Z-образная (4-блочная) – уже была
        List<int[]> zShape = new ArrayList<>();
        zShape.add(new int[]{0, 0});
        zShape.add(new int[]{1, 0});
        zShape.add(new int[]{1, 1});
        zShape.add(new int[]{2, 1});
        blockList.add(new Block(zShape));

        // 8) Блок 1x1 – уже был
        List<int[]> oneBlock = new ArrayList<>();
        oneBlock.add(new int[]{0, 0});
        blockList.add(new Block(oneBlock));

        // ==============================
        // Добавляем новые фигуры:
        // ==============================

        // 9) Двухблочная линия (2-блочная)
        List<int[]> twoBlock = new ArrayList<>();
        twoBlock.add(new int[]{0, 0});
        twoBlock.add(new int[]{1, 0});
        blockList.add(new Block(twoBlock));

        // 10) Трёхблочная линия
        List<int[]> threeLine = new ArrayList<>();
        threeLine.add(new int[]{0, 0});
        threeLine.add(new int[]{1, 0});
        threeLine.add(new int[]{2, 0});
        blockList.add(new Block(threeLine));

        // 11) Трёхблочная L (уголок из 3)
        // Пример: XX
        //         X
        List<int[]> threeL = new ArrayList<>();
        threeL.add(new int[]{0, 0});
        threeL.add(new int[]{1, 0});
        threeL.add(new int[]{0, 1});
        blockList.add(new Block(threeL));

        // 12) Фигура-крест (5-блочная)
        //  (1,0)
        // (0,1)(1,1)(2,1)
        //  (1,2)
        List<int[]> cross = new ArrayList<>();
        cross.add(new int[]{1, 0});
        cross.add(new int[]{0, 1});
        cross.add(new int[]{1, 1});
        cross.add(new int[]{2, 1});
        cross.add(new int[]{1, 2});
        blockList.add(new Block(cross));
    }

    /**
     * Генерация 3 новых блоков
     */
    public void generateNextBlocks() {
        Random rnd = new Random();
        for (int i = 0; i < 3; i++) {
            int index = rnd.nextInt(blockList.size());
            Block newBlock = blockList.get(index).copy();
            // Задаём случайный цвет 1..6
            newBlock.setColor(rnd.nextInt(6) + 1);
            currentBlocks[i] = newBlock;
        }
        usedCount = 0;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public Field getField() {
        return field;
    }

    public Score getScore() {
        return score;
    }

    public Block[] getCurrentBlocks() {
        return currentBlocks;
    }

    public int getRotationsLeft() {
        return rotationsLeft;
    }

    public boolean isGameOver() {
        return (this.state == GameState.ENDEND);
    }

    public void endGame() {
        this.state = GameState.ENDEND;
    }

    /**
     * Пытается разместить блок на поле.
     * Возвращает true, если блок успешно размещён.
     */
    public boolean moveBlock(Block block, int startRow, int startCol) {
        if (block == null) return false;

        // Проверяем, можно ли разместить блок (границы + свободные клетки)
        for (int[] coord : block.getShape()) {
            int r = startRow + coord[1];
            int c = startCol + coord[0];
            if (r < 0 || r >= field.getRows() || c < 0 || c >= field.getCols()) {
                return false;
            }
            if (!field.checkEmpty(r, c)) {
                return false;
            }
        }

        // «Фиксируем» блок на поле
        for (int[] coord : block.getShape()) {
            int r = startRow + coord[1];
            int c = startCol + coord[0];
            field.getBlock(r, c).setStone(new Stone(block.getColor()));
        }
        block.setBlockState(BlockState.FIXED);

        // Проверяем заполненные линии, начисляем очки и повороты
        int linesCleared = 0;
        for (int r = 0; r < field.getRows(); r++) {
            if (field.checkLine(r)) {
                field.removeLine(r);
                linesCleared++;
            }
        }
        if (linesCleared > 0) {
            score.addPoints(linesCleared * 100);
            rotationsLeft += linesCleared * 2;
        }
        return true;
    }

    /**
     * Размещает блок i (0..2) на поле в позиции (row, col).
     */
    public boolean placeBlockAtIndex(int i, int row, int col) {
        if (i < 0 || i >= 3) return false;
        if (currentBlocks[i] == null) return false;

        boolean success = moveBlock(currentBlocks[i], row, col);
        if (success) {
            currentBlocks[i] = null; // блок «исчезает»
            usedCount++;
            if (usedCount == 3) {
                generateNextBlocks();
            }
        }
        return success;
    }

    /**
     * Поворот блока i (0..2) влево (90° CCW).
     * Формула: (x, y) -> (y, -x).
     * Затем нормализуем координаты, чтобы не было отрицательных x,y.
     */
    public boolean rotateBlockLeftAtIndex(int i) {
        if (i < 0 || i >= 3) return false;
        if (currentBlocks[i] == null) return false;
        if (rotationsLeft <= 0) return false;

        Block block = currentBlocks[i];
        rotateLeft(block);
        normalizeShape(block); // чтобы координаты стали неотрицательными

        rotationsLeft--;
        return true;
    }

    /**
     * Поворот блока i (0..2) вправо (90° CW).
     * Формула (x, y) -> (-y, x).
     * Затем нормализуем координаты.
     */
    public boolean rotateBlockRightAtIndex(int i) {
        if (i < 0 || i >= 3) return false;
        if (currentBlocks[i] == null) return false;
        if (rotationsLeft <= 0) return false;

        Block block = currentBlocks[i];
        block.rotatingBlock(); // в Block.java (x, y) -> (-y, x)
        normalizeShape(block);

        rotationsLeft--;
        return true;
    }

    /**
     * Поворот влево (90° CCW) без изменения rotationsLeft.
     * (x, y) -> (y, -x)
     */
    private void rotateLeft(Block block) {
        List<int[]> shape = block.getShape();
        List<int[]> rotated = new ArrayList<>();
        for (int[] coord : shape) {
            int x = coord[0];
            int y = coord[1];
            rotated.add(new int[]{ y, -x });
        }
        shape.clear();
        shape.addAll(rotated);
    }

    private void normalizeShape(Block block) {
        List<int[]> shape = block.getShape();
        if (shape.isEmpty()) return;

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        for (int[] coord : shape) {
            if (coord[0] < minX) minX = coord[0];
            if (coord[1] < minY) minY = coord[1];
        }
        // Если уже все координаты >= 0, ничего не делаем
        if (minX >= 0 && minY >= 0) return;
        // Иначе сдвигаем все координаты, чтобы они стали неотрицательными
        for (int[] coord : shape) {
            coord[0] -= minX;
            coord[1] -= minY;
        }
    }

    public void updateGameState() {
        if (!canPlaceAnyBlock()) {
            System.out.println("No possible moves left.");
            this.state = GameState.ENDEND;
        }
    }

    private boolean canPlaceAnyBlock() {

        for (int i = 0; i < 3; i++) {
            Block block = currentBlocks[i];
            if (block == null) continue;

            // Пробуем все 4 ориентации
            for (int orientation = 0; orientation < 4; orientation++) {
                if (orientation > rotationsLeft) break;  // не хватает «поворотов»

                // Делаем копию
                Block copy = block.copy();
                // Повернём orientation раз влево
                for (int r = 0; r < orientation; r++) {
                    rotateLeft(copy);
                }
                // Нормализуем, чтобы не было отрицательных координат
                normalizeShape(copy);

                // Проверяем, можно ли copy где-то поставить
                if (canPlaceBlockAnywhere(copy)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canPlaceBlockAnywhere(Block block) {
        for (int row = 0; row < field.getRows(); row++) {
            for (int col = 0; col < field.getCols(); col++) {
                if (canPlaceBlock(block, row, col)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canPlaceBlock(Block block, int row, int col) {
        for (int[] coord : block.getShape()) {
            int r = row + coord[1];
            int c = col + coord[0];
            if (r < 0 || r >= field.getRows() || c < 0 || c >= field.getCols()) {
                return false;
            }
            if (!field.checkEmpty(r, c)) {
                return false;
            }
        }
        return true;
    }
}