package listeners;

import animations.GameLevel;
import collisions.Ball;
import collisions.Block;

/**
 * The class of ShieldBlockRemover.
 * @author barak
 */
public class ShieldBlockRemover extends BlockRemover {
    private GameLevel game;

    /**
     * Constructor of ShieldBlockRemover.
     * @param game - game Level
     */
    public ShieldBlockRemover(GameLevel game) {
        super(game);
        this.game = game;
    }

    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        if (beingHit.getHitPoints() == 0) {
            beingHit.removeFromGame(this.game);
            beingHit.removeHitListener(this);
        }
    }
}