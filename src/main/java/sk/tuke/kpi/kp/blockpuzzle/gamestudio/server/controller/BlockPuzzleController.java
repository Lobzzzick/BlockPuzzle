package sk.tuke.kpi.kp.blockpuzzle.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.kpi.kp.blockpuzzle.gamestudio.entity.*;
import sk.tuke.kpi.kp.blockpuzzle.gamestudio.game.gamecore.BlockPuzzle;
import sk.tuke.kpi.kp.blockpuzzle.gamestudio.service.*;

import java.util.*;
import java.util.stream.IntStream;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)          // одна сессия — одна партия
public class BlockPuzzleController {

    private static final String GAME_NAME = "BlockPuzzle";

    /* --------‑ состояние сессии ‑-------- */
    private BlockPuzzle game        = new BlockPuzzle();
    private String      playerName  = "anonymous";
    private Integer     selectedIdx = null;
    private boolean     scoreSaved  = false;          // ⇐ новое: чтобы «не дублировать» запись

    /* --------‑ DI сервисы ‑-------- */
    @Autowired private ScoreService   scoreService;
    @Autowired private CommentService commentService;
    @Autowired private RatingService  ratingService;

    /* ---------- стартовое меню ---------- */
    @GetMapping("/")  public String menu() { return "index"; }

    @PostMapping("/start")
    public String start(@RequestParam String nick) {
        if (!nick.isBlank()) playerName = nick.trim();
        game = new BlockPuzzle();
        selectedIdx = null;
        scoreSaved  = false;
        return "redirect:/bpuzzle";
    }

    /* ---------- игровая страница ---------- */
    @GetMapping("/bpuzzle")
    public String bpuzzle(@RequestParam(value="command", required=false) String cmd,
                          @RequestParam(value="i",       required=false) Integer i,
                          @RequestParam(value="row",     required=false) Integer row,
                          @RequestParam(value="col",     required=false) Integer col,
                          Model model) {

        switch (cmd == null ? "" : cmd) {
            case "rotateLeft"  -> { if (i != null) game.rotateBlockLeftAtIndex(i); }
            case "rotateRight" -> { if (i != null) game.rotateBlockRightAtIndex(i); }
            case "select"      -> { if (i != null) selectedIdx = i; }
            case "place"       -> {
                if (selectedIdx != null && row != null && col != null &&
                        game.placeBlockAtIndex(selectedIdx, row, col)) {
                    selectedIdx = null;
                }
            }
        }

        game.updateGameState();

        /* ----- если ходов нет → фиксируем счёт и переходим на /gameover ----- */
        if (game.isGameOver()) {
            saveScoreOnce();
            return "redirect:/gameover";
        }

        fillModelForGame(model);
        return "bpuzzle";
    }

    /* ---------- GAME OVER ---------- */
    @GetMapping("/gameover")
    public String gameOver(Model model) {
        if (!game.isGameOver()) return "redirect:/bpuzzle";
        saveScoreOnce();                       // гарантируем запись

        model.addAttribute("finalScore", game.getScore().getPoints());
        fillServiceData(model);
        return "gameover";
    }

    @PostMapping("/gameover/playAgain")
    public String playAgain() {
        game = new BlockPuzzle();
        selectedIdx = null;
        scoreSaved  = false;
        return "redirect:/bpuzzle";
    }

    /* ---------- формы на странице gameover ---------- */
    @PostMapping("/gameover/comment")
    public String addComment(@RequestParam String text) {
        if (!text.isBlank())
            commentService.addComment(
                    new Comment(playerName, GAME_NAME, text.trim(), new Date()));
        return "redirect:/gameover";
    }

    @PostMapping("/gameover/rate")
    public String rate(@RequestParam int stars) {
        if (stars >= 1 && stars <= 5)
            ratingService.setRating(
                    new Rating(playerName, GAME_NAME, stars, new Date()));
        return "redirect:/gameover";
    }

    /* ---------- helpers ---------- */

    /** Записывает счёт в БД единожды. */
    private void saveScoreOnce() {
        if (scoreSaved) return;
        scoreService.addScore(new Score(
                GAME_NAME, playerName, game.getScore().getPoints(), new Date()));
        scoreSaved = true;
    }

    private void fillModelForGame(Model m) {
        m.addAttribute("playerName",   playerName);
        m.addAttribute("selectedIdx",  selectedIdx);
        m.addAttribute("field",        game.getField());
        m.addAttribute("currentBlocks", Arrays.asList(game.getCurrentBlocks()));
        m.addAttribute("currentScore",  game.getScore().getPoints());
        m.addAttribute("rotationsLeft", game.getRotationsLeft());
        m.addAttribute("gameOver",      false);

        m.addAttribute("rowIdx",
                IntStream.range(0, game.getField().getRows()).boxed().toList());
        m.addAttribute("colIdx",
                IntStream.range(0, game.getField().getCols()).boxed().toList());

        fillServiceData(m);
    }

    private void fillServiceData(Model m) {
        m.addAttribute("topScores",     scoreService.getTopScores(GAME_NAME));
        m.addAttribute("comments",      commentService.getComments(GAME_NAME));
        m.addAttribute("averageRating", ratingService.getAverageRating(GAME_NAME));
        m.addAttribute("yourRating",
                ratingService.getRating(GAME_NAME, playerName));
    }
}