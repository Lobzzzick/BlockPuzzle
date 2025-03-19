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

    private GameState state;
    private Field field;
    private Score score;

    private List<Block> blockList;
    private Block[] currentBlocks = new Block[3];
    private int usedCount = 0;
    private int rotationsLeft;

    public BlockPuzzle() {
        this.state = GameState.PLAYING;
        this.field = new Field(DEFAULT_ROWS, DEFAULT_COLS);
        this.score = new Score();
        this.rotationsLeft = 7;
        initBlockList();
        generateNextBlocks();
    }

    private void initBlockList() {
        this.blockList = new ArrayList<>();

        List<int[]> square = new ArrayList<>();
        square.add(new int[]{0, 0});
        square.add(new int[]{1, 0});
        square.add(new int[]{0, 1});
        square.add(new int[]{1, 1});
        blockList.add(new Block(square));

        List<int[]> lShape4 = new ArrayList<>();
        lShape4.add(new int[]{0, 0});
        lShape4.add(new int[]{0, 1});
        lShape4.add(new int[]{0, 2});
        lShape4.add(new int[]{1, 2});
        blockList.add(new Block(lShape4));

        List<int[]> tShape = new ArrayList<>();
        tShape.add(new int[]{0, 0});
        tShape.add(new int[]{1, 0});
        tShape.add(new int[]{2, 0});
        tShape.add(new int[]{1, 1});
        blockList.add(new Block(tShape));

        List<int[]> iShape = new ArrayList<>();
        iShape.add(new int[]{0, 0});
        iShape.add(new int[]{1, 0});
        iShape.add(new int[]{2, 0});
        iShape.add(new int[]{3, 0});
        blockList.add(new Block(iShape));

        List<int[]> jShape = new ArrayList<>();
        jShape.add(new int[]{0, 0});
        jShape.add(new int[]{0, 1});
        jShape.add(new int[]{0, 2});
        jShape.add(new int[]{-1, 2});
        blockList.add(new Block(jShape));

        List<int[]> sShape = new ArrayList<>();
        sShape.add(new int[]{0, 1});
        sShape.add(new int[]{1, 1});
        sShape.add(new int[]{1, 0});
        sShape.add(new int[]{2, 0});
        blockList.add(new Block(sShape));

        List<int[]> zShape = new ArrayList<>();
        zShape.add(new int[]{0, 0});
        zShape.add(new int[]{1, 0});
        zShape.add(new int[]{1, 1});
        zShape.add(new int[]{2, 1});
        blockList.add(new Block(zShape));

        List<int[]> oneBlock = new ArrayList<>();
        oneBlock.add(new int[]{0, 0});
        blockList.add(new Block(oneBlock));

        // Additional pieces
        List<int[]> twoBlock = new ArrayList<>();
        twoBlock.add(new int[]{0, 0});
        twoBlock.add(new int[]{1, 0});
        blockList.add(new Block(twoBlock));

        List<int[]> threeLine = new ArrayList<>();
        threeLine.add(new int[]{0, 0});
        threeLine.add(new int[]{1, 0});
        threeLine.add(new int[]{2, 0});
        blockList.add(new Block(threeLine));

        List<int[]> threeL = new ArrayList<>();
        threeL.add(new int[]{0, 0});
        threeL.add(new int[]{1, 0});
        threeL.add(new int[]{0, 1});
        blockList.add(new Block(threeL));

        List<int[]> cross = new ArrayList<>();
        cross.add(new int[]{1, 0});
        cross.add(new int[]{0, 1});
        cross.add(new int[]{1, 1});
        cross.add(new int[]{2, 1});
        cross.add(new int[]{1, 2});
        blockList.add(new Block(cross));
    }

    public void generateNextBlocks() {
        Random rnd = new Random();
        for (int i = 0; i < 3; i++) {
            int index = rnd.nextInt(blockList.size());
            Block newBlock = blockList.get(index).copy();
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

    public boolean moveBlock(Block block, int startRow, int startCol) {
        if (block == null) return false;

        // Check if block can be placed
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

        // Place block
        for (int[] coord : block.getShape()) {
            int r = startRow + coord[1];
            int c = startCol + coord[0];
            field.getBlock(r, c).setStone(new Stone(block.getColor()));
        }
        block.setBlockState(BlockState.FIXED);

        // Check full rows and columns
        int linesCleared = 0;
        for (int r = 0; r < field.getRows(); r++) {
            if (field.checkLine(r)) {
                field.removeLine(r);
                linesCleared++;
            }
        }

        // Now check columns
        for (int c = 0; c < field.getCols(); c++) {
            if (field.checkColumn(c)) {
                field.removeColumn(c);
                linesCleared++;
            }
        }

        // Score points and rotations
        if (linesCleared > 0) {
            score.addPoints(linesCleared * 100);
            rotationsLeft += linesCleared * 2;
        }
        return true;
    }

    public boolean placeBlockAtIndex(int i, int row, int col) {
        if (i < 0 || i >= 3) return false;
        if (currentBlocks[i] == null) return false;

        boolean success = moveBlock(currentBlocks[i], row, col);
        if (success) {
            currentBlocks[i] = null;
            usedCount++;
            if (usedCount == 3) {
                generateNextBlocks();
            }
        }
        return success;
    }

    public boolean rotateBlockLeftAtIndex(int i) {
        if (i < 0 || i >= 3) return false;
        if (currentBlocks[i] == null) return false;
        if (rotationsLeft <= 0) return false;

        Block block = currentBlocks[i];
        rotateLeft(block);
        normalizeShape(block);
        rotationsLeft--;
        return true;
    }

    public boolean rotateBlockRightAtIndex(int i) {
        if (i < 0 || i >= 3) return false;
        if (currentBlocks[i] == null) return false;
        if (rotationsLeft <= 0) return false;

        Block block = currentBlocks[i];
        block.rotatingBlock();
        normalizeShape(block);
        rotationsLeft--;
        return true;
    }

    private void rotateLeft(Block block) {
        List<int[]> shape = block.getShape();
        List<int[]> rotated = new ArrayList<>();
        for (int[] coord : shape) {
            int x = coord[0];
            int y = coord[1];
            rotated.add(new int[]{y, -x});
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
        if (minX >= 0 && minY >= 0) return;
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

            for (int orientation = 0; orientation < 4; orientation++) {
                if (orientation > rotationsLeft) break;
                Block copy = block.copy();
                for (int r = 0; r < orientation; r++) {
                    rotateLeft(copy);
                }
                normalizeShape(copy);

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