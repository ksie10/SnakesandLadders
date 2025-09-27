package UI;

import javafx.application.Application;
import java.util.Scanner;

/**
 * Custom exception for invalid user input.
 */
class InvalidUserInputException extends Exception {
    public InvalidUserInputException(String message) {
        super(message);
    }
}

/**
 * Main entry point for the Snakes and Ladders game.
 * Handles UI selection (Console vs GUI) and player type selection.
 *
 * @author Kit Siegel
 * @version 1.0
 */
public class Main {
    /**
     * Main method that starts the game.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(System.in);
            
            // UI Selection
            System.out.println("Welcome to Snakes and Ladders!");
            System.out.println("Please select your preferred interface:");
            System.out.println("1. Graphical User Interface (GUI)");
            System.out.println("2. Console-based Interface");
            
            int uiChoice = getUserChoice(scanner, 1, 2);
            
            // Player Type Selection
            System.out.println("\nWould you like to play against:");
            System.out.println("1. Computer");
            System.out.println("2. Another Player");
            
            int playerChoice = getUserChoice(scanner, 1, 2);
            
            if (uiChoice == 1) {
                try {
                    // Launch GUI version
                    GameGui.isVsComputer = (playerChoice == 1);
                    Application.launch(GameGui.class, args);
                } catch (RuntimeException e) {
                    throw new Exception("Failed to start GUI: " + e.getMessage() +
                                      "\nMake sure JavaFX is properly configured.");
                }
            } else {
                // Launch Console version
                GameConsoleUI consoleUI = new GameConsoleUI();
                consoleUI.startGame(playerChoice == 1);
            }
            
        } catch (InvalidUserInputException e) {
            System.err.println("Input error: " + e.getMessage());
            System.err.println("Please restart the game and try again.");
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error starting game: " + e.getMessage());
            System.exit(1);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }
    
    /**
     * Gets a valid user choice within the specified range.
     * @param scanner Scanner for user input
     * @param min Minimum valid choice
     * @param max Maximum valid choice
     * @return The user's valid choice
     */
    private static int getUserChoice(Scanner scanner, int min, int max) throws InvalidUserInputException {
        int attempts = 0;
        final int MAX_ATTEMPTS = 3;
        
        while (attempts < MAX_ATTEMPTS) {
            try {
                System.out.print("Enter your choice (" + min + "-" + max + "): ");
                String input = scanner.nextLine().trim();
                
                if (input.isEmpty()) {
                    throw new InvalidUserInputException("Input cannot be empty");
                }
                
                int choice = Integer.parseInt(input);
                
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    throw new InvalidUserInputException("Choice must be between " + min + " and " + max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
                attempts++;
            } catch (InvalidUserInputException e) {
                System.out.println("Error: " + e.getMessage());
                attempts++;
            }
            
            if (attempts < MAX_ATTEMPTS) {
                System.out.println("You have " + (MAX_ATTEMPTS - attempts) + " attempts remaining");
            }
        }
        
        throw new InvalidUserInputException("Maximum number of attempts exceeded");
    }
}
