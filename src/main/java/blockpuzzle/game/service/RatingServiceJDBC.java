package blockpuzzle.game.service;

import blockpuzzle.game.entity.Rating;

import java.sql.*;

public class RatingServiceJDBC implements RatingService {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    private static final String INSERT =
            "INSERT INTO rating (player, game, rating, ratedon) VALUES (?, ?, ?, ?)";
    private static final String UPDATE =
            "UPDATE rating SET rating = ?, ratedon = ? WHERE player = ? AND game = ?";
    private static final String SELECT_AVG =
            "SELECT AVG(rating) FROM rating WHERE game = ?";
    private static final String SELECT_ONE =
            "SELECT rating FROM rating WHERE game = ? AND player = ?";
    private static final String DELETE =
            "DELETE FROM rating";
    private static final String CHECK_EXISTS =
            "SELECT rating FROM rating WHERE player = ? AND game = ?";

    @Override
    public void setRating(Rating rating) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            boolean exists;
            try (PreparedStatement psCheck = connection.prepareStatement(CHECK_EXISTS)) {
                psCheck.setString(1, rating.getPlayer());
                psCheck.setString(2, rating.getGame());
                try (ResultSet rs = psCheck.executeQuery()) {
                    exists = rs.next();
                }
            }

            if (exists) {
                try (PreparedStatement psUpdate = connection.prepareStatement(UPDATE)) {
                    psUpdate.setInt(1, rating.getRating());
                    psUpdate.setTimestamp(2, new Timestamp(rating.getRatedOn().getTime()));
                    psUpdate.setString(3, rating.getPlayer());
                    psUpdate.setString(4, rating.getGame());
                    psUpdate.executeUpdate();
                }
            } else {
                try (PreparedStatement psInsert = connection.prepareStatement(INSERT)) {
                    psInsert.setString(1, rating.getPlayer());
                    psInsert.setString(2, rating.getGame());
                    psInsert.setInt(3, rating.getRating());
                    psInsert.setTimestamp(4, new Timestamp(rating.getRatedOn().getTime()));
                    psInsert.executeUpdate();
                }
            }

        } catch (SQLException e) {
            throw new RatingException("Error setting rating in DB", e);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(SELECT_AVG)) {

            ps.setString(1, game);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double avg = rs.getDouble(1);
                    return (int)Math.round(avg);
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new RatingException("Error reading average rating from DB", e);
        }
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(SELECT_ONE)) {

            ps.setString(1, game);
            ps.setString(2, player);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new RatingException("Error reading rating from DB", e);
        }
    }

    @Override
    public void reset() throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = connection.createStatement()) {

            st.executeUpdate(DELETE);

        } catch (SQLException e) {
            throw new RatingException("Error deleting rating from DB", e);
        }
    }
}