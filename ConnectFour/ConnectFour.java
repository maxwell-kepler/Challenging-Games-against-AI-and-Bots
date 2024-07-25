package ConnectFour;

public class ConnectFour {
    private final int columnCount = 7;
    private final int rowCount = 6;
    private String board[][] = new String[columnCount][rowCount];

    private final String player_1 = "R";
    private final String player_2 = "Y";
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

    public void runGame() {
        board[0][5] = currentPlayer;
        System.out.println(this);
    }
}
