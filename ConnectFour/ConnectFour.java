package ConnectFour;

public class ConnectFour {
    private final int columnCount = 7;
    private final int rowCount = 6;
    private String board[][] = new String[columnCount][rowCount];

    private final String player_1 = "R";
    private final String player_2 = "Y";
    private final String tie = "tie";
    private final String empty = " ";
    private String currentPlayer;

    public ConnectFour() {
        for (int column = 0; column < columnCount; column++) {
            for (int row = 0; row < rowCount; row++) {
                board[column][row] = empty;
            }
        }

        currentPlayer = player_1;
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

    /**
     * Runs the "Main Loop" of the game
     */
    public void runGame() {
        board[0][5] = currentPlayer;
        board[0][4] = currentPlayer;
        board[0][3] = currentPlayer;
        board[0][2] = currentPlayer;
        System.out.println(this);

        endingMessage(checkWinner());
    }
}
