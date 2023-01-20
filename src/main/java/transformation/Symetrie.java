package transformation;

/**
 * Nos Symétries sur les 3 axes
 */
public enum Symetrie {

	SYMETRIEX(new double[][] { { -1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } }),
	SYMETRIEY(new double[][] { { 1, 0, 0, 0 }, { 0, -1, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } }),
	SYMETRIEZ(new double[][] { { 1, 0, 0, 0 }, { 0.0, 1, 0, 0 }, { 0, 0, -1, 0 }, { 0, 0, 0, 1} });

	/**
	 * Notre matrice (double[][])
	 */
	private double[][] matrix;

	/**
	 * Créé une matrice de Symetrie
	 * @param un double[][] matrix
	 */
	Symetrie(double[][] matrix) {
		this.matrix = matrix;
	}
	
	/**
	 * Getter de la matrice de sym�trie
	 * @return un double[][] de la matrice
	 */
	public double[][] getSymetrie() {
		return matrix;
	}
}
