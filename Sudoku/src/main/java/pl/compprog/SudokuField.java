package pl.compprog;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * stores one sudoku field and allows to set and get it's value.
 */
public class SudokuField {
    /**
     * stores a value of the field.
     */
    private int value = 0;

    /**
     * returns the value of the field.
     *
     * @return returns the value of the field
     */
    public final int getFieldValue() {
        return this.value;
    }

    /**
     * sets the value of the field.
     *
     * @param inputValue value to be assigned to the field
     */
    public final void setFieldValue(final int inputValue) {
        this.value = inputValue;
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
                append(value).
                toString();
    }

    /**
     * Hashing function. Uses apache commons-langs3.
     *
     * @return unique identifier for the current sudoku board
     */
    @Override
    public final int hashCode() {
        return new HashCodeBuilder(3, 43).
                append(value).
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
        SudokuField rhs = (SudokuField) obj;
        return new EqualsBuilder().
                appendSuper(super.equals(obj)).
                append(value, rhs.value).
                isEquals();
    }
}
