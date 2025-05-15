package blockpuzzle.game.server.controller;

import blockpuzzle.game.entity.Comment;
import blockpuzzle.game.entity.Rating;
import blockpuzzle.game.entity.Score;
import blockpuzzle.game.service.CommentService;
import blockpuzzle.game.service.RatingService;
import blockpuzzle.game.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import blockpuzzle.game.game.gamecore.BlockPuzzle;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.IntStream;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
@SessionAttributes("theme")
public class BlockPuzzleController {

    /* konst */
    private static final String GAME = "BlockPuzzle";
    private static final Set<String> THEMES = Set.of("light","dark","fei","feidark");

    /* session stats*/
    private BlockPuzzle game = new BlockPuzzle();
    private String  nick       = "anonymous";
    private Integer selected   = null;
    private boolean scoreSaved = false;
    private String  theme      = "light";

    /* services */
    @Autowired private ScoreService scoreService;
    @Autowired private CommentService commentService;
    @Autowired private RatingService ratingService;

    @ModelAttribute("theme")
    public String exposeTheme() { return theme; }

    /* start menu*/
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/start")
    public String start(@RequestParam String nick) {
        if (!nick.isBlank()) this.nick = nick.trim();
        game = new BlockPuzzle(); selected = null; scoreSaved = false;
        return "redirect:/bpuzzle";
    }

    /* theme change */
    @GetMapping("/theme/{t}")
    public String setTheme(@PathVariable String t,
                           @RequestHeader(value = "referer", required = false) String ref,
                           HttpSession session) {
        if (THEMES.contains(t)) {
            this.theme = t;
            session.setAttribute("theme", t);
        }
        return "redirect:" + (ref != null ? ref : "/");
    }

    /*game page*/
    @GetMapping("/bpuzzle")
    public String play(@RequestParam(value="command", required=false) String cmd,
                       @RequestParam(value="i",       required=false) Integer i,
                       @RequestParam(value="row",     required=false) Integer r,
                       @RequestParam(value="col",     required=false) Integer c,
                       Model m) {

        switch (cmd==null?"":cmd) {
            case "rotateLeft"  -> { if (i != null) game.rotateBlockLeftAtIndex(i); }
            case "rotateRight" -> { if (i != null) game.rotateBlockRightAtIndex(i); }
            case "select"      -> { if (i != null) selected = i; }
            case "place" -> {
                if (selected!=null && r!=null && c!=null &&
                        game.placeBlockAtIndex(selected, r, c))
                    selected = null;
            }
        }

        game.updateGameState();
        if (game.isGameOver()) { saveScore(); return "redirect:/gameover"; }

        fillForGame(m);                                // кладём theme → сеанс
        return "bpuzzle";
    }


    @PostMapping("/bpuzzle/comment")
    public String addComment(@RequestParam String text) {
        if (!text.isBlank())
            commentService.addComment(new Comment(nick, GAME, text.trim(), new Date()));
        return "redirect:/bpuzzle";
    }

    @PostMapping("/bpuzzle/rate")
    public String rate(@RequestParam int stars) {
        if (stars>=1 && stars<=5)
            ratingService.setRating(new Rating(nick, GAME, stars, new Date()));
        return "redirect:/bpuzzle";
    }

    /*GAME OVER*/
    @GetMapping("/gameover")
    public String gameOver(Model m) {
        if (!game.isGameOver()) return "redirect:/bpuzzle";
        saveScore();
        m.addAttribute("finalScore", game.getScore().getPoints());
        fillService(m);
        m.addAttribute("theme", theme);
        return "gameover";
    }

    @PostMapping("/gameover/playAgain")
    public String playAgain() {
        game = new BlockPuzzle(); selected = null; scoreSaved = false;
        return "redirect:/bpuzzle";
    }

    @PostMapping("/gameover/comment")
    public String addCommentGO(@RequestParam String text) {
        if (!text.isBlank())
            commentService.addComment(new Comment(nick, GAME, text.trim(), new Date()));
        return "redirect:/gameover";
    }

    @PostMapping("/gameover/rate")
    public String rateGO(@RequestParam int stars) {
        if (stars>=1 && stars<=5)
            ratingService.setRating(new Rating(nick, GAME, stars, new Date()));
        return "redirect:/gameover";
    }

    /* support functions */
    private void saveScore() {
        if (scoreSaved) return;
        scoreService.addScore(new Score(GAME, nick, game.getScore().getPoints(), new Date()));
        scoreSaved = true;
    }

    private void fillForGame(Model m) {
        m.addAttribute("playerName", nick);
        m.addAttribute("selectedIdx", selected);
        m.addAttribute("field", game.getField());
        m.addAttribute("currentBlocks", Arrays.asList(game.getCurrentBlocks()));
        m.addAttribute("currentScore", game.getScore().getPoints());
        m.addAttribute("rotationsLeft", game.getRotationsLeft());
        m.addAttribute("gameOver", false);

        m.addAttribute("rowIdx",
                IntStream.range(0, game.getField().getRows()).boxed().toList());
        m.addAttribute("colIdx",
                IntStream.range(0, game.getField().getCols()).boxed().toList());

        m.addAttribute("theme", theme);
        fillService(m);
    }

    private void fillService(Model m) {
        m.addAttribute("topScores",     scoreService.getTopScores(GAME));
        m.addAttribute("comments",      commentService.getComments(GAME));
        m.addAttribute("averageRating", ratingService.getAverageRating(GAME));
        m.addAttribute("yourRating",    ratingService.getRating(GAME, nick));
    }
}