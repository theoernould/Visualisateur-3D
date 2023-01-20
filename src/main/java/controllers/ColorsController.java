package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.stage.Stage;
import maths.Repere;
import views.GraphicUtils;

/**
 * Class qui créé notre colorController
 */
public class ColorsController {

	/**
	 * ColorPicker qui modifie la couleur des faces
	 */
    @FXML protected ColorPicker face;
    /**
     * ColorPicker qui modifie la couleur des sommets
     */
    @FXML protected ColorPicker edge;
    /**
     * ColorPicker qui modifie la couleur des points
     */
    @FXML protected ColorPicker point;

    /**
     * Notre repere
     */
    protected Repere repere;
    
    /**
     * On initalise nos colorPicker
     */
    @FXML
    public void initialize() {
        face.setValue(GraphicUtils.face);
        edge.setValue(GraphicUtils.edge);
        point.setValue(GraphicUtils.point);
    }

    /**
     * on regarde si les couleurs des faces sont modifiées
     */
    public void faceModified() {
        GraphicUtils.face = face.getValue();
    }

    /**
     * On regarde si les couleurs des sommets sont modifiés
     */
    public void edgeModified() {
        GraphicUtils.edge = edge.getValue();
    }

    /**
     * On regarde si les couleurs des points sont modifiés
     */
    public void pointModified() {
        GraphicUtils.point = point.getValue();
    }

    /**
     * On initialise notre repere
     * @param repere
     */
    public void setRepere(Repere repere) {
        this.repere = repere;
    }

    /**
     * On ferme la fenêtre de notre color controller
     */
    public void close() {
        Repere repere = Repere.getSingleton();
        if(repere != null) repere.initCanvas();
        ((Stage) face.getScene().getWindow()).close();
    }
}
