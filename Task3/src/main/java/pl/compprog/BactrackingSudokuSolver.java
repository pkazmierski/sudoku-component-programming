package pl.compprog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Class used to solve sudoku board with bactracking algorithm.
 */
public class BactrackingSudokuSolver implements SudokuSolver {

    /**
     * Serves as a source to shuffled candidates arrays.
     */
    private static final ArrayList<Integer> CANDIDATES_SOURCE =
            new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

    /**
     * Size of rectangle representing sudoku board.
     */
    private final int sizeOfSudoku = 9;

    /**
     * Fills board with 0 and solve it.
     * @param board sudoku board being solved
     */
    public final void solve(final SudokuBoard board) {
        for (int i = 0; i < sizeOfSudoku; i++) {
            for (int j = 0; j < sizeOfSudoku; j++) {
                board.set(i, j, 0);
            }
        }
        fill(board);
    }

    /**
     * Solves sudoku board according to sudoku rules-
     * uses so called backtracking algorithm.
     * @param board sudoku board being solved
     * @return returns boolean meaning grid is correct after this assignment
     */
    private boolean fill(final SudokuBoard board) {
        for (int row = 0; row < sizeOfSudoku; row++) {
            for (int col = 0; col < sizeOfSudoku; col++) {
                if (board.get(row, col) == 0) {
                    ArrayList<Integer> candidates =
                            (ArrayList) CANDIDATES_SOURCE.clone();
                    Collections.shuffle(candidates);
                    for (int index = 0; index < candidates.size(); index++) {
                        int number = candidates.get(index);
                        if (board.canBePlaced(row, col, number)) {
                            board.set(row, col, number);

                            if (fill(board)) {
                                return true;
                            } else {
                                board.set(row, col, 0);
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
