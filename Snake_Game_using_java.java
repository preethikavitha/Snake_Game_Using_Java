// Importing necessary libraries
import java.util.*;

// Entry point of the program
public class Main {
    public static void main(String[] args) {  
        // Creates a new instance of the SnakeGame class and starts the game
        SnakeGame snakeGame = new SnakeGame(6, 6); 
        snakeGame.start(); 
    }
}

// Class to handle all game mechanics
class SnakeGame {
    // A 2D array representing the game board
    private final char[][] board;
    // Queue to track the snake's body positions
    private final Queue<Node> snakeBody = new LinkedList<>();
    // Queue to store predefined or randomly generated food positions
    private final Queue<Node> foodQueue = new LinkedList<>();
    private final Random random = new Random(); // Random generator for food placement
    private int currentRow = 0;  // Snake head's current row position
    private int currentCol = 0;  // Snake head's current column position
    private char currentHead = '>'; // Default head direction ('>' for right)

    // Constructor initializes the board, snake's starting position, and food
    public SnakeGame(int rows, int cols) {
        board = new char[rows][cols];  // Create the game board with given dimensions
        snakeBody.add(new Node(0, 0)); // Add the initial snake position to the queue
        board[0][0] = currentHead;     // Mark the snake's starting position on the board
        generateFoodPositions();       // Generate predefined food positions
        placeFood();                   // Place the first food on the board
    }

    // Method to start the game loop
    public void start() {
        Scanner scanner = new Scanner(System.in);  // Scanner to take user input
        printBoard();  // Display the initial game board

        // Game loop to keep running until the snake dies
        while (true) {
            System.out.print("Enter move (U, D, L, R): "); 
            // Take user input for movement and normalize it to uppercase
            char move = scanner.next().toUpperCase().charAt(0); 

            // Process the move and update the game state
            if (makeMove(move)) { 
                printBoard();  // Print the updated board if the move is valid
            } else {
                System.out.println("Game Over!"); // End the game if the move is invalid
                break;
            }
        }
        scanner.close(); // Close the scanner to release resources
    }

    // Method to process the player's move
    private boolean makeMove(char direction) {
        int newRow = currentRow, newCol = currentCol; // Variables to store the new position
        char newHead; // Variable to store the new head direction

        // Update the snake's head position and direction based on user input
        switch (direction) {
            case 'U': newRow--; newHead = '^'; break; // Move up
            case 'D': newRow++; newHead = 'v'; break; // Move down
            case 'L': newCol--; newHead = '<'; break; // Move left
            case 'R': newCol++; newHead = '>'; break; // Move right
            default:
                System.out.println("Invalid move! Use U, D, L, R."); // Handle invalid input
                return true; // Continue the game
        }

        // Check if the new move is valid
        if (!isValidMove(newRow, newCol)) {
            return false; // End the game if the move is invalid
        }

        // Check if the snake eats food
        if (board[newRow][newCol] == 'X') {
            placeFood(); // Place a new food item
        } else {
            // Remove the tail of the snake to maintain its length
            Node tail = snakeBody.poll();  
            board[tail.row][tail.col] = '\0'; // Clear the tail position on the board
        }

        // Update the previous head position to the body symbol
        board[currentRow][currentCol] = '.';

        // Update the snake's head position
        snakeBody.add(new Node(newRow, newCol)); // Add the new head position to the queue
        currentRow = newRow;  // Update the current row
        currentCol = newCol;  // Update the current column
        currentHead = newHead;  // Update the current head direction
        board[newRow][newCol] = currentHead;  // Mark the new head position on the board

        return true; // Continue the game
    }

    // Method to check if a move is valid
    private boolean isValidMove(int row, int col) {
        // Ensure the new position is within the board and not colliding with the snake's body
        return row >= 0 && row < board.length && col >= 0 && col < board[0].length && board[row][col] != '.';
    }

    // Method to generate predefined food positions
    private void generateFoodPositions() {
        foodQueue.add(new Node(1, 0)); // Food 1
        foodQueue.add(new Node(2, 2)); // Food 2
        foodQueue.add(new Node(3, 4)); // Food 3
        foodQueue.add(new Node(5, 2)); // Food 4
        foodQueue.add(new Node(4, 5)); // Food 5
    }

    // Method to place food on the board
    private void placeFood() {
        Node food;
        if (!foodQueue.isEmpty()) {
            food = foodQueue.poll(); // Place predefined food if available
        } else {
            do {
                // Generate random food positions until an empty cell is found
                food = new Node(random.nextInt(board.length), random.nextInt(board[0].length));
            } while (board[food.row][food.col] != '\0');
        }
        board[food.row][food.col] = 'X'; // Mark the food position on the board
    }

    // Method to print the game board
    private void printBoard() {
        for (char[] row : board) {
            for (char cell : row) {
                System.out.print((cell == '\0' ? '■' : cell) + " "); // Display empty cells as '■'
            }
            System.out.println(); // Newline after each row
        }
        System.out.println(); // Extra newline for better readability
    }
}

// Class to represent positions on the board (row and column)
class Node {
    final int row, col; // Immutable row and column values

    Node(int row, int col) {
        this.row = row; // Initialize the row
        this.col = col; // Initialize the column
    }
}