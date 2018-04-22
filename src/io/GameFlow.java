package io;

import java.io.File;
import java.io.IOException;

import biuoop.DialogManager;
import biuoop.KeyboardSensor;
import listeners.Counter;
import animations.AnimationRunner;
import animations.EndScreen;
import animations.GameLevel;
import animations.HighScoresAnimation;
import animations.KeyPressStoppableAnimation;
import animations.ScoreInfo;

/**
 * The class that charges on the moving between the levels.
 * @author barak
 */
public class GameFlow {
    private KeyboardSensor keyboard;
    private AnimationRunner animationRunner;
    private Counter lives;
    private Counter score;
    private HighScoresTable hST;
    private DialogManager dialog;
    private Counter levels;

    /**
     * The constructor of the gameFlow.
     * @param ar - the animation runner
     * @param ks - the keyboard sensor
     * @param lives counter
     */
    public GameFlow(AnimationRunner ar, KeyboardSensor ks, Counter lives) {
        this.score = new Counter(0);
        this.lives = lives;
        this.animationRunner = ar;
        this.keyboard = ks;
        this.hST = new HighScoresTable(5);
        this.levels = new Counter(0);
        this.dialog = this.animationRunner.getGui().getDialogManager();
        try {
            File f = new File("highscores.txt");
            if (!f.createNewFile()) {
                this.hST.load(f);
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    /**
     * The method reset the scores.
     */
    public void resetScores() {
        this.score = new Counter(0);
    }

    /**
     * The method runs the levels.
     */
    public void runLevels() {
        while (true) {
            this.levels.increase(1);
            GameLevel level = new GameLevel(this.keyboard, this.animationRunner, this.score, this.lives, this.levels);
            level.initialize();
            while (level.getNumberOfRemainingBlocks() > 0 && lives.getValue() > 0) {
                level.playOneTurn();
            }
            if (this.lives.getValue() == 0) {
                break;
            }
        }
        this.animationRunner.run(new KeyPressStoppableAnimation(this.keyboard, "space",
                new EndScreen(this.lives, this.score, this.keyboard)));
        if (this.hST.getRank(this.score.getValue()) <= this.hST.size()) {
            String name = dialog.showQuestionDialog("Name", "What is your name?", "");
            this.hST.add(new ScoreInfo(name, this.score.getValue()));
        }
        this.animationRunner
                .run(new KeyPressStoppableAnimation(this.keyboard, "space", new HighScoresAnimation(this.hST)));
        try {
            File f = new File("highscores.txt");
            this.hST.save(f);
        } catch (IOException e) {
            e.getStackTrace();
        }
    }
}
