package pl.compprog;


/**
 * abstract class for storing and verifying groups of SudokuBoard fields, for
 * example boxes, fields or columns.
 */
public abstract class SudokuFieldGroup {
    /**
     * stores the fields that make up a field group.
     */
    private SudokuField[] fields;


    /**
     * assigns received array to the fields array.
     * @param c an array that should be made up of sudoku fields
     */
    public SudokuFieldGroup(final SudokuField[] c) {
        if (c.length != SudokuBoard.SIZE_OF_SUDOKU) {
            throw new IllegalArgumentException("Wrong length of the fields "
                    + "array.");
        }
        fields = c;
    }

    /**
     * verifies whether all the values in the fields are unique.
     * @return returns the result of the test
     */
    public final boolean verify() {
        for (int i = 0; i < SudokuBoard.SIZE_OF_SUDOKU - 1; i++) {
            for (int j = i + 1; j < SudokuBoard.SIZE_OF_SUDOKU; j++) {
                if (fields[i].getFieldValue() == fields[j].getFieldValue()
                        && fields[i].getFieldValue() != 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
