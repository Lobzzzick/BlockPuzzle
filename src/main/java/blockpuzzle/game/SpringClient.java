package blockpuzzle.game;

import blockpuzzle.game.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.client.RestTemplate;
import blockpuzzle.game.game.gamecore.ConsoleUI;
import blockpuzzle.game.game.field.Field;

@SpringBootApplication
@ComponentScan(
        basePackages = "blockpuzzle.game",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.REGEX,
                pattern = "sk.tuke.kpi.kp.blockpuzzle.gamestudio.server.*"
        )
)
public class SpringClient {

    public static void main(String[] args) {
        // Либо так:
        // SpringApplication.run(SpringClient.class, args);
        // Но если хотим без веб-порта (NONE):
        new SpringApplicationBuilder(SpringClient.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Field field() {
        return new Field(10, 10);
    }

    @Bean
    public ScoreService scoreService() {
        // Используем REST-клиент, обращающийся к http://localhost:8080/api/score
        return new ScoreServiceRestClient();
    }

    @Bean
    public CommentService commentService() {
        return new CommentServiceRestClient();
    }

    @Bean
    public RatingService ratingService() {
        return new RatingServiceRestClient();
    }

    @Bean
    public CommandLineRunner runner(ConsoleUI ui) {
        // Как только клиент поднялся, вызываем ui.start()
        return args -> ui.start();
    }
}