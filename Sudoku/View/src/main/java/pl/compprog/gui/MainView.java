package pl.compprog.gui;

import javafx.application.Platform;
import javafx.beans.property.adapter.JavaBeanIntegerProperty;
import javafx.beans.property.adapter.JavaBeanIntegerPropertyBuilder;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import pl.compprog.*;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ResourceBundle;

public class MainView implements Initializable {
public Button verifyButton;
public MenuItem loadMenuItem;
public MenuItem saveMenuItem;
public MenuItem closeMenuItem;
public MenuItem easyMenuItem;
public MenuItem mediumMenuItem;
public MenuItem hardMenuItem;
public BorderPane mainPane;

private SudokuBoard board = new SudokuBoard();
private SudokuSolver solver = new BacktrackingSudokuSolver();
private Difficulty difficulty;
private GridPane grid;
private StringConverter converter = new IntegerStringConverter();
private JavaBeanIntegerProperty[][] integerProperties = new JavaBeanIntegerProperty[9][9];
private TextField[][] textFields = new TextField[9][9];

@Override
public void initialize(URL url, ResourceBundle resourceBundle) {
	grid = new GridPane();
	JavaBeanIntegerPropertyBuilder builder = JavaBeanIntegerPropertyBuilder.create();
	//j = x, i = y
	for (int i = 0; i < SudokuBoard.SIZE_OF_SUDOKU; i++) {
		for (int j = 0; j < SudokuBoard.SIZE_OF_SUDOKU; j++) {
			textFields[j][i] = new TextField();
			textFields[j][i].setPrefWidth(45);
			textFields[j][i].setPrefHeight(45);
			textFields[j][i].setAlignment(Pos.CENTER);
			//tf.setStyle("-fx-background-color: transparent;");
			textFields[j][i].setFont(Font.font(21.5));
			final int j2 = j, i2 = i;
			try {
				integerProperties[j][i] = builder.bean(board.getFieldAt(j, i)).name("value").build();
				textFields[j][i].textProperty().bindBidirectional(integerProperties[j][i], converter);
				textFields[j][i].textProperty().addListener((observable, oldValue, newValue) -> {
					if(newValue.equals("0")) {
						textFields[j2][i2].setText("");
					}
					else if (newValue.matches("\\d+") && (Integer.valueOf(newValue) > 9 || Integer.valueOf(newValue) < 0)) {
//						System.out.println("Value > 9 or < 0 at " + j2 + ", " + i2);
						textFields[j2][i2].setText(oldValue);
//						Platform.runLater(() -> {
//							integerProperties[j2][i2].set(0);
//						});
					}
//					else if (!(newValue.matches("\\d+")) && !newValue.equals("")) {
//						System.out.println("NaN at " + j2 + ", " + i2);
//						integerProperties[j2][i2].set(0);
//						textFields[j2][i2].setText("");
//					}
				});
				DecimalFormat format = new DecimalFormat( "#" );
				//https://stackoverflow.com/questions/31039449/java-8-u40-textformatter-javafx-to-restrict-user-input-only-for-decimal-number
				textFields[j][i].setTextFormatter( new TextFormatter<>(c ->
				{
					if ( c.getControlNewText().isEmpty() )
					{
						return c;
					}
					
					ParsePosition parsePosition = new ParsePosition( 0 );
					Object object = format.parse( c.getControlNewText(), parsePosition );
					
					if ( object == null || parsePosition.getIndex() < c.getControlNewText().length() )
					{
						return null;
					}
					else
					{
						return c;
					}
				}));
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
			textFields[j][i].setText("");
			textFields[j][i].setEditable(false);
			textFields[j][i].setStyle("-fx-background-color: #e0e0e0; -fx-cursor: none;");
//			textFields[j][i].setOnMouseEntered(new EventHandler<MouseEvent>() {
//				public void handle(MouseEvent event) {
//					textFields[j2][i2].getScene().setCursor(Cursor.NONE); //Change cursor to hand
//				}
//			});
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
}

public void verifyAction(ActionEvent actionEvent) {
	if (board.checkBoard()) {
		verifyButton.setText("CORRECT");
		verifyButton.setTextFill(Color.GREEN);
	} else {
		verifyButton.setText("WRONG");
		verifyButton.setTextFill(Color.RED);
	}
//	System.out.println("=================");
//	board.print();
}

public void loadAction(ActionEvent actionEvent) {
	SudokuBoardDaoFactory sudokuBoardDaoFactory = new SudokuBoardDaoFactory();
//	System.out.print("Reading... ");
	try(FileSudokuBoardDao dao = (FileSudokuBoardDao) sudokuBoardDaoFactory.getFileDao("sudoku.ser")) {
		board = dao.read();
//		System.out.println("SUCCESS");
	}
	catch (Exception e) {
//		System.out.println("SUCCESS");
		e.printStackTrace();
	}
	reinitializeBoard();
}

public void saveAction(ActionEvent actionEvent) {
	SudokuBoardDaoFactory sudokuBoardDaoFactory = new SudokuBoardDaoFactory();
//	System.out.print("Writing... ");
	try(FileSudokuBoardDao dao = (FileSudokuBoardDao) sudokuBoardDaoFactory.getFileDao("sudoku.ser")) {
		dao.write(board);
//		System.out.println("SUCCESS");
	}
	catch (Exception e) {
//		System.out.println("FAILED");
		e.printStackTrace();
	}
	reinitializeBoard();
}

public void closeAction(ActionEvent actionEvent) {
	Platform.exit();
}

private TextField getTextField(final int x, final int y) {
		Node result = null;
		ObservableList<Node> childrens = grid.getChildren();
		
		for (Node node : childrens) {
			if(grid.getRowIndex(node) == y && grid.getColumnIndex(node) == x) {
				result = node;
				break;
			}
		}
		
		return (TextField) result;
}

private void reinitializeBoard() {
//	System.out.println("Reinitializing...");
	for (int i = 0; i < SudokuBoard.SIZE_OF_SUDOKU; i++) {
		for (int j = 0; j < SudokuBoard.SIZE_OF_SUDOKU; j++) {
			int value = integerProperties[j][i].get();
			integerProperties[j][i].set(value);
			TextField tf = getTextField(j, i);
			if (value == 0) {
				tf.setEditable(true);
				tf.setStyle("-fx-background-color: #ffffff; -fx-cursor: text;");
//				tf.removeEventHandler(MouseEvent.MOUSE_ENTERED, tf.getOnMouseEntered());
			} else {
				tf.setEditable(false);
				tf.setStyle("-fx-background-color: #e0e0e0; -fx-cursor: none;");
//				tf.setOnMouseEntered(new EventHandler<MouseEvent>() {
//					public void handle(MouseEvent event) {
//						tf.getScene().setCursor(Cursor.NONE); //Change cursor to hand
//					}
//				});
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
}
