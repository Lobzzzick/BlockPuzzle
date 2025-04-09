package sk.tuke.kpi.kp.blockpuzzle.gamestudio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import sk.tuke.kpi.kp.blockpuzzle.gamestudio.game.gamecore.ConsoleUI;
import sk.tuke.kpi.kp.blockpuzzle.gamestudio.game.field.Field;
import sk.tuke.kpi.kp.blockpuzzle.gamestudio.service.ScoreService;
import sk.tuke.kpi.kp.blockpuzzle.gamestudio.service.ScoreServiceJPA;
import sk.tuke.kpi.kp.blockpuzzle.gamestudio.service.CommentService;
import sk.tuke.kpi.kp.blockpuzzle.gamestudio.service.CommentServiceJPA;
import sk.tuke.kpi.kp.blockpuzzle.gamestudio.service.RatingService;
import sk.tuke.kpi.kp.blockpuzzle.gamestudio.service.RatingServiceJPA;

@SpringBootApplication
public class SpringClient {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);
    }


    @Bean
    public CommandLineRunner runner(ConsoleUI ui) {
        return args -> ui.start();
    }

    @Bean
    public ConsoleUI consoleUI() {
        return new ConsoleUI();
    }

    @Bean
    public Field field() {
        return new Field(10, 10);
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
