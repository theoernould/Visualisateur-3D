package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import maths.Repere;
import views.GraphicUtils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.function.BiConsumer;

/**
 * Notre classe qui montre les détails d'un objet ply
 */
public class DetailsController {
	/**
	 * Notre liste de detail
	 */
    @FXML
    protected ListView<DetailItem> details;

    // Edition des informations du modèle pas finies donc pas éditables.
    public void setRepere(Repere repere) {
        String descStr = repere.getDescription() == null ? "" : repere.getDescription();
        String auteurStr = repere.getAuteur() == null ? "" : repere.getAuteur();
        LocalDate dateLD = repere.getDate();
        DetailItem nom = new DetailItem<String>(false,"Nom du modèle : ", repere.getName(), repere, Repere::setName, "nom");
        DetailItem auteur = new DetailItem<String>(false,"Auteur : ", auteurStr, repere, Repere::setAuteur, "auteur");
        DetailItem date = new DetailItem<LocalDate>(false,"Date : ", dateLD, repere, Repere::setDate, "date");
        DetailItem description = new DetailItem<String>(false,"Description : ", descStr, repere, Repere::setDescription, "description");
        DetailItem nbPoints = new DetailItem(false,"Nombre de points : ","" +  repere.getPointsSize(), null, null, "");
        DetailItem nbFaces = new DetailItem(false,"Nombre de faces : ","" +  repere.getPolygonesSize(), null, null, "");
        details.getItems().addAll(nom, auteur, date, description, nbPoints, nbFaces);
    }


    public class DetailItem<T> extends HBox {
        /**
         * Indique à l'utilisateur le nomde l'attribut qu'il change
         */
    	protected Text label;
        /*
         * Valeur de base
         */
        protected Text text;
        protected T value;
        protected Repere repere;
        /**
         * Valeur que l'utilisateur peut modifier
         */
        protected TextField field;
        protected boolean editable;

        /**
         * Création des détail d'un objet
         * @param label
         * @param value
         * @param repere
         */
        public DetailItem(boolean editable, String label, T value, Repere repere, BiConsumer<Repere, T> apply, String name) {
            super();
            this.editable = editable;
            this.label = new Text(label);
            this.text = new Text(value == null ? "" : value.toString());
            this.field = new TextField();
            this.value = value;
            this.repere = repere;
            field.setVisible(false);
            if(editable) {
                this.text.setOnMouseClicked(eValue -> {
                    if (eValue.getClickCount() >= 2) {
                        this.text.setVisible(false);
                        this.field.setText(this.value == null ? "null" : this.value.toString());
                        this.field.setVisible(true);
                    }
                });
                this.field.setOnKeyReleased(key -> {
                    if (key.getCode().equals(KeyCode.ENTER)) {
                        this.field.setVisible(false);
                        String val = this.field.getText();
                        if(this.value instanceof LocalDate) {
                            try {
                                this.value = (T) LocalDate.parse(val);
                            } catch(DateTimeParseException e) {
                                GraphicUtils.showMessage(Alert.AlertType.WARNING, "Date invalide", "Veuillez rentrer une date valide");
                            }
                        } else {
                            this.value = (T) val;
                        }
                        this.text.setText(val);
                        this.text.setVisible(true);
                        apply.accept(repere, value);
                    }
                });
            }
            this.getChildren().addAll(this.label, this.text, this.field);
        }

        public T getValue() {
            return value;
        }
    }


}
