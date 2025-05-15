package blockpuzzle.game.service;

import blockpuzzle.game.entity.Rating;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class RatingServiceJPA implements RatingService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) throws RatingException {
        try {
            Rating existing = entityManager.createNamedQuery("Rating.findRating", Rating.class)
                    .setParameter("game", rating.getGame())
                    .setParameter("player", rating.getPlayer())
                    .getSingleResult();
            existing.setRating(rating.getRating());
            existing.setRatedOn(rating.getRatedOn());
        } catch (NoResultException e) {
            // Если записи нет, сохраняем новую
            entityManager.persist(rating);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        Double avg = (Double) entityManager.createNamedQuery("Rating.getAverageRating")
                .setParameter("game", game)
                .getSingleResult();
        return avg == null ? 0 : (int) Math.round(avg);
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try {
            Integer r = (Integer) entityManager.createNamedQuery("Rating.getRating")
                    .setParameter("game", game)
                    .setParameter("player", player)
                    .getSingleResult();
            return r;
        } catch (NoResultException e) {
            return 0;
        }
    }

    @Override
    public void reset() throws RatingException {
        entityManager.createNamedQuery("Rating.resetRatings").executeUpdate();
    }
}