package pl.compprog.daos;

import pl.compprog.exceptions.ApplicationException;
import pl.compprog.exceptions.DaoException;
import pl.compprog.sudoku.SudokuBoard;

public class JdbcSudokuBoardDao extends AbstractDao<SudokuBoard>{

    public JdbcSudokuBoardDao() throws ApplicationException {
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public SudokuBoard readEx() throws DaoException {
        return null;
    }

    @Override
    public SudokuBoard read() {
        return null;
    }

    @Override
    public void writeEx(SudokuBoard obj) throws DaoException {

    }

    @Override
    public void write(SudokuBoard obj) {

    }
}
