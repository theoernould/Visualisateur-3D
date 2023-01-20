package maths;

import transformation.Rotation;

public class RotationArray {

    protected Matrice rotationX;
    protected Matrice rotationY;
    protected Matrice rotationZ;
    
	public RotationArray() {
	     rotationX = new Matrice(Rotation.ROTATIONX.getRotation());
	     rotationY = new Matrice(Rotation.ROTATIONY.getRotation());
	     rotationZ = new Matrice(Rotation.ROTATIONZ.getRotation());
	}

	public Matrice getX() {
		return rotationX;
	}

	public Matrice getY() {
		return rotationY;
	}

	public Matrice getZ() {
		return rotationZ;
	}
	public double[][] getMatX() {
		return rotationX.matrice;
	}

	public double[][] getMatY() {
		return rotationY.matrice;
	}

	public double[][] getMatZ() {
		return rotationZ.matrice;
	}
}
