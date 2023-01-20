package UtilsClasses;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import views.GraphicUtils;

/**
 * Classe pour créer la preview d'un fichier PLY
 *
 */
public class Preview extends HBox implements Comparable<Preview> {

	/**
	 * Le nom du de la preview
	 */
	protected Text fileName;
	/**
	 * L'image de notre preview
	 */
	protected ImageView image;
	/**
	 * L'écart entre l'image et le nom de la preview
	 */
	public static final double SPACING = 12.5;
	/**
	 * Image par défaut représentant une image "vide"
	 */
	private static ImageView defaultPreview = GraphicUtils.getResourceImageView("vide.png");
	
	/**
	 * On créer une préview qu'avec le nom 
	 * de fichier et une image par défault 
	 * (l'image vide)
	 * @param fileName (String)
	 */
	public Preview(String fileName) {
		this(fileName, defaultPreview, new Button("+"));
	}
	
	
	/**
	 * On créer notre préview grâce à un nom de fichier et une image
	 * @param fileName (String)
	 * @param image (ImageView)
	 */
	public Preview(String fileName, ImageView image, Button button) {
		super(SPACING);
		this.fileName = new Text(fileName);
		this.image = image;
		this.setAlignment(Pos.CENTER_LEFT);
		super.getChildren().addAll(this.image, this.fileName, button);
	}

	@Override
	public String toString() {
		return fileName + " " + image;
	}

	public String getFileName() {
		return fileName.getText();
	}

	public ImageView getImage() {
		return image;
	}

	@Override
	public int compareTo(Preview preview) {
		return getFileName().compareTo(preview.getFileName());
	}
}
