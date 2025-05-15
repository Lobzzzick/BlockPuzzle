package blockpuzzle.game.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import blockpuzzle.game.entity.Comment;
import blockpuzzle.game.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentServiceRest {

    @Autowired
    private CommentService commentService;

    @GetMapping("/{game}")
    public List<Comment> getComments(@PathVariable String game) {
        return commentService.getComments(game);
    }

    @PostMapping
    public void addComment(@RequestBody Comment comment) {
        commentService.addComment(comment);
    }
}