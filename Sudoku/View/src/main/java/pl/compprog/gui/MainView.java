package pl.compprog.gui;

import javafx.application.Platform;
import javafx.beans.property.adapter.JavaBeanIntegerProperty;
import javafx.beans.property.adapter.JavaBeanIntegerPropertyBuilder;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import pl.compprog.*;

import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.*;


public class MainView implements Initializable {
    private final String WAS_GENERATED_FILENAME = "wasGenerated.ser";
    private final String SUDOKU_FILENAME = "sudoku.ser";
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
    private Locale englishLocale = new Locale("en", "EN");
    private ResourceBundle englishBundle = ResourceBundle.getBundle("i18n.SudokuBundle", englishLocale);
    private ResourceBundle polishBundle = ResourceBundle.getBundle("i18n.SudokuBundle");
    private ResourceBundle currentBundle = englishBundle;
    private ResourceBundle englishBundleAuthors = ResourceBundle.getBundle("i18n.authors.AuthorsBundle", englishLocale);
    private ResourceBundle polishBundleAuthors = ResourceBundle.getBundle("i18n.authors.AuthorsBundle");
    private ResourceBundle currentBundleAuthors = englishBundleAuthors;
    private SudokuBoard board = new SudokuBoard();
    private SudokuSolver solver = new BacktrackingSudokuSolver();
    private Difficulty difficulty;
    private GridPane grid;
    private StringConverter converter = new IntegerStringConverter();
    private JavaBeanIntegerProperty[][] integerProperties = new JavaBeanIntegerProperty[9][9];
    private TextField[][] textFields = new TextField[9][9];
    private String currentVerifyButtonKey = "verify";
    private JavaBeanIntegerPropertyBuilder builder = JavaBeanIntegerPropertyBuilder.create();
    private boolean wasGenerated[][] = new boolean[9][9];

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
                    //https://stackoverflow.com/questions/31039449/java-8-u40-textformatter-javafx-to-restrict-user-input-only-for-decimal-number
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

        authorsLabel.setText(currentBundleAuthors.getString("authors_university") + ", " +
                currentBundleAuthors.getString("authors_country"));
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
        try (FileSudokuBoardDao dao = (FileSudokuBoardDao) sudokuBoardDaoFactory.getFileDao(SUDOKU_FILENAME)) {
            SudokuBoard tempBoard = dao.read();
            FileInputStream fis = new FileInputStream(WAS_GENERATED_FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            wasGenerated =  (boolean[][]) ois.readObject();
            reinitializeBoardLoading(tempBoard);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void saveAction(ActionEvent actionEvent) {
        SudokuBoardDaoFactory sudokuBoardDaoFactory = new SudokuBoardDaoFactory();
        try (FileSudokuBoardDao dao = (FileSudokuBoardDao) sudokuBoardDaoFactory.getFileDao(SUDOKU_FILENAME)) {
            dao.write(board);
            File file = new File(WAS_GENERATED_FILENAME);
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(wasGenerated);
            fos.close();
            oos.close();
        } catch (Exception e) {

            e.printStackTrace();
        }
        //reinitializeBoard();
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


    private void reinitializeBoardLoading(SudokuBoard tempBoard) {
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
                    wasGenerated [j][i] = false;
                    tf.setEditable(true);
                    tf.setStyle("-fx-background-color: #ffffff; -fx-cursor: text;");

                } else {
                    wasGenerated [j][i] = true;
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

    public void changeLangauge(boolean isEnglish) {
        if (isEnglish) {
            currentBundle = englishBundle;
            currentBundleAuthors = englishBundleAuthors;
        } else {
            currentBundle = polishBundle;
            currentBundleAuthors = polishBundleAuthors;
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
    }

    public void changeToPolishAction(ActionEvent actionEvent) {
        changeLangauge(false);
    }

    public void changeToEnglishAction(ActionEvent actionEvent) {
        changeLangauge(true);
    }
}
