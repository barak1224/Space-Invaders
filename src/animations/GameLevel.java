package animations;

import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import biuoop.KeyboardSensor;
import collisions.AlienFormation;
import collisions.Ball;
import collisions.Block;
import collisions.Collidable;
import collisions.GameEnvironment;
import collisions.Paddle;
import collisions.ShieldCreator;
import collisions.Velocity;
import geometryprimitives.Point;
import geometryprimitives.Rectangle;
import listeners.BlockRemover;
import listeners.BallRemover;
import listeners.Counter;
import listeners.HitListener;
import listeners.ScoreTrackingListener;
import listeners.ShieldBlockRemover;
import sprites.BackgroundLevel;
import sprites.LivesIndicator;
import sprites.NameIndicator;
import sprites.ScoreIndicator;
import sprites.Sprite;
import sprites.SpriteCollection;
import biuoop.DrawSurface;

/**
 * The class for creating the game for assignment 3.
 * @author Barak Talmor
 */
public class GameLevel implements Animation {
    private SpriteCollection sprites;
    private GameEnvironment environment;
    private Counter remainingBlocks;
    private Counter score;
    private Counter lives;
    private AnimationRunner runner;
    private boolean running;
    private Paddle paddle;
    private KeyboardSensor keyboard;
    private Counter levelNo;
    private AlienFormation aliens;
    private HitListener bulletRemover;
    private HitListener blockRemover;
    private HitListener scoreTrackingListener;
    private Map<String, Image> images;
    private long sleeperAlienShoot;
    private long sleeperPlayerShoot;
    private List<Ball> aliensBullets;
    private List<Ball> playerBullets;
    private double speed;

    /**
     * Constructor of the game.
     * @param k - keyboard sensor
     * @param aR - animation runner
     * @param score - the counter of the score
     * @param lives - the counter of the remaining lives
     * @param levels - counter
     */
    public GameLevel(KeyboardSensor k, AnimationRunner aR, Counter score, Counter lives, Counter levels) {
        this.sprites = new SpriteCollection();
        this.environment = new GameEnvironment();
        this.remainingBlocks = new Counter(50);
        this.score = score;
        this.lives = lives;
        this.running = true;
        this.runner = aR;
        this.keyboard = k;
        this.levelNo = levels;
        this.playerBullets = new ArrayList<>();
        this.aliensBullets = new ArrayList<>();
        this.speed = 80;
        this.images = new TreeMap<>();
        this.initilizeHitListeners();
        this.loadImages();
    }

    /**
     * The method initialize hit listeners.
     */
    public void initilizeHitListeners() {
        this.bulletRemover = new BallRemover(this);
        this.blockRemover = new BlockRemover(this, this.remainingBlocks, lives);
        this.scoreTrackingListener = new ScoreTrackingListener(this.score);
    }

    /**
     * The method reading the image and return it.
     */
    public void loadImages() {
        try {
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("alien.png");
            this.images.put("alien", ImageIO.read(is));
            is = ClassLoader.getSystemClassLoader().getResourceAsStream("space.jpg");
            this.images.put("background", ImageIO.read(is));
            is = ClassLoader.getSystemClassLoader().getResourceAsStream("ship.png");
            this.images.put("ship", ImageIO.read(is));
            is = ClassLoader.getSystemClassLoader().getResourceAsStream("playerShot.png");
            this.images.put("playerShot", ImageIO.read(is));
            is = ClassLoader.getSystemClassLoader().getResourceAsStream("enemyShot.png");
            this.images.put("alienShot", ImageIO.read(is));
            is = ClassLoader.getSystemClassLoader().getResourceAsStream("heart.png");
            this.images.put("heart", ImageIO.read(is));
        } catch (IOException e) {
            System.exit(1);
            ;
        }
    }

    /**
     * The method returns the aliens's bullets.
     * @return bullets
     */
    public List<Ball> getAliensBullents() {
        List<Ball> bullets = new ArrayList<>(this.aliensBullets);
        return bullets;
    }

    /**
     * The method removing the alien from the list.
     * @param alien as block
     */
    public void removeAlien(Block alien) {
        this.aliens.removeAlien(alien);
    }

    /**
     * The method gets collidable object and add it to the spriteCollection.
     * @param c - collidable object
     */
    public void addCollidable(Collidable c) {
        this.environment.addCollidable(c);
    }

    /**
     * The method gets collidable object and remove it to the list.
     * @param c - collidable object
     */
    public void removeCollidable(Collidable c) {
        this.environment.removeCollidable(c);
    }

    /**
     * The method gets sprite object and add it to the spriteCollection.
     * @param s - sprite object
     */
    public void addSprite(Sprite s) {
        this.sprites.addSprite(s);
        ;
    }

    /**
     * The method gets sprite object and remove it to the spriteCol.
     * @param s - sprite object
     */
    public void removeSprite(Sprite s) {
        this.sprites.removeSprite(s);
    }

    /**
     * The methods remove the paddle from the game.
     */
    public void removePaddle() {
        this.removeCollidable(this.paddle);
        this.removeSprite(this.paddle);
    }

    /**
     * The method return the number of remaining blocks in the level.
     * @return remainingBlocks.getValue()
     */
    public int getNumberOfRemainingBlocks() {
        return this.remainingBlocks.getValue();
    }

    /**
     * The method organize all indicator sprites and add it to the
     * game.
     */
    public void addIndicatorSprites() {
        Sprite scoreI = new ScoreIndicator(this.score);
        Sprite livesRemain = new LivesIndicator(this.lives, this.images.get("heart"));
        Sprite nameLevel = new NameIndicator("Battle: " + this.levelNo.getValue());
        this.sprites.addSprite(scoreI);
        this.sprites.addSprite(livesRemain);
        this.sprites.addSprite(nameLevel);
    }

    /**
     * Initialize a new game: create the Blocks and Ball (and Paddle)
     * and add them to the game.
     */
    public void initialize() {
        this.sprites.addSprite(new BackgroundLevel(this.images.get("background")));
        this.buildsBulletRemoverFrame();
        this.addIndicatorSprites();
        this.initializeAlienFormation();
        this.buildSheilds();
    }

    /**
     * The method builds the destroy bullet blocks.
     */
    public void buildsBulletRemoverFrame() {
        // Death region
        Block destroyBulletBlock1 = new Block(new Rectangle(new Point(0, 0), 850, 20), Color.GRAY, 0,
                this.bulletRemover);
        Block destroyBulletBlock2 = new Block(new Rectangle(new Point(-50, 610), 850, 5), Color.GRAY, 0,
                this.bulletRemover);
        destroyBulletBlock1.addToGame(this);
        destroyBulletBlock2.addToGame(this);
    }

    /**
     * The method initializing the shields.
     */
    public void buildSheilds() {
        ShieldCreator sc = new ShieldCreator(this, Arrays.asList(this.bulletRemover, new ShieldBlockRemover(this)));
        sc.buildShield(new Point(100, 480));
        sc.buildShield(new Point(350, 480));
        sc.buildShield(new Point(600, 480));
    }

    /**
     * The method builds the blocks and add them to the game.
     */
    public void initializeAlienFormation() {
        this.aliens = new AlienFormation(this);
        this.aliens.buildsAlienFormation(
                Arrays.asList(this.bulletRemover, this.blockRemover, this.scoreTrackingListener),
                this.images.get("alien"), this.speed * this.levelNo.getValue());
        this.sprites.addSprite(this.aliens);
    }

    /**
     * The method builds the balls and add them to the game.
     */
    public void buildsPaddle() {
        this.paddle = new Paddle(new Rectangle(370, 550, 60, 20), this.images.get("ship"), runner.getGui(), 420);
        paddle.addToGame(this);
        paddle.addHitListener(
                Arrays.asList(new BallRemover(this), new BlockRemover(this, this.remainingBlocks, this.lives)));
    }

    /**
     * The method create new bullet.
     */
    public void shootingPaddle() {
        Point upperLeft = this.paddle.getCollisionRectangle().getUpperLeft();
        Ball ball = new Ball(new Point(upperLeft.getX() + 30, upperLeft.getY() - 10), 3, this.images.get("playerShot"));
        ball.setVelocity(Velocity.fromAngleAndSpeed(0, 350));
        ball.addToGame(this);
        ball.setGameEnviornment(this.environment);
        this.playerBullets.add(ball);
    }

    /**
     * The lowest alien in the column shoots.
     * @param alien as block
     */
    public void shootingAlien(Block alien) {
        Point lowerLeft = alien.getCollisionRectangle().getLowerLeft();
        Ball ball = new Ball(new Point(lowerLeft.getX() + 20, lowerLeft.getY() + 10), 5, this.images.get("alienShot"));
        ball.setVelocity(Velocity.fromAngleAndSpeed(180, 350));
        ball.addToGame(this);
        ball.setGameEnviornment(this.environment);
        this.aliensBullets.add(ball);
    }

    /**
     * The method choosing random column to shoot.
     */
    public void randomEnemyShooting() {
        this.aliens.randomAlienShooting();
    }

    /**
     * The method reseting the position of the paddle.
     */
    public void resetPaddle() {
        this.removePaddle();
        this.buildsPaddle();
    }

    /**
     * The method reseting the formation.
     */
    public void restFormation() {
        if (this.lives.getValue() != 0) {
            this.removeAllBalls();
            this.resetPaddle();
            this.aliens.restFormation();
            this.runner.run(new CountdownAnimation(2, 3, this.sprites));
        }
    }

    /**
     * The method decreasing lives.
     * @param decrease - number of lives to decrease
     */
    public void decreseLives(int decrease) {
        this.lives.decrease(decrease);
    }

    /**
     * The method removes all balls.
     */
    public void removeAllBalls() {
        for (Ball ball : this.playerBullets) {
            ball.removeFromGame(this);
        }
        for (Ball ball : this.aliensBullets) {
            ball.removeFromGame(this);
        }
    }

    /**
     * The method returns true if the game should stop, otherwise false.
     * @return boolean
     */
    public boolean shouldStop() {
        return !this.running;
    }

    /**
     * The method charges on the logic of the game.
     * @param d - DrawSurface
     * @param dt - amount of seconds passed since the last frame
     */
    public void doOneFrame(DrawSurface d, double dt) {
        this.sprites.drawAllOn(d);
        this.sprites.notifyAllTimePassed(dt);
        if (System.currentTimeMillis() - this.sleeperAlienShoot > 500) {
            this.randomEnemyShooting();
            this.sleeperAlienShoot = System.currentTimeMillis();
        }
        if (this.keyboard.isPressed(KeyboardSensor.SPACE_KEY)
                && System.currentTimeMillis() - this.sleeperPlayerShoot > 350) {
            this.shootingPaddle();
            this.sleeperPlayerShoot = System.currentTimeMillis();
        }
        if (this.keyboard.isPressed("p") || this.keyboard.isPressed("P")) {
            this.runner.run(new KeyPressStoppableAnimation(this.keyboard, "space", new PauseScreen(this.keyboard)));
            this.runner.run(new CountdownAnimation(2, 3, this.sprites));
        }
        if (this.lives.getValue() == 0 || this.remainingBlocks.getValue() == 0) {
            this.running = false;
        }
    }

    /**
     * The method runs the game and start the animation loop.
     */
    public void playOneTurn() {
        this.buildsPaddle();
        this.runner.run(new CountdownAnimation(2, 3, this.sprites));
        this.running = true;
        this.sleeperAlienShoot = System.currentTimeMillis();
        this.sleeperPlayerShoot = System.currentTimeMillis();
        this.runner.run(this);
    }
}