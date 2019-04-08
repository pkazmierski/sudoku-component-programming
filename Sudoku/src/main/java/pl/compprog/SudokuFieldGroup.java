package pl.compprog;
import java.util.Arrays;
import java.util.List;

/**
 * abstract class for storing and verifying groups of SudokuBoard fields, for
 * example boxes, fields or columns.
 */
public abstract class SudokuFieldGroup {
    /**
     * stores the fields that make up a field group.
     */
    private List<SudokuField> fields;


    /**
     * assigns received array to the fields list.
     * @param c an fixed size list that should be made up of sudoku fields
     */
    public SudokuFieldGroup(final SudokuField[] c) {
        if (c.length != SudokuBoard.SIZE_OF_SUDOKU) {
            throw new IllegalArgumentException("Wrong length of the fields "
                    + "array.");
        }
        fields = Arrays.asList(c);
    }

    /**
     * verifies whether all the values in the fields are unique.
     * @return returns the result of the test
     */
    public final boolean verify() {
        for (int i = 0; i < SudokuBoard.SIZE_OF_SUDOKU - 1; i++) {
            for (int j = i + 1; j < SudokuBoard.SIZE_OF_SUDOKU; j++) {
                if (fields.get(i).getFieldValue() == fields.get(j).getFieldValue()
                        && fields.get(i).getFieldValue() != 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
