package Core;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the game board for Snakes and Ladders.
 * The board is 10x10 with snakes and ladders placed at fixed positions.
 * @author Kit Siegel
 * @version 2.0
 */
public class Board {
    private static final int BOARD_SIZE = 100;
    private Map<Integer, Integer> snakes;  // Key: head, Value: tail
    private Map<Integer, Integer> ladders; // Key: base, Value: top
    private Map<Integer, Integer> snakeNumbers;  // Key: head position, Value: snake number
    private Map<Integer, Integer> snakeTailNumbers;  // Key: tail position, Value: snake number
    private Map<Integer, Integer> ladderNumbers;  // Key: base position, Value: ladder number
    private Map<Integer, Integer> ladderTopNumbers;  // Key: top position, Value: ladder number

    /**
     * Creates a new game board with predefined snakes and ladders.
     */
    public Board() {
        initializeSnakesAndLadders();
    }

    /**
     * Sets up the snakes and ladders on the board.
     */
    private void initializeSnakesAndLadders() {
        snakes = new HashMap<>();
        ladders = new HashMap<>();
        snakeNumbers = new HashMap<>();
        snakeTailNumbers = new HashMap<>();
        ladderNumbers = new HashMap<>();
        ladderTopNumbers = new HashMap<>();

        // Initialize snakes (head -> tail)
        addSnake(97, 78, 1);  // S1 -> T1
        addSnake(95, 56, 2);  // S2 -> T2
        addSnake(88, 24, 3);  // S3 -> T3
        addSnake(62, 18, 4);  // S4 -> T4
        addSnake(48, 26, 5);  // S5 -> T5

        // Initialize ladders (base -> top)
        addLadder(4, 14, 1);   // L1 -> H1
        addLadder(8, 30, 2);   // L2 -> H2
        addLadder(21, 42, 3);  // L3 -> H3
        addLadder(28, 76, 4);  // L4 -> H4
        addLadder(50, 67, 5);  // L5 -> H5
    }

    private void addSnake(int head, int tail, int number) {
        snakes.put(head, tail);
        snakeNumbers.put(head, number);
        snakeTailNumbers.put(tail, number);
    }

    private void addLadder(int base, int top, int number) {
        ladders.put(base, top);
        ladderNumbers.put(base, number);
        ladderTopNumbers.put(top, number);
    }

    /**
     * Gets the final position after checking for snakes or ladders.
     * @param position The current position
     * @return The final position after applying snake or ladder effects
     */
    public int getFinalPosition(int position) {
        if (snakes.containsKey(position)) {
            return snakes.get(position);
        } else if (ladders.containsKey(position)) {
            return ladders.get(position);
        }
        return position;
    }

    /**
     * Gets the snake number at a position, if any.
     * @param position The position to check
     * @return The snake number, or 0 if no snake
     */
    public int getSnakeNumber(int position) {
        return snakeNumbers.getOrDefault(position, 0);
    }

    /**
     * Gets the snake tail number at a position, if any.
     * @param position The position to check
     * @return The snake tail number, or 0 if no snake tail
     */
    public int getSnakeTailNumber(int position) {
        return snakeTailNumbers.getOrDefault(position, 0);
    }

    /**
     * Gets the ladder number at a position, if any.
     * @param position The position to check
     * @return The ladder number, or 0 if no ladder
     */
    public int getLadderNumber(int position) {
        return ladderNumbers.getOrDefault(position, 0);
    }

    /**
     * Gets the ladder top number at a position, if any.
     * @param position The position to check
     * @return The ladder top number, or 0 if no ladder top
     */
    public int getLadderTopNumber(int position) {
        return ladderTopNumbers.getOrDefault(position, 0);
    }

    /**
     * Checks if a position has a snake.
     * @param position The position to check
     * @return true if there's a snake head at this position
     */
    public boolean hasSnake(int position) {
        return snakes.containsKey(position);
    }

    /**
     * Checks if a position has a snake tail.
     * @param position The position to check
     * @return true if there's a snake tail at this position
     */
    public boolean hasSnakeTail(int position) {
        return snakeTailNumbers.containsKey(position);
    }

    /**
     * Checks if a position has a ladder.
     * @param position The position to check
     * @return true if there's a ladder base at this position
     */
    public boolean hasLadder(int position) {
        return ladders.containsKey(position);
    }

    /**
     * Checks if a position has a ladder top.
     * @param position The position to check
     * @return true if there's a ladder top at this position
     */
    public boolean hasLadderTop(int position) {
        return ladderTopNumbers.containsKey(position);
    }

    /**
     * Gets the board size.
     * @return The size of the board
     */
    public int getBoardSize() {
        return BOARD_SIZE;
    }

    /**
     * Gets the snake mapping for display purposes.
     * @return Map of snake heads to tails
     */
    public Map<Integer, Integer> getSnakes() {
        return new HashMap<>(snakes);
    }

    /**
     * Gets the ladder mapping for display purposes.
     * @return Map of ladder bases to tops
     */
    public Map<Integer, Integer> getLadders() {
        return new HashMap<>(ladders);
    }
}
