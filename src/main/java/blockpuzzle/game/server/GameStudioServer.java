package blockpuzzle.game.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import blockpuzzle.game.service.ScoreService;
import blockpuzzle.game.service.ScoreServiceJPA;
import blockpuzzle.game.service.CommentService;
import blockpuzzle.game.service.CommentServiceJPA;
import blockpuzzle.game.service.RatingService;
import blockpuzzle.game.service.RatingServiceJPA;

@SpringBootApplication
@EntityScan("sk.tuke.kpi.kp.blockpuzzle.gamestudio.entity")
public class GameStudioServer {

    public static void main(String[] args) {
        SpringApplication.run(GameStudioServer.class, args);
    }

    @Bean
    public ScoreService scoreService() {
        return new ScoreServiceJPA();
    }

    @Bean
    public CommentService commentService() {
        return new CommentServiceJPA();
    }

    @Bean
    public RatingService ratingService() {
        return new RatingServiceJPA();
    }
}