package UtilsClasses;

import javafx.scene.control.TextArea;

/**
 * Classe qui créer notre console personnalisée
 */
public class Console {
	/**
	 * Attribut de notre console
	 */
    protected TextArea textArea;

    /**
     * On créer notre console
     * @param textArea
     */
    public Console(TextArea textArea) {
        this.textArea = textArea;
    }

    /**
     * On affiche le message dans la console
     * @param msg
     */
    public void println(String msg) {
        textArea.appendText(msg + "\n");
        textArea.setScrollTop(Double.MAX_VALUE);
    }

    /**
     * On efface notre console personnalisée
     */
    public void clear() {
        textArea.clear();
    }
}
