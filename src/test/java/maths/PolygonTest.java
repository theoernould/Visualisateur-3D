package maths;

import javafx.scene.paint.Color;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Classe de test pour la classe Polygon
 */
class PolygonTest {

	/**
	 * Notre repere
	 */
	Repere repere = Repere.getInstance(0, 0, 0);
	/**
	 * Polygon de test
	 */
	Polygon polygon = null;

	@BeforeEach
	void inti() {
		repere.clearList();
		polygon = new Polygon(repere);
	}

	@Test
	void testSurSegment() {
		Point pointP = new Point(0, 0, 0);
		Point pointQ = new Point(1, 1, 1);
		Point pointR = new Point(2, 2, 2);

		assertTrue(polygon.surSegement(pointP, pointQ, pointR));
		assertFalse(polygon.surSegement(pointP, pointR, pointQ));
	}

	@Test
	void testOrientationTripletCasZero() {
		Point pointP = new Point(0, 0, 0);
		Point pointQ = new Point(0, 0, 0);
		Point pointR = new Point(0, 0, 0);
		/**
		 * Dans orientation val est égale à 0
		 */
		assertEquals(0, polygon.orientation(pointP, pointQ, pointR));
	}

	@Test
	void testOrientationTripletCasUn() {
		Point pointP = new Point(1, 2, 0);
		Point pointQ = new Point(2, 3, 0);
		Point pointR = new Point(6, 5, 0);
		/**
		 * Dans orientation val est positif
		 */
		assertEquals(1, polygon.orientation(pointP, pointQ, pointR));
	}

	@Test
	void testOrientationTripletCasDeux() {
		Point pointP = new Point(1, 1, 0);
		Point pointQ = new Point(1, 2, 0);
		Point pointR = new Point(-2, 1, 0);
		/**
		 * Dans orientation val est négatif
		 */
		assertEquals(2, polygon.orientation(pointP, pointQ, pointR));
	}

	@Test
	void testSeCroise() {
		Point point1 = new Point(3, 4, 0);
		Point pointQ1 = new Point(7, 4, 0);
		Point point2 = new Point(5, 6, 0);
		Point pointQ2 = new Point(5, 2, 0);

		assertTrue(polygon.seCroise(point1, pointQ1, point2, pointQ2));
		assertFalse(polygon.seCroise(point1, pointQ2, point2, pointQ1));
	}

	@Test
	void testEstInterieur() {
		Point[] points = { new Point(1, 1, 0), new Point(1, 2, 0), new Point(-2, 1, 0), new Point(-2, 1, 1) };

		Point point = new Point(1, 1, 0);
		Point point1 = new Point(1, 1, 1);

		assertFalse(polygon.estInterieur(points, 2, point));
		assertTrue(polygon.estInterieur(points, 42, point));
		assertTrue(polygon.estInterieur(points, 42, point1));
	}

	@Test
	void testGetColor() {
		Repere repere2 = Repere.getInstance(5, 5, 5);
		Point point = new Point(4, 4, 4);
		@SuppressWarnings("unused")
		Polygon polygon2 = new Polygon(repere2);
		Color color = point.getColor();
		assertNotNull(color);
	}

	@Test
	void testAddPoint() {
		polygon.addPoint(1);
		assertEquals(1, polygon.getPoints().size());
	}

	@Test
	void testRemovePoint() {
		Integer point = 1;
		polygon.addPoint(point);
		assertEquals(1, polygon.getPoints().size());
		polygon.removePoint(point);
		assertEquals(0, polygon.getPoints().size());
	}

	@Test
	void testGetPointX() {
		Polygon polygon = new Polygon(repere);
		assertNotNull(polygon.getPointsX());
	}

	@Test
	void testGetPointY() {
		Polygon polygon = new Polygon(repere);
		assertNotNull(polygon.getPointsY());
	}

	@Test
	void testGetPointZ() {
		Polygon polygon = new Polygon(repere);
		assertNotNull(polygon.getPointsZ());
	}

	@Test
	void testGetPointDisplayX() {
		Polygon polygon = new Polygon(repere);
		assertNotNull(polygon.getPointsDisplayX());
	}

	@Test
	void testGetPointDisplayY() {
		Polygon polygon = new Polygon(repere);
		assertNotNull(polygon.getPointsDisplayY());
	}

	@Test
	void testGetTotalX() {
		Polygon polygon = new Polygon(repere);
		assertEquals(0, polygon.getTotalX());
	}

	@Test
	void testGetTotalY() {
		Polygon polygon = new Polygon(repere);
		assertEquals(0, polygon.getTotalY());
	}

	@Test
	void testGetTotalZ() {
		Polygon polygon = new Polygon(repere);
		assertEquals(0, polygon.getTotalZ());
	}

	@Test
	void testSize() {
		assertEquals(0, polygon.size());
	}

	private Polygon createPolygonWithPoint(Repere repere,int i) {
		Polygon polygon = new Polygon(repere);
		Point point = new Point(i, i, i, 100, 100, 100);
		Point point1 = new Point(i, i, i, 50, 50, 50);
		Point point2 = new Point(i, i, i, 0, 0, 0);
		repere.addPoint(point);
		repere.addPoint(point1);
		repere.addPoint(point2);
		polygon.addPoint(0);
		polygon.addPoint(1);
		polygon.addPoint(2);
		return polygon;
	}
	private Polygon createPolygonWithPoint(Repere repere) {
		return createPolygonWithPoint(repere,1);
	}

	@Test
	void testInitializeColorCreate() {
		Polygon polygon = createPolygonWithPoint(repere);
		polygon.initializeColor();
		assertEquals(Color.rgb(50,50,50), polygon.getColor());
	}

	@Test
	void testToString() {
		Polygon polygon = createPolygonWithPoint(repere);
		assertEquals("[0, 1, 2]", polygon.toString());
	}

	@Test
	void testGetTotalXCreate() {
		Polygon polygon = createPolygonWithPoint(repere);
		assertEquals(3, polygon.getTotalX());
	}

	@Test
	void testGetTotalYCreate() {
		Polygon polygon = createPolygonWithPoint(repere);
		assertEquals(3, polygon.getTotalY());
	}

	@Test
	void testGetTotalZCreate() {
		Polygon polygon = createPolygonWithPoint(repere);
		assertEquals(3, polygon.getTotalZ());
	}

	@Test
	void testConstructorWithPolygon() {
		Polygon polygon = createPolygonWithPoint(repere);
		Polygon polygon1 = new Polygon(polygon);
		assertEquals(polygon.getPoints(), polygon1.getPoints());
		assertEquals(polygon.getColor(), polygon1.getColor());
	}

	@Test
	void testNormale() {
		Polygon polygon = new Polygon(repere);
		Point point1 = new Point(1.0, 1.0, 1.0, 0, 0, 0);
		Point point2 = new Point(2.0, 2.0, 2.0, 0, 0, 0);
		Point point3 = new Point(2.0, 3.0, 4.0, 0, 0, 0);
		repere.addPoint(point1);
		repere.addPoint(point2);
		repere.addPoint(point3);
		polygon.addPoint(0);
		polygon.addPoint(1);
		polygon.addPoint(2);
		polygon.refreshNormale();

		assertEquals(polygon.getNormale(), new Vecteur(1 / Math.sqrt(6), -2 / Math.sqrt(6), 1 / Math.sqrt(6)));
	}

	@Test
	void testGetColorInitiale(){
		Polygon polygon = createPolygonWithPoint(repere);
		polygon.initializeColor();
		assertEquals(Color.rgb(50,50,50),polygon.getColorInitiale());
	}
	@Test
	void testSetColor(){
		Polygon polygon = new Polygon(repere);
		polygon.setColor(Color.RED);
		assertEquals(Color.RED,polygon.getColor());
	}

}