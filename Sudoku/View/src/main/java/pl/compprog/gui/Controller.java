package pl.compprog.gui;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import pl.compprog.*;

import java.net.URL;
import java.util.ResourceBundle;

import static pl.compprog.gui.MainApp.*;


public class Controller implements Initializable {
	public Button easyDifficultyeButton;
	public Button mediumDifficultyButton;
	public Button hardDifficultyButton;
	public Button verifyButton;
	public Button quitButton;
	public Label boardStatus;
	public Pane mainPane;
	private Difficulty d;
	private GridPane grid;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		grid = new GridPane();
//		for(int i = 0; i < SudokuBoard.SIZE_OF_SUDOKU; i++) {
//			for(int j = 0; j < SudokuBoard.SIZE_OF_SUDOKU; j++) {
//				fields[i][j] = new SimpleIntegerProperty(sudokuBoard.get(j,i));
//			}
//		}
		
		
		for(int i = 0; i < SudokuBoard.SIZE_OF_SUDOKU; i++) {
			for(int j = 0; j < SudokuBoard.SIZE_OF_SUDOKU; j++) {
				Label l = new Label(String.valueOf(sudokuBoard.get(j, i)));
//				l.textProperty().bind(fields[i][j].asString());
//
//				final ChangeListener changeListener = new ChangeListener() {
//					@Override
//					public void changed(ObservableValue observableValue, Object oldValue,
//					                    Object newValue) {
//						System.out.println("oldValue:"+ oldValue + ", newValue = " + newValue);
//					}
//				};
//
//				l.textProperty().addListener(changeListener);
				l.setFont(Font.font(24));
				grid.add(l, j, i);
			}
		}
		grid.setLayoutX(125);
		grid.setLayoutY(100);
		grid.setHgap(30);
		grid.setVgap(10);
		mainPane.getChildren().add(grid);
	}
	
	private void updateBoard() {
		for (Node child : grid.getChildren()) {
			Integer column = GridPane.getColumnIndex(child);
			Integer row = GridPane.getRowIndex(child);
			if (column != null && row != null) {
				Label l = (Label) child;
				l.setText(String.valueOf(sudokuBoard.get(column,row)));
			}
		}
	}

	public void quitGame(ActionEvent actionEvent) {
		Stage stage = (Stage) quitButton.getScene().getWindow();
		stage.close();
	}
	
	public void verifyBoard(ActionEvent actionEvent) {
		if(sudokuBoard.checkBoard()) {
			boardStatus.setText("CORRECT");
			boardStatus.setTextFill(Color.GREEN);
		} else {
			boardStatus.setText("WRONG");
			boardStatus.setTextFill(Color.RED);
		}
	}
	
	public void startMediumDifficulty(ActionEvent actionEvent) {
		solver.solve(sudokuBoard);
		d = new DifficultyNormal();
		d.prepareBoard(sudokuBoard);
		updateBoard();
	}

	public void startEasyDifficulty(ActionEvent actionEvent) {
		solver.solve(sudokuBoard);
		d = new DifficultyEasy();
		d.prepareBoard(sudokuBoard);
		updateBoard();
	}
	
	public void startHardDifficulty(ActionEvent actionEvent) {
		solver.solve(sudokuBoard);
		d = new DifficultyHard();
		d.prepareBoard(sudokuBoard);
		updateBoard();
	}
}
