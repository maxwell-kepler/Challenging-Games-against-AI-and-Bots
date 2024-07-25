package ConnectFour;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ConnectFour {
    // Board
    private final int columnCount = 7;
    private final int rowCount = 6;
    private String board[][] = new String[columnCount][rowCount];

    // Player
    private final String player_1 = "R";
    private final String player_2 = "Y";
    private final String tie = "tie";
    private final String empty = " ";
    private String currentPlayer;

    // Util
    private Scanner input;
    private Random rand;

    // Bot
    private final int AiSkillset_1 = 100;

    // Minimax
    private Dictionary<String, Integer> moveScores = new Hashtable<>();
    private int AlphaBetaPruningIterCount = 0;
    private final int AlphaBetaMaxDepth = 8;
    private final boolean showCount = false;

    /**
     * Constructs a new Connect Four instance.
     */
    public ConnectFour() {
        for (int column = 0; column < columnCount; column++) {
            for (int row = 0; row < rowCount; row++) {
                board[column][row] = empty;
            }
        }
        input = new Scanner(System.in);
        rand = new Random();

        moveScores.put(player_2, 10);
        moveScores.put(player_1, -10);
        moveScores.put(tie, 0);
    }

    /**
     * Print a string representation of the current board state
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String dashedLine = "\n-------------\n";
        String doubleDashedLine = "\n=============\n";

        String[][] transposedGrid = new String[rowCount][columnCount];
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                transposedGrid[row][col] = board[col][row];
            }
        }
        sb.append(doubleDashedLine);
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                switch (transposedGrid[row][col]) {
                    case player_1:
                        sb.append(player_1);
                        break;
                    case player_2:
                        sb.append(player_2);
                        break;
                    default:
                        sb.append(empty);
                        break;
                }
                if (col < columnCount - 1)
                    sb.append("|");
            }
            if (row < rowCount - 1)
                sb.append(dashedLine);
        }
        sb.append(doubleDashedLine);
        return sb.toString();
    }

    // Game Over Methods
    /**
     * Checks if the four piece are the same, and aren't empty
     * 
     * @param piece1 the first piece to compare
     * @param piece2 the second piece to compare
     * @param piece3 the third piece to compare
     * @param piece4 the fourth piece to compare
     * @return true if the pieces are all the same
     */
    private boolean equals4(String piece1, String piece2, String piece3, String piece4) {
        return piece1.equals(piece2) && piece2.equals(piece3) && piece3.equals(piece4) && !piece1.equals(empty);
    }

    /**
     * Gets the number of open spots on the board
     * 
     * @return the number of open spots
     */
    private int openSpots() {
        int numberOfSpots = 0;
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                if (board[col][row] == empty)
                    numberOfSpots++;
            }
        }
        return numberOfSpots;
    }

    /**
     * Checks if the game is complete.
     * The game is deemed complete where there are 4 identical piece in a row,
     * column, or diagonal.
     * 
     * @return the player's name if they won the game
     * @return "tie" if the game ended in a tie
     * @return null if the game isn't complete
     */
    private String checkWinner() {
        // Horizontal check
        for (int row = 0; row < rowCount; row++)
            for (int col = 0; col < 4; col++)
                if (equals4(board[col][row], board[col + 1][row], board[col + 2][row], board[col + 3][row]))
                    return board[col][row];

        // Vertical check
        for (int col = 0; col < columnCount; col++) {
            for (int row = 0; row < 3; row++) {
                if (equals4(board[col][row], board[col][row + 1], board[col][row + 2], board[col][row + 3]))
                    return board[col][row];
            }
        }

        // Diagonal check \
        for (int col = 0; col < 4; col++) {
            for (int row = 0; row < 3; row++) {
                if (equals4(board[col][row], board[col + 1][row + 1], board[col + 2][row + 2], board[col + 3][row + 3]))
                    return board[col][row];
            }
        }

        // Diagonal check /
        for (int col = columnCount - 1; col > 2; col--) {
            for (int row = 0; row < 3; row++) {
                if (equals4(board[col][row], board[col - 1][row + 1], board[col - 2][row + 2], board[col - 3][row + 3]))
                    return board[col][row];
            }
        }
        // Full board check
        if (openSpots() == 0)
            return tie;
        return null;
    }

    /**
     * Prints out a message indicating the final state of the game
     * 
     * @param outcome The outcome of the game
     */
    public void endingMessage(String outcome) {
        if (outcome == player_1)
            System.out.println("Player 1 wins!");
        if (outcome == player_2)
            System.out.println("Player 2 wins!");
        if (outcome == tie)
            System.out.println("Tie game!");
    }

    // Human input
    /**
     * Get the index of a column from a human player.
     * 
     * @return true if the piece was successfully place in the column, and false
     *         otherwise.
     */
    private boolean receiveInput() {
        System.out.print("Enter the column where you'd like to drop your piece: ");
        String inputString = input.next();
        int column = Integer.valueOf(inputString);
        return validateInput(column);
    }

    /**
     * Validates the user's input.
     * 
     * @param column Index of the column to place a piece in to.
     * @return true if the piece was successfully placed.
     * @return false if an error has occured.
     */
    private boolean validateInput(int column) {
        try {
            this.putPiece(column);
        } catch (IllegalArgumentException e) {
            System.out.println(e.toString());
            return false;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    /**
     * Method that takes a column index, and attempts to put a piece in that column.
     * 
     * @param column Index of the column to place a piece in to.
     * @throws IllegalArgumentException If the column number is out of bounds.
     * @throws IllegalArgumentException If the column is full.
     */
    private void putPiece(int column) throws IllegalArgumentException {
        // Column is out of bounds:
        if ((column < 0) || (column >= columnCount))
            throw new IllegalArgumentException("Invalid board position");
        // Column is full:
        if (board[column][0] != empty)
            throw new IllegalArgumentException("Board Position Occupied");

        int availableRow = dropsToRow(column, board);
        board[column][availableRow] = currentPlayer;
        currentPlayer = currentPlayer == player_1 ? player_2 : player_1;
    }

    /**
     * Finds the row that a piece would land in for a given column.
     * 
     * @param column Index of the column to check.
     * @param board  Current state of the board.
     * @return The row index for the given column.
     */
    private int dropsToRow(int column, String[][] board) {
        int firstAvailableRow = 0;
        while (firstAvailableRow < rowCount - 1 && board[column][firstAvailableRow + 1] == empty) {
            firstAvailableRow++;
        }
        return firstAvailableRow;
    }

    /**
     * Game mode where 2 human players face off against each other.
     * The terminal will prompt them for their moves.
     */
    public void twoPlayerScenario() {
        String winner = null;
        currentPlayer = player_1;
        while (winner == null) {
            boolean validMove = false;
            System.out.println("\nCurrent player: " + currentPlayer);
            while (!validMove) {
                if (currentPlayer == player_1) {
                    validMove = receiveInput();
                } else {
                    validMove = receiveInput();
                }
            }
            System.out.println(this);
            winner = checkWinner();
        }
        endingMessage(winner);
    }

    /**
     * Simulates a human player.
     * Does this by generating a value between 0 and 99.
     * If this value is greater than the threshold, the Bot will do a random move.
     * Otherwise, the bot will find it's move using a minimax search
     * 
     * @param threshold Value between 0 and 100, indicate the likelyhood of the Bot
     *                  making a random move. If set to 100, the Bot will play near
     *                  perfectly.
     * @return boolean value indicating if the move was successful, should almost
     *         always be true.
     */
    private boolean humanPlayerSimulator(int threshold) {

        int action = rand.nextInt(100);
        if (action > threshold)
            return randomMove();
        else
            return bestMoveWithAlphaBetaPruning();
    }

    /**
     * Drops a piece in a random column that isn't full.
     * 
     * @return boolean value indicating if the move was successful
     */
    private boolean randomMove() {
        List<Integer> validColumns = new ArrayList<>();
        for (int i = 0; i < columnCount; i++) {
            if (board[i][0] == empty)
                validColumns.add(i);
        }
        int index = rand.nextInt(validColumns.size());
        return validateInput(validColumns.get(index));
    }

    /**
     * Drops a piece in the column with the best score.
     * The score is determined from the minimax algorithm using Alpha Beta pruning.
     * If it's the first move of the game, drops the piece in the middle column.
     * 
     * @return boolean value indicating if the move was successful
     */
    public boolean bestMoveWithAlphaBetaPruning() {
        int bestScore = Integer.MIN_VALUE;
        int move_col = -1;
        AlphaBetaPruningIterCount = 0;

        if (openSpots() == 7 * 6)
            return validateInput(3);

        for (int col = 0; col < columnCount; col++) {
            if (board[col][0] == empty) {
                int availableRow = dropsToRow(col, board);
                board[col][availableRow] = player_2;
                int score = minimaxAlphaBetaPruning(board, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, false);

                board[col][availableRow] = empty;
                if (score > bestScore) {
                    bestScore = score;
                    move_col = col;
                }
            }
        }
        if (showCount)
            System.out.println("\nAB: " + AlphaBetaPruningIterCount);
        return validateInput(move_col);
    }

    /**
     * Mimimax algorithm using Alpha Beta Pruning.
     * Pruning lets us perform less calculations if a better outcome is known.
     * 
     * @param boardInstance Current state of the board
     * @param depth         Current depth of the recursion algorithm (how many moves
     *                      ahead are we looking?). To help with computation times,
     *                      a max depth is set.
     * @param alpha         Alpha value, set to pseudo -inf initially
     * @param beta          Alpha value, set to pseudo inf initially
     * @param isMaximizing  Determines if the algorithm is trying to maximize or
     *                      minimize the score
     * @return The best score for the current depth.
     */
    private int minimaxAlphaBetaPruning(String[][] boardInstance, int depth, int alpha, int beta,
            boolean isMaximizing) {
        AlphaBetaPruningIterCount++;
        if (depth > AlphaBetaMaxDepth)
            return moveScores.get(tie);

        String result = checkWinner();
        if (result != null)
            return moveScores.get(result);

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int col = 0; col < columnCount; col++) {
                if (boardInstance[col][0] == empty) {
                    int availableRow = dropsToRow(col, boardInstance);
                    boardInstance[col][availableRow] = player_2;
                    int score = minimaxAlphaBetaPruning(boardInstance, depth + 1, alpha, beta, false);
                    boardInstance[col][availableRow] = empty;
                    bestScore = Math.max(score, bestScore);

                    alpha = Math.max(alpha, score);
                    if (beta <= alpha)
                        break;
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int col = 0; col < columnCount; col++) {
                if (boardInstance[col][0] == empty) {
                    int availableRow = dropsToRow(col, boardInstance);
                    boardInstance[col][availableRow] = player_1;
                    int score = minimaxAlphaBetaPruning(boardInstance, depth + 1, alpha, beta, true);
                    boardInstance[col][availableRow] = empty;
                    bestScore = Math.min(score, bestScore);

                    beta = Math.min(beta, score);
                    if (beta <= alpha)
                        break;
                }

            }
            return bestScore;
        }
    }

    /**
     * Game mode where a human player face off against an Bot program.
     * The terminal will prompt them for their moves.
     */
    public void humanAiScenario() {
        System.out.println("Would you like to go first? (1 for yes, 0 for no)");
        String inputString = input.next();
        int choice = Integer.valueOf(inputString);
        currentPlayer = (choice == 1) ? player_1 : player_2;

        String winner = null;
        while (winner == null) {
            boolean validMove = false;
            System.out.println("\nCurrent player: " + currentPlayer);
            while (!validMove) {
                if (currentPlayer == player_1) {
                    validMove = receiveInput(); // human move
                } else {
                    validMove = humanPlayerSimulator(AiSkillset_1); // Bot move
                }
            }
            System.out.println(this);
            winner = checkWinner();
        }
        endingMessage(winner);
    }

    /**
     * Prompts the user to determine the number of human players.
     * If the user enters <1>, they will play against an Bot program.
     * If the user enters <2>, they will play against another human.
     * 
     * @return The user's choice.
     */
    public int gameOptions() {
        System.out.println("Welcome to Connect 4!");
        System.out.println("How many human players are there (1 or 2) ?");
        String inputString = input.next();
        int choice = Integer.valueOf(inputString);
        if (choice != 1 && choice != 2)
            return -1;
        return choice;
    }

    /**
     * Runs the "Main Loop" of the game
     */
    public void runGame() {
        int gameMode = gameOptions();
        if (gameMode == -1) {
            System.out.println("Invalid number of players, exiting game.");
            return;
        }

        if (gameMode == 2)
            twoPlayerScenario();
        if (gameMode == 1)
            humanAiScenario();
    }

}
