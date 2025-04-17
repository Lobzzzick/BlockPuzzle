package sk.tuke.kpi.kp.blockpuzzle.gamestudio.game.gamecore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sk.tuke.kpi.kp.blockpuzzle.gamestudio.entity.Comment;
import sk.tuke.kpi.kp.blockpuzzle.gamestudio.entity.Rating;
import sk.tuke.kpi.kp.blockpuzzle.gamestudio.entity.Score;
import sk.tuke.kpi.kp.blockpuzzle.gamestudio.game.field.FieldBlock;
import sk.tuke.kpi.kp.blockpuzzle.gamestudio.game.field.block.Block;
import sk.tuke.kpi.kp.blockpuzzle.gamestudio.service.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@Component
public class ConsoleUI {
    // ANSI escape последовательности для цветов (без изменений)
    private static final String ANSI_RESET  = "\u001B[0m";
    private static final String ANSI_RED    = "\u001B[31m";
    private static final String ANSI_GREEN  = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE   = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN   = "\u001B[36m";
    private static final String ANSI_WHITE  = "\u001B[37m";

    // Сервисы
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;

    private static final String GAME_NAME = "BlockPuzzle";
    private String playerName = "anonymous";

    /**
     * Запуск консольного интерфейса.
     */
    public void start() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Block Puzzle (10x10, 3 active blocks) ===");
        System.out.println("Enter your name (for scoreboard): ");
        String nameInput = scanner.nextLine().trim();
        if (!nameInput.isEmpty()) {
            playerName = nameInput;
        }

        // Повторяем «партию» пока пользователь хочет играть заново
        while (true) {
            // Запускаем одну партию
            playOneGame(scanner);

            // После окончания игры спрашиваем «снова?»
            System.out.println();
            System.out.println("Type 'play again' to start a new game, or just press Enter to exit:");
            String answer = scanner.nextLine().trim();
            if (!answer.equalsIgnoreCase("play again")) {
                System.out.println("Thanks for playing! Goodbye.");
                break; // выходим окончательно
            }

            System.out.println("Starting a new game... Good luck!");
        }
    }

    /**
     * Запускает одну «партию» (один игровой цикл),
     * завершается либо при вводе 'exit', либо когда game.isGameOver().
     */
    private void playOneGame(Scanner scanner) {
        BlockPuzzle game = new BlockPuzzle();

        System.out.println("\nCommands:");
        System.out.println("  rotate i left       -> rotate block i 90° CCW");
        System.out.println("  rotate i right      -> rotate block i 90° CW");
        System.out.println("  select i r c        -> place block i at row=r, col=c");
        System.out.println("  scores              -> show top scores");
        System.out.println("  comment <text>      -> add a comment");
        System.out.println("  comments            -> show latest comments");
        System.out.println("  rate <1..5>         -> rate this game");
        System.out.println("  rating              -> show average rating");
        System.out.println("  exit                -> exit the game (end this round)");

        while (!game.isGameOver()) {
            game.updateGameState(); // проверяем, не закончилась ли игра
            if (game.isGameOver()) {
                break;
            }

            System.out.println("\nCurrent score: " + game.getScore().getPoints());
            System.out.println("Rotations left: " + game.getRotationsLeft());
            printField(game);
            System.out.println("Current blocks:");
            printBlocksSideBySide(game.getCurrentBlocks());

            System.out.print("Enter command: ");
            String line = scanner.nextLine().trim();

            if (line.equalsIgnoreCase("exit")) {
                // досрочный выход
                game.setState(GameState.ENDEND);
            }
            else if (line.equalsIgnoreCase("scores")) {
                showTopScores();
            }
            else if (line.equalsIgnoreCase("comments")) {
                showComments();
            }
            else if (line.startsWith("comment ")) {
                String text = line.substring("comment ".length()).trim();
                addComment(text);
            }
            else if (line.startsWith("rate ")) {
                String ratingStr = line.substring("rate ".length()).trim();
                setGameRating(ratingStr);
            }
            else if (line.equalsIgnoreCase("rating")) {
                showAverageRating();
            }
            else if (line.toLowerCase().startsWith("rotate")) {
                handleRotate(line, game);
            }
            else if (line.toLowerCase().startsWith("select")) {
                handleSelect(line, game);
            }
            else {
                System.out.println("Unknown command. Type 'exit' to quit.");
            }
        }

        // Игра завершена
        int finalPoints = game.getScore().getPoints();
        System.out.println("\nGame Over! Final score: " + finalPoints);
        saveFinalScore(finalPoints);
    }

    // --- Методы для работы с сервисами ---

    private void saveFinalScore(int points) {
        Score score = new Score(GAME_NAME, playerName, points, new Date());
        scoreService.addScore(score);
        System.out.println("Your score has been saved to the database!");
    }

    private void showTopScores() {
        List<Score> topScores = scoreService.getTopScores(GAME_NAME);
        if (topScores.isEmpty()) {
            System.out.println("No scores yet for this game.");
            return;
        }
        System.out.println("Top scores for " + GAME_NAME + ":");
        for (Score s : topScores) {
            System.out.printf("%s - %d points (%s)\n",
                    s.getPlayer(), s.getPoints(), s.getPlayedOn());
        }
    }

    private void addComment(String text) {
        if (text.isEmpty()) {
            System.out.println("Cannot add empty comment!");
            return;
        }
        Comment c = new Comment(playerName, GAME_NAME, text, new Date());
        commentService.addComment(c);
        System.out.println("Comment saved!");
    }

    private void showComments() {
        List<Comment> comments = commentService.getComments(GAME_NAME);
        if (comments.isEmpty()) {
            System.out.println("No comments yet for this game.");
            return;
        }
        System.out.println("Latest comments for " + GAME_NAME + ":");
        for (Comment c : comments) {
            System.out.printf("%s [%s]: %s\n",
                    c.getPlayer(), c.getCommentedOn(), c.getComment());
        }
    }

    private void setGameRating(String ratingStr) {
        try {
            int stars = Integer.parseInt(ratingStr);
            if (stars < 1 || stars > 5) {
                System.out.println("Rating must be between 1 and 5!");
                return;
            }
            Rating r = new Rating(playerName, GAME_NAME, stars, new Date());
            ratingService.setRating(r);
            System.out.println("Rating saved!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid rating. Use: rate <1..5>.");
        }
    }

    private void showAverageRating() {
        int avg = ratingService.getAverageRating(GAME_NAME);
        System.out.println("Average rating for " + GAME_NAME + " is: " + avg);
    }

    // --- Методы для обработки rotate/select ---

    private void handleRotate(String line, BlockPuzzle game) {
        String[] parts = line.split("\\s+");
        if (parts.length != 3) {
            System.out.println("Invalid command. Use: rotate i left/right");
            return;
        }
        try {
            int blockIndex = Integer.parseInt(parts[1]) - 1;
            String direction = parts[2];
            if (blockIndex < 0 || blockIndex >= 3) {
                System.out.println("Invalid block index (should be 1..3).");
                return;
            }
            if ("left".equalsIgnoreCase(direction)) {
                boolean rotated = game.rotateBlockLeftAtIndex(blockIndex);
                System.out.println(rotated ? "Block rotated left." : "Cannot rotate left.");
            } else if ("right".equalsIgnoreCase(direction)) {
                boolean rotated = game.rotateBlockRightAtIndex(blockIndex);
                System.out.println(rotated ? "Block rotated right." : "Cannot rotate right.");
            } else {
                System.out.println("Unknown direction. Use 'left' or 'right'.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid block index. Must be an integer.");
        }
    }

    private void handleSelect(String line, BlockPuzzle game) {
        String[] parts = line.split("\\s+");
        if (parts.length != 4) {
            System.out.println("Invalid command. Use: select i r c");
            return;
        }
        try {
            int blockIndex = Integer.parseInt(parts[1]) - 1;
            int row = Integer.parseInt(parts[2]);
            int col = Integer.parseInt(parts[3]);
            if (blockIndex < 0 || blockIndex >= 3) {
                System.out.println("Invalid block index (should be 1..3).");
                return;
            }
            boolean success = game.placeBlockAtIndex(blockIndex, row, col);
            System.out.println(success ? "Block placed successfully!" : "Cannot place block.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid numbers. Use: select i r c");
        }
    }

    // --- Методы для вывода поля и блоков ---

    private static void printField(BlockPuzzle game) {
        int rows = game.getField().getRows();
        int cols = game.getField().getCols();
        System.out.print("   ");
        for (int c = 0; c < cols; c++) {
            System.out.print(c + " ");
        }
        System.out.println();

        for (int r = 0; r < rows; r++) {
            System.out.printf("%2d ", r);
            for (int c = 0; c < cols; c++) {
                FieldBlock fb = game.getField().getBlock(r, c);
                if (fb != null && fb.getStone() != null) {
                    int color = fb.getStone().getColor();
                    String colorCode = getStoneColor(color);
                    System.out.print(colorCode + "X" + ANSI_RESET + " ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }

    private static char[][] createBlockPreview(Block block) {
        if (block == null) {
            return new char[][] { "USED".toCharArray() };
        }
        List<int[]> shape = block.getShape();
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
        for (int[] coord : shape) {
            int x = coord[0];
            int y = coord[1];
            if (x < minX) minX = x;
            if (x > maxX) maxX = x;
            if (y < minY) minY = y;
            if (y > maxY) maxY = y;
        }
        int width = maxX - minX + 1;
        int height = maxY - minY + 1;
        char[][] grid = new char[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grid[row][col] = '.';
            }
        }
        for (int[] coord : shape) {
            int x = coord[0] - minX;
            int y = coord[1] - minY;
            grid[y][x] = 'X';
        }
        return grid;
    }

    private static void printBlocksSideBySide(Block[] blocks) {
        List<char[][]> previews = new ArrayList<>();
        int maxHeight = 0;
        int globalMaxWidth = 0;

        for (Block block : blocks) {
            char[][] preview = (block == null)
                    ? new char[][] { "USED".toCharArray() }
                    : createBlockPreview(block);
            previews.add(preview);
            if (preview[0].length > globalMaxWidth) {
                globalMaxWidth = preview[0].length;
            }
            if (preview.length > maxHeight) {
                maxHeight = preview.length;
            }
        }

        StringBuilder labelLine = new StringBuilder();
        for (int i = 0; i < blocks.length; i++) {
            String label = "[" + (i + 1) + "]";
            int padLeft = (globalMaxWidth - label.length()) / 2;
            int padRight = globalMaxWidth - label.length() - padLeft;
            labelLine.append(repeatChar(' ', padLeft))
                    .append(label)
                    .append(repeatChar(' ', padRight))
                    .append("   ");
        }
        System.out.println(labelLine);

        for (int row = 0; row < maxHeight; row++) {
            StringBuilder line = new StringBuilder();
            for (int i = 0; i < previews.size(); i++) {
                char[][] preview = previews.get(i);
                Block block = blocks[i];
                String rowStr = (row < preview.length) ? new String(preview[row]) : "";

                int padLeft = (globalMaxWidth - rowStr.length()) / 2;
                if (padLeft < 0) padLeft = 0;
                int padRight = globalMaxWidth - rowStr.length() - padLeft;
                if (padRight < 0) padRight = 0;

                StringBuilder rowCentered = new StringBuilder();
                rowCentered.append(repeatChar(' ', padLeft))
                        .append(rowStr)
                        .append(repeatChar(' ', padRight));

                StringBuilder coloredRow = new StringBuilder();
                for (char ch : rowCentered.toString().toCharArray()) {
                    if (ch == 'X') {
                        String blockColor = (block != null) ? getStoneColor(block.getColor()) : ANSI_WHITE;
                        coloredRow.append(blockColor).append(ch).append(ANSI_RESET);
                    } else {
                        coloredRow.append(ch);
                    }
                }
                line.append(coloredRow).append("   ");
            }
            System.out.println(line);
        }
    }

    private static String getStoneColor(int color) {
        switch (color) {
            case 1: return ANSI_RED;
            case 2: return ANSI_GREEN;
            case 3: return ANSI_YELLOW;
            case 4: return ANSI_BLUE;
            case 5: return ANSI_PURPLE;
            case 6: return ANSI_CYAN;
            default: return ANSI_WHITE;
        }
    }

    private static String repeatChar(char ch, int count) {
        if (count < 0) {
            count = 0;
        }
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            sb.append(ch);
        }
        return sb.toString();
    }
}