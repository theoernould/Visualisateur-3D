package controllers;

import UtilsClasses.SettingElement;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

public class SettingsController {
	@FXML
	protected VBox liste;
	
	public static double zoomPlus = 1.10;
	public static double zoomMoins = 0.90;
	
	@FXML
	public void initialize() {
		Slider slider = new Slider(1, 2, zoomPlus);
		slider.setOrientation(Orientation.HORIZONTAL);
		slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
    				zoomPlus = new_val.doubleValue();
                }
        });
		SettingElement zoomPlusElement = new SettingElement("Zoom plus : ", slider);
		slider = new Slider(0, 1, zoomMoins);
		slider.setOrientation(Orientation.HORIZONTAL);
		slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
    				zoomMoins = new_val.doubleValue();
                }
           });
		SettingElement zoomMoinsElement = new SettingElement("Zoom moins : ", slider);
		liste.getChildren().addAll(zoomPlusElement, zoomMoinsElement);
	}
}
