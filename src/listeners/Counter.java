package listeners;
/**
 * The class which contains the method of the Counter object.
 * @author Barak Talmor
 *
 */
public class Counter {
    private int counter;

    /**
     * Constructor for Counter object.
     * @param counter for initialize
     */
    public Counter(int counter) {
        this.counter = counter;
    }

    /**
     * The method add number to current count.
     * @param number to add
     */
    public void increase(int number) {
        this.counter += number;
    }

    /**
     * The method subtract number from current count.
     * @param number to subtract
     */
    public void decrease(int number) {
        this.counter -= number;
    }

    /**
     * The method get current count.
     * @return the value of the counter
     */
    public int getValue() {
        return this.counter;
    }
}