package Core;

import java.util.Random;

/**
 * Handles the core game logic for Snakes and Ladders.
 *
 * @author Kit Siegel
 * @version 3.0
 */
public class GameLogic {
    private Board board;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Random random;
    private boolean gameOver;

    /**
     * Creates a new game with two players.
     * @param player1 The first player
     * @param player2 The second player
     * @throws IllegalArgumentException if either player is null
     */
    public GameLogic(Player player1, Player player2) {
        if (player1 == null || player2 == null) {
            throw new IllegalArgumentException("Players cannot be null");
        }
        this.board = new Board();
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;
        this.random = new Random();
        this.gameOver = false;
    }

    /**
     * Rolls the dice for the current player and updates their position.
     * @return The dice roll value
     * @throws IllegalStateException if the game is already over
     */
    public int rollDiceAndMove() {
        if (gameOver) {
            throw new IllegalStateException("Game is already over");
        }

        int roll;
        if (currentPlayer.isComputer() && currentPlayer instanceof ComputerPlayer) {
            roll = ((ComputerPlayer) currentPlayer).takeTurn();
        } else {
            roll = random.nextInt(6) + 1;
        }

        int newPosition = currentPlayer.getPosition() + roll;

        // Check if the player wins
        if (newPosition == board.getBoardSize()) {
            currentPlayer.setPosition(newPosition);
            gameOver = true;
            return roll;
        }

        // Check if player goes beyond the board
        if (newPosition > board.getBoardSize()) {
            return roll;
        }

        // Update position and check for snakes/ladders
        currentPlayer.setPosition(newPosition);
        int finalPosition = board.getFinalPosition(newPosition);
        currentPlayer.setPosition(finalPosition);

        return roll;
    }

    /**
     * Switches to the next player's turn.
     */
    public void switchPlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }

    /**
     * Gets the current player.
     * @return The current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Checks if the game is over.
     * @return true if the game is over
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Gets the game board.
     * @return The game board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Gets player 1.
     * @return Player 1
     */
    public Player getPlayer1() {
        return player1;
    }

    /**
     * Gets player 2.
     * @return Player 2
     */
    public Player getPlayer2() {
        return player2;
    }
}
