package sk.tuke.kpi.kp.blockpuzzle.gamestudio.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "\"comment\"")  // указано имя таблицы с кавычками
@NamedQueries({
        @NamedQuery(
                name = "Comment.getComments",
                query = "SELECT c FROM Comment c WHERE c.game = :game ORDER BY c.commentedOn DESC"
        ),
        @NamedQuery(
                name = "Comment.resetComments",
                query = "DELETE FROM Comment"
        )
})
@IdClass(Comment.CommentId.class)
public class Comment implements Serializable {

    @Id
    private String player;

    @Id
    private String game;
//
    @Id
    @Temporal(TemporalType.TIMESTAMP)            // <-- TIMESTAMP в основной сущности
    @Column(name = "commentedon")
    private Date commentedOn;

    @Column(name = "comment")
    private String comment;

    public Comment() {
    }

    public Comment(String player, String game, String comment, Date commentedOn) {
        this.player = player;
        this.game = game;
        this.comment = comment;
        this.commentedOn = commentedOn;
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

    public Date getCommentedOn() {
        return commentedOn;
    }
    public void setCommentedOn(Date commentedOn) {
        this.commentedOn = commentedOn;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    // Вложенный класс идентификатора
    public static class CommentId implements Serializable {
        private String player;
        private String game;

        @Temporal(TemporalType.TIMESTAMP)        // <-- TIMESTAMP и здесь тоже
        private Date commentedOn;

        public CommentId() {
        }

        public CommentId(String player, String game, Date commentedOn) {
            this.player = player;
            this.game = game;
            this.commentedOn = commentedOn;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CommentId)) return false;
            CommentId that = (CommentId) o;
            return Objects.equals(player, that.player)
                    && Objects.equals(game, that.game)
                    && Objects.equals(commentedOn, that.commentedOn);
        }

        @Override
        public int hashCode() {
            return Objects.hash(player, game, commentedOn);
        }
    }
}