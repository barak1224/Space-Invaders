package collisions;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import animations.GameLevel;
import geometryprimitives.Point;
import geometryprimitives.Rectangle;
import listeners.HitListener;

/**
 * The class which builds shields.
 * @author barak
 */
public class ShieldCreator {
    private GameLevel game;
    private List<HitListener> htl;

    /**
     * The constructor of the Shield Creator.
     * @param g game level
     * @param htl list of listeners
     */
    public ShieldCreator(GameLevel g, List<HitListener> htl) {
        this.game = g;
        this.htl = htl;
    }

    /**
     * The method builds the shield formation.
     * @param point - start point
     */
    public void buildShield(Point point) {
        List<Block> l = new ArrayList<Block>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 30; j++) {
                Block block = new Block(new Rectangle(point.getX() + j * 5, point.getY() + i * 5, 5, 5), Color.blue,
                        Color.black);
                l.add(block);
                block.addToGame(this.game);
                block.addHitListener(this.htl);
            }
        }
    }
}
