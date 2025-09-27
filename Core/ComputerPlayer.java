package Core;
import java.util.Random;

/**
 * Represents a computer-controlled player in the Snakes and Ladders game.
 *  *  @author Kit Siegel
 *  *  @version 1.0
 */
public class ComputerPlayer extends Player {
    private Random random;

    /**
     * Creates a new computer player.
     */
    public ComputerPlayer() {
        super("Computer", true);
        this.random = new Random();
    }

    /**
     * Simulates the computer's turn by rolling the dice.
     * @return The dice roll value (1-6)
     */
    public int takeTurn() {
        // Simulate thinking time (0.5-1.5 seconds)
        try {
            Thread.sleep(random.nextInt(1000) + 500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return random.nextInt(6) + 1;
    }
}
