package io;


import animations.AnimationRunner;
import biuoop.KeyboardSensor;
import listeners.Counter;

/**
 * The class of levelsTask.
 * @author Barak Talmorshh
 */
public class LevelsTask implements Task<Void> {
    private AnimationRunner ar;
    private KeyboardSensor ks;
    private int lives;

    /**
     * The constructor of LevelsTask.
     * @param ar - animationRunner
     * @param ks - keyboard sensor
     * @param lives - number of lives
     */
    public LevelsTask(AnimationRunner ar, KeyboardSensor ks, int lives) {
        this.ar = ar;
        this.ks = ks;
        this.lives = lives;

    }

    @Override
    public Void run() {
        GameFlow gf = new GameFlow(ar, ks, new Counter(this.lives));
        gf.runLevels();
        return null;
    }
}
