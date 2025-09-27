package UI;

import Core.*;
import java.util.Map;
import java.util.Scanner;

/**
 * Handles the console-based user interface for the Snakes and Ladders game.
 *  *  @author Kit Siegel
 *  *  @version 2.0
 */
public class GameConsoleUI {
    private GameLogic game;
    private Scanner scanner;

    /**
     * Creates a new console UI instance.
     */
    public GameConsoleUI() {
        scanner = new Scanner(System.in);
    }

    /**
     * Starts the game by setting up players and initializing the game.
     */
    /**
     * Starts the console-based game.
     * @param vsComputer true if playing against computer, false if playing against another player
     */
    public void startGame(boolean vsComputer) {
        System.out.println("Welcome to Snakes and Ladders!");

        System.out.println("Enter Player 1 name:");
        String player1Name = scanner.nextLine().trim();
        Player player1 = new Player(player1Name, false);

        Player player2;
        if (vsComputer) {
            player2 = new ComputerPlayer();
            System.out.println("Player 2 is Computer");
        } else {
            System.out.println("Enter Player 2 name:");
            String player2Name = scanner.nextLine().trim();
            player2 = new Player(player2Name, false);
        }

        game = new GameLogic(player1, player2);
        playGame();
    }

    /**
     * Main game loop that handles player turns and displays the game state.
     */
    private void playGame() {
        while (!game.isGameOver()) {
            displayBoard();
            Player currentPlayer = game.getCurrentPlayer();

            System.out.println("\n" + currentPlayer.getName() + "'s turn");

            if (!currentPlayer.isComputer()) {
                System.out.println("Press Enter to roll the dice...");
                scanner.nextLine();
            }

            try {
                int roll = game.rollDiceAndMove();
                System.out.println(currentPlayer.getName() + " rolled a " + roll);

                int position = currentPlayer.getPosition();
                if (position == game.getBoard().getBoardSize()) {
                    System.out.println(currentPlayer.getName() + " has won the game!");
                    break;
                }

                System.out.println(currentPlayer.getName() + " moved to position " + position);

                // Check if player hit a snake or ladder
                Board board = game.getBoard();
                if (board.hasSnake(position)) {
                    System.out.println("Oops! Snake bite! Going down...");
                } else if (board.hasLadder(position)) {
                    System.out.println("Yay! Climbing up the ladder!");
                }

                game.switchPlayer();

            } catch (IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
                break;
            }
        }

        displayBoard();
    }

    /**
     * Displays the current state of the game board.
     */
    private void displayBoard() {
        Board board = game.getBoard();
        System.out.println("\n=== GAME BOARD ===");

        // Display board from top to bottom
        for (int row = 9; row >= 0; row--) {
            StringBuilder line = new StringBuilder();

            // Alternate direction for each row
            if (row % 2 == 0) {
                for (int col = 0; col < 10; col++) {
                    line.append(formatCell(row * 10 + col + 1, board));
                }
            } else {
                for (int col = 9; col >= 0; col--) {
                    line.append(formatCell(row * 10 + col + 1, board));
                }
            }

            System.out.println(line.toString());
        }

        // Display player positions
        System.out.println("\nPlayer Positions:");
        System.out.println("P1 - " + game.getPlayer1().getName() + ": " + game.getPlayer1().getPosition());
        System.out.println("P2 - " + game.getPlayer2().getName() + ": " + game.getPlayer2().getPosition());
    }

    /**
     * Formats a cell on the board with appropriate markers for snakes and ladders.
     */
    private String formatCell(int number, Board board) {
        StringBuilder cell = new StringBuilder();
        cell.append(String.format("%3d", number));

        // Add player markers or snake/ladder markers
        if (game.getPlayer1().getPosition() == number && game.getPlayer2().getPosition() == number) {
            cell.append("P*"); // Both players on same spot
        } else if (game.getPlayer1().getPosition() == number) {
            cell.append("P1");
        } else if (game.getPlayer2().getPosition() == number) {
            cell.append("P2");
        } else if (board.hasSnake(number)) {
            cell.append("S" + board.getSnakeNumber(number));
        } else if (board.hasSnakeTail(number)) {
            cell.append("T" + board.getSnakeTailNumber(number));
        } else if (board.hasLadder(number)) {
            cell.append("L" + board.getLadderNumber(number));
        } else if (board.hasLadderTop(number)) {
            cell.append("H" + board.getLadderTopNumber(number));
        } else {
            cell.append("  ");
        }

        return cell.toString();
    }

}
