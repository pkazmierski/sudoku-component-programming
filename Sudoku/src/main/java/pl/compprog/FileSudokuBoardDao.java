package pl.compprog;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.File;

/**
 * Interface representing an abstract DAO object.
 */
public class FileSudokuBoardDao implements Dao<SudokuBoard>, AutoCloseable {

    /**
     * Name of processed file.
     */
    private String filename;

    /**
     * FileOutputStream object used to write
     * an object to file.
     */
    private FileOutputStream fos;

    /**
     * ObjectOutputStream object used to write
     * an object to file.
     */
    private ObjectOutputStream oos;

    /**
     * FileInputStream object used to read
     * an object from file.
     */
    private FileInputStream fis;

    /**
     * ObjectInputStream object used to read
     * an object from file.
     */
    private ObjectInputStream ois;


    /**
     * FileSudokuBoardDao constructor.
     *
     *@param filename1 Name of processed file
     */
    public FileSudokuBoardDao(final String filename1) {
        filename = filename1;

    }

    /**
     * Reads SudokuBoard object from a file
     * and returns it.
     *
     *@return SudokuBoard object read form the file
     */
    @Override
    public final SudokuBoard read() throws IOException, ClassNotFoundException {

        try (FileInputStream fis = new FileInputStream(filename);
        ObjectInputStream ois = new ObjectInputStream(fis)) {
            this.fis = fis;
            this.ois = ois;
            return (SudokuBoard) ois.readObject();
        }

    }

    /**
     * Writes SudokuBoard object to a file.
     *
     * @param obj SudokuBoard object to be written
     */
    @Override
    public final void write(final SudokuBoard obj) throws IOException {

        File file = new File(filename);
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            this.fos = fos;
            this.oos = oos;
            oos.writeObject(obj);
        }
    }

    /**
     * Overriden close method
     * to be sure that all used
     * resources are closed.
     */
    @Override
    public final void close() throws Exception {

        if (fos != null) {
            fos.close();
            oos.close();
        }
        if (fis != null) {
            fis.close();
            ois.close();
        }
    }
}
