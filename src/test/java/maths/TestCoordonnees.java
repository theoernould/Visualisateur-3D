package maths;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Classe de test pour la classe Coordonnées
 */
public class TestCoordonnees {

	/**
	 * Coordonnées de test
	 */
	Coordonnees coord;

	@BeforeEach
	void creationPoint() {
		coord = new Coordonnees(1.0, 2.0, 3.0);
	}

	@Test
	void testGetter() {
		assertEquals(1.0, coord.getxCoordonnee());
		assertEquals(2.0, coord.getyCoordonnee());
		assertEquals(3.0, coord.getzCoordonnee());
	}

	@Test
	void testSetter() {
		coord.setxCoordonnee(11.0);
		assertEquals(11.0, coord.getxCoordonnee());
		coord.setyCoordonnee(12.0);
		assertEquals(12.0, coord.getyCoordonnee());
		coord.setzCoordonnee(13.0);
		assertEquals(13.0, coord.getzCoordonnee());
	}

	@Test
	void testToString() {
		assertEquals("1.0, 2.0, 3.0", coord.toString());
	}
}
