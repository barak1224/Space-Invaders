package collisions;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import animations.GameLevel;
import biuoop.DrawSurface;
import geometryprimitives.Point;
import geometryprimitives.Rectangle;
import listeners.HitListener;
import sprites.Sprite;

/**
 * The class of the alien formation.
 * @author barak
 */
public class AlienFormation implements Sprite {
    private List<List<Alien>> aliens;
    private GameLevel g;
    private double dx;
    private double startDx;
    private Alien rightMost;
    private Alien leftMost;
    private Alien lowestAlien;

    /**
     * The constructor.
     * @param g - game level
     */
    public AlienFormation(GameLevel g) {
        this.aliens = new ArrayList<>();
        this.g = g;
        this.startDx = 1;
    }

    /**
     * The method setting dx.
     * @param newDx - new dx
     */
    public void setDx(double newDx) {
        this.dx = newDx;
    }

    /**
     * The method is building the alien formation.
     * @param htl - hitListener list
     * @param img - image
     * @param speed for the aliens
     */
    public void buildsAlienFormation(List<HitListener> htl, Image img, double speed) {
        this.dx = speed;
        this.startDx = speed;
        for (int i = 0; i < 10; i++) {
            List<Alien> columnI = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                Alien alien = new Alien(50 + i * 50, 50 + j * 40, htl, img);
                alien.addToGame(this.g);
                columnI.add(alien);
            }
            this.aliens.add(columnI);
        }
        this.checkLeftMost();
        this.checkRightMost();
    }

    /**
     * The method getting an alien and remove it from the formation.
     * @param alien as block
     */
    public void removeAlien(Block alien) {
        for (List<Alien> list : this.aliens) {
            if (list.contains(alien)) {
                list.remove(alien);
                if (alien.equals(this.leftMost)) {
                    this.leftMost = null;
                }
                if (alien.equals(this.rightMost)) {
                    this.rightMost = null;
                }
                if (alien.equals(this.lowestAlien)) {
                    this.lowestAlien = null;
                }
            }
        }
    }

    /**
     * The method choose a column of aliens to shoot the bullet.
     * @param eviornment
     */
    public void randomAlienShooting() {
        Random r = new Random();
        int i = r.nextInt(10);
        while (this.aliens.get(i).size() == 0) {
            i = r.nextInt(10);
        }
        List<Alien> column = this.aliens.get(i);
        g.shootingAlien(column.get(column.size() - 1));
    }

    /**
     * The method moving the formation.
     * @param dt - dt
     */
    public void moveFormation(double dt) {
        if (!checkAboveShileds()) {
            this.g.decreseLives(1);
            this.g.restFormation();
        }
        if (this.dx > 0) {
            this.checkRightMost();
        } else {
            this.checkLeftMost();
        }
        double newDx = this.dx * dt;
        for (List<Alien> list : this.aliens) {
            for (Block block : list) {
                block.moveOneStep(newDx, 0);
            }
        }
    }

    /**
     * The method checks the lowest alien.
     * @return boolean
     */
    public boolean checkAboveShileds() {
        for (List<Alien> column : this.aliens) {
            for (Alien alien : column) {
                if (this.lowestAlien == null) {
                    this.lowestAlien = alien;
                } else {
                    double lowestY = this.lowestAlien.getCollisionRectangle().getLowerLeft().getY();
                    double tempY = alien.getCollisionRectangle().getLowerLeft().getY();
                    if (lowestY < tempY) {
                        this.lowestAlien = alien;
                    }
                }
            }
        }
        double lowestY = this.lowestAlien.getCollisionRectangle().getLowerLeft().getY();
        return (lowestY <= 480);
    }

    /**
     * The method checking the rightMost current alien
     * and checks if he is not getting out the right bound.
     */
    public void checkRightMost() {
        if (this.rightMost == null) {
            int i = 1;
            List<Alien> column = this.aliens.get(this.aliens.size() - i);
            while (column.size() == 0 && column != null) {
                i++;
                column = this.aliens.get(this.aliens.size() - i);
            }
            this.rightMost = column.get(column.size() - 1);
        }
        if (this.rightMost != null) {
            Point upperRight = this.rightMost.getCollisionRectangle().getLowerRight();
            if (upperRight.getX() >= 800) {
                this.downFormation();
                this.dx *= -1.1;
            }
        }
    }

    /**
     * The method checking the leftMost current alien
     * and checks if he is not getting out the left bound.
     */
    public void checkLeftMost() {
        if (this.leftMost == null) {
            int i = 0;
            List<Alien> column = this.aliens.get(i);
            while (column.size() == 0 && column != null) {
                i++;
                column = this.aliens.get(i);
            }
            this.leftMost = column.get(0);
        }
        if (this.leftMost != null) {
            Point upperLeft = this.leftMost.getCollisionRectangle().getUpperLeft();
            if (upperLeft.getX() <= 0) {
                this.downFormation();
                this.dx *= -1.1;
            }
        }
    }

    /**
     * The method downing the formation by 5 in y coordinate.
     */
    public void downFormation() {
        for (List<Alien> cloumn : this.aliens) {
            for (Block alien : cloumn) {
                Rectangle rect = alien.getCollisionRectangle();
                alien.setRect(new Rectangle(rect.getUpperLeft().getX(), rect.getUpperLeft().getY() + 40,
                        rect.getWidth(), rect.getHeight()));
            }
        }
    }

    /**
     * The method finds the top most Y.
     * @return topMostY
     */
    public double findTopMostY() {
        double topMostY = 600;
        for (List<Alien> column : this.aliens) {
            for (Alien alien : column) {
                Point upperLeft = alien.getCollisionRectangle().getUpperLeft();
                if (upperLeft.getY() < topMostY) {
                    topMostY = upperLeft.getY();
                }
            }
        }
        return topMostY;
    }

    /**
     * The method resting the formation to the original set.
     */
    public void restFormation() {
        double topMostY = this.findTopMostY();
        this.checkLeftMost();
        double leftMostX = this.leftMost.getCollisionRectangle().getUpperLeft().getX();
        this.setDx(this.startDx);
        int restX = (int) Math.round(50.9 - leftMostX);
        int restY = (int) Math.round(50.0 - topMostY);
        for (List<Alien> columnI : this.aliens) {
            for (Alien alien : columnI) {
                alien.moveOneStep(restX, restY);
            }
        }
    }

    @Override
    public void drawOn(DrawSurface d) {
    }

    @Override
    public void timePassed(double dt) {
        this.moveFormation(dt);
    }
}
