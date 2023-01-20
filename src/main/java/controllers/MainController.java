package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;

import javax.imageio.ImageIO;

import errors.EndHeaderException;
import errors.NoAsciiFileException;
import errors.NoFaceException;
import errors.NoVertexEception;
import UtilsClasses.ComparatorInfosElement;
import UtilsClasses.InfosElement;
import io.PlyReader;
import javafx.animation.AnimationTimer;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import maths.Repere;
import maths.Vecteur;
import observer.Observer;
import views.ConnectedCanvas;
import UtilsClasses.Console;
import UtilsClasses.DrawMethods;
import views.GraphicUtils;
import UtilsClasses.LoadingWindow;
import UtilsClasses.Preview;

public class MainController {

	@FXML
	protected BorderPane mainStage; // BorderPane global
	@FXML
	protected ListView<Preview> gestionnaire; // Liste des fichiers ply
	@FXML
	protected GridPane grid; // Grille
	@FXML
	protected TextField fileText; // TextField du gestionnaire de fichiers
	@FXML
	protected TextArea console; // Console des logs - à mettre dans une page spécifique !

	@FXML
	protected RadioMenuItem colorDisplay; // Choix des couleurs par défaut ou couleurs du modèle
	@FXML
	protected RadioMenuItem aretesDisplay; // Choix de l'affichage des arêtes
	@FXML
	protected RadioMenuItem eclairageDisplay; // Choix de l'affichage de l'éclairage
	@FXML
	protected RadioMenuItem ombresDisplay; // Choix de l'affichage des ombres

	@FXML
	protected ImageView transRotImage;
	@FXML
	protected ImageView axisImage;
	@FXML
	protected ImageView sortImage;
	@FXML
	protected Button eclairageButton;
	@FXML
	protected Button lissageButton;
	@FXML
	protected Button pointsButton;
	@FXML
	protected Button aretesButton;
	@FXML
	protected Button colorsButton;
	@FXML
	protected Button horlogeButton;
	@FXML
	protected ComboBox<String> sortBox;

	private Repere repere; // Repere actuellement utilisé

	private Stage stage; // Stage de la fenêtre
	private File dir; // Chemin du dossier des PLY
	private File selectedFile; // Fichier actuellement affiché
	public static Console cons; // Console liée au modèle affiché

	public static double z = 0;
	private static final String PNG_EXTENSION = ".png";
	private static final String PLY_EXTENSION = ".ply";
	private static final int HEIGHT_PREVIEW = 50;
	private static final int WIDTH_PREVIEW = 50;

	private final double PAS_ROT = 5.0;
	private double lastX = -1212;
	private double lastY = -1212;

	private List<Canvas> canvasList = new ArrayList<>();

	private boolean translationMode = true;
	private String transAxis = "x";
	private boolean colorMode = true;
	private boolean eclairageMode = true;
	private boolean aretesVisibles = true;
	private boolean pointsVisibles = true;
	private boolean controleurHorloge = false;
	private boolean lissage = false;
	private boolean sortAsc = true;

	ComparatorInfosElement sortComp = null;
	BiPredicate<InfosElement, String> sortPredicate = null;

	private ConnectedCanvas dessus;
	private ConnectedCanvas droite;
	private ConnectedCanvas face;
	private boolean aretesVisible = true;
	
	private static final String PAS_DE_REPERE = "Impossible d'appliquer la transformation, aucun repère n'est chargé !";
	private static final String BUTTON_SELECTED = "buttonSelected";

	/**
	 * Fonction appelée lorsque le FXML est chargé, semblable à un main
	 */
	@FXML
	public void initialize() throws IOException {
		cons = new Console(console);
		dir = new File("./");
		sortComp = new ComparatorInfosElement<String>(InfosElement::getNom);
		sortPredicate = (elm, str) -> !elm.getNom().contains(str);
		sortBox.getItems().addAll("Nom","Auteur","Description","Date","Nb faces", "Nb arêtes");
		sortBox.getSelectionModel().select(0);
		refreshGestionnaire();
		initToggleButtons();
		for (Node node : mainStage.getChildren()) {
			if (!node.equals(fileText)) {
				node.setOnMouseClicked(e -> {
					mainStage.requestFocus();
				});
			}
		}
	}

	/**
	 * Ferme la fenêtre
	 */
	public void close() {
		stage.close();
	}

	/**
	 * Ouvre un modèle via l'explorateur de fichiers
	 */
	public void openFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(dir);
		fileChooser.setTitle("Ouvrir un modèle 3D");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Fichiers PLY", "*.ply"));
		File selectedFile = fileChooser.showOpenDialog(stage);
		if (selectedFile != null) {
			try {
				loadFile(selectedFile.getCanonicalPath());
			} catch (NoAsciiFileException e) {
				GraphicUtils.showMessage(Alert.AlertType.ERROR, e.getMessage(), null);
			} catch (IOException e) {
				GraphicUtils.showMessage(Alert.AlertType.ERROR, "Fichier impossible à récupérer", null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			GraphicUtils.showMessage(Alert.AlertType.WARNING, "Aucun fichier choisi !", null);
		}

	}

	/**
	 * Ouvrir un dossier pour afficher les modèles contenus dedans
	 */
	public void openDir() throws IOException {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setInitialDirectory(dir);
		File selectedDir = directoryChooser.showDialog(stage);
		if (selectedDir != null) {
			dir = selectedDir;
		}
		refreshGestionnaire();
	}

	/**
	 * Ouvre la fenêtre d'édition des couleurs d'affichage par défaut
	 *
	 * @throws Exception
	 */
	public void defaultColors() throws Exception {
		Stage newStage = new Stage();
		newStage.initOwner(stage);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/choixCouleurs.fxml"));
		BorderPane root = loader.load();
		Scene scene = new Scene(root);
		newStage.setScene(scene);
		newStage.setOnCloseRequest(e -> {
			canvasUpdate();
		});
		newStage.setTitle("Configuration des couleurs");
		newStage.show();
		newStage.centerOnScreen();
	}

	/**
	 * Affiche les couleurs
	 */
	public void colorsClicked() {
		colorMode = !colorMode;
		if (colorMode) {
			colorsButton.setStyle(null);
			colorsButton.getStyleClass().add(BUTTON_SELECTED);
		} else {
			colorsButton.getStyleClass().clear();
		}
		canvasUpdate();
	}

	/**
	 * Switch entre la propri�t� translation et rotation
	 */
	public void transRotClicked() {
		translationMode = !translationMode;
		Image image;
		if (translationMode) {
			image = new Image(getClass().getResourceAsStream("/translation.png"));
		} else {
			image = new Image(getClass().getResourceAsStream("/rotation.png"));
		}
		transRotImage.setImage(image);
	}

	/**
	 * D�finit l'axe sur lequel on agit (X, Y ou Z)
	 */
	public void axisClicked() {
		Image image = null;
		switch (transAxis) {
			case "x":
				transAxis = "y";
				image = new Image(getClass().getResourceAsStream("/y.png"));
				break;
			case "y":
				transAxis = "z";
				image = new Image(getClass().getResourceAsStream("/z.png"));
				break;
			case "z":
				transAxis = "all";
				image = new Image(getClass().getResourceAsStream("/bidirectionnel.png"));
				break;
			case "all":
				transAxis = "x";
				image = new Image(getClass().getResourceAsStream("/x.png"));
				break;
		}
		axisImage.setImage(image);
	}

	public void sortDirClicked() throws IOException {
		Image image = null;
		sortAsc = !sortAsc;
		if(sortAsc)
			image = new Image(getClass().getResourceAsStream("/down.png"));
		else
			image = new Image(getClass().getResourceAsStream("/up.png"));
		sortImage.setImage(image);
		refreshGestionnaire();
	}

	/**
	 * Recentre le repere
	 */
	public void centrageClicked() {
		if (repere != null)
			repere.center(repere.getPoints());
		else {
			GraphicUtils.showMessage(Alert.AlertType.WARNING, "Aucun modèle n'est chargé !", null);
		}
	}

	/**
	 * R��value l'�chelle du modele 3D pour mieux le visualiser
	 */
	public void echelleClicked() {
		if (repere != null)
			autoEchelle();
		else {
			GraphicUtils.showMessage(Alert.AlertType.WARNING, "Aucun modèle n'est chargé !", null);
		}
	}

	/**
	 * Ajoute l'�clairage sur le modele
	 */
	public void eclairageClicked() {
		eclairageMode = !eclairageMode;
		if (eclairageMode) {
			eclairageButton.setStyle(null);
			eclairageButton.getStyleClass().add(BUTTON_SELECTED);
		} else {
			eclairageButton.getStyleClass().clear();
		}
		canvasUpdate();
	}

	/**
	 * Visualisation de la vue en tranche du modele
	 */
	public void lissageClicked() {
		lissage = !lissage;
		if (lissage) {
			/*if(repere != null && repere.getFaceNbPoints() != 3) {
    			GraphicUtils.showMessage(Alert.AlertType.WARNING, "Attention, le système de lissage ne fonctionne qu'avec des repères contenant exclusivement des triangles !", null);
			}*/
			lissageButton.setStyle(null);
			lissageButton.getStyleClass().add(BUTTON_SELECTED);
		} else {
			lissageButton.getStyleClass().clear();
		}
		canvasUpdate();
	}

	/**
	 * 
	 */
	public void pointsClicked() {
		pointsVisibles = !pointsVisibles;
		if (pointsVisibles) {
			pointsButton.setStyle(null);
			pointsButton.getStyleClass().add(BUTTON_SELECTED);
		} else {
			pointsButton.getStyleClass().clear();
		}
		canvasUpdate();
	}
	
	protected AnimationTimer timer = new AnimationTimer() {

		@Override
		public void handle(long time) {
			if (repere != null) {
				repere.rotationY(-PAS_ROT);
			}

		}
	};


	/**
	 * Reset l'horloge
	 */
	@FXML
	public void horlogeClicked() {
		controleurHorloge = !controleurHorloge;
		
		if (controleurHorloge) {
			horlogeButton.setStyle(null);
			horlogeButton.getStyleClass().add(BUTTON_SELECTED);
			timer.handle(1000);
			timer.start();
		} else {
			horlogeButton.getStyleClass().clear();
			timer.stop();
		}
	}

	/**
	 * 
	 */
	public void aretesClicked() {
		aretesVisibles = !aretesVisibles;
		if (aretesVisibles) {
			aretesButton.setStyle(null);
			aretesButton.getStyleClass().add(BUTTON_SELECTED);
		} else {
			aretesButton.getStyleClass().clear();
		}
		canvasUpdate();
	}

	/**
	 * Ouvre la fenetre d'informations
	 * 
	 * @throws IOException
	 */
	public void openInfos() throws IOException {
		if (repere != null) {
			Stage newStage = new Stage();
			newStage.initOwner(stage);
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/details.fxml"));
			BorderPane root = loader.load();
				DetailsController controller = loader.getController();
				controller.setRepere(repere);
			Scene scene = new Scene(root);
			newStage.setScene(scene);
			newStage.setTitle("Détails du modèle");
			newStage.show();
			newStage.centerOnScreen();
		} else {
			GraphicUtils.showMessage(Alert.AlertType.WARNING, "Aucun modèle n'est chargé !", null);
		}
	}

	public void openSettings() throws IOException {
		Stage newStage = new Stage();
		newStage.initOwner(stage);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/settings.fxml"));
		BorderPane root = loader.load();
		Scene scene = new Scene(root);
		newStage.setScene(scene);
		newStage.setTitle("Paramètres");
		newStage.show();
		newStage.centerOnScreen();
	}

	/**
	 * Fonction appelée quand l'utilisateur fait un drag sur la grille pour
	 * translater ou tourner le modèle
	 *
	 * @param e (MouseEvent)
	 */
	public void gridDrag(MouseEvent e) {
		if (repere != null) {
			double x = e.getX();
			double y = e.getY();
			double transX = (x - lastX) / 10;
			double transY = -1 * ((y - lastY) / 10);
			if (lastX != -1212 && lastY != -1212) {
				if (Math.abs(transX) <= 5 && Math.abs(transY) <= 5) {
					if (translationMode) {
						double pasX = Math.signum(transX) * repere.width()/30/z;
						double pasY = Math.signum(transY) * repere.height()/30/z;
						double pasZ = Math.signum(transY) * repere.depth()/30/z;
						//System.out.println(pasX + " " + pasY + " " + pasZ);
						if (transAxis.equals("x")) {
							Vecteur vX = new Vecteur(pasX, 0, 0);
							repere.translate(vX);
						} else if (transAxis.equals("y")) {
							Vecteur vY = new Vecteur(0, pasY, 0);
							repere.translate(vY);
						} else if (transAxis.equals("z")) {
							Vecteur vZ = new Vecteur(0, 0, pasZ);
							repere.translate(vZ);
						} else {
							Vecteur vX = new Vecteur(pasX, 0, 0);
							repere.translate(vX);
							Vecteur vY = new Vecteur(0, pasY, 0);
							repere.translate(vY);
						}
					} else {
						if (transAxis.equals("x")) {
							repere.rotationX(transY * 10);
						} else if (transAxis.equals("y")) {
							repere.rotationY(transX * 10);
						} else if (transAxis.equals("z")) {
							repere.rotationZ(transX * -10);
						} else {
							repere.rotationX(transY*5);
							repere.rotationY(transX*-5);
						}
					}
				}
			}
			lastX = x;
			lastY = y;
		}
	}

	/**
	 * Fonction appelée quand l'utilisateur scroll avec la souris sur la grille pour
	 * zoomer
	 *
	 * @param event (ScrollEvent)
	 */
	public void gridScroll(ScrollEvent event) {
		if (repere != null) {
			if (event.getDeltaY() >= 0) {
				repere.homothetie(SettingsController.zoomPlus);
			}
			else {
				repere.homothetie(SettingsController.zoomMoins);
			}
		}
	}

	/**
	 * Tourne le modele sur X positivement
	 */
	@FXML
	void rotXplus() {
		if (repere != null) {
			repere.rotationX(PAS_ROT);
		} else
			GraphicUtils.showMessage(Alert.AlertType.WARNING, PAS_DE_REPERE, null);
	}

	/**
	 * Tourne le modele sur X negativement
	 */
	@FXML
	void rotXmoins() {
		if (repere != null) {
			repere.rotationX(-1 * PAS_ROT);
		} else
			GraphicUtils.showMessage(Alert.AlertType.WARNING, PAS_DE_REPERE, null);
	}

	/**
	 * Tourne le modele sur Y positivement
	 */

	@FXML
	void rotYplus() {
		if (repere != null) {
			repere.rotationY(PAS_ROT);
		} else
			GraphicUtils.showMessage(Alert.AlertType.WARNING ,PAS_DE_REPERE, null);
	}

	/**
	 * Tourne le modele sur Y negativement
	 */

	@FXML
	void rotYmoins() {
		if (repere != null) {
			repere.rotationY(-1 * PAS_ROT);
		} else
			GraphicUtils.showMessage(Alert.AlertType.WARNING, PAS_DE_REPERE, null);
	}

	/**
	 * Tourne le modele sur Z positivement
	 */
	@FXML
	void rotZplus() {
		if (repere != null) {
			repere.rotationZ(PAS_ROT);
		} else
			GraphicUtils.showMessage(Alert.AlertType.WARNING, PAS_DE_REPERE, null);
	}

	/**
	 * Tourne le modele sur Z negativement
	 */
	@FXML
	void rotZmoins() {
		if (repere != null) {
			repere.rotationZ(-1 * PAS_ROT);
		} else
			GraphicUtils.showMessage(Alert.AlertType.WARNING, PAS_DE_REPERE, null);
	}

	/**
	 * Translate le modele sur X positivement
	 */
	@FXML
	void transXplus() {
		if (repere != null) {
			double pas = repere.width() / 10 / z;
			Vecteur vecteur = new Vecteur(pas, 0, 0);
			repere.translate(vecteur);
		} else
			GraphicUtils.showMessage(Alert.AlertType.WARNING, PAS_DE_REPERE, null);
	}

	/**
	 * Translate le modele sur X negativement
	 */
	@FXML
	void transXmoins() {
		if (repere != null) {
			double pas = repere.width() / 10 / z;
			Vecteur vecteur = new Vecteur(-1 * pas, 0, 0);
			repere.translate(vecteur);
		} else
			GraphicUtils.showMessage(Alert.AlertType.WARNING, PAS_DE_REPERE, null);
	}

	/**
	 * Translate le modele sur Y positivement
	 */
	@FXML
	void transYplus() {
		if (repere != null) {
			double pas = repere.height() / 10 / z;
			Vecteur vecteur = new Vecteur(0, pas, 0);
			repere.translate(vecteur);
		} else
			GraphicUtils.showMessage(Alert.AlertType.WARNING, PAS_DE_REPERE, null);
	}

	/**
	 * Translate le modele sur Y negativement
	 */
	@FXML
	void transYmoins() {
		if (repere != null) {
			double pas = repere.height() / 10 / z;
			Vecteur vecteur = new Vecteur(0, -1 * pas, 0);
			repere.translate(vecteur);
		} else
			GraphicUtils.showMessage(Alert.AlertType.WARNING, PAS_DE_REPERE, null);
	}

	/**
	 * Translate le modele sur Z positivement
	 */
	@FXML
	void transZplus() {
		if (repere != null) {
			double pas = repere.depth() / 10 / z;
			Vecteur vecteur = new Vecteur(0, 0, pas);
			repere.translate(vecteur);
		} else
			GraphicUtils.showMessage(Alert.AlertType.WARNING, PAS_DE_REPERE, null);
	}

	/**
	 * Translate le modele sur Z negativement
	 */
	@FXML
	void transZmoins() {
		if (repere != null) {
			double pas = repere.depth() / 10 / z;
			Vecteur vecteur = new Vecteur(0, 0, -1 * pas);
			repere.translate(vecteur);
		} else
			GraphicUtils.showMessage(Alert.AlertType.WARNING, PAS_DE_REPERE, null);
	}

	/**
	 * Ouvre un explorateur de fichier pour .ply
	 */
	public void openExternal() {
		Stage newStage = new Stage();
		newStage.initOwner(stage);
		BorderPane root = new BorderPane();

		Text titre = new Text("Veuillez rentrer un lien internet finissant par .ply");
		TextField texte = new TextField();
			texte.setOnInputMethodTextChanged(event -> {
				String str = texte.getText().trim();
				if(str.endsWith(".ply")) {
					URL url;
					try {
						url = new URL(str);
					} catch (Exception e) {
						System.out.println("lien invalide !");
					}
				}
			});
		HBox validation = new HBox(15);
			validation.setAlignment(Pos.CENTER_LEFT);
			Label label = new Label("Enregistrer : ");
			RadioButton enregistrer = new RadioButton();
			Button importer = new Button("Importer");
				importer.setOnAction(event -> {
					String str = texte.getText().trim();
					if(str.endsWith(".ply")) {
						URL url = null;
						File file = null;
						try {
							url = new URL(str);
						} catch (Exception e) {
							System.out.println("lien invalide !");
						} finally {
							try {
								
								Proxy proxy = Proxy.NO_PROXY;
								
								if(System.getProperty("user.home").startsWith("/home/infoetu/")) {
									proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("cache.univ-lille1.fr", 3128));
								}
								
								InputStream is = url.openConnection(proxy).getInputStream();
								
								String[] elm = str.split("/");
								String fileName = elm[elm.length-1];
								file = new File(dir + "/" + fileName);
								file.createNewFile();
	//							System.out.println(file.getCanonicalPath());
	//							for(String line : PlyReader.getLines(file.getCanonicalPath())) System.out.println(line);
								Files.copy(is, Paths.get(file.getCanonicalPath()), StandardCopyOption.REPLACE_EXISTING);
	//							for(String line : PlyReader.getLines(file.getCanonicalPath())) System.out.println(line);
								loadFile(file.getCanonicalPath());
							} catch (NoAsciiFileException e) {
								GraphicUtils.showMessage(Alert.AlertType.ERROR, e.getMessage(), null);
							} catch (IOException e) {
								GraphicUtils.showMessage(Alert.AlertType.ERROR, "Fichier impossible à récupérer", null);
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								if(!enregistrer.isSelected()) {
									file.delete();
								} else {
									try {
										refreshGestionnaire();
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
								newStage.close();
							}
						}
					}
				});
		validation.getChildren().addAll(importer,label,enregistrer);

		root.setTop(titre);
		root.setCenter(texte);
		root.setBottom(validation);

		Scene scene = new Scene(root);
		newStage.setScene(scene);
		newStage.setOnCloseRequest(e -> {
			canvasUpdate();
		});
		newStage.setTitle("Importer un fichier en ligne");
		newStage.show();
		newStage.centerOnScreen();
	}
	/**
	 * Ouvre le moteur de recherche vers le gitlab du projet
	 * 
	 * @throws IOException
	 * @throws URISyntaxException
	 */

	/*public void openGitlab() throws IOException, URISyntaxException {
		if(Desktop.isDesktopSupported()) {
			Desktop.getDesktop()
			.browse(new URI("https://gitlab.univ-lille.fr/2021-projet-modelisation/projetmodej6"));
		} else {
			GraphicUtils.showMessage(Alert.AlertType.WARNING, "Ce système d'exploitation ne prend pas en charge cette fonctionalité, veuillez réessayer sur Windows par exemple", null);
		}
	}*/

	/**
	 * 
	 * @param event (DragEvent)
	 */
	public void dragOver(DragEvent event) {
		Dragboard dragborad = event.getDragboard();

		if (dragborad.hasFiles()) {
			List<File> files = dragborad.getFiles();
			files.removeIf(file -> !file.getName().endsWith(PLY_EXTENSION));
			if (files.size() == 1) {
				event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
			}
		}
		event.consume();
	}

	/**
	 * 
	 * @param event
	 */
	public void dragDrop(DragEvent event) {
		Dragboard dragBoard = event.getDragboard();
		if (dragBoard.hasFiles()) {
			List<File> files = dragBoard.getFiles();
			for (File file : files) {
				try {
					loadFile(file.getCanonicalPath());
				} catch (NoAsciiFileException excepetion1) {
					GraphicUtils.showMessage(Alert.AlertType.ERROR, excepetion1.getMessage(), null);
				} catch (IOException excepetion2) {
					GraphicUtils.showMessage(Alert.AlertType.ERROR, "Fichier impossible à récupérer", null);
				} catch (Exception excepetion3) {
					excepetion3.printStackTrace();
				}
			}
		}
	}

	/**
	 * Effectue une action sur le modele en fonction de touches entrées : UPPER
	 * 
	 * ARROW (Key UP) : Effectue une transformation du modele vers le haut DOWN
	 * ARROW (Key DOWN) : Effectue une transformation du modele vers le bas LEFT
	 * ARROW (Key LEFT) : Effectue une transformation du modele vers la gauche RIGHT
	 * ARROW (Key RIGHT) : Effectue une transformation du modele vers la droite
	 * 
	 * @param event (KeyEvent)
	 */
	public void keyTyped(KeyEvent event) {
		if (!fileText.isFocused() && repere != null) {
				KeyCode code = event.getCode();
				if (translationMode) {
					double repereWidth = repere.width();
					double repereHeight = repere.height();
					double pasWidth = repereWidth / 10;
					double pasHeight = repereHeight / 10;
					Vecteur vecteur = new Vecteur(0, 0, 0);
					switch (code) {
					case UP:
						vecteur.setyCoordonnee(pasHeight/z);
						break;
					case DOWN:
						vecteur.setyCoordonnee(-1 * pasHeight/z);
						break;
					case LEFT:
						vecteur.setxCoordonnee(-1 * pasWidth/z);
						break;
					case RIGHT:
						vecteur.setxCoordonnee(pasWidth/z);
						break;
					default:
						break;
					}
					repere.translate(vecteur);
				} else {
					switch (code) {
					case UP:
						repere.rotationX(-1 * PAS_ROT);
						break;
					case DOWN:
						repere.rotationX(PAS_ROT);
						break;
					case LEFT:
						repere.rotationY(-1 * PAS_ROT);
						break;
					case RIGHT:
						repere.rotationY(PAS_ROT);
						break;
					default:
						break;
					}
				}
			}
	}

	/**
	 * Rafraichissement de la liste des modèles
	 */
	public void refreshFiles() throws IOException {
		refreshGestionnaire();
	}

	/**
	 * Recherche de modèles dans la liste
	 */
	@FXML
	void fileTextChanged(KeyEvent event) throws IOException {
		KeyCode keycode = event.getCode();
		if (keycode.equals(KeyCode.ENTER))
			mainStage.requestFocus();
		else {
			String text = fileText.getText();
			refreshGestionnaireFromText(text);
		}
	}

	public void sortBoxClicked() throws IOException {
		String selectedItem = sortBox.getSelectionModel().getSelectedItem();
		switch(selectedItem) {
			case "Nom":
				sortComp = new ComparatorInfosElement<String>(InfosElement::getNom);
				sortPredicate = (elm, str) -> !elm.getNom().contains(str);
				break;
			case "Description":
				sortComp = new ComparatorInfosElement<String>(InfosElement::getDescription);
				sortPredicate = (elm, str) -> !elm.getDescription().contains(str) || elm.getDescription().isBlank();
				break;
			case "Auteur":
				sortComp = new ComparatorInfosElement<String>(InfosElement::getAuteur);
				sortPredicate = (elm, str) -> !elm.getAuteur().contains(str) || elm.getAuteur().isBlank();
				break;
			case "Date":
				sortComp = new ComparatorInfosElement<LocalDate>(InfosElement::getDate);
				sortPredicate = (elm, str) -> elm.getDate() == null || !elm.getDate().toString().contains(str);
				break;
			case "Nb faces":
				sortComp = new ComparatorInfosElement<Integer>(InfosElement::getNbFaces);
				sortPredicate = (elm, str) -> !String.valueOf(elm.getNbFaces()).contains(str);
				break;
			case "Nb points":
				sortComp = new ComparatorInfosElement<Integer>(InfosElement::getNbPoints);
				sortPredicate = (elm, str) -> !String.valueOf(elm.getNbPoints()).contains(str);
				break;
			/*default:
				sortComp = new ComparatorInfosElement<String>(InfosElement::getNom);
				sortPredicate = (elm, str) -> !elm.getNom().contains(str);
				break;*/
		}
		refreshGestionnaire();
	}

	/**
	 * Raffraichit les �l�ments de la liste et ajoute le parametre
	 * 
	 * @param text
	 */
	private void refreshGestionnaireFromText(String text) throws IOException {
		long debTime = System.nanoTime();

		List<InfosElement> infosElements = getInfosElementsFromDir();
		infosElements.removeIf((elm) -> sortPredicate.test(elm,text));
		Collections.sort(infosElements, sortAsc ? sortComp : sortComp.reversed());

		gestionnaire.getItems().clear();
		Preview prev;
		for(InfosElement elm : infosElements) {
			//System.out.println("name : " + elm.getNom() + " desc : '" + elm.getDescription() + "' date: " + (elm.getDate() == null ? "" : elm.getDate().toString()));
			ImageView imgView = new ImageView();
			try {
				imgView.setImage(
						new Image(new File(dir + "/" + elm.getNom().replace(PLY_EXTENSION, PNG_EXTENSION))
								.toURI().toString()));
			} catch (NullPointerException npe) {
				Image img = GraphicUtils.getResourceImage("vide.png");
				imgView.setImage(img);
				npe.printStackTrace();
			} finally {
				setupImageView(imgView);
			}

			Button plus = new Button("+");
				plus.setOnAction(e -> {
					Stage newStage = new Stage();
					newStage.initOwner(stage);
					VBox root = new VBox();

					Text nom = new Text("Nom : " + elm.getNom());
					Text auteur = new Text("Auteur : " + elm.getAuteur());
					Text description = new Text("Nom : " + elm.getDescription());
					Text date = new Text("Date : " + (elm.getDate() == null ? "" : elm.getDate().toString()));
					Text nbFaces = new Text("Nb faces : " + elm.getNbFaces());
					Text nbPoints = new Text("Nb points : " + elm.getNbPoints());

					root.getChildren().addAll(nom,auteur,description,date,nbFaces,nbPoints);

					Scene scene = new Scene(root,400,200);
					newStage.setScene(scene);
					newStage.setTitle("Details du modèle " + elm.getNom());
					newStage.show();
					newStage.centerOnScreen();
				});

			plus.setAlignment(Pos.CENTER_RIGHT);

			prev = new Preview(elm.getNom(), imgView, plus);
			gestionnaire.getItems().add(prev);
		}
		long aftTime = System.nanoTime();
		//System.out.println("element réalisé en : " + (aftTime-debTime)/1000000 + " ms");
	}

	private List<InfosElement> getInfosElementsFromDir() throws IOException {
		List<InfosElement> elements = new ArrayList<>();
		List<File> listFiles = List.of(dir.listFiles());
		InfosElement element;
		for (File file : listFiles) {
			if (file.isFile() && file.getName().endsWith(PLY_EXTENSION)) {
				try {
					long debTime = System.nanoTime();
					BufferedReader br = new BufferedReader(new FileReader(file));
					int cpt = 0;
					String line = null;
					int nbFaces = 0;
					int nbPoints = 0;
					String nom = file.getName();
					String description = "";
					String auteur = "";
					LocalDate date = null;
					do {
						line = br.readLine();
						if(line != null && !line.isBlank()) {
							String[] options = line.split(" ");
							if(options[0].equals("element")) {
								if(options[1].equals("face")) {
									nbFaces = Integer.parseInt(options[2]);
								} else if(options[1].equals("vertex")) {
									nbPoints = Integer.parseInt(options[2]);
								}
							} else if(options[0].equals("comment")) {
								switch(options[1]) {
									case "description:":
										options = Arrays.copyOfRange(options, 2, options.length);
										description = String.join(" ", options);
										break;
									case "auteur:":
										options = Arrays.copyOfRange(options, 2, options.length);
										auteur = String.join(" ", options);
										break;
									case "dateCreation:":
										date = LocalDate.parse(options[2]);
										break;
								}
							}
						}
						cpt++;
					} while(line != null && !line.contains("end_header"));
					br.close();
					long aftTime = System.nanoTime();
					//System.out.println("iter: " + cpt + "  getInfosElementsFromDir réalisé en : " + (aftTime-debTime)/1000000 + " ms");
					element = new InfosElement(nom,description,auteur,date,nbFaces,nbPoints);
					elements.add(element);
				} catch(Exception e) {
					System.out.println("fichier invalide : " + file.getName());
				}
			}
		}
		return elements;
	}

	/**
	 * La liste des fichiers est cliquée (sélection d'un modèle)
	 */
	public void gestionnaireClicked() {
		Preview fileName = gestionnaire.getSelectionModel().getSelectedItem();

		if (fileName == null)
			GraphicUtils.showMessage(Alert.AlertType.WARNING, "Aucun fichier choisi !", null);
		else {
			try {
				loadFile(dir.getCanonicalPath() + "/" + fileName.getFileName());
			} catch (NoAsciiFileException e) {
				GraphicUtils.showMessage(Alert.AlertType.ERROR, e.getMessage(), null);
			} catch (IOException e) {
				GraphicUtils.showMessage(Alert.AlertType.ERROR, e.getMessage(), null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	private void refreshGestionnaire() throws IOException {
		if (dir != null) {
			refreshGestionnaireFromText(fileText.getText());
		}

	}

	/**
	 * On initialise les images de la preview
	 *
	 * @param imgView
	 */
	private void setupImageView(ImageView imgView) {
		imgView.setFitWidth(WIDTH_PREVIEW);
		imgView.setFitHeight(HEIGHT_PREVIEW);
		imgView.setCache(true);
	}

	/**
	 * Charge un fichier
	 *
	 * @param  filename Nom du fichier
	 * @throws EndHeaderException 
	 * @throws NoFaceException 
	 * @throws NoVertexEception 
	 * @throws NoAsciiFileException 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void loadFile(String filename) throws Exception {
		LoadingWindow loadingWindow = new LoadingWindow(stage, "Récupération du fichier", "Récupération du fichier en cours...");
		if (repere != null) {
			for (Canvas c : canvasList) {
				grid.getChildren().remove(c);
				repere.detach((Observer<Repere>) c);
			}
			canvasList.clear();
			repere.resetRepere();
			cons.clear();
			z = 0;
		}
		try {
			repere = PlyReader.readFile(filename);
		} catch (IOException | NoAsciiFileException exception) {
			loadingWindow.close();
			throw exception;
		}

		// Nom du fichier actuellement affiché

		/*
		 * repereWidth = w; repereHeight = h; repereDepth = repere.depth();
		 */
		
		repere.center(repere.getPoints());
		autoEchelle();

		double width = grid.getWidth() / 2;
		double height = grid.getHeight() / 2;

		dessus = new ConnectedCanvas(width, height, repere, DrawMethods.DESSUS, aretesVisibles, pointsVisibles,
				colorMode, eclairageMode, lissage);
		droite = new ConnectedCanvas(width, height, repere, DrawMethods.DROITE, aretesVisibles, pointsVisibles,
				colorMode, eclairageMode, lissage);
		face = new ConnectedCanvas(width, height, repere, DrawMethods.FACE, aretesVisibles, pointsVisibles, colorMode, eclairageMode, lissage);
		canvasList.add(dessus);
		canvasList.add(droite);
		canvasList.add(face);

		grid.add(dessus, 0, 0);
		grid.add(droite, 1, 0);
		grid.add(face, 0, 1);

		loadingWindow.close();

		repere.initCanvas();

		
		String fileToPng = filename;
		String removeDotPly = "";
		if (fileToPng.contains(PLY_EXTENSION)) {
			removeDotPly = fileToPng.replace(PLY_EXTENSION, "");
		}

		fileToPng = removeDotPly;

		exportCanvasToPNG(face, dir + "/" + new File(fileToPng).getName() + PNG_EXTENSION);
	}

	/**
	 * Prend une capture du canvas et en ressort une image png
	 * 
	 * @param fileName
	 */
	public void exportCanvasToPNG(Canvas canvas, String fileName) {
		WritableImage img = canvas.snapshot(new SnapshotParameters(), null);
		try {
			File outputFile = new File(fileName);
			ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", outputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * R�adapte le canvas pour optimiser la visualisation
	 */
	private void autoEchelle() {
		double width = grid.getWidth() / 2;
		double height = grid.getHeight() / 2;
		double w = repere.width();
		double h = repere.height();

		double zoom = w >= h ? (width / w) * 0.75 : (height / h) * 0.75;

		/*System.out.println("grid width : " + width + " - grid height : " + height + " | repere width : " + w
				+ " - repere height : " + h + " | zoom : " + zoom);
*/
		z = zoom;
		repere.homothetie(zoom);
	}

	/**
	 * Initialise les boutons de l'interface
	 */
	private void initToggleButtons() {
		initToggleButton(colorsButton, colorMode);
		initToggleButton(eclairageButton, eclairageMode);
		initToggleButton(lissageButton, lissage);
		initToggleButton(pointsButton, pointsVisibles);
		initToggleButton(aretesButton, aretesVisibles);
		initToggleButton(horlogeButton, controleurHorloge);
	}

	/**
	 * Initialise un bouton de l'interface
	 */
	private void initToggleButton(Button button, boolean bool) {
		if (bool) {
			button.getStyleClass().add("buttonSelected");
		} else if (button != null) {
			button.setStyle("-fx-background-color: none;");
		}
	}

	/**
	 * Met a jour le canvas
	 */
	public void canvasUpdate() {
		ConnectedCanvas connectedCanvas;
		for (Canvas canvas : canvasList) {
			connectedCanvas = (ConnectedCanvas) canvas;
			connectedCanvas.setAretesVisibles(aretesVisibles);
			connectedCanvas.setPointsVisibles(pointsVisibles);
			connectedCanvas.setColorDisplay(colorMode);
			connectedCanvas.setEclairage(eclairageMode);
			connectedCanvas.setLissage(lissage);
			connectedCanvas.update(repere);
		}
	}

}