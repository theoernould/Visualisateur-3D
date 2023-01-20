package transformation;

/**
 * transaltion
 */
public enum Translation {

	TRANSALATION(new double[][] { { 1, 0, 0, -1 }, { 0, 1, 0, -1 }, { 0, 0, 1, -1 }, { 0, 0, 0, 1 } });

	/**
	 * Notre matrice (double[][])
	 */
	private double[][] matrix;

	/**
	 * Créé une matrice de Translation
	 * @param un double[][] matrix
	 */
	Translation(double[][] matrix) {
		this.matrix = matrix;
	}

	/**
	 * Getter de la matrice de sym�trie
	 * @return un double[][] de la matrice
	 */
	public double[][] getTranslation() {
		return matrix;
	}
}
