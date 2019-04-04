package pl.compprog;


/**
 * class representing a column in a sudoku board.
 */
public class SudokuColumn extends SudokuFieldGroup {

    /**
     * assigns received array to the fields array.
     *
     * @param c an array that should be made up of sudoku fields
     */
    public SudokuColumn(final SudokuField[] c) {
        super(c);
    }
}
