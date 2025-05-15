package blockpuzzle.game.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import blockpuzzle.game.entity.Rating;

@Component
public class RatingServiceRestClient implements RatingService {
    private final String url = "http://localhost:8080/api/rating";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void setRating(Rating rating) {
        // POST http://localhost:8080/api/rating
        restTemplate.postForEntity(url, rating, Rating.class);
    }

    @Override
    public int getAverageRating(String game) {
        // GET http://localhost:8080/api/rating/average/{game}
        return restTemplate.getForObject(url + "/average/" + game, Integer.class);
    }

    @Override
    public int getRating(String game, String player) {
        // GET http://localhost:8080/api/rating/{game}/{player}
        return restTemplate.getForObject(url + "/" + game + "/" + player, Integer.class);
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}