package transformation;

/**
 * Nos Rotations sur les 3 axes
 */
public enum Rotation {
	
	ROTATIONX(new double[][] { { 1, 0, 0, 0 }, { 0, -1, -1, 0 }, { 0, -1, -1, 0 }, { 0, 0, 0, 1 } }),
	ROTATIONY(new double[][] { { -1, 0, -1, 0 }, { 0, 1, 0, 0 }, { -1, 0, -1, 0 }, { 0, 0, 0, 1 } }),
	ROTATIONZ(new double[][] { { -1, -1, 0, 0 }, { -1, -1, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } });

	/**
	 * Notre matrice (double[][])
	 */
	private double[][] matrix;
	
	/**
	 * Créé une matrice de rotation
	 * @param un double[][] matrix
	 */
	Rotation(double[][] matrix) {
		this.matrix = matrix;
	}
	
	/**
	 * Getter de la matrice de rotation
	 * @return un double[][] de la matrice
	 */
	public double[][] getRotation() {
		return matrix;
	}
}
