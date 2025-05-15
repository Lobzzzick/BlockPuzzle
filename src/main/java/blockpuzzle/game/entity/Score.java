package blockpuzzle.game.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "Score.getTopScores",
                query = "SELECT s FROM Score s WHERE s.game = :game ORDER BY s.points DESC"
        ),
        @NamedQuery(
                name = "Score.resetScores",
                query = "DELETE FROM Score"
        )
})
@IdClass(Score.ScoreId.class)
public class Score implements Serializable {

    @Id
    private String game;

    @Id
    private String player;

    @Id
    @Temporal(TemporalType.TIMESTAMP)            // <-- TIMESTAMP в основной сущности
    @Column(name = "playedon")
    private Date playedOn;

    private int points;

    public Score() {
    }

    public Score(String game, String player, int points, Date playedOn) {
        this.game = game;
        this.player = player;
        this.points = points;
        this.playedOn = playedOn;
    }

    // Getters & Setters
    public String getGame() {
        return game;
    }
    public void setGame(String game) {
        this.game = game;
    }

    public String getPlayer() {
        return player;
    }
    public void setPlayer(String player) {
        this.player = player;
    }

    public Date getPlayedOn() {
        return playedOn;
    }
    public void setPlayedOn(Date playedOn) {
        this.playedOn = playedOn;
    }

    public int getPoints() {
        return points;
    }
    public void setPoints(int points) {
        this.points = points;
    }

    // Вложенный класс идентификатора
    public static class ScoreId implements Serializable {
        private String game;
        private String player;

        @Temporal(TemporalType.TIMESTAMP)        // <-- Тоже TIMESTAMP
        private Date playedOn;

        public ScoreId() {
        }

        public ScoreId(String game, String player, Date playedOn) {
            this.game = game;
            this.player = player;
            this.playedOn = playedOn;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ScoreId)) return false;
            ScoreId that = (ScoreId) o;
            return Objects.equals(game, that.game)
                    && Objects.equals(player, that.player)
                    && Objects.equals(playedOn, that.playedOn);
        }

        @Override
        public int hashCode() {
            return Objects.hash(game, player, playedOn);
        }
    }
}