package maths;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Classe de test de la classe Matrice
 */
class TestMatrice {

	/**
	 * Matrice de test
	 */
	Matrice matrice1;
	/**
	 * Matrice de test
	 */
	Matrice matrice2;
	/**
	 * Matrice de test
	 */
	Matrice matrice3;

	@BeforeEach
	void creationMatrice() {
		double[][] tableau1 = new double[3][3];
		double[][] tableau2 = new double[3][3];
		for (int x = 0; x < tableau1.length; ++x) {
			for (int y = 0; y < tableau1[x].length; ++y) {
				tableau1[x][y] = 2;
				tableau2[x][y] = 5;
			}
		}
		double[][] tableau3 = new double[5][5];
		for (int x = 0; x < tableau3.length; ++x) {
			for (int y = 0; y < tableau3[x].length; ++y) {
				tableau3[x][y] = 2;
			}
		}

		matrice1 = new Matrice(tableau1);
		matrice2 = new Matrice(tableau2);
		// tableau1 est remplie de 2 [3][3]
		// tableau2 est remplie de 5 [3][3]

		matrice3 = new Matrice(tableau3);
		// tableau3 est remplie de 2 [5][5]
	}

	@Test
	void testAdditionAvecEntier() {
		matrice1.addition(2);
		// tableau1 remplie de 2 donc après l'addition il ne doit y avoir que des 4
		for (int x = 0; x < matrice1.longueurX; ++x) {
			for (int y = 0; y < matrice1.longueurY; ++y) {
				assertEquals(matrice1.matrice[x][y], 4);
			}
		}
	}

	@Test
	void testAdditionAvecAutreTableau() {
		matrice1.addition(matrice2);
		// tableau1 remplie de 2, tableau2 remplie de 5 donc tableau 1 remplie de 7
		matrice1.addition(matrice3);// cela ne fais rien car ils n'ont pas les même dimension
		for (int x = 0; x < matrice1.longueurX; ++x) {
			for (int y = 0; y < matrice1.longueurY; ++y) {
				assertEquals(matrice1.matrice[x][y], 7);
			}
		}
	}

	@Test
	void testSoustractionAvecEntier() {
		matrice1.soustraction(2);
		// tableau1 remplie de 2 donc après l'addition il ne doit y avoir que des 4
		for (int x = 0; x < matrice1.longueurX; ++x) {
			for (int y = 0; y < matrice1.longueurY; ++y) {
				assertEquals(matrice1.matrice[x][y], 0);
			}
		}
	}

	@Test
	void testSoustractionAvecAutreTableau() {
		matrice1.soustraction(matrice2);
		// tableau1 remplie de 2, tableau2 remplie de 5 donc tableau 1 remplie de 7
		matrice1.soustraction(matrice3);// cela ne fais rien car ils n'ont pas les même dimension
		for (int x = 0; x < matrice1.longueurX; ++x) {
			for (int y = 0; y < matrice1.longueurY; ++y) {
				assertEquals(matrice1.matrice[x][y], -3);
			}
		}
	}

	@Test
	void testMultiplicationAvecAutreTableau() {
		Matrice matrice = matrice1.multiplication(matrice2);
		// tableau1 remplie de 2, tableau2 remplie de 5 donc tableau 1 remplie de 7
		for (int x = 0; x < matrice.longueurX; ++x) {
			for (int y = 0; y < matrice.longueurY; ++y) {
				assertEquals(matrice.matrice[x][y], 30);
			}
		}

		matrice = matrice1.multiplication(matrice3);// cela retourne null car ils n'ont pas les même dimension
		assertNull(matrice);
	}

	@Test
	void testToString() {
		String expected = "matrice=[2.0, 2.0, 2.0]\n        [2.0, 2.0, 2.0]\n        [2.0, 2.0, 2.0]\n";
		assertEquals(expected, matrice1.toString());
	}
}
