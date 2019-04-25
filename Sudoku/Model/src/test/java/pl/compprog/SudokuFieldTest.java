package pl.compprog;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class used to test SudokuFiled class.
 */
class SudokuFieldTest {

    @Test
    void toStringTest() {
    }

    @Test
    void hashCodeTest() {
    }

    @Test
    void equalsTest() {
    }

    /**
     * Tests compareTo method of SudokuField
     * object.
     */
    @Test
    void compareToTest() {

        SudokuField s1 = new SudokuField();
        s1.setFieldValue(1);
        SudokuField s2 = new SudokuField();
        s2.setFieldValue(2);
        SudokuField s3 = new SudokuField();
        s3.setFieldValue(3);
        SudokuField s1a = new SudokuField();
        s1a.setFieldValue(1);

        assertTrue(s2.compareTo(s1) > 0);
        assertTrue(s2.compareTo(s3) < 0);
        assertEquals(s1.compareTo(s1a), 0);


    }
}