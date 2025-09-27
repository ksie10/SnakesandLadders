package Core;

/**
 * Represents a player in the Snakes and Ladders game.
 *
 *  @author Kit Siegel
 *  @version 1.0
 */
public class Player {
    private String name;
    private int position;
    private boolean isComputer;

    /**
     * Creates a new player with the given name.
     * @param name The name of the player
     * @param isComputer Whether this player is a computer player
     */
    public Player(String name, boolean isComputer) {
        this.name = name;
        this.position = 0;  // Start at position 0 (before first square)
        this.isComputer = isComputer;
    }

    /**
     * Gets the player's current position.
     * @return The current position
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets the player's position.
     * @param position The new position
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Gets the player's name.
     * @return The player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if this player is a computer player.
     * @return true if computer player, false otherwise
     */
    public boolean isComputer() {
        return isComputer;
    }
}
