package maths;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Classe de test de la classe Repere
 */
public class TestRepere {
	/**
	 * Notre repere
	 */
	static Repere repere;

	@BeforeAll
	static void creationRepere() {
		repere = Repere.getInstance(0.0, 0.0, 0.0);
	}

	@Test
	void testZoom() {
		if (repere != null) {
			repere.zoom();
			assertEquals(-0.25, repere.getPointX());
			assertEquals(-0.25, repere.getPointY());
			assertEquals(-0.25, repere.getPointZ());
		}
	}

	@Test
	void testDezoom() {
		if (repere != null) {
			repere.dezoom();
			assertEquals(0.00, repere.getPointX());
			assertEquals(0.00, repere.getPointY());
			assertEquals(0.00, repere.getPointZ());
		}
	}

	@Test
	void testAddPoint() {
		if (repere != null) {
			repere.addPoint(new Point(1.0, 1.0, 1.0));
			assertEquals(1, repere.getPoints().size());
			assertTrue(repere.getPoints().get(0).equals(new Point(1.0, 1.0, 1.0)));

			repere.addPoint(new Point(2.0, 2.0, 2.0));
			assertEquals(2, repere.getPoints().size());
			assertTrue(repere.getPoints().get(1).equals(new Point(2.0, 2.0, 2.0)));
		}
	}

	@Test
	void testGetSingleton() {
		assertEquals(repere, Repere.getSingleton());
	}

	@Test
	void testGetX() {
		assertEquals(0.0, repere.getPointX());
	}

	@Test
	void testGetY() {
		assertEquals(0.0, repere.getPointY());
	}

	@Test
	void testGetZ() {
		assertEquals(0.0, repere.getPointZ());
	}

	@Test
	void testSetAndGetName() {
		repere.setName("name");
		assertEquals("name", repere.getName());
	}

	@Test
	void testSetAndGetDescription() {
		repere.setDescription("description");
		assertEquals("description", repere.getDescription());
	}

	@Test
	void testSetAndGetDate() {
		repere.setDate(LocalDate.now());
		assertEquals(LocalDate.now(), repere.getDate());
	}

	@Test
	void testSetAndGetAuteur() {
		repere.setAuteur("Auteur");
		assertEquals("Auteur", repere.getAuteur());
	}

	@Test
	void testResetRepere() {
		repere.resetRepere();
		assertEquals(new ArrayPoint(), repere.getPoints());
		assertEquals(new ArrayList<Polygon>(), repere.getPolygones());
	}

	@Test
	void testClearList() {
		repere.clearList();
		assertEquals(new ArrayPoint(), repere.getPoints());
		assertEquals(new ArrayList<Polygon>(), repere.getPolygones());
	}

	@Test
	void testToString() {
		Polygon polygon = new Polygon(repere);
		polygon.addPoint(0);
		polygon.addPoint(1);
		polygon.addPoint(2);
		repere.addPolygon(polygon);
		assertEquals("[[0, 1, 2]]", repere.toString());
	}

	@Test
	void testGetPolygones() {
		repere.clearList();
		assertEquals(new ArrayList<Polygon>(), repere.getPolygones());
	}

	@Test
	void testGetPolygonessize() {
		repere.clearList();
		assertEquals(0, repere.getPolygonesSize());
	}

}
