package pl.compprog;

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
     * @param value value to be assigned to the field
     */
    public final void setFieldValue(final int value) {
        this.value = value;
    }
}
