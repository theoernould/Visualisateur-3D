package maths;


import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Classe de test pour la classe MapPoint
 */
public class TestArrayPoint {

	/*
	 * Cette classe sert uniquement à avoir une approximation du temps pour
	 * appliquer un calcul sur toute l'arrayPoint
	 */

	ArrayPoint arrayPoint = new ArrayPoint();

	@BeforeEach
	void creerPoint() {
		for (double i = 0; i < 100000; i++) {
			 arrayPoint.add(new Point(1,1,1));
		}

	}

	@Test
	void testHomothetie() {
		long befTime = System.nanoTime();
		arrayPoint.homothetie(2);
		long aftTime = System.nanoTime();
		assertTrue((aftTime-befTime)/1000000<200); // 200 ms
	}

	@Test
	void testTranslation() {
		long befTime = System.nanoTime();
		Vecteur vecteur = new Vecteur(1, 2, 3);
		arrayPoint.translation(vecteur);
		long aftTime = System.nanoTime();
		assertTrue((aftTime-befTime)/1000000<200); // 200 ms
	}

	@Test
	void testSymetrieX() {
		long befTime = System.nanoTime();
		arrayPoint.symetrieSurX();
		long aftTime = System.nanoTime();
		assertTrue((aftTime-befTime)/1000000<200); // 200 ms
	}

	@Test
	void testSymetrieY() {
		long befTime = System.nanoTime();
		arrayPoint.symetrieSurY();
		long aftTime = System.nanoTime();
		assertTrue((aftTime-befTime)/1000000<200); // 200 ms
	}

	@Test
	void testSymetrieZ() {
		long befTime = System.nanoTime();
		arrayPoint.symetrieSurX();
		long aftTime = System.nanoTime();
		assertTrue((aftTime-befTime)/1000000<200); // 200 ms
	}

	@Test
	void testRotationX() {
		long befTime = System.nanoTime();
		arrayPoint.rotationXDegree(90);
		long aftTime = System.nanoTime();
		assertTrue((aftTime-befTime)/1000000<200); // 200 ms
	}

	@Test
	void testRotationY() {
		long befTime = System.nanoTime();
		arrayPoint.rotationYDegree(90);
		long aftTime = System.nanoTime();
		assertTrue((aftTime-befTime)/1000000<200); // 200 ms
	}

	@Test
	void testRotationZ() {
		long befTime = System.nanoTime();
		arrayPoint.rotationZDegree(90);
		long aftTime = System.nanoTime();
		assertTrue((aftTime-befTime)/1000000<200); // 200 ms
	}

}
