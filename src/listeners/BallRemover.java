package listeners;

import animations.GameLevel;
import collisions.Ball;
import collisions.Block;
import collisions.Paddle;

/**
 * BlockRemover is in charge of removing blocks from the game, as well as
 * keeping count
 * of the number of blocks that remain.
 * @author Barak Talmor
 */
public class BallRemover implements HitListener {
    private GameLevel game;
    private Counter remainingBalls;

    /**
     * The constructor of the BlockRemover object.
     * @param game - the current game
     */
    public BallRemover(GameLevel game) {
        this.game = game;
    }

    /**
     * Blocks that are hit and reach 0 hit-points should be removed
     * from the game. Remember to remove this listener from the block
     * that is being removed from the game.
     * @param beingHit - block
     * @param hitter - ball
     */
    public void hitEvent(Block beingHit, Ball hitter) {
        hitter.removeFromGame(game);
    }

    @Override
    public void hitEvent(Paddle beingHit, Ball hitter) {
        hitter.removeFromGame(game);
    }

    /**
     * The method returns the remainingBlocks counter.
     * @return remainingBlocks
     */
    public Counter getRemainingBlocks() {
        return this.remainingBalls;
    }
}