package pl.compprog;

/**
 * Interface used to solve sudoku board.
 */
public interface SudokuSolver {

    /**
     * Solves board.
     @param board sudoku board being solved
     */
    void solve(SudokuBoard board);
}
