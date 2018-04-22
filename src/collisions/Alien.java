package collisions;

import listeners.HitListener;

import java.awt.Image;
import java.util.List;

import geometryprimitives.Rectangle;

/**
 * The class of Aliens object.
 * @author barak
 */
public class Alien extends Block {

    /**
     * The constructor of Alien.
     * @param x - upperleft position
     * @param y - upperleft position
     * @param hList - listeners
     * @param img - image of the alien
     */
    public Alien(double x, double y, List<HitListener> hList, Image img) {
        super(x, y, img);
        this.addHitListener(hList);
    }

    @Override
    public void moveOneStep(double dx, double dy) {
        double x = this.getCollisionRectangle().getUpperLeft().getX() + dx;
        double y = this.getCollisionRectangle().getUpperLeft().getY() + dy;
        this.setRect(
                new Rectangle(x, y, this.getCollisionRectangle().getWidth(), this.getCollisionRectangle().getHeight()));
    }
}
