package blockpuzzle.game.service;

import blockpuzzle.game.entity.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentServiceJDBC implements CommentService {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    private static final String INSERT =
            "INSERT INTO \"comment\" (player, game, comment, commentedon) VALUES (?, ?, ?, ?)";
    private static final String SELECT =
            "SELECT player, game, comment, commentedon FROM \"comment\" WHERE game = ? ORDER BY commentedon DESC";
    private static final String DELETE =
            "DELETE FROM \"comment\"";

    @Override
    public void addComment(Comment comment) throws CommentException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(INSERT)) {

            ps.setString(1, comment.getPlayer());
            ps.setString(2, comment.getGame());
            ps.setString(3, comment.getComment());
            ps.setTimestamp(4, new Timestamp(comment.getCommentedOn().getTime()));
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new CommentException("Error inserting comment into DB", e);
        }
    }

    @Override
    public List<Comment> getComments(String game) throws CommentException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(SELECT)) {

            ps.setString(1, game);
            try (ResultSet rs = ps.executeQuery()) {
                List<Comment> comments = new ArrayList<>();
                while (rs.next()) {
                    Comment c = new Comment();
                    c.setPlayer(rs.getString(1));
                    c.setGame(rs.getString(2));
                    c.setComment(rs.getString(3));
                    c.setCommentedOn(new Date(rs.getTimestamp(4).getTime()));
                    comments.add(c);
                }
                return comments;
            }
        } catch (SQLException e) {
            throw new CommentException("Error reading comments from DB", e);
        }
    }

    @Override
    public void reset() throws CommentException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = connection.createStatement()) {

            st.executeUpdate(DELETE);

        } catch (SQLException e) {
            throw new CommentException("Error deleting comments from DB", e);
        }
    }
}