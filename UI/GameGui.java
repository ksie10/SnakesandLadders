package UI;

import Core.Board;
import Core.GameLogic;
import Core.Player;
import Core.ComputerPlayer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.application.Platform;
import javafx.scene.layout.*;
import java.util.Map;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * JavaFX GUI implementation for the Snakes and Ladders game.
 * Provides a graphical interface for playing the game with visual representation
 * of the board, players, snakes, and ladders.
 *
 * @author Kit Siegel
 * @version 1.0
 */
public class GameGui extends Application {
    private GameLogic game;
    private GridPane boardGrid;
    private Label statusLabel;
    private Button rollButton;
    private Rectangle player1Token;
    private Rectangle player2Token;
    private static final int CELL_SIZE = 80;
    private static final int BOARD_SIZE = 10;
    /** Flag indicating whether the game is against computer (true) or another player (false) */
    public static boolean isVsComputer = false;

    @Override
    public void start(Stage primaryStage) {
        try {
            System.setProperty("prism.order", "sw");
            System.setProperty("glass.platform", "mac");
            System.setProperty("javafx.animation.fullspeed", "false");
            
            Platform.setImplicitExit(true);
            initializeGame();
            
            VBox root = new VBox(10);
            root.setPadding(new Insets(10));
            root.setAlignment(Pos.CENTER);
            
            boardGrid = new GridPane();
            createBoard();
            
            // Add legend
            HBox legend = new HBox(20);
            legend.setAlignment(Pos.CENTER);
            
            // Player 1 legend
            HBox player1Legend = new HBox(5);
            Rectangle p1Square = new Rectangle(15, 15, Color.RED);
            Label p1Label = new Label("Player 1");
            player1Legend.getChildren().addAll(p1Square, p1Label);
            
            // Player 2 legend
            HBox player2Legend = new HBox(5);
            Rectangle p2Square = new Rectangle(15, 15, Color.BLUE);
            Label p2Label = new Label("Player 2");
            player2Legend.getChildren().addAll(p2Square, p2Label);
            
            // Snake/Ladder legend
            HBox snakeLadderLegend = new HBox(10);
            Label snakeLabel = new Label("Snakes (S/T)");
            snakeLabel.setTextFill(Color.DARKRED);
            Label ladderLabel = new Label("Ladders (L/H)");
            ladderLabel.setTextFill(Color.DARKGREEN);
            snakeLadderLegend.getChildren().addAll(snakeLabel, ladderLabel);
            
            legend.getChildren().addAll(player1Legend, player2Legend, snakeLadderLegend);
            
            statusLabel = new Label("Player 1's turn");
            statusLabel.setFont(new Font(16));
            
            rollButton = new Button("Roll Dice");
            rollButton.setOnAction(e -> handleRollDice());
            
            root.getChildren().addAll(boardGrid, legend, statusLabel, rollButton);
            
            Scene scene = new Scene(root);
            primaryStage.setTitle("Snakes and Ladders");
            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(e -> Platform.exit());
            primaryStage.show();
        } catch (Exception e) {
            showError("Error starting game", e.getMessage());
        }
    }

    /**
     * Initializes the game with appropriate players.
     */
    private void initializeGame() {
        Player player1 = new Player("Player 1", false);
        Player player2;
        if (isVsComputer) {
            player2 = new ComputerPlayer();
        } else {
            player2 = new Player("Player 2", false);
        }
        game = new GameLogic(player1, player2);
    }

    /**
     * Creates the game board grid with visual elements.
     * @return GridPane representing the game board
     */
    private void createBoard() {
        // Ensure board grid exists
        if (boardGrid == null) {
            boardGrid = new GridPane();
            boardGrid.setAlignment(Pos.CENTER);
            boardGrid.setHgap(2);
            boardGrid.setVgap(2);
        }
        
        // Clear existing cells
        boardGrid.getChildren().clear();

        // Create board cells
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                int number = calculateCellNumber(i, j);
                StackPane cell = createCell(number);
                boardGrid.add(cell, j, i);
            }
        }

        // Add snakes and ladders visual indicators
        addSnakesAndLadders(boardGrid);

        // Initialize player tokens
        player1Token = createPlayerToken(Color.RED);
        player2Token = createPlayerToken(Color.BLUE);
        updatePlayerPositions();

    }

    /**
     * Calculates the cell number based on row and column.
     * @param row Row index
     * @param col Column index
     * @return Cell number
     */
    private int calculateCellNumber(int row, int col) {
        int rowNumber = BOARD_SIZE - 1 - row;
        if (rowNumber % 2 == 0) {
            return rowNumber * BOARD_SIZE + col + 1;
        } else {
            return (rowNumber + 1) * BOARD_SIZE - col;
        }
    }

    /**
     * Creates a single cell for the board.
     * @param number Cell number
     * @return StackPane representing the cell
     */
    private StackPane createCell(int number) {
        StackPane cell = new StackPane();
        Rectangle border = new Rectangle(CELL_SIZE, CELL_SIZE);
        border.setFill(Color.WHITE);
        border.setStroke(Color.BLACK);

        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(4));
        content.setMaxWidth(CELL_SIZE - 4);
        content.setMaxHeight(CELL_SIZE - 4);

        Text numberText = new Text(String.valueOf(number));
        numberText.setFont(new Font(20));
        numberText.setFill(Color.BLACK);
        
        StackPane numberPane = new StackPane(numberText);
        numberPane.setPadding(new Insets(2));
        content.getChildren().add(numberPane);

        cell.getChildren().addAll(border, content);
        return cell;
    }

    /**
     * Creates a player token with specified color.
     * @param color Color of the token
     * @return Rectangle representing the player token
     */
    private Rectangle createPlayerToken(Color color) {
        Rectangle token = new Rectangle(15, 15);
        token.setFill(color);
        return token;
    }

    /**
     * Adds visual indicators for snakes and ladders on the board.
     * @param grid The game board grid
     */
    private void addSnakesAndLadders(GridPane grid) {
        Board board = game.getBoard();
        Map<Integer, Integer> snakes = board.getSnakes();
        Map<Integer, Integer> ladders = board.getLadders();

        // Add ladder indicators
        for (Map.Entry<Integer, Integer> ladder : ladders.entrySet()) {
            int base = ladder.getKey();
            int top = ladder.getValue();
            int number = board.getLadderNumber(base);
            addIndicator(grid, base, "L" + number, Color.DARKGREEN);
            addIndicator(grid, top, "H" + number, Color.DARKGREEN);
        }

        // Add snake indicators
        for (Map.Entry<Integer, Integer> snake : snakes.entrySet()) {
            int head = snake.getKey();
            int tail = snake.getValue();
            int number = board.getSnakeNumber(head);
            addIndicator(grid, head, "S" + number, Color.DARKRED);
            addIndicator(grid, tail, "T" + number, Color.DARKRED);
        }
    }

    /**
     * Adds an indicator (snake or ladder) to a specific cell.
     * @param grid The game board grid
     * @param position Position on the board
     * @param indicator The indicator label
     */
    private void addIndicator(GridPane grid, int position, String text, Color color) {
        if (position <= 0 || position > 100) return;

        int row = BOARD_SIZE - 1 - ((position - 1) / BOARD_SIZE);
        int col = (row % 2 == 0) ? 
                 (position - 1) % BOARD_SIZE : 
                 BOARD_SIZE - 1 - ((position - 1) % BOARD_SIZE);

        Node cell = getNodeFromGridPane(grid, col, row);
        if (cell instanceof StackPane) {
            StackPane stackPane = (StackPane) cell;
            VBox content = (VBox) stackPane.getChildren().get(1);
            
            Text indicator = new Text(text);
            indicator.setFill(color);
            indicator.setFont(new Font(16));
            indicator.setStyle("-fx-font-weight: bold");
            
            // Create a background for the indicator
            Rectangle background = new Rectangle(
                indicator.getBoundsInLocal().getWidth() + 12,
                indicator.getBoundsInLocal().getHeight() + 8,
                Color.WHITE
            );
            background.setStroke(color);
            background.setStrokeWidth(1.5);
            
            StackPane indicatorPane = new StackPane(background, indicator);
            indicatorPane.setPadding(new Insets(2));
            content.getChildren().add(indicatorPane);
        }
    }

    /**
     * Gets a node from the GridPane at specified coordinates.
     * @param grid The GridPane
     * @param col Column index
     * @param row Row index
     * @return Node at the specified position
     */
    private Node getNodeFromGridPane(GridPane grid, int col, int row) {
        for (Node node : grid.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    /**
     * Updates the positions of player tokens on the board.
     */
    private void updatePlayerPositions() {
        try {
            // Remove existing tokens
            if (boardGrid != null && player1Token != null && player2Token != null) {
                boardGrid.getChildren().removeAll(player1Token, player2Token);
                
                // Update Player 1 position
                int pos1 = game.getPlayer1().getPosition();
                if (pos1 > 0) {
                    int row1 = BOARD_SIZE - 1 - ((pos1 - 1) / BOARD_SIZE);
                    int col1 = (row1 % 2 == 0) ? 
                             (pos1 - 1) % BOARD_SIZE : 
                             BOARD_SIZE - 1 - ((pos1 - 1) % BOARD_SIZE);
                    boardGrid.add(player1Token, col1, row1);
                }
                
                // Update Player 2 position
                int pos2 = game.getPlayer2().getPosition();
                if (pos2 > 0) {
                    int row2 = BOARD_SIZE - 1 - ((pos2 - 1) / BOARD_SIZE);
                    int col2 = (row2 % 2 == 0) ? 
                             (pos2 - 1) % BOARD_SIZE : 
                             BOARD_SIZE - 1 - ((pos2 - 1) % BOARD_SIZE);
                    boardGrid.add(player2Token, col2, row2);
                }
            }
        } catch (Exception e) {
            showError("Error updating positions", e.getMessage());
        }
    }

    /**
     * Handles the roll dice button click event.
     */
    private void handleRollDice() {
        try {
            int roll = game.rollDiceAndMove();
            Player currentPlayer = game.getCurrentPlayer();
            String playerName = currentPlayer.getName();
            
            statusLabel.setText(playerName + " rolled a " + roll);
            updatePlayerPositions();

            if (game.isGameOver()) {
                showGameOver(playerName);
                rollButton.setDisable(true);
                return;
            }

            game.switchPlayer();
            
            // If next player is computer, automatically take their turn
            if (game.getCurrentPlayer().isComputer()) {
                rollButton.setDisable(true);
                // Add small delay for computer's turn
                javafx.application.Platform.runLater(() -> {
                    try {
                        Thread.sleep(1000);
                        handleRollDice();
                        rollButton.setDisable(false);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
        } catch (Exception e) {
            showError("Error", "An error occurred during the game: " + e.getMessage());
        }
    }

    /**
     * Shows the game over dialog.
     * @param winner Name of the winning player
     */
    private void showGameOver(String winner) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText("Congratulations! " + winner + " has won the game!");
        alert.showAndWait();
    }

    /**
     * Shows an error dialog.
     * @param title Title of the error dialog
     * @param message Error message
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
