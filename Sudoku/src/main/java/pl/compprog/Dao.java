package pl.compprog;

/**
 * Interface representing a generic DAO object.
 *
 * @param <T> generic object
 */
public interface Dao<T> {

    /**
     * Reads T object from a file
     * and returns it.
     *
     * @return T object read form the file
     * @throws Exception execption which could be thrown
     */
    T read() throws Exception;

    /**
     * Writes T object to a file.
     *
     * @param obj T object to be written
     * @throws Exception execption which could be thrown
     */
    void write(T obj) throws  Exception;
}
