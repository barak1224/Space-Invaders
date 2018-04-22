package listeners;
import collisions.Ball;
import collisions.Block;
import collisions.Paddle;

/**
 * The class of the ScoreTrackingListener.
 * @author Barak Talmor
 */
public class ScoreTrackingListener implements HitListener {
    private Counter currentScore;

    /**
     * The constructor of the ScoreTrackingListener.
     * @param scoreCounter - counter of score
     */
    public ScoreTrackingListener(Counter scoreCounter) {
        this.currentScore = scoreCounter;
    }

    /**
     * The method increase the score, 5 points for every hit
     * and more 10 points if the block was destroyed.
     * @param beingHit - the block
     * @param hitter - the ball
     */
    public void hitEvent(Block beingHit, Ball hitter) {
        if (beingHit.getHitPoints() == 0) {
            this.currentScore.increase(100);
        }
    }

    /**
     * The method returns the counter of the curentScore.
     * @return currentScore
     */
    public Counter getCurrentScore() {
        return this.currentScore;
    }

    @Override
    public void hitEvent(Paddle beingHit, Ball hitter) {
        ;
    }
}