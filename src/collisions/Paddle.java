package collisions;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import animations.GameLevel;
import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.KeyboardSensor;
import geometryprimitives.Point;
import geometryprimitives.Rectangle;
import listeners.HitListener;
import sprites.Sprite;

/**
 * Class that includes methods and constructor for Paddle object.
 * @author Barak Talmor
 */
public class Paddle implements Sprite, Collidable, HitNotifier {
    private KeyboardSensor keyboard;
    private Rectangle rect;
    private Color color;
    private int speedPaddle;
    private List<HitListener> hitListeners;
    private Image img;

    /**
     * Constructor of paddle.
     * @param rect - the rectangle of the paddle
     * @param color - the color of the paddle
     * @param gui - the gui of the game
     * @param speedPaddle - the move speed for the paddle
     */
    public Paddle(Rectangle rect, Color color, GUI gui, int speedPaddle) {
        this.rect = rect;
        this.color = color;
        this.keyboard = gui.getKeyboardSensor();
        this.speedPaddle = speedPaddle;
        this.hitListeners = new ArrayList<HitListener>();
    }

    /**
     * Constructor of paddle.
     * @param rect - the rectangle of the paddle
     * @param img - image of the paddle
     * @param gui - the gui of the game
     * @param speedPaddle - the move speed for the paddle
     */
    public Paddle(Rectangle rect, Image img, GUI gui, int speedPaddle) {
        this.rect = rect;
        this.img = img;
        this.keyboard = gui.getKeyboardSensor();
        this.speedPaddle = speedPaddle;
        this.hitListeners = new ArrayList<HitListener>();
    }

    /**
     * The method move the paddle to the right.
     * @param dt - amount of seconds passed since the last frame
     */
    public void moveRight(double dt) {
        int speed = (int) (this.speedPaddle * dt);
        if (this.keyboard.isPressed(KeyboardSensor.RIGHT_KEY) && (this.rect.getLowerRight().getX() < 800 - speed)) {
            this.rect = new Rectangle(this.rect.getUpperLeft().getX() + speed, this.rect.getUpperLeft().getY(),
                    this.rect.getWidth(), this.rect.getHeight());
        }
        if (this.keyboard.isPressed(KeyboardSensor.RIGHT_KEY) && this.rect.getLowerRight().getX() >= 800) {
            this.rect = new Rectangle(775 - this.rect.getWidth(), this.rect.getUpperLeft().getY(), this.rect.getWidth(),
                    this.rect.getHeight());
        }
    }

    /**
     * The method move the paddle to the left.
     * @param dt - amount of seconds passed since the last frame
     */
    public void moveLeft(double dt) {
        int speed = (int) (this.speedPaddle * dt);
        if (this.keyboard.isPressed(KeyboardSensor.LEFT_KEY) && this.rect.getUpperLeft().getX() > speed) {
            this.rect = new Rectangle(this.rect.getUpperLeft().getX() - speed, this.rect.getUpperLeft().getY(),
                    this.rect.getWidth(), this.rect.getHeight());
        }
        if (this.keyboard.isPressed(KeyboardSensor.LEFT_KEY) && this.rect.getUpperLeft().getX() < 0) {
            this.rect = new Rectangle(25, this.rect.getUpperLeft().getY(), this.rect.getWidth(), this.rect.getHeight());
        }
    }

    /**
     * The method return the rectangle of the paddle.
     * @return rectangle
     */
    public Rectangle getCollisionRectangle() {
        return this.rect;
    }

    /**
     * The method operating the move of the paddle.
     * @param dt - amount of seconds passed since the last frame
     */
    public void timePassed(double dt) {
        this.moveLeft(dt);
        this.moveRight(dt);
    }

    /**
     * The method draws the ball.
     * @param d - the DrawSurface for drawing the paddle
     */
    public void drawOn(DrawSurface d) {
        if (this.color != null) {
            d.setColor(this.color);
            d.fillRectangle((int) this.rect.getUpperLeft().getX(), (int) this.rect.getUpperLeft().getY(),
                    (int) this.rect.getWidth(), (int) this.rect.getHeight());
            // Draws the frame of the paddle
            d.setColor(Color.BLACK);
            d.drawRectangle((int) this.rect.getUpperLeft().getX(), (int) this.rect.getUpperLeft().getY(),
                    (int) this.rect.getWidth(), (int) this.rect.getHeight());
        } else {
            d.drawImage((int) this.rect.getUpperLeft().getX(), (int) this.rect.getUpperLeft().getY(), this.img);
        }
    }

    /**
     * the method return new velocity to the object, depends where was the hit.
     * @param hitter - the ball that hits
     * @param collisionPoint - the location where was the hit
     * @param currentVelocity - velocity of the object
     * @return velocity - new velocity
     */
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        this.notifyHit(hitter);
        if (collisionPoint.isPointOnLine(this.rect.getUpperLine())) {
            return hitOnUpperLine(collisionPoint, currentVelocity);
        } else if (collisionPoint.isPointOnLine(this.rect.getLowerLine())) {
            return new Velocity(currentVelocity.getDx(), -currentVelocity.getDy());
        }
        if (collisionPoint.isPointOnLine(this.rect.getRightLine())
                || collisionPoint.isPointOnLine(this.rect.getLeftLine())) {
            return new Velocity(-currentVelocity.getDx(), currentVelocity.getDy());
        }
        return currentVelocity;
    }

    /**
     * The method dividing the upper line to 5 parts and return different
     * angles.
     * depends where the collision point was
     * @param collisionPoint - the location where was the hit
     * @param currentVelocity - velocity of the object
     * @return velocity - new one with another angle
     */
    public Velocity hitOnUpperLine(Point collisionPoint, Velocity currentVelocity) {
        Point leftCorner = this.rect.getUpperLeft();
        int paddleLength = (int) this.rect.getUpperLeft().distance(this.rect.getUppeRight());
        if (collisionPoint.distance(leftCorner) < paddleLength * ((1) / 5.0)) {
            return Velocity.fromAngleAndSpeed(300, currentVelocity.getSpeed());
        } else if (collisionPoint.distance(leftCorner) < paddleLength * ((2) / 5.0)) {
            return Velocity.fromAngleAndSpeed(330, currentVelocity.getSpeed());
        } else if (collisionPoint.distance(leftCorner) < paddleLength * ((3) / 5.0)) {
            return new Velocity(currentVelocity.getDx(), -currentVelocity.getDy());
        } else if (collisionPoint.distance(leftCorner) < paddleLength * ((4) / 5.0)) {
            return Velocity.fromAngleAndSpeed(30, currentVelocity.getSpeed());
        } else {
            return Velocity.fromAngleAndSpeed(60, currentVelocity.getSpeed());
        }
    }

    /**
     * Notify hit to the block.
     * @param hitter - the ball that hits
     */
    private void notifyHit(Ball hitter) {
        // Make a copy of the hitListeners before iterating over them.
        List<HitListener> listeners = new ArrayList<HitListener>(this.hitListeners);
        // Notify all listeners about a hit event:
        for (HitListener hl : listeners) {
            hl.hitEvent(this, hitter);
        }
    }

    /**
     * Add list of listeners to hit events.
     * @param hList - list of hitListener object
     */
    public void addHitListener(List<HitListener> hList) {
        this.hitListeners.addAll(hList);
    }

    /**
     * Add hl as a listener to hit events.
     * @param hl - hitListener object
     */
    public void addHitListener(HitListener hl) {
        this.hitListeners.add(hl);
    }

    /**
     * Remove hl from the list of listeners to hit events.
     * @param hl - hitListener object
     */
    public void removeHitListener(HitListener hl) {
        this.hitListeners.remove(hl);
    }

    /**
     * Method adds the block object to the game spirte list and to the
     * gameEnvironment.
     * @param game - the game object
     */
    public void addToGame(GameLevel game) {
        game.addSprite(this);
        game.addCollidable(this);
    }

    /**
     * Method removes the block object from the game spirte list and from the
     * gameEnvironment.
     * @param game - the game object
     */
    public void removeFromGame(GameLevel game) {
        game.removeCollidable(this);
        game.removeSprite(this);
    }
}