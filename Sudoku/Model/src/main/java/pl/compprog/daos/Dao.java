package pl.compprog.daos;

import pl.compprog.exceptions.DaoException;

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
     * @throws DaoException execption which could be thrown
     */
    T readEx() throws DaoException;

    T read();

    /**
     * Writes T object to a file.
     *
     * @param obj T object to be written
     * @throws DaoException execption which could be thrown
     */
    void writeEx(T obj) throws DaoException;

    void write(T obj);
}
