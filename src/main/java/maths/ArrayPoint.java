package maths;

import java.util.ArrayList;

import transformation.Rotation;
import transformation.Symetrie;
import transformation.Translation;

public class ArrayPoint extends ArrayList<Point> {

    //matrices de symetrie
    SymetrieArray symetrie = new SymetrieArray();

    //matrices de translation
    private static Translation eTranslation = Translation.TRANSALATION;
    public static Matrice mtxtranslation = new Matrice(eTranslation.getTranslation());

    //matrices de rotation
    RotationArray rotation = new RotationArray();
    
    /**
     * Applique une symetrie sur X à tout les points de l'arrayPoint
     */
    public void symetrieSurX() {
        this.multiplicate(symetrie.getX());
    }

    /**
     * Applique une symetrie sur Y à tout les points de l'arrayPoint
     */
    public void symetrieSurY() {
        this.multiplicate(symetrie.getY());
    }

    /**
     * Applique une symetrie sur Z à tout les points de l'arrayPoint
     */
    public void symetrieSurZ() {
        this.multiplicate(symetrie.getZ());
    }

    /**
     * Applique une homothétie de valeur value à tout les points de l'arrayPoint
     *
     * @param value
     */
    public void homothetie(double value) {
        this.multiplicate(value);
    }

    /**
     * Applique une translation de vecteur vector à tout les points de l'arrayPoint
     *
     * @param vector vecteur directeur
     */
    public void translation(Vecteur vector) {
        mtxtranslation.matrice[0][3] = vector.getxCoordonnee();
        mtxtranslation.matrice[1][3] = vector.getyCoordonnee();
        mtxtranslation.matrice[2][3] = vector.getzCoordonnee();
        this.multiplicate(mtxtranslation);
    }

    /**
     * Applique une rotation sur l'axe X d'angle angle en degrée
     *
     * @param angle angle en degrée
     */
    public void rotationXDegree(double angle) {
        rotationXRadiant(Math.toRadians(angle));
    }

    /**
     * Applique une rotation sur l'axe X d'angle angle en radiant
     *
     * @param angle angle en radiant
     */
    public void rotationXRadiant(double angle) {
        rotation.getMatX()[1][1] = Math.cos(angle);
        rotation.getMatX()[1][2] = Math.sin(angle) * -1;
        rotation.getMatX()[2][1] = Math.sin(angle);
        rotation.getMatX()[2][2] = Math.cos(angle);
        this.multiplicate(rotation.getX());
    }


    /**
     * Applique une rotation sur l'axe Y d'angle angle en degrée
     *
     * @param angle angle en degree
     */
    public void rotationYDegree(double angle) {
        rotationYRadiant(Math.toRadians(angle));
    }

    /**
     * Applique une rotation sur l'axe Y d'angle angle en radiant
     *
     * @param angle angle en radiant
     */
    public void rotationYRadiant(double angle) {
        rotation.getMatY()[0][0] = Math.cos(angle);
        rotation.getMatY()[0][2] = Math.sin(angle) * -1;
        rotation.getMatY()[2][0] = Math.sin(angle);
        rotation.getMatY()[2][2] = Math.cos(angle);
        this.multiplicate(rotation.getY());
    }

    /**
     * Applique une rotation sur l'axe Z d'angle angle en degrée
     *
     * @param angle angle en degrée
     */
    public void rotationZDegree(double angle) {
        rotationZRadiant(Math.toRadians(angle));
    }

    /**
     * Applique une rotation sur l'axe Z d'angle angle en radiant
     *
     * @param angle angle en radiant
     */
    public void rotationZRadiant(double angle) {
    	rotation.getMatZ()[0][0] = Math.cos(angle);
    	rotation.getMatZ()[0][1] = Math.sin(angle) * -1;
    	rotation.getMatZ()[1][0] = Math.sin(angle);
    	rotation.getMatZ()[1][1] = Math.cos(angle);
        this.multiplicate(rotation.getZ());
    }

    /**
     * Multiplie les points de la liste par la matrice de transformation
     */
    private void multiplicate(Matrice matrice) {
        for (Point p : this) {
            p.mutiplicate(matrice);
        }
    }
    
    private void multiplicate(double value) {
        for (Point p : this) {
            p.mutiplicate(value);
        }
    }
}