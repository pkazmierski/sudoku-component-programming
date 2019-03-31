package pl.compprog;

/**
 * class representing a row in a sudoku board.
 */
public class SudokuRow {
    /**
     * stores the fields that make up a row.
     */
    private SudokuField[] row;


    /**
     * assigns received array to the row array.
     * @param c an array that should be made up of row fields
     */
    public SudokuRow(final SudokuField[] c) {
        if (c.length != SudokuBoard.SIZE_OF_SUDOKU) {
            throw new IllegalArgumentException("Wrong length of the row.");
        }
        row = c;
    }

    /**
     * verifies whether all the values in the row are unique.
     * @return returns the result of the test
     */
    public final boolean verify() {
        for (int i = 0; i < SudokuBoard.SIZE_OF_SUDOKU; i++) {
            for (int j = 0; j < SudokuBoard.SIZE_OF_SUDOKU; j++) {
                if (row[i].getFieldValue() == row[j].getFieldValue()
                        && i != j) {
                    return false;
                }
            }
        }
        return true;
    }
}
