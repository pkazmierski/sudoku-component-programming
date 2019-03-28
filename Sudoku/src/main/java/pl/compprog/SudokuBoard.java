package pl.compprog;

/**
 * Class representing a sudoku board.
 */
public class SudokuBoard {


    /**
     * Size of rectangle representing sudoku board.
     */
    private final int sizeOfSudoku = 9;

    /**
     * 2D array od integers representing sudoku board.
     */
    private int[][] board = new int[sizeOfSudoku][sizeOfSudoku];

    /**
     * Gets the value from sudoku board at (row, column).
     *
     * @param row    row of the needed value
     * @param column column of the needed value
     * @return value at (row, column).
     */
    public final int get(final int row, final int column) {
        return board[row][column];
    }


    /**
     * Sets the value from sudoku board at (row, column).
     *
     * @param row    row of the needed value
     * @param column column of the needed value
     * @param num new value at (row, column).
     */
    public final void set(final int row, final int column, final int num) {
        board[row][column] = num;
    }


    /**
     * Copies the sudoku board to a newly allocated array.
     *
     * @return newly allocated copy of board
     */
    public final int[][] getCopyOfBoard() {
        return board.clone();
    }


    /**
     * Transforms sudoku board into printable string of characters.
     *
     * @return returns string consisting of sudoku's values
     */
    @Override
    public final String toString() {
        String str = "";
        for (int i = 0; i < sizeOfSudoku; i++) {
            for (int j = 0; j < sizeOfSudoku; j++) {
                str += " " + ((Integer) board[i][j]).toString();
            }

            str += "\n";
        }

        return str + "\n";
    }

    /**
     * Hashing function.
     *
     * @return returns unique identifier for each sudoku board
     */
    @Override
    public final int hashCode() {
        int sum = 0;
        for (int i = 0; i < sizeOfSudoku; i++) {
            for (int j = 0; j < sizeOfSudoku; j++) {
                sum +=  (i * j + i) * board[i][j];
            }
        }
        return sum;
    }

    /**
     * Checks whether two sudoku boards are identical.
     *
     * * @return true if so and false if not
     */
    @Override
    public final boolean equals(final Object o) {
        if (o == this) { //reference to itself
            return true;
        }
        if (!(o instanceof SudokuBoard)) { //incompatible type
            return false;
        }
        SudokuBoard sudokuBoard = (SudokuBoard) o;
        for (int i = 0; i < sizeOfSudoku; i++) {
            for (int j = 0; j < sizeOfSudoku; j++) {
                if (board[i][j] != sudokuBoard.board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Determines whether one can put such number
     * on given (row, column) according to sudoku rules.
     *
     * @param row row of the checked value
     * @param col column of checked value
     * @param num checked value
     * @return true if such value can be placed and false if not
     */
    public final boolean canBePlaced(final int row, final int col,
                                     final int num) {
        for (int i = 0; i < sizeOfSudoku; i++) {
            if (row != i && board[i][col] == num) {
                return false;
            }
        }
        for (int j = 0; j < sizeOfSudoku; j++) {
            if (col != j && board[row][j] == num) {
                return false;
            }
        }

        int sqrtOfSize = (int) Math.sqrt(sizeOfSudoku);

        int r = row - row % sqrtOfSize;
        int c = col - col % sqrtOfSize;

        for (int i = r; i < r + sqrtOfSize; i++) {
            for (int j = c; j < c + sqrtOfSize; j++) {
                if (row != i && col != j && board[i][j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Tests whether each value of sudoku board is valid according to sudoku
     * rules.
     * @return true if board is valid and false if not
     */
    private boolean checkBoard() {

        for (int i = 0; i < sizeOfSudoku; i++) {
            for (int j = 0; j < sizeOfSudoku; j++) {
                if (!canBePlaced(i, j, board[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }

}
