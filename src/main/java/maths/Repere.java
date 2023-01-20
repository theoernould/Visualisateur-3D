package maths;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import controllers.MainController;
import observer.Observable;
import views.Main;

/**
 * Notre classe Repere qui hérite de la classe Observable  (c'est notre singleton)
 */
@SuppressWarnings("rawtypes")
public class Repere extends Observable {

	/**
	 * Point X
	 */
	protected double pointX;
	/**
	 * Point Y
	 */
	protected double pointY;
	/**
	 * Point Z
	 */
	protected double pointZ;

	/**
	 * Nom du fichier
	 */
	protected String name;
	/**
	 * Description du fichier
	 */
	protected String description;
	/**
	 * Date de création du fichier
	 */
	protected LocalDate date;
	protected int faceNbPoints = 0;

	/**
	 * Auteur du fichier
	 */
	protected String auteur;
	protected boolean colored;

	/**
	 * Notre liste de polygon
	 */
	protected List<Polygon> polygones;
	/**
	 * Liste de point
	 */
	protected ArrayPoint points;
	/**
	 * Liste de point transformé
	 */
	protected ArrayPoint pointsTranformes;

	/**
	 * Notre source de lumière
	 */
	protected final Vecteur lightSource = new Vecteur(0, 0, 1);

	/**
	 * Notre singleton
	 */
	protected static Repere singleton = null;

	/**
	 * Singleton de la classe Repere
	 *
	 * @param pointX
	 * @param pointY
	 * @param pointZ
	 * @return une instance de la classe
	 */
	public static Repere getInstance(double pointX, double pointY, double pointZ) {
		if (singleton == null)
			singleton = new Repere(pointX, pointY, pointZ);
		return singleton;
	}

	/**
	 * Getter du singleton
	 * 
	 * @return le singleton en Repere
	 */

	public static Repere getSingleton() {
		return singleton;
	}

	/**
	 * Getter de coord X
	 * 
	 * @return un double de X
	 */

	public double getPointX() {
		return pointX;
	}

	/**
	 * Getter de coord Y
	 * 
	 * @return un double de Y
	 */

	public double getPointY() {
		return pointY;
	}

	/**
	 * Getter de coord Z
	 * 
	 * @return un double de Z
	 */

	public double getPointZ() {
		return pointZ;
	}


	public boolean isColored() {
		return colored;
	}

	public void setColored(boolean colored) {
		this.colored = colored;
	}

	public int getFaceNbPoints() {
		return faceNbPoints;
	}

	public void setFaceNbPoints(int faceNbPoints) {
		this.faceNbPoints = faceNbPoints;
	}

	/**
	 * Setter du nom
	 * 
	 * @param name un nom en String
	 */

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Setter de la description
	 * 
	 * @param description une description en String
	 */

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Setter de la date
	 * 
	 * @param date une date en LocalDate
	 */

	public void setDate(LocalDate date) {
		this.date = date;
	}

	/**
	 * Setter de l'auteur
	 * 
	 * @param auteur un auteur en String
	 */

	public void setAuteur(String auteur) {
		this.auteur = auteur;
	}

	/**
	 * Getter du nom
	 * 
	 * @return un String du nom
	 */

	public String getName() {
		return name;
	}

	/**
	 * Getter de la description
	 * 
	 * @return un String de la description
	 */

	public String getDescription() {
		return description;
	}

	/**
	 * Getter de la date
	 * 
	 * @return une LocalDate de la date
	 */

	public LocalDate getDate() {
		return date;
	}

	/**
	 * Getter de l'auteur
	 * 
	 * @return un String de l'auteur
	 */

	public String getAuteur() {
		return auteur;
	}

	public List<Point> getPointsTransformes() {
		List<Point> copyMapPoints = new ArrayPoint();
		for (Point p : pointsTranformes)
			copyMapPoints.add(new Point(p));
		return copyMapPoints;
	}

	/**
	 * On initialise notre rep�re
	 * 
	 * @param pointX
	 * @param pointY
	 * @param pointZ
	 */
	private Repere(double pointX, double pointY, double pointZ) {
		this.pointX = pointX;
		this.pointY = pointY;
		this.pointZ = pointZ;
		polygones = new ArrayList<>();
		points = new ArrayPoint();
	}

	/**
	 * On refait notre rep�re � neuf
	 */
	public void resetRepere() {
		polygones.clear();
		points.clear();
		colored = false;
		faceNbPoints = 0;
	}

	public void clearList() {
		polygones = new ArrayList<>();
		points = new ArrayPoint();
	}

	/**
	 * On affiche notre rep�re
	 */
	public String toString() {
		return polygones.toString();
	}

	/**
	 * On r�cup�re les polygons de notre rep�re
	 *
	 * @return Les polygons de notre rep�re
	 */
	public List<Polygon> getPolygones() {
		return polygones;
	}

	/**
	 * On calcul la taille de notre liste de polygon (Pour régler les problèmes de
	 * loi de déméter dans la classe DetailsController.java)
	 * 
	 * @return la taille de la liste de polygons
	 */
	public int getPolygonesSize() {
		return getPolygones().size();
	}

	/**
	 * On centre notre rep�re
	 */
	public void center(List<Point> liste) {
		// long befTime = System.nanoTime();

		Vecteur vecteur = getVector(liste);

		//vecteur.multiplicate(-1);

//		Vecteur vecteur = new Vecteur(-1,-1,-1);
		if(MainController.z!=0) {
			translate(vecteur.divide(MainController.z));
		}else {
			translate(vecteur);
		}
	}

	public Vecteur getVector(List<Point> liste) {
		liste.sort(new Point.ComparatorPointX());
		Point middlePointX = liste.get(liste.size() / 2);

		liste.sort(new Point.ComparatorPointY());
		Point middlePointY = liste.get(liste.size() / 2);

		liste.sort(new Point.ComparatorPointZ());
		Point middlePointZ = liste.get(liste.size() / 2);

		return new Vecteur(middlePointX.getX() * -1, middlePointY.getY() * -1, middlePointZ.getZ() * -1);
	}

	/**
	 * Permet de faire une translation sur notre rep�re gr�ce � un vecteur
	 *
	 * @param vecteur (Vecteur)
	 */
	@SuppressWarnings("unchecked")
	public void translate(Vecteur vecteur) {
		points.translation(vecteur);
		notifyObservers(this);
	}

	@SuppressWarnings("unchecked")
	public void homothetie(double value) {
		points.homothetie(value);
		notifyObservers(this);
	}

	/**
	 * Permet de faire une rotation sur l'axe x gr�ce � un angle angle
	 *
	 * @param angle (double)
	 */
	@SuppressWarnings("unchecked")
	public void rotationX(double angle) {
		points.rotationXDegree(angle);
		notifyObservers(this);
	}

	/**
	 * Permet de faire une rotation sur l'axe y gr�ce � un angle angle
	 *
	 * @param angle (double)
	 */
	@SuppressWarnings("unchecked")
	public void rotationY(double angle) {
		points.rotationYDegree(angle);
		notifyObservers(this);
	}

	/**
	 * Permet de faire une rotation sur l'axe z gr�ce � un angle angle
	 *
	 * @param angle (double)
	 */
	@SuppressWarnings("unchecked")
	public void rotationZ(double angle) {
		points.rotationZDegree(angle);
		notifyObservers(this);
	}

	@SuppressWarnings("unchecked")
	public void initCanvas() {
		notifyObservers(this);
	}

	/**
	 * On r�up�re un point gr�ce � un indice (idx)
	 *
	 * @param idx (int)
	 * @return Un point
	 */
	public Point getPoint(int idx) {
		return points.get(idx);
	}

	/**
	 * On r�cup�re le point transform� gr�ce � un indice (idx)
	 *
	 * @param idx (int)
	 * @return le point transform�
	 */
	public Point getPointTransforme(int idx) {
		return pointsTranformes.get(idx);
	}

	/**
	 * On r�cup�re une copie des points
	 *
	 * @return Une copie des points
	 */
	public ArrayPoint getPoints() {
		ArrayPoint copyMapPoints = new ArrayPoint();
		for (Point p : points)
			copyMapPoints.add(new Point(p));
		// System.out.println("copy :"+copyMapPoints.size());
		return copyMapPoints;
	}

	/**
	 * On r�cup�re la largeur entre le point le plus � gauche et celui le plus �
	 * droite
	 *
	 * @return La largeur
	 */
	public double width() {
		Point min = Collections.min(points, new Point.ComparatorPointX());
		Point max = Collections.max(points, new Point.ComparatorPointX());
		return max.getX() - min.getX();
	}

	/**
	 * On r�cup�re la hauteur entre le point le plus � gauche et celui le plus �
	 * droite
	 *
	 * @return La hauteur
	 */
	public double height() {
		Point min = Collections.min(points, new Point.ComparatorPointY());
		Point max = Collections.max(points, new Point.ComparatorPointY());
		return max.getY() - min.getY();
	}

	public double depth() {
		Point min = Collections.min(points, new Point.ComparatorPointZ());
		Point max = Collections.max(points, new Point.ComparatorPointZ());
		return max.getZ() - min.getZ();
	}
	
	public double depth(double depth) {
		return depth;
	}

	/**
	 * Permet de d�zoom
	 */
	public void dezoom() {
		pointX += 0.25;
		pointY += 0.25;
		pointZ += 0.25;
	}

	/**
	 * Permet de zoom
	 */
	public void zoom() {
		pointX -= 0.25;
		pointY -= 0.25;
		pointZ -= 0.25;
	}

	/**
	 * On ajoute un point � notre liste de polygon
	 *
	 * @param point
	 */
	public void addPoint(Point point) {
		points.add(point);
	}

	/**
	 * Permet d'ajouter plusieur points
	 *
	 * @param points (List<Point>)
	 */
	public void addPoints(List<Point> points) {
		this.points.addAll(points);
	}

	/**
	 * Ajout d'un polygon � la liste
	 *
	 * @param polygon (Polygon)
	 */
	public void addPolygon(Polygon polygon) {
		polygones.add(polygon);
	}

	/*
	 * public void addPoint(double x, double y, double z) { points.add(new Point(x,
	 * y, z)); }
	 */

	/**
	 * On retire un polygon de la liste gr�ce � sa position (index)
	 *
	 * @param index (int)
	 */
	public void removePolygon(int index) {
		polygones.remove(index);
	}

	/**
	 * On retire un polygon de la liste
	 *
	 * @param polygon (Polygon)
	 */
	public void removePolygon(Polygon polygon) {
		polygones.remove(polygon);
	}

	/**
	 * Retourne la taille de notre liste de points (pour régler la loi de déméter de
	 * la classe DetailsController.java)
	 * 
	 * @return la taille de la liste de points
	 */
	public int getPointsSize() {
		return getPoints().size();
	}

}