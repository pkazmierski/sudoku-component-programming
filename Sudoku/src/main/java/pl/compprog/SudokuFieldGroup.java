package pl.compprog;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.builder.*;

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
     *
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
     *
     * @return returns the result of the test
     */
    public final boolean verify() {
        for (int i = 0; i < SudokuBoard.SIZE_OF_SUDOKU - 1; i++) {
            for (int j = i + 1; j < SudokuBoard.SIZE_OF_SUDOKU; j++) {
                if (fields.get(i).getFieldValue() == fields.get(j)
                        .getFieldValue()
                        && fields.get(i).getFieldValue() != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Transforms sudoku field group into printable string of characters. Uses
     * apache commons-langs3.
     *
     * @return returns string consisting of sudoku's values
     */
    @Override
    public final String toString() {
        return new ToStringBuilder(this).
                append(fields).
                toString();
    }

    /**
     * Hashing function. Uses apache commons-langs3.
     *
     * @return unique identifier for the current sudoku board
     */
    @Override
    public final int hashCode() {
        return new HashCodeBuilder(9, 49).
                append(fields).
                toHashCode();
    }

    /**
     * Checks whether two sudoku field groups are identical.
     *
     * @return true if so and false if not
     */
    @Override
    public final boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        SudokuFieldGroup rhs = (SudokuFieldGroup) obj;
        return new EqualsBuilder().
                appendSuper(super.equals(obj)).
                append(fields, rhs.fields).
                isEquals();
    }
}
