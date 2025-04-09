package sk.tuke.kpi.kp.blockpuzzle.gamestudio.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "Rating.findRating",
                query = "SELECT r FROM Rating r WHERE r.game = :game AND r.player = :player"
        ),
        @NamedQuery(
                name = "Rating.getAverageRating",
                query = "SELECT AVG(r.rating) FROM Rating r WHERE r.game = :game"
        ),
        @NamedQuery(
                name = "Rating.getRating",
                query = "SELECT r.rating FROM Rating r WHERE r.game = :game AND r.player = :player"
        ),
        @NamedQuery(
                name = "Rating.resetRatings",
                query = "DELETE FROM Rating"
        )
})
@IdClass(Rating.RatingId.class)
public class Rating implements Serializable {

    @Id
    private String player;

    @Id
    private String game;

    private int rating;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ratedon")
    private Date ratedOn;

    public Rating() {
    }

    public Rating(String player, String game, int rating, Date ratedOn) {
        this.player = player;
        this.game = game;
        this.rating = rating;
        this.ratedOn = ratedOn;
    }

    // Getters & Setters
    public String getPlayer() {
        return player;
    }
    public void setPlayer(String player) {
        this.player = player;
    }

    public String getGame() {
        return game;
    }
    public void setGame(String game) {
        this.game = game;
    }

    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getRatedOn() {
        return ratedOn;
    }
    public void setRatedOn(Date ratedOn) {
        this.ratedOn = ratedOn;
    }

    // Вложенный класс идентификатора
    public static class RatingId implements Serializable {
        private String player;
        private String game;

        public RatingId() {
        }

        public RatingId(String player, String game) {
            this.player = player;
            this.game = game;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof RatingId)) return false;
            RatingId that = (RatingId) o;
            return Objects.equals(player, that.player)
                    && Objects.equals(game, that.game);
        }

        @Override
        public int hashCode() {
            return Objects.hash(player, game);
        }
    }
}