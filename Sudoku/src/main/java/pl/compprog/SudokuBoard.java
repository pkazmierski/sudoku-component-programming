package pl.compprog;

/**
 * Class representing a sudoku board.
 */
public class SudokuBoard {
    /**
     * Size of rectangle representing sudoku board.
     */
    public static final int SIZE_OF_SUDOKU = 9;

    /**
     * Number of fields in a line (vertical or horizontal / row or column) in a
     * box (subgrid).
     */
    private final int fieldsInLineOfBox = 3;

    /**
     * 2D array od integers representing sudoku board.
     * First field is y (row number), the second one is x (column number).
     */
    private SudokuField[][] board =
            new SudokuField[SIZE_OF_SUDOKU][SIZE_OF_SUDOKU];

    {
        for (int i = 0; i < SIZE_OF_SUDOKU; i++) {
            for (int j = 0; j < SIZE_OF_SUDOKU; j++) {
                board[i][j] = new SudokuField();
            }
        }
    }

    /**
     * Returns the row of SudokuFields at a given height y.
     *
     * @param y row's number / height
     * @return SudokuRow object containing a row of SudokuFields
     */
    public final SudokuRow getRow(final int y) {
        return new SudokuRow(board[y]);
    }


    /**
     * Returns the column of SudokuFields at a given lenght x.
     *
     * @param x column's number / length
     * @return SudokuColumn object containing a column of SudokuFields
     */
    public final SudokuColumn getColumn(final int x) {
        SudokuField[] col = new SudokuField[SIZE_OF_SUDOKU];
        for (int i = 0; i < SIZE_OF_SUDOKU; i++) {
            col[i] = board[i][x];
        }
        return new SudokuColumn(col);
    }

    /**
     * Returns the position of the left top corner of the box in which the
     * given element is, whether it is y or x.
     *
     * @param num y or x coordinate
     * @return int representing one coordinate
     */
    private int getLeftBoxCorner(final int num) {
        return fieldsInLineOfBox * (num / fieldsInLineOfBox);
    }

    /**
     * Returns a box (3x3 subgrid) of the SudokuBoard that the given element
     * (x,y) resides in.
     *
     * @param x x coordinate of the field
     * @param y y coordinate of the field
     * @return SudokuBox object containing values from a box.
     */
    public final SudokuBox getBox(final int x, final int y) {
        SudokuField[] box = new SudokuField[SIZE_OF_SUDOKU];
        int newX = getLeftBoxCorner(x);
        int newY = getLeftBoxCorner(y);
        for (int i = 0; i < fieldsInLineOfBox; i++) {
            for (int j = 0; j < fieldsInLineOfBox; j++) {
                box[i * fieldsInLineOfBox + j] = board[i + newY][j + newX];
            }
        }
        return new SudokuBox(box);
    }

    /**
     * Gets the value from sudoku board at (row, column).
     *
     * @param y row of the needed value
     * @param x column of the needed value
     * @return value at (row, column).
     */
    public final int get(final int x, final int y) {
        return board[y][x].getFieldValue();
    }


    /**
     * Sets the value from sudoku board at (row, column).
     *
     * @param y     row of the needed value
     * @param x     column of the needed value
     * @param value new value at (row, column).
     */
    public final void set(final int x, final int y, final int value) {
        board[y][x].setFieldValue(value);
    }


    /**
     * Copies the sudoku board to a newly allocated array.
     *
     * @return newly allocated copy of board
     */
    public final SudokuField[][] getCopyOfBoard() {
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
        for (int i = 0; i < SIZE_OF_SUDOKU; i++) {
            for (int j = 0; j < SIZE_OF_SUDOKU; j++) {
                str += " " + ((Integer) board[i][j].getFieldValue()).toString();
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
        for (int i = 0; i < SIZE_OF_SUDOKU; i++) {
            for (int j = 0; j < SIZE_OF_SUDOKU; j++) {
                sum += (i * j + i) * board[i][j].getFieldValue();
            }
        }
        return sum;
    }

    /**
     * Checks whether two sudoku boards are identical.
     * <p>
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
        for (int i = 0; i < SIZE_OF_SUDOKU; i++) {
            for (int j = 0; j < SIZE_OF_SUDOKU; j++) {
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
        for (int i = 0; i < SIZE_OF_SUDOKU; i++) {
            if (row != i && board[i][col].getFieldValue() == num) {
                return false;
            }
        }
        for (int j = 0; j < SIZE_OF_SUDOKU; j++) {
            if (col != j && board[row][j].getFieldValue() == num) {
                return false;
            }
        }

        int sqrtOfSize = (int) Math.sqrt(SIZE_OF_SUDOKU);

        int r = row - row % sqrtOfSize;
        int c = col - col % sqrtOfSize;

        for (int i = r; i < r + sqrtOfSize; i++) {
            for (int j = c; j < c + sqrtOfSize; j++) {
                if (row != i && col != j
                        && board[i][j].getFieldValue() == num) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Tests whether each value of sudoku board is valid according to sudoku
     * rules.
     *
     * @return true if board is valid and false if not
     */
    private boolean checkBoard() {

        for (int i = 0; i < SIZE_OF_SUDOKU; i++) {
            for (int j = 0; j < SIZE_OF_SUDOKU; j++) {
                if (!canBePlaced(i, j, board[i][j].getFieldValue())) {
                    return false;
                }
            }
        }
        return true;
    }
}
