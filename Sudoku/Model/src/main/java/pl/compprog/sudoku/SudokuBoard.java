package pl.compprog.sudoku;

import java.io.*;
import java.util.List;
import java.util.Arrays;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Class representing a sudoku board.
 */
public class SudokuBoard implements Serializable {
    /**
     * Size of rectangle representing sudoku board.
     */
    public static final int SIZE_OF_SUDOKU = 9;
    /**
     * First parameter for HashCodeBuilder. Must be a non zero odd number.
     * Should not be the same as in other classes.
     */
    private static final int HASH_CODE_INITIAL = 17;
    /**
     * Second parameter for HashCodeBuilder. Must be a non zero odd number.
     * Should not be the same as in other classes.
     */
    private static final int HASH_CODE_MULTIPLIER = 37;

    /**
     * Number of fields in a line (vertical or horizontal / row or column) in a
     * box (subgrid).
     */
    private final int fieldsInLineOfBox = 3;

    private static final long serialVersionUID = 1L;

    /**
     * Fixed size list of SudokuField representing sudoku board.
     */
    private List<SudokuField> board =
            Arrays.asList(new SudokuField[SIZE_OF_SUDOKU * SIZE_OF_SUDOKU]);


    {
        for (int i = 0; i < SIZE_OF_SUDOKU; i++) {
            for (int j = 0; j < SIZE_OF_SUDOKU; j++) {
                board.set(i * SIZE_OF_SUDOKU + j, new SudokuField());
            }
        }
    }

    /**
     * Gets the field from sudoku board at (row, column).
     *
     * @param y row of the needed field
     * @param x column of the needed field
     * @return field at (row, column).
     */
    public SudokuField getFieldAt(final int x, final int y) {
        if (x < 0 || y < 0 || x >= SIZE_OF_SUDOKU || y >= SIZE_OF_SUDOKU) {
            throw new IllegalArgumentException("Cannot access " + x + " "
                    + y);
        }
        return board.get(y * SIZE_OF_SUDOKU + x);
    }

    /**
     * Returns the row of SudokuFields at a given height y.
     *
     * @param y row's number / height
     * @return SudokuRow object containing a row of SudokuFields
     */
    public final SudokuRow getRow(final int y) {
        SudokuField[] row = new SudokuField[SIZE_OF_SUDOKU];
        for (int i = 0; i < SIZE_OF_SUDOKU; i++) {
            row[i] = getFieldAt(i, y);
        }
        return new SudokuRow(row);
    }


    /**
     * Returns the column of SudokuFields at a given lenght x.
     *
     * @param x column's number / length
     * @return SudokuColumn object containing a column of SudokuFields
     */
    public final SudokuColumn getColumn(final int x) {
        SudokuField[] col = new SudokuField[SIZE_OF_SUDOKU];
        for (int i = 0; i < SIZE_OF_SUDOKU; i++) {
            col[i] = getFieldAt(x, i);
        }
        return new SudokuColumn(col);
    }

    /**
     * Returns the position of the left top corner of the box in which the
     * given element is, whether it is y or x.
     *
     * @param num y or x coordinate
     * @return int representing one coordinate
     */
    private int getLeftBoxCorner(final int num) {
        return fieldsInLineOfBox * (num / fieldsInLineOfBox);
    }

    /**
     * Returns a box (3x3 subgrid) of the SudokuBoard that the given element
     * (x,y) resides in.
     *
     * @param x x coordinate of the field
     * @param y y coordinate of the field
     * @return SudokuBox object containing values from a box.
     */
    public final SudokuBox getBox(final int x, final int y) {
        SudokuField[] box = new SudokuField[SIZE_OF_SUDOKU];
        int newX = getLeftBoxCorner(x);
        int newY = getLeftBoxCorner(y);
        for (int i = 0; i < fieldsInLineOfBox; i++) {
            for (int j = 0; j < fieldsInLineOfBox; j++) {
                box[i * fieldsInLineOfBox + j] = getFieldAt(j + newX, i + newY);
            }
        }
        return new SudokuBox(box);
    }

    /**
     * Gets the integer value from sudoku board at (row, column).
     *
     * @param y row of the needed value
     * @param x column of the needed value
     * @return value at (row, column).
     */
    public final int get(final int x, final int y) {
        return getFieldAt(x, y).getValue();
    }


    /**
     * Sets the value from sudoku board at (row, column).
     *
     * @param y     row of the needed value
     * @param x     column of the needed value
     * @param value new value at (row, column).
     */
    public final void set(final int x, final int y, final int value) {
        getFieldAt(x, y).setValue(value);
        if ((!(getRow(y).verify() && getColumn(x).verify()
                && getBox(x, y).verify()) && value != 0)
                || value > SIZE_OF_SUDOKU || value < 0) {
            getFieldAt(x, y).setValue(0);
            throw new IllegalArgumentException(
                    "Cannot place the value " + value + " at " + x + ", " + y
                            + "");
        }
    }

    public final void unsafeSet(final int x, final int y, final int value) {
        getFieldAt(x, y).setValue(value);
    }


    /**
     * Determines whether one can put such number
     * on given (row, column) according to sudoku rules.
     *
     * @param row row of the checked value
     * @param col column of checked value
     * @param num checked value
     * @return true if such value can be placed and false if not
     */
    public final boolean canBePlaced(final int row, final int col,
                                     final int num) {
        for (int i = 0; i < SIZE_OF_SUDOKU; i++) {
            if (row != i && getFieldAt(col, i).getValue() == num) {
                return false;
            }
        }
        for (int j = 0; j < SIZE_OF_SUDOKU; j++) {
            if (col != j && getFieldAt(j, row).getValue() == num) {
                return false;
            }
        }

        int sqrtOfSize = (int) Math.sqrt(SIZE_OF_SUDOKU);

        int r = row - row % sqrtOfSize;
        int c = col - col % sqrtOfSize;

        for (int i = r; i < r + sqrtOfSize; i++) {
            for (int j = c; j < c + sqrtOfSize; j++) {
                if (row != i && col != j
                        && getFieldAt(j, i).getValue() == num) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Transforms sudoku board into printable string of characters. Uses
     * apache commons-langs3.
     *
     * @return returns string consisting of sudoku's values
     */
    @Override
    public final String toString() {
        var tsb = new ToStringBuilder(this);
        for(int i = 0; i < SIZE_OF_SUDOKU; i++) {
            tsb.append(getRow(i));
            tsb.append('\n');
        }
        return tsb.toString();
    }
    
    
    public final void print() {
        for(int i = 0; i < SIZE_OF_SUDOKU; i++) {
            for(int j = 0; j < SIZE_OF_SUDOKU; j++) {
                System.out.print(this.get(j,i));
                System.out.print(" ");
            }
            System.out.println("");
        }
    }

    /**
     * Hashing function. Uses apache commons-langs3.
     *
     * @return unique identifier for the current sudoku board
     */
    @Override
    public final int hashCode() {
        return new HashCodeBuilder(HASH_CODE_INITIAL, HASH_CODE_MULTIPLIER).
                append(board).
                toHashCode();
    }

    /**
     * Checks whether two sudoku boards are identical.
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
        SudokuBoard rhs = (SudokuBoard) obj;
        return new EqualsBuilder().
                append(board, rhs.board).
                isEquals();
    }

    /**
     * Tests whether each value of sudoku board is valid according to sudoku
     * rules.
     *
     * @return true if board is valid and false if not
     */
    public boolean checkBoard() {
        for(int i = 0; i <  SIZE_OF_SUDOKU * SIZE_OF_SUDOKU; i++) {
            if(board.get(i).getValue() == 0) {
                return false;
            }
        }
        for (int i = 0; i < SIZE_OF_SUDOKU; i++) {
            if (!getRow(i).verify()) {
                return false;
            }
        }
        for (int i = 0; i < SIZE_OF_SUDOKU; i++) {
            if (!getColumn(i).verify()) {
                return false;
            }
        }
        final int boxSide = 3;
        for (int i = 0; i < SIZE_OF_SUDOKU; i += boxSide) {
            for (int j = 0; j < SIZE_OF_SUDOKU; j += boxSide) {
                if (!getBox(i, j).verify()) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public  final SudokuBoard clone() {
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
            SudokuBoard clone = (SudokuBoard) ois.readObject();
            return (SudokuBoard) clone;
        } catch (IOException | ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            return null;
        }
    }
}
