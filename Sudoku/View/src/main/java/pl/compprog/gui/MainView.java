package pl.compprog.gui;

import javafx.application.Platform;
import javafx.beans.property.adapter.JavaBeanIntegerProperty;
import javafx.beans.property.adapter.JavaBeanIntegerPropertyBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
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

@Override
public void initialize(URL url, ResourceBundle resourceBundle) {
	solver.solve(board);
	grid = new GridPane();
	for (int i = 0; i < SudokuBoard.SIZE_OF_SUDOKU; i++) {
		for (int j = 0; j < SudokuBoard.SIZE_OF_SUDOKU; j++) {
			TextField tf = new TextField();
			tf.setPrefWidth(45);
			tf.setPrefHeight(45);
			tf.setAlignment(Pos.CENTER);
			//tf.setStyle("-fx-background-color: transparent;");
			tf.setFont(Font.font(21.5));
			try {
				JavaBeanIntegerPropertyBuilder builder = JavaBeanIntegerPropertyBuilder.create();
				JavaBeanIntegerProperty integerProperty = builder.bean(board.getFieldAt(j, i)).name("value").build();
				tf.textProperty().bindBidirectional(integerProperty, converter);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
			grid.add(tf, j, i);
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
	
	System.out.println("=================");
	board.print();
}

public void loadAction(ActionEvent actionEvent) {
}

public void saveAction(ActionEvent actionEvent) {
}

public void closeAction(ActionEvent actionEvent) {
	Platform.exit();
}

public void easyAction(ActionEvent actionEvent) {
	solver.solve(board);
	difficulty = new DifficultyEasy();
	difficulty.prepareBoard(board);
//	System.out.println("Preparing board: easy difficulty");
//	board.print();
}

public void mediumAction(ActionEvent actionEvent) {
	solver.solve(board);
	difficulty = new DifficultyHard();
	difficulty.prepareBoard(board);
}

public void hardAction(ActionEvent actionEvent) {
	solver.solve(board);
	difficulty = new DifficultyHard();
	difficulty.prepareBoard(board);
}
}
