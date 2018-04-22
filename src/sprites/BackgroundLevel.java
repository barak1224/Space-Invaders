package sprites;

import java.awt.Image;

import biuoop.DrawSurface;

/**
 * The class of BackgroundLevel.
 * @author Barak Talmor
 */
public class BackgroundLevel implements Sprite {
    private Image img;

    /**
     * Constructor.
     * @param img - the background image
     */
    public BackgroundLevel(Image img) {
        this.img = img;
    }

    @Override
    public void drawOn(DrawSurface d) {
        d.drawImage(0, 0, this.img);
    }

    @Override
    public void timePassed(double dt) {
    }
}
