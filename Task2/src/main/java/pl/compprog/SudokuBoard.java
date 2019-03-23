package pl.compprog;

import java.util.ArrayList;
import java.util.Collections;

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
     * Array of values which can be placed,
     * shuffling them introduces possibility of generating
     * random sudoku boards.
     */
    private static ArrayList<Integer> candidates = new ArrayList<Integer>();


    /**
     * Gets the value from sudoku board at (row, column).
     * @return value at (row, column).
     * @param row row of the needed value
     * @param column column of the needed value
     */
    public final int getValueAt(final int row, final int column) {
        return board[row][column];
    }


    /**
     * Copies the sudoku board to a newly allocated array.
     * @return newly allocated copy of board
     */
    public final int[][] getCopyOfBoard() {
        int[][] copy = new int[sizeOfSudoku][sizeOfSudoku];

        for (int i = 0; i < sizeOfSudoku; i++) {
            for (int j = 0; j < sizeOfSudoku; j++) {
                copy[i][j] = board[i][j];
            }
        }
        return copy;
    }

    /**
     * Fills board with 0 and solve it.
     */
    public final void fillBoard() {
        for (int i = 1; i <= sizeOfSudoku; i++) {
            candidates.add(i);
        }

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
     * @return true if such value can be placed and false if not
     * @param row row of the checked value
     * @param col column of checked value
     * @param num checked value
     */
    public final boolean canBePlaced(final int row, final int col, final int num) {
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
     * Solves sudoku board according to sudoku rules-
     * uses so called backtracking algorithm.
     * @return returns boolean meaning grid is correct after this assignment
     */
    public final boolean solve() {

        for (int row = 0; row < sizeOfSudoku; row++) {
            for (int col = 0; col < sizeOfSudoku; col++) {
                if (board[row][col] == 0) {
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
