package sk.tuke.kpi.kp.blockpuzzle.game;

public class Score {
    private int points;

    public Score() {
        this.points = 0;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int p) {
        this.points += p;
    }

    public void reset() {
        this.points = 0;
    }
}