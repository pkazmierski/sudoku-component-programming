package pl.compprog.sudoku;

/**
 * class representing a row in a sudoku board.
 */
public class SudokuRow extends SudokuFieldGroup {

    /**
     * assigns received array to the fields array.
     *
     * @param c an array that should be made up of sudoku fields
     */
    public SudokuRow(final  SudokuField[] c) {
        super(c);
    }
}
