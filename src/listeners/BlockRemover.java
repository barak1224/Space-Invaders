package listeners;

import animations.GameLevel;
import collisions.Ball;
import collisions.Block;
import collisions.Paddle;

/**
 * a class of BlockRemover is in charge of removing blocks from the game, as
 * well as keeping count
 * of the number of blocks that remain.
 * @author Barak Talmor
 */
public class BlockRemover implements HitListener {
    private GameLevel game;
    private Counter remainingBlocks;
    private Counter lives;

    /**
     * The constructor of the BlockRemover object.
     * @param game - the current game
     * @param remainingBlocks - the blocks in the game
     * @param lives - lives in game
     */
    public BlockRemover(GameLevel game, Counter remainingBlocks, Counter lives) {
        this.game = game;
        this.remainingBlocks = remainingBlocks;
        this.lives = lives;
    }

    /**
     * The constructor of Block remover.
     * @param game gameLevel object
     */
    public BlockRemover(GameLevel game) {
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
        if (!game.getAliensBullents().contains(hitter)) {
            beingHit.removeFromGame(game);
            beingHit.removeHitListener(this);
            game.removeAlien(beingHit);
            remainingBlocks.decrease(1);
        } else {
            hitter.removeFromGame(game);
        }
    }

    /**
     * Blocks that are hit and reach 0 hit-points should be removed
     * from the game. Remember to remove this listener from the block
     * that is being removed from the game.
     * @param beingHit - block
     * @param hitter - ball
     */
    public void hitEvent(Paddle beingHit, Ball hitter) {
        this.lives.decrease(1);
        this.game.restFormation();
    }

    /**
     * The method returns the remainingBlocks counter.
     * @return remainingBlocks
     */
    public Counter getRemainingBlocks() {
        return this.remainingBlocks;
    }
}