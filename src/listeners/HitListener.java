package listeners;

import collisions.Ball;
import collisions.Block;
import collisions.Paddle;

/**
 * The interface of the listeners.
 * @author Barak Talmor
 */
public interface HitListener {
    /**
     * This method is called whenever the beingHit object is hit.
     * @param beingHit - the block oject
     * @param hitter - the Ball that's doing the hitting
     */
    void hitEvent(Block beingHit, Ball hitter);

    /**
     * This method is called whenever the beingHit object is hit.
     * @param beingHit - the block oject
     * @param hitter - the Ball that's doing the hitting
     */
    void hitEvent(Paddle beingHit, Ball hitter);
}
