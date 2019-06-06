package pl.compprog.gui;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pl.compprog.daos.FileSudokuBoardDao;
import pl.compprog.daos.JdbcSudokuBoardDao;
import pl.compprog.daos.SudokuBoardDaoFactory;
import pl.compprog.exceptions.ApplicationException;
import pl.compprog.exceptions.DaoException;
import pl.compprog.logs.FileAndConsoleLoggerFactory;
import pl.compprog.sudoku.SudokuBoard;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbSaveDialogue implements Initializable {
    public TextField saveNameField;
    public Button saveDbButton;
    private final Logger logger = FileAndConsoleLoggerFactory.getConfiguredLogger(DbSaveDialogue.class.getName());
    private final ResourceBundle messagesBundle = ResourceBundle.getBundle("pl.compprog.messages");

    private Locale englishLocale = new Locale("en", "EN");
    private ResourceBundle englishBundle = ResourceBundle.getBundle("i18n.SudokuBundle", englishLocale);
    private ResourceBundle polishBundle = ResourceBundle.getBundle("i18n.SudokuBundle");
    private ResourceBundle currentBundle = englishBundle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        saveNameField.setPromptText(MainView.getInstance().getCurrentBundle().getString("save_name"));
        saveDbButton.setText(MainView.getInstance().getCurrentBundle().getString("save"));
    }

    private enum Language {ENGLISH, POLISH}

    private boolean isBoardEmpty(SudokuBoard boardToTest) {
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                if(boardToTest.get(i, j) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void saveToDb(ActionEvent actionEvent) {
        SudokuBoardDaoFactory sudokuBoardDaoFactory = new SudokuBoardDaoFactory();
        if (!saveNameField.getText().equals("") && !isBoardEmpty(MainView.getSudokuBoard())) {
            try (JdbcSudokuBoardDao dao = (JdbcSudokuBoardDao) sudokuBoardDaoFactory.getDatabaseDao(saveNameField.getText(), MainView.getWasGenerated())) {
                dao.writeEx(MainView.getSudokuBoard());
            } catch (DaoException | SQLException ex) {
                logger.log(Level.SEVERE, messagesBundle.getString(DaoException.NULL_FILE), ex);
            } catch (ApplicationException aex) {
                logger.log(Level.SEVERE, messagesBundle.getString(ApplicationException.RESOURCE_BUNDLE_IS_NULL), aex);
            }

            Stage stage = (Stage) saveDbButton.getScene().getWindow();
            stage.close();
        }
    }
}
