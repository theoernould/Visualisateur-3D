package UtilsClasses;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class SettingElement extends HBox {
	Text libelle;
	Node modifier;
	
	public SettingElement(String libelle, Node modifier) {
		super();
		this.libelle = new Text(libelle);
		this.modifier = modifier;
		this.getChildren().addAll(this.libelle,modifier);
	}
	
}
