package pl.compprog.gui;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import pl.compprog.BactrackingSudokuSolver;
import pl.compprog.SudokuBoard;
import pl.compprog.SudokuSolver;

import java.net.URL;


public class MainApp extends Application {
	static SudokuBoard sudokuBoard = new SudokuBoard();
	static SudokuSolver solver = new BactrackingSudokuSolver();
	static IntegerProperty[][] fields = new SimpleIntegerProperty[SudokuBoard.SIZE_OF_SUDOKU][SudokuBoard.SIZE_OF_SUDOKU];
	
	
	@Override
	public void start(Stage stage) throws Exception {
		URL location = getClass().getResource("/pl/compprog/gui/MainView.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(location);
		Pane pane = (Pane) fxmlLoader.load();
		Scene scene = new Scene(pane);
		
		GridPane gridpane = new GridPane();
		for (int i = 0; i < 10; i++) {
			RowConstraints row = new RowConstraints(50);
			gridpane.getRowConstraints().add(row);
		}
		
		stage.setScene(scene);
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
