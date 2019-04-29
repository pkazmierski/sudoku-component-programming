package pl.compprog;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Class used to test SudokuRow class.
 */
class SudokuRowTest {

    /**
     * Tests whether cloned sudokuRow is not the
     * same board as the previous one.
     */
    @Test
    public void cloneTest(){
        SudokuField[] array = new SudokuField[9];
        for (int i = 0; i < 9; i++) {
            array[i] = new SudokuField();
            array[i].setValue(i);
        }

        SudokuRow r1 = new SudokuRow(array);
        SudokuRow r2 = (SudokuRow) r1.clone();

        assertNotSame(r1, r2);
        assertEquals(r1, r2);
    }

}