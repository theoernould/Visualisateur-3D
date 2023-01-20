package maths;


import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de test de la classe Point
 */
public class TestPoint {

	/**
	 * Liste de point
	 */
	ArrayPoint arrayPoint = new ArrayPoint();
	/**
	 * Point initiale
	 */
	Point point;

	/**
	 * Point supérieur
	 */
	Point otherAbove = new Point(2.0, 2.0, 2.0);
	/**
	 * Point de base
	 */
	Point otherEquals = new Point(1.0, 1.0, 1.0);
	/**
	 * Point inférieur
	 */
	Point otherLower = new Point(0.0, 0.0, 0.0);

	@BeforeEach
	void creerPoint() {
		arrayPoint.clear();
		point = new Point(1, 1, 1);
		Point point1 = new Point(point);
		arrayPoint.add(0, point);
		arrayPoint.add(1, point1);
	}

	@Test
	void testHomothetie() {
		arrayPoint.homothetie(5);
		assertEquals(arrayPoint.get(0), new Point(5.0, 5.0, 5.0));
	}

	@Test
	void testTranslation() {
		arrayPoint.translation(new Vecteur(1, 2, 3));
		assertEquals(point, new Point(2.0, 3.0, 4.0));
	}

	@Test
	void testSymetrieX() {
		arrayPoint.symetrieSurX();
		assertEquals(new Point(-1.0, 1.0, 1.0), point);
	}

	@Test
	void testSymetrieY() {
		arrayPoint.symetrieSurY();
		assertEquals(point, new Point(1.0, -1.0, 1.0));
	}

	@Test
	void testSymetrieZ() {
		arrayPoint.symetrieSurZ();
		assertEquals(point, new Point(1.0, 1.0, -1.0));
	}

	@Test
	void testRotationX() {
		arrayPoint.rotationXDegree(90);
		assertEquals(new Point(1.0, -1.0, 1.0), point);
	}

	@Test
	void testRotationY() {
		arrayPoint.rotationYDegree(90);
		assertEquals(new Point(-1.0, 1.0, 1.0), point);
	}

	@Test
	void testRotationZ() {
		arrayPoint.rotationZDegree(90);
		assertEquals(new Point(-1.0, 1.0, 1.0), point);
	}

	@Test
	void testToString() {
		assertEquals("(x :1.0, y :1.0, z :1.0)\n", point.toString());
	}

	@Test
	void testSetter() {
		point.setX(5.0);
		point.setY(10.0);
		point.setZ(15.0);
		assertEquals(new Point(5.0, 10.0, 15.0), point);
	}

	@Test
	void testGetDisplayX() {
		assertEquals(point.getDisplayX(), 1.0);
	}

	@Test
	void testGetDisplayY() {
		assertEquals(point.getDisplayY(), -1.0);
	}

	@Test
	void testIsHidden() {
		assertFalse(point.isHidden());
	}

	@Test
	void testFetHidden() {
		point.setHidden(true);
		assertTrue(point.isHidden());
	}

	@Test
	void testMultiplacteWithDouble() {
		point.mutiplicate(5);
		assertEquals(new Point(5.0, 5.0, 5.0), point);
	}

	@Test
	void testComparatorX() {
		Point.ComparatorPointX comparatorPointX = new Point.ComparatorPointX();
		assertEquals(-1, comparatorPointX.compare(point, otherAbove));
		assertEquals(0, comparatorPointX.compare(point, otherEquals));
		assertEquals(1, comparatorPointX.compare(point, otherLower));
	}

	@Test
	void testComparatorY() {
		Point.ComparatorPointY comparatorPointY = new Point.ComparatorPointY();
		assertEquals(-1, comparatorPointY.compare(point, otherAbove));
		assertEquals(0, comparatorPointY.compare(point, otherEquals));
		assertEquals(1, comparatorPointY.compare(point, otherLower));
	}

	@Test
	void testComparatorZ() {
		Point.ComparatorPointZ comparatorPointZ = new Point.ComparatorPointZ();
		assertEquals(-1, comparatorPointZ.compare(point, otherAbove));
		assertEquals(0, comparatorPointZ.compare(point, otherEquals));
		assertEquals(1, comparatorPointZ.compare(point, otherLower));
	}

	@Test
	void TestConstructeur(){
		Polygon p = null;
		Point point = new Point(1,1,1,255,255,255,p);
		Point point1 = new Point(1,1,1,p);
		assertEquals(Color.WHITE,point.getColor());
	}

	@Test
	void TestAddPolygon(){
		Point p = new Point(1,1,1);
		p.addPolygon(null);
		assertNull(p.getPolygonList().get(0));
	}

	@Test
	void TestGetColorWithPolygon(){
		Repere repere = Repere.getInstance(0,0,0);
		Polygon p = new Polygon(repere);
		p.setColor(new Color(0.1,0.1,0.1,1));
		Polygon p1 = new Polygon(repere);
		p1.setColor(new Color(0.2,0.2,0.2,1));
		Point point = new Point(0,0,0);
		point.addPolygon(p);point.addPolygon(p1);
		assertEquals(new Color(0.15,0.15,0.15,1),point.getColorWithPolygon());
	}

	@Test
	void TestMultiplicateWithMatrice(){
		Point point2 = new Point(1,1,1);
		double[][] tab = new double[][]{{2,2,2,2},{2,2,2,2},{2,2,2,2},{2,2,2,2}};
		Matrice m = new Matrice(tab);
		point2.mutiplicate(m);
		assertEquals(new Point(8,8,8),point2);

	}
}