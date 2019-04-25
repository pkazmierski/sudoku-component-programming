package pl.compprog;

import java.io.Serializable;

/**
 * class representing a 3x3 grid of fields (subgrid) called box.
 */
public class SudokuBox extends SudokuFieldGroup {

    /**
     * assigns received array to the fields array.
     *
     * @param c an array that should be made up of sudoku fields
     */
    public SudokuBox(final SudokuField[] c) {
        super(c);
    }
}
