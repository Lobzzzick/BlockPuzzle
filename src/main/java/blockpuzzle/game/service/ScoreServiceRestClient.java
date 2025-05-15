package blockpuzzle.game.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import blockpuzzle.game.entity.Score;

import java.util.Arrays;
import java.util.List;

@Component
public class ScoreServiceRestClient implements ScoreService {
    private final String url = "http://localhost:8080/api/score";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void addScore(Score score) {
        restTemplate.postForEntity(url, score, Score.class);
    }

    @Override
    public List<Score> getTopScores(String game) {
        //GET http://localhost:8080/api/score/{game}
        Score[] response = restTemplate.getForObject(url + "/" + game, Score[].class);
        return Arrays.asList(response != null ? response : new Score[0]);
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}