package pl.compprog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Class representing a sudoku board.
 */
public class SudokuBoard {

    /**
     * serves as a source to shuffled candidates arrays.
     */
    private static final ArrayList<Integer> CANDIDATES_SOURCE =
            new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
    /**
     * Size of rectangle representing sudoku board.
     */
    private final int sizeOfSudoku = 9;

    /**
     * 2D array od integers representing sudoku board.
     */
    private int[][] board = new int[sizeOfSudoku][sizeOfSudoku];

    /**
     * Array of values which can be placed,
     * shuffling them introduces possibility of generating
     * random sudoku boards.
     */


    /**
     * Gets the value from sudoku board at (row, column).
     *
     * @param row    row of the needed value
     * @param column column of the needed value
     * @return value at (row, column).
     */
    public final int getValueAt(final int row, final int column) {
        return board[row][column];
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
     * Fills board with 0 and solve it.
     */
    public final void fillBoard() {
        for (int i = 0; i < sizeOfSudoku; i++) {
            for (int j = 0; j < sizeOfSudoku; j++) {
                board[i][j] = 0;
            }
        }
        solve();
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

    @Override
    public final int hashCode() {
        int sum = 0;
        for (int i = 0; i < sizeOfSudoku; i++) {
            for (int j = 0; j < sizeOfSudoku; j++) {
                sum += board[i][j];
            }
        }
        return sum;
    }

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
     * Solves sudoku board according to sudoku rules-
     * uses so called backtracking algorithm.
     *
     * @return returns boolean meaning grid is correct after this assignment
     */
    private boolean solve() {
        for (int row = 0; row < sizeOfSudoku; row++) {
            for (int col = 0; col < sizeOfSudoku; col++) {
                if (board[row][col] == 0) {
                    ArrayList<Integer> candidates =
                            (ArrayList) CANDIDATES_SOURCE.clone();
                    Collections.shuffle(candidates);
                    for (int index = 0; index < candidates.size(); index++) {
                        int number = candidates.get(index);
                        if (canBePlaced(row, col, number)) {
                            board[row][col] = number;

                            if (solve()) {
                                return true;
                            } else {
                                board[row][col] = 0;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }


}
