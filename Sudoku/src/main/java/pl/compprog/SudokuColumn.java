package pl.compprog;


/**
 * class representing a column in a sudoku board.
 */
public class SudokuColumn {
    /**
     * stores the fields that make up a column.
     */
    private SudokuField[] column;


    /**
     * assigns received array to the column array.
     *
     * @param c an array that should be made up of column fields
     */
    public SudokuColumn(final SudokuField[] c) {
        if (c.length != SudokuBoard.SIZE_OF_SUDOKU) {
            throw new IllegalArgumentException("Wrong length of the column.");
        }
        column = c;
    }


    /**
     * verifies whether all the values in the column are unique.
     *
     * @return returns the result of the test
     */
    public final boolean verify() {
        for (int i = 0; i < SudokuBoard.SIZE_OF_SUDOKU - 1; i++) {
            for (int j = i+1; j < SudokuBoard.SIZE_OF_SUDOKU; j++) {
                if (column[i].getFieldValue() == column[j].getFieldValue()
                        && column[i].getFieldValue() != 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
