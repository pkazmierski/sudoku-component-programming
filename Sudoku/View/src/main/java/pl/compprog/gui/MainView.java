package pl.compprog.gui;

import javafx.application.Platform;
import javafx.beans.property.adapter.JavaBeanIntegerProperty;
import javafx.beans.property.adapter.JavaBeanIntegerPropertyBuilder;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import pl.compprog.logs.FileAndConsoleLoggerFactory;
import pl.compprog.daos.FileSudokuBoardDao;
import pl.compprog.daos.SudokuBoardDaoFactory;
import pl.compprog.difficulties.Difficulty;
import pl.compprog.difficulties.DifficultyEasy;
import pl.compprog.difficulties.DifficultyHard;
import pl.compprog.exceptions.ApplicationException;
import pl.compprog.exceptions.DaoException;
import pl.compprog.solvers.BacktrackingSudokuSolver;
import pl.compprog.solvers.SudokuSolver;
import pl.compprog.sudoku.SudokuBoard;

import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainView implements Initializable {

    public Button verifyButton;
    public MenuItem loadMenuItem;
    public MenuItem saveMenuItem;
    public MenuItem closeMenuItem;
    public MenuItem easyMenuItem;
    public MenuItem mediumMenuItem;
    public MenuItem hardMenuItem;
    public Menu fileMenu;
    public Menu newGameMenu;
    public BorderPane mainPane;
    public Menu languageMenu;
    public MenuItem englishMenuItem;
    public MenuItem polishMenuItem;
    public Label authorsLabel;
    public AnchorPane anchorPane;
    public MenuItem loadFromDbMenuItem;
    public MenuItem saveToDbMenuItem;
    public Menu dbMenu;
    public MenuItem deleteFromDbMenuItem;
    private FileChooser saveFileChooser = new FileChooser();
    private FileChooser loadFileChooser = new FileChooser();
    private Stage stage;
    private Locale englishLocale = new Locale("en", "EN");
    private ResourceBundle englishBundle = ResourceBundle.getBundle("i18n.SudokuBundle", englishLocale);
    private ResourceBundle polishBundle = ResourceBundle.getBundle("i18n.SudokuBundle");
    private ResourceBundle currentBundle = englishBundle;
    private ResourceBundle englishBundleAuthors = ResourceBundle.getBundle("i18n.authors.AuthorsBundle", englishLocale);
    private ResourceBundle polishBundleAuthors = ResourceBundle.getBundle("i18n.authors.AuthorsBundle");
    private ResourceBundle currentBundleAuthors = englishBundleAuthors;
    private static SudokuBoard board = new SudokuBoard();
    private SudokuSolver solver = new BacktrackingSudokuSolver();
    private Difficulty difficulty;
    private GridPane grid;
    private StringConverter converter = new IntegerStringConverter();
    private JavaBeanIntegerProperty[][] integerProperties = new JavaBeanIntegerProperty[9][9];
    private TextField[][] textFields = new TextField[9][9];
    private String currentVerifyButtonKey = "verify";
    private JavaBeanIntegerPropertyBuilder builder = JavaBeanIntegerPropertyBuilder.create();
    private static boolean[][] wasGenerated = new boolean[9][9];

    private static MainView instance;

    public MainView() {
        if (instance == null) {
            instance = this;
        } else {
            throw new RuntimeException("MainView already created!");
        }
    }

    public static MainView getInstance() {
        return instance;
    }

    public static boolean[][] getWasGenerated() {
        return wasGenerated;
    }

    public static void setWasGenerated(boolean[][] wasGenerated) {
        MainView.wasGenerated = wasGenerated;
    }

    public static SudokuBoard getSudokuBoard() {
        return board;
    }

    public static void setWasGenerated(SudokuBoard board) {
        MainView.board = board;
    }

    @SuppressWarnings("Duplicates")
    public void loadFromDbAction(ActionEvent actionEvent) throws IOException {
        URL location = getClass().getResource("/pl/compprog/gui/DbLoadDialogue.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        fxmlLoader.setResources(ResourceBundle.getBundle("i18n.SudokuBundle", new Locale("en", "EN")));
        Parent root = fxmlLoader.load();

        Scene secondScene = new Scene(root);
        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle(currentBundle.getString("db_load_dialogue"));
        newWindow.setScene(secondScene);
        // Specifies the modality for new window.
        newWindow.initModality(Modality.WINDOW_MODAL);
        // Specifies the owner Window (parent) for new window
        newWindow.initOwner(stage);
        // Set position of second window, related to primary window.
        newWindow.setX(stage.getX() + 50);
        newWindow.setY(stage.getY() + 200);
        newWindow.show();
    }

    @SuppressWarnings("Duplicates")
    public void saveToDbAction(ActionEvent actionEvent) throws IOException {
        URL location = getClass().getResource("/pl/compprog/gui/DbSaveDialogue.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        fxmlLoader.setResources(ResourceBundle.getBundle("i18n.SudokuBundle", new Locale("en", "EN")));
        Parent root = fxmlLoader.load();

        Scene secondScene = new Scene(root);
        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle(currentBundle.getString("db_save_dialogue"));
        newWindow.setScene(secondScene);
        // Specifies the modality for new window.
        newWindow.initModality(Modality.WINDOW_MODAL);
        // Specifies the owner Window (parent) for new window
        newWindow.initOwner(stage);
        // Set position of second window, related to primary window.
        newWindow.setX(stage.getX() + 50);
        newWindow.setY(stage.getY() + 200);
        newWindow.show();
    }

    @SuppressWarnings("Duplicates")
    public void deleteFromDbAction(ActionEvent actionEvent) throws IOException {
        URL location = getClass().getResource("/pl/compprog/gui/DbDeleteDialogue.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        fxmlLoader.setResources(ResourceBundle.getBundle("i18n.SudokuBundle", new Locale("en", "EN")));
        Parent root = fxmlLoader.load();
        Scene secondScene = new Scene(root);
        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle(currentBundle.getString("db_delete_dialogue"));
        newWindow.setScene(secondScene);
        // Specifies the modality for new window.
        newWindow.initModality(Modality.WINDOW_MODAL);
        // Specifies the owner Window (parent) for new window
        newWindow.initOwner(stage);
        // Set position of second window, related to primary window.
        newWindow.setX(stage.getX() + 50);
        newWindow.setY(stage.getY() + 200);
        newWindow.show();
    }

    private enum Language {ENGLISH, POLISH}
    private final Logger logger = FileAndConsoleLoggerFactory.getConfiguredLogger(MainView.class.getName());
    private final ResourceBundle messagesBundle = ResourceBundle.getBundle("pl.compprog.messages");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        grid = new GridPane();

        for (int i = 0; i < SudokuBoard.SIZE_OF_SUDOKU; i++) {
            for (int j = 0; j < SudokuBoard.SIZE_OF_SUDOKU; j++) {
                textFields[j][i] = new TextField();
                textFields[j][i].setPrefWidth(45);
                textFields[j][i].setPrefHeight(45);
                textFields[j][i].setAlignment(Pos.CENTER);
                textFields[j][i].setFont(Font.font(21.5));
                final int j2 = j, i2 = i;
                try {
                    integerProperties[j][i] = builder.bean(board.getFieldAt(j, i)).name("value").build();
                    textFields[j][i].textProperty().bindBidirectional(integerProperties[j][i], converter);
                    textFields[j][i].textProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue.equals("0")) {
                            textFields[j2][i2].setText("");
                        } else if (newValue.matches("\\d+") && (Integer.valueOf(newValue) > 9 || Integer.valueOf(newValue) < 0)) {
                            textFields[j2][i2].setText(oldValue);
                        }

                    });
                    DecimalFormat format = new DecimalFormat("#");
                    textFields[j][i].setTextFormatter(new TextFormatter<>(c ->
                    {
                        if (c.getControlNewText().isEmpty()) {
                            return c;
                        }

                        ParsePosition parsePosition = new ParsePosition(0);
                        Object object = format.parse(c.getControlNewText(), parsePosition);

                        if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
                            return null;
                        } else {
                            return c;
                        }
                    }));
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                textFields[j][i].setText("");
                textFields[j][i].setEditable(false);
                textFields[j][i].setStyle("-fx-background-color: #e0e0e0; -fx-cursor: none;");
                grid.add(textFields[j][i], j, i);
            }
        }
        grid.setHgap(5);
        grid.setVgap(5);

        HBox hBox = new HBox();
        hBox.getChildren().add(grid);
        hBox.setAlignment(Pos.CENTER);

        VBox vBox = new VBox();
        vBox.getChildren().add(hBox);
        vBox.setAlignment(Pos.CENTER);
        mainPane.setCenter(vBox);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("SER files (*.ser)", "*.ser");
        saveFileChooser.getExtensionFilters().add(extFilter);
        loadFileChooser.getExtensionFilters().add(extFilter);
        authorsLabel.setText(currentBundleAuthors.getString("authors_university") + ", " +
                currentBundleAuthors.getString("authors_country"));

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void verifyAction(ActionEvent actionEvent) {
        if (board.checkBoard()) {
            currentVerifyButtonKey = "correct";
            verifyButton.setTextFill(Color.GREEN);
        } else {
            currentVerifyButtonKey = "wrong";
            verifyButton.setTextFill(Color.RED);
        }
        verifyButton.setText(currentBundle.getString(currentVerifyButtonKey));
    }

    public void loadAction(ActionEvent actionEvent) {
        SudokuBoardDaoFactory sudokuBoardDaoFactory = new SudokuBoardDaoFactory();
        File boardFile = loadFileChooser.showOpenDialog(stage);
        if (boardFile != null) {
            try (FileSudokuBoardDao dao =
                         (FileSudokuBoardDao) sudokuBoardDaoFactory.getFileDao(boardFile.getAbsolutePath(), wasGenerated)) {
                SudokuBoard tempBoard = dao.readEx();
                wasGenerated = dao.getWasGenerated();
                reinitializeBoardLoading(tempBoard);
            } catch (DaoException | IOException ex) {
                logger.log(Level.SEVERE, messagesBundle.getString(DaoException.OPEN_ERROR), ex);
            }catch (ApplicationException aex) {
                logger.log(Level.SEVERE, messagesBundle.getString(ApplicationException.RESOURCE_BUNDLE_IS_NULL), aex);
            }
        }
    }

    public void saveAction(ActionEvent actionEvent) {
        SudokuBoardDaoFactory sudokuBoardDaoFactory = new SudokuBoardDaoFactory();
        File boardFile = saveFileChooser.showSaveDialog(stage);
        if (boardFile != null) {
            try (FileSudokuBoardDao dao =
                         (FileSudokuBoardDao) sudokuBoardDaoFactory.getFileDao(boardFile.getAbsolutePath(), wasGenerated)) {
                dao.writeEx(board);
            } catch (DaoException | IOException ex) {
                logger.log(Level.SEVERE, messagesBundle.getString(DaoException.NULL_FILE), ex);
            } catch (ApplicationException aex) {
                logger.log(Level.SEVERE, messagesBundle.getString(ApplicationException.RESOURCE_BUNDLE_IS_NULL), aex);
            }
        }
    }

    public void closeAction(ActionEvent actionEvent) {
        Platform.exit();
    }

    private TextField getTextField(final int x, final int y) {
        Node result = null;
        ObservableList<Node> childrens = grid.getChildren();

        for (Node node : childrens) {
            if (grid.getRowIndex(node) == y && grid.getColumnIndex(node) == x) {
                result = node;
                break;
            }
        }

        return (TextField) result;
    }


    public void reinitializeBoardLoading(SudokuBoard tempBoard) {
        for (int i = 0; i < SudokuBoard.SIZE_OF_SUDOKU; i++) {
            for (int j = 0; j < SudokuBoard.SIZE_OF_SUDOKU; j++) {
                int value = tempBoard.get(j, i);
                integerProperties[j][i].set(value);
                TextField tf = getTextField(j, i);
                if (value == 0 || !wasGenerated[j][i]) {
                    tf.setEditable(true);
                    tf.setStyle("-fx-background-color: #ffffff; -fx-cursor: text;");

                } else {
                    tf.setEditable(false);
                    tf.setStyle("-fx-background-color: #e0e0e0; -fx-cursor: none;");

                }
            }
        }
    }

    private void reinitializeBoard() {
        for (int i = 0; i < SudokuBoard.SIZE_OF_SUDOKU; i++) {
            for (int j = 0; j < SudokuBoard.SIZE_OF_SUDOKU; j++) {
                int value = integerProperties[j][i].get();
                integerProperties[j][i].set(value);
                TextField tf = getTextField(j, i);
                if (value == 0) {
                    wasGenerated[j][i] = false;
                    tf.setEditable(true);
                    tf.setStyle("-fx-background-color: #ffffff; -fx-cursor: text;");

                } else {
                    wasGenerated[j][i] = true;
                    tf.setEditable(false);
                    tf.setStyle("-fx-background-color: #e0e0e0; -fx-cursor: none;");

                }
            }
        }
    }

    public void easyAction(ActionEvent actionEvent) {
        solver.solve(board);
        difficulty = new DifficultyEasy();
        difficulty.prepareBoard(board);
        reinitializeBoard();
    }

    public void mediumAction(ActionEvent actionEvent) {
        solver.solve(board);
        difficulty = new DifficultyHard();
        difficulty.prepareBoard(board);
        reinitializeBoard();
    }

    public void hardAction(ActionEvent actionEvent) {
        solver.solve(board);
        difficulty = new DifficultyHard();
        difficulty.prepareBoard(board);
        reinitializeBoard();
    }

    public void changeLangauge(Language language) {
        switch (language) {
            case ENGLISH:
                currentBundle = englishBundle;
                currentBundleAuthors = englishBundleAuthors;
                break;

            case POLISH:
                currentBundle = polishBundle;
                currentBundleAuthors = polishBundleAuthors;
                break;
        }
        loadMenuItem.setText(currentBundle.getString("load"));
        saveMenuItem.setText(currentBundle.getString("save"));
        closeMenuItem.setText(currentBundle.getString("close"));
        easyMenuItem.setText(currentBundle.getString("easy"));
        mediumMenuItem.setText(currentBundle.getString("medium"));
        hardMenuItem.setText(currentBundle.getString("hard"));
        fileMenu.setText(currentBundle.getString("file"));
        newGameMenu.setText(currentBundle.getString("new_game"));
        verifyButton.setText(currentBundle.getString(currentVerifyButtonKey));
        languageMenu.setText(currentBundle.getString("language"));
        englishMenuItem.setText(currentBundle.getString("english"));
        polishMenuItem.setText(currentBundle.getString("polish"));
        authorsLabel.setText(currentBundleAuthors.getString("authors_university") + ", " +
                currentBundleAuthors.getString("authors_country"));
        dbMenu.setText(currentBundle.getString("database"));
        stage.setTitle(currentBundle.getString("sudoku_game"));
        loadFromDbMenuItem.setText(currentBundle.getString("load"));
        saveToDbMenuItem.setText(currentBundle.getString("save"));
        deleteFromDbMenuItem.setText(currentBundle.getString("delete"));
    }

    public void changeToPolishAction(ActionEvent actionEvent) {
        changeLangauge(Language.POLISH);
    }

    public void changeToEnglishAction(ActionEvent actionEvent) {
        changeLangauge(Language.ENGLISH);
    }

    public ResourceBundle getCurrentBundle() {
        return currentBundle;
    }
}
