package views;

import javafx.application.Application;

/**
 * Main classe
 */
public class Main {

	// java --module-path 'path-to-javafx-binaries/lib' --add-modules
	// javafx.controls,javafx.fxml -jar .\projetmode.jar

	public static void main(String[] args) {
		Application.launch(GraphicRepere.class, args);
	}
}
