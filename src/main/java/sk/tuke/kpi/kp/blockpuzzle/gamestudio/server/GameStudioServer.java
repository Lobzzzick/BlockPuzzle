package sk.tuke.kpi.kp.blockpuzzle.gamestudio.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import sk.tuke.kpi.kp.blockpuzzle.gamestudio.service.ScoreService;
import sk.tuke.kpi.kp.blockpuzzle.gamestudio.service.ScoreServiceJPA;
import sk.tuke.kpi.kp.blockpuzzle.gamestudio.service.CommentService;
import sk.tuke.kpi.kp.blockpuzzle.gamestudio.service.CommentServiceJPA;
import sk.tuke.kpi.kp.blockpuzzle.gamestudio.service.RatingService;
import sk.tuke.kpi.kp.blockpuzzle.gamestudio.service.RatingServiceJPA;

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