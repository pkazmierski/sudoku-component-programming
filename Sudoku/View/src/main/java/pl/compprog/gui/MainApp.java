package pl.compprog.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class MainApp extends Application {
@Override
public void start(Stage stage) throws Exception {
	URL location = getClass().getResource("/pl/compprog/gui/MainView.fxml");
	FXMLLoader fxmlLoader = new FXMLLoader(location);
	Parent root = fxmlLoader.load();
	//BorderPane mainPane = (BorderPane) fxmlLoader.load();
	Scene scene = new Scene(root);
	stage.setTitle("Sudoku Game");
	stage.setScene(scene);
	stage.show();
}

public static void main(String[] args) {
	launch(args);
}
}
