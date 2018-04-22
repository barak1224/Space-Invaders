package animations;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import biuoop.DrawSurface;
import biuoop.KeyboardSensor;

/**
 * The menuAnimation class.
 * @author Barak Talmor
 * @param <T> - the type of task that will receive
 */
public class MenuAnimation<T> implements Menu<T> {
    private String menuTitle;
    private KeyboardSensor keyboard;
    private List<SelectionInfo<T>> sIL;
    private boolean stop;
    private T currentReturnVal;
    private Image logo;

    /**
     * The constructor of the MenuAnimation.
     * @param menuTitle - the title of the menu
     * @param ar - for running the subMenu
     * @param keyboard - the keyboard sensor
     * @param logo - image of the logo
     */
    public MenuAnimation(String menuTitle, AnimationRunner ar, KeyboardSensor keyboard, Image logo) {
        this.menuTitle = menuTitle;
        this.keyboard = keyboard;
        this.sIL = new ArrayList<>();
        this.stop = false;
        this.currentReturnVal = null;
        this.logo = logo;
    }

    @Override
    public void doOneFrame(DrawSurface d, double dt) {
        d.setColor(Color.gray);
        d.fillRectangle(0, 0, 800, 600);
        d.drawImage(60, 20, this.logo);
        int i = 0;
        for (SelectionInfo<T> s : this.sIL) {
            d.setColor(Color.BLACK);
            d.drawText(260, 360 + i * 40, "(" + s.getKey() + ") " + s.getMessage(), 30);
            d.setColor(Color.BLUE);
            d.drawText(263, 360 + i * 40, "(" + s.getKey() + ") " + s.getMessage(), 30);
            i++;
        }
        for (SelectionInfo<T> s : this.sIL) {
            if (this.keyboard.isPressed(s.getKey())) {
                this.currentReturnVal = s.getReturnVal();
                this.stop = true;
                break;
            }
        }
    }

    @Override
    public boolean shouldStop() {
        return this.stop;
    }

    @Override
    public void addSelection(String key, String message, T returnVal) {
        this.sIL.add(new SelectionInfo<T>(key, message, returnVal));
    }

    @Override
    public void addSubMenu(String key, String message, Menu<T> subMenu) {
        this.sIL.add(new SelectionInfo<T>(key, message, subMenu));
    }

    @Override
    public T getStatus() {
        return this.currentReturnVal;
    }

    @Override
    public void resetStop() {
        this.stop = false;
    }

    /**
     * The method returns the menu title.
     * @return menuTitle
     */
    public String getMenuTitle() {
        return this.menuTitle;
    }
}
