package pl.compprog;


/**
 * class representing a 3x3 grid of fields (subgrid) called box.
 */
public class SudokuBox {
    /**
     * stores the fields that make up a box.
     */
    private SudokuField[] box;


    /**
     * assigns received array to the box array.
     *
     * @param c an array that should be made up of box fields
     */
    public SudokuBox(final SudokuField[] c) {
        if (c.length != SudokuBoard.SIZE_OF_SUDOKU) {
            throw new IllegalArgumentException("Wrong length of the box.");
        }
        box = c;
    }

    /**
     * verifies whether all the values in the box are unique.
     *
     * @return returns the result of the test
     */
    public final boolean verify() {
        for (int i = 0; i < SudokuBoard.SIZE_OF_SUDOKU; i++) {
            for (int j = 0; j < SudokuBoard.SIZE_OF_SUDOKU; j++) {
                if (box[i].getFieldValue() == box[j].getFieldValue()
                        && i != j) {
                    return false;
                }
            }
        }
        return true;
    }
}
