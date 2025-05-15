package blockpuzzle.game.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import blockpuzzle.game.entity.Comment;

import java.util.Arrays;
import java.util.List;

@Component
public class CommentServiceRestClient implements CommentService {
    private final String url = "http://localhost:8080/api/comment";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void addComment(Comment comment) {
        restTemplate.postForEntity(url, comment, Comment.class);
    }

    @Override
    public List<Comment> getComments(String game) {
        // GET http://localhost:8080/api/comment/{game}
        Comment[] response = restTemplate.getForObject(url + "/" + game, Comment[].class);
        return Arrays.asList(response != null ? response : new Comment[0]);
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}