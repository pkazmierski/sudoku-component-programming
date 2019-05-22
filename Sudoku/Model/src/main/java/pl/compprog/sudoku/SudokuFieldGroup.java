package pl.compprog.sudoku;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * abstract class for storing and verifying groups of SudokuBoard fields, for
 * example boxes, fields or columns.
 */
public abstract class SudokuFieldGroup implements Serializable, Cloneable {
    /**
     * stores the fields that make up a field group.
     */
    private List<SudokuField> fields;

    /**
     * First parameter for HashCodeBuilder. Must be a non zero odd number.
     * Should not be the same as in other classes.
     */
    private static final int HASH_CODE_INITIAL = 3;
    /**
     * Second parameter for HashCodeBuilder. Must be a non zero odd number.
     * Should not be the same as in other classes.
     */
    private static final int HASH_CODE_MULTIPLIER = 43;

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
    
//    public SudokuField get(int index) {
//        return fields.get(index);
//    }

    /**
     * verifies whether all the values in the fields are unique.
     *
     * @return returns the result of the test
     */
    public final boolean verify() {
        for (int i = 0; i < SudokuBoard.SIZE_OF_SUDOKU - 1; i++) {
            for (int j = i + 1; j < SudokuBoard.SIZE_OF_SUDOKU; j++) {
                if (fields.get(i).getValue() == fields.get(j)
                        .getValue()
                        && fields.get(i).getValue() != 0) {
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
        return new HashCodeBuilder(HASH_CODE_INITIAL, HASH_CODE_MULTIPLIER).
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
                append(fields, rhs.fields).
                isEquals();
    }

    @Override
    public  final SudokuFieldGroup clone() {
        byte[] object;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos);) {
            oos.writeObject(this);
            object = baos.toByteArray();

        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }

        try (ByteArrayInputStream bais = new ByteArrayInputStream(object);
             ObjectInputStream ois = new ObjectInputStream(bais);) {

            SudokuFieldGroup clone = (SudokuFieldGroup) ois.readObject();
            return (SudokuFieldGroup) clone;
        } catch (IOException | ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            return null;
        }
    }
}


