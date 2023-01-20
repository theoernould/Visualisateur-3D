package UtilsClasses;

/**
 * Enum qui stocke nos différente faces
 */
public enum DrawMethods {

	DESSUS("Vue du dessus"), FACE("Vue de face"), DROITE("Vue de droite");

	/**
	 * Le nom de la face
	 */
	private String name;

	DrawMethods(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
