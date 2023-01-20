package maths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;


/**
 * Classe de test pour la classe Vecteur
 */
public class TestVecteur {

	/**
	 * Vecteur de test
	 */
	Vecteur vecteur1;
	/**
	 * Vecteur de test
	 */
	Vecteur vecteur2;
	/**
	 * Vecteur de test
	 */
	Vecteur vecteur3;
	/**
	 * Vecteur de test
	 */
	Vecteur vecteur4;

	@BeforeEach
	void initVecteur() {
		vecteur1 = new Vecteur(1, 2, 3);
		vecteur2 = new Vecteur(3, 4, 7);
		vecteur3 = new Vecteur(10, 20, 30);
		vecteur4 = new Vecteur(6, 7, 8);
	}

	@Test
	void testProduitScalaire() {
		assertEquals(32, Vecteur.produitScalaire(vecteur1, vecteur2));
		assertEquals(320, Vecteur.produitScalaire(vecteur2, vecteur3));
		assertEquals(140, Vecteur.produitScalaire(vecteur1, vecteur3));
	}

	@Test
	void testCosinus() throws ParseException {
		assertEquals(32 / (Math.sqrt(14) * Math.sqrt(74)), Vecteur.getCosinusBetweenTwoVector(vecteur1, vecteur2));
	}

	@Test
	void testProduitVectoriel() {
		assertEquals(new Vecteur(-5, 10, -5), Vecteur.produitVectoriel(vecteur1, vecteur4));
	}

	@Test
	void testNorme() {
		assertEquals(Math.sqrt(14), vecteur1.getNorme());
	}
}
