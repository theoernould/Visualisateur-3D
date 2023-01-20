package maths;

import transformation.Rotation;
import transformation.Symetrie;

public class SymetrieArray {
	
	protected Matrice symetrieX;
    protected Matrice symetrieY;
    protected Matrice symetrieZ;
    
	public SymetrieArray() {
		symetrieX = new Matrice(Symetrie.SYMETRIEX.getSymetrie());
		symetrieY = new Matrice(Symetrie.SYMETRIEY.getSymetrie());
		symetrieZ = new Matrice(Symetrie.SYMETRIEZ.getSymetrie());
	}

	public Matrice getX() {
		return symetrieX;
	}

	public Matrice getY() {
		return symetrieY;
	}

	public Matrice getZ() {
		return symetrieZ;
	}

}
