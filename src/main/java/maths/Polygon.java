package maths;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javafx.scene.paint.Color;

public class Polygon {

	/**
	 * On récupère notre repère
	 */
	private Repere originRepere;
	/**
	 * On a une liste de points
	 */
	private List<Integer> points;
	/**
	 * Cas ou il y a la couleur
	 */
	private Color color;

	private Color colorInitiale;

	private double cos;

	/**
	 * On fait un vecteur "normale"
	 */
	private Vecteur normale;

	/**
	 * On créer un polygon grâce à notre repère
	 * @param originRepere (Repere)
	 */
	public Polygon(Repere originRepere) {
		this(new ArrayList<Integer>(), originRepere);
	}

	/**
	 * On créer un Polygon grâce aux attributs de notre classe
	 * @param points (ist<Integer>)
	 * @param originRepere (Repere)
	 */
	public Polygon(List<Integer> points, Repere originRepere) {
		this.points = points;
		this.originRepere = originRepere;
	}

	/**
	 * On créer un polygon à partir d'un autre
	 * @param polygon (Polygon)
	 */
	public Polygon(Polygon polygon) {
		this(polygon.originRepere);
		for (Integer point : polygon.points) {
			points.add(point);
		}
		this.color = polygon.color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 *
	 * @return On recupère notre vecteur
	 */
	public Vecteur getNormale() {
		return normale;
	}

	/**
	 *
	 * @return On définit si un point est caché ou pas (si c'est le cas on retourne Vrai)
	 */
	public boolean isHidden() {
		boolean hidden = true;
		int cpt = 0;
		while (hidden && cpt < points.size()) {
			hidden = originRepere.getPoint(cpt).isHidden();
			cpt++;
		}
		return hidden;
	}
	
	/*On regarde si un Point est dans un Polygon*/
	
	/**
	 * On défine trois points colinéaire p, q et r et on check si le point q est situé sur le segment [pr]
	 * @param p (Point)
	 * @param q (Point)
	 * @param r (Point)
	 * @return vrai si q est sur le segment [pr]
	 */
	public boolean surSegement(Point p, Point q, Point r) {	
		return (q.getX() <= Math.max(p.getX(), r.getX()) &&
			    q.getX() >= Math.min(p.getX(), r.getX()) &&
			    q.getY() <= Math.max(p.getY(), r.getY()) &&
			    q.getY() >= Math.min(p.getY(), r.getY()) );
	}
	
	/**
	 * Pour trouver l'orientation du triplet
	 * (p, q, r)
	 * @param p (Point)
	 * @param q (Point)
	 * @param r (Point)
	 * @return 0 : p, q, r sont colinéaires; 1: Sens horraire; 2 : sens anti-horraire
	 */
	public int orientation(Point p, Point q, Point r) {
		int val = (int) ((q.getY() - p.getY()) * (r.getX() - q.getX()) - ((q.getX() - p.getX()) * (r.getY() - q.getY())));
		
		if(val == 0) 
			return 0;
		return (val > 0) ? 1 : 2;
	}
	

	/**
	 * On regarde si 2 segments se croisent
	 * @param p1
	 * @param q1
	 * @param p2
	 * @param q2
	 * @return vrai si le segement [p1, q1] 
	 * et le segmeent [p2, q2] se croisent
	 */
	public boolean seCroise(Point p1, Point q1, Point p2, Point q2) {
		int orientation1 = orientation(p1, q1, p2);
		int orientation2 = orientation(p1, q1, q2);
		int orientation3 = orientation(p2, q2, p1);
		int orientation4 = orientation(p2, q2, q1);

		return (orientation1 != orientation2 && orientation3 != orientation4) || (orientation1 == 0 && surSegement(p1, p2, q1)) ||  (orientation2 == 0 && surSegement(p1, p2, q1)) ||  (orientation3 == 0 && surSegement(p2, p1, q2)) || (orientation4 == 0 && surSegement(p2, q1, q2));
	}

	/**
	 *
	 * @param polygon
	 * @param n
	 * @param p
	 * @return vrai si le point p est dans le tableau polygon avec n sommets
	 */
	public boolean estInterieur(Point polygon[], int n, Point p) {
		if(n < 3)
			return false;

		// On a créé un point allant de p à l'infinie
		Point extreme = new Point(10000, p.getY(), 0);
		int cptLoop = 0;
		int cpt = 0;

		do {
			int next = (cpt + 1) % n;

			/**
			 * On vérifie si le segment [p, extreme] intercept la ligne
			 * du segment [polygon[cpt], polygon[next]]
			 */
			if(seCroise(polygon[cpt], polygon[next], p, extreme)) {
				/**
				 * Si le point p est colinéaire à la ligne [cpt, next]
				 * alors on vérifie s'il se trouve sur le segment (retourne vrai
				 * si c'est le cas)
				 */
				if(orientation(polygon[cpt], p, polygon[next]) == 0) {
					return surSegement(polygon[cpt], p, polygon[next]);
				}
				cptLoop++;
			}
			cpt = next;
		} while(cpt != 0);
		return (cptLoop % 2 == 1);
	}

	/**
	 * On initialise la couleur de chaque polygon
	 */
	public void initializeColor() {
		double totalR = 0;
		double totalG = 0;
		double totalB = 0;
		int size = points.size();
		for (Integer p : points) {
			Point point = originRepere.getPoint(p);
			Color pointColor = point.getColor();
			totalR +=  (pointColor.getRed());
			totalG +=  (pointColor.getGreen());
			totalB +=  (pointColor.getBlue());
		}
		color = Color.rgb((int)((totalR / size)*255), (int)((totalG / size)*255), (int)((totalB / size)*255));
		colorInitiale = color;
	}

	public Color getColorInitiale() {
		return colorInitiale;
	}

	/**
	 * On r�cup�re la couleur d'un polygon
	 * 
	 * @return Le rgb d'un polygon
	 */
	public Color getColor() {
		return color;
	}

	public Color getColorWithCos(Color couleurFournie) {
		if (cos<0.05){
			return new Color(0.1,0.1,0.1,1);
		}
		return new Color(couleurFournie.getRed() * cos, couleurFournie.getGreen() * cos, couleurFournie.getBlue() * cos,1);
	}

	/**
	 * Affiche un polygon correctement
	 * 
	 * @return L'affichage d'un polygon
	 */
	public String toString() {
		return points.toString();
	}

	/**
	 * On r�cup�re les points
	 * 
	 * @return Les points
	 */
	public List<Integer> getPoints() {
		return points;
	}

	/**
	 * On ajoute un point � notre liste
	 * 
	 * @param point
	 */
	public void addPoint(Integer point) {
		points.add(point);
	}

	/**
	 * On retire un point p de notre liste
	 * 
	 * @param point
	 */
	public void removePoint(Integer point) {
		points.remove(point);
	}

	/**
	 * On r�cup�re tous les points sur l'axe x
	 * 
	 * @return Un tableau de double qui r�cup�re tous les points sur l'axe x
	 */
	public double[] getPointsX() {
		return points.stream().mapToDouble((p) -> originRepere.getPoint(p).getX()).toArray();
	}

	/**
	 * On r�cup�re tout les points sur l'axe y
	 * 
	 * @return Un tableau de double qui r�cup�re tout les points sur l'axe y
	 */
	public double[] getPointsY() {
		return points.stream().mapToDouble((p) -> originRepere.getPoint(p).getY()).toArray();
	}

	/**
	 * On r�cup�re tout les points sur l'axe z
	 * 
	 * @return Un tableau de double qui r�cup�re tout les points sur l'axe z
	 */
	public double[] getPointsZ() {
		return points.stream().mapToDouble((p) -> originRepere.getPoint(p).getZ()).toArray();
	}

	/**
	 * On r�cup�re tout les points sur l'axe x pour pouvoir les afficher
	 * 
	 * @return Un tableau de double qui r�cup�re tout les points sur l'axe x pour
	 *         pouvoir les afficher
	 */
	public double[] getPointsDisplayX() {
		return points.stream().mapToDouble((p) -> originRepere.getPoint(p).getDisplayX()).toArray();
	}

	/**
	 * On r�cup�re tout les points sur l'axe y pour pouvoir les afficher
	 * 
	 * @return Un tableau de double qui r�cup�re tout les points sur l'axe y pour
	 *         pouvoir les afficher
	 */
	public double[] getPointsDisplayY() {
		return points.stream().mapToDouble((p) -> originRepere.getPoint(p).getDisplayY()).toArray();
	}

	/**
	 * On calcul le nombre de point pr�sent sur l'axe x
	 * 
	 * @return la somme du nombre de point pr�sent sur l'axe x
	 */
	public double getTotalX() {
		double total = 0;
		for (Integer p : points)
			total += originRepere.getPoint(p).getX();
		return total;
	}

	/**
	 * On calcul le nombre de point pr�sent sur l'axe y
	 * 
	 * @return la somme du nombre de point pr�sent sur l'axe y
	 */
	public double getTotalY() {
		double total = 0;
		for (Integer p : points)
			total += originRepere.getPoint(p).getY();
		return total;
	}

	/**
	 * On calcul le nombre de point pr�sent sur l'axe z
	 * 
	 * @return la somme du nombre de point pr�sent sur l'axe z
	 */
	public double getTotalZ() {
		double total = 0;
		for (Integer p : points)
			total += originRepere.getPoint(p).getZ();
		return total;
	}

	/**
	 * On calcul le nombre de point total
	 * 
	 * @return le nombre de point total
	 */
	public int size() {
		return points.size();
	}

	public void refreshNormale() {
		Vecteur vecteur1 = new Vecteur(originRepere.getPoint(points.get(0)), originRepere.getPoint(points.get(1)));
		Vecteur vecteur2 = new Vecteur(originRepere.getPoint(points.get(0)), originRepere.getPoint(points.get(2)));
		Vecteur produitVectoriel = Vecteur.produitVectoriel(vecteur1,vecteur2);
		normale = produitVectoriel.divide(produitVectoriel.getNorme());
	}

	/**
	 * On initialise notre lumière
	 * @param luminosity (Vecteur)
	 */
	public void setLuminosity(Vecteur luminosity){
		cos = Vecteur.getCosinusBetweenTwoVector(normale,luminosity);
	}

	/**
	 * Compare les coordonnées X de 2 Polygones
	 * 
	 */
	public static class ComparatorPolygonX implements Comparator<Polygon> {

		@Override
		public int compare(Polygon polygon1, Polygon polygon2) {
			return (int) polygon1.getTotalX() - (int) polygon2.getTotalX();
		}

	}
	
	/**
	 * Compare les coordonnées Y de 2 Polygones
	 * 
	 */

	public static class ComparatorPolygonY implements Comparator<Polygon> {

		@Override
		public int compare(Polygon polygon1, Polygon polygon2) {
			return (int) polygon1.getTotalY() - (int) polygon2.getTotalY();
		}

	}

	/**
	 * Compare les coordonnées Z de 2 Polygones
	 * 
	 */

	public static class ComparatorPolygonZ implements Comparator<Polygon> {

		@Override
		public int compare(Polygon polygon1, Polygon polygon2) {
			return (int) polygon1.getTotalZ() - (int) polygon2.getTotalZ();
		}

	}

}
