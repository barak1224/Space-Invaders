import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import animations.AnimationRunner;
import animations.HighScoresAnimation;
import animations.Menu;
import animations.MenuAnimation;
import animations.QuitTask;
import animations.ShowHiScoresTask;
import biuoop.GUI;
import biuoop.KeyboardSensor;
import io.HighScoresTable;
import io.LevelsTask;
import io.Task;

/**
 * The ass3 assignment starter.
 * @author Barak Talmor
 */
public class Ass7Game {
    static final int LIVES = 3;

    /**
     * The method start the game.
     * @param args none
     * @throws IOException - exception
     */
    public static void main(String[] args) {
        Image logo = loadLogo();
        GUI gui = new GUI("Space Invaders", 800, 600);
        AnimationRunner ar = new AnimationRunner(gui);
        KeyboardSensor ks = ar.getGui().getKeyboardSensor();
        Menu<Task<Void>> menu = new MenuAnimation<>("Arknoid", ar, ks, logo);
        menu.addSelection("s", "Start Game", new LevelsTask(ar, ks, LIVES));
        HighScoresTable hST = new HighScoresTable(5);
        menu.addSelection("h", "High Scores", new ShowHiScoresTask(ar, new HighScoresAnimation(hST)));
        menu.addSelection("q", "Quit", new QuitTask());
        while (true) {
            ar.run(menu);
            Task<Void> status = menu.getStatus();
            status.run();
            menu.resetStop();
        }
    }

    /**
     * The method loading the image of the logo.
     * @return logo
     */
    public static Image loadLogo() {
        Image logo = null;
        try {
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("logo.png");
            logo = ImageIO.read(is);
        } catch (IOException e) {
            ;
        }
        return logo;
    }
}
