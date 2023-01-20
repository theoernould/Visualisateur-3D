package views;

import UtilsClasses.DrawMethods;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import maths.Repere;
import observer.Observer;

/**
 * Classe qui créé un canvas personnalisé
 */
public class ConnectedCanvas extends Canvas implements Observer<Repere> {

	/**
     * Le centre du canvas en X
     */
    protected double centerX;
    /**
     * Le centre du canvas en Y
     */
    protected double centerY;
    /**
     * Définit quelle face on dessine
     */
    protected DrawMethods method;
    /**
     * Définit si on affiche les couleurs
     */
    protected boolean colorDisplay;
    /**
     * Notre graphics Context
     */
    protected GraphicsContext graphicsContext;
    private boolean lissage;
    public void setLissage(boolean lissage) {
    	this.lissage = lissage;
    }
    /**
     * Défnit si les arrêtes sont visibles
     */
    private boolean aretesVisibles;
    /**
     * Défnit si les points sont visibles
     */
    private boolean pointsVisibles;
    private boolean eclairage;

    /**
     * On créé notre canvas personnalisé
     * @param width
     * @param height
     * @param repere
     * @param method
     * @param aretesVisibles
     * @param pointsVisibles
     * @param colors
     */
	@SuppressWarnings("unchecked")
	public ConnectedCanvas(double width, double height, Repere repere, DrawMethods method, boolean aretesVisibles, boolean pointsVisibles, boolean colors, boolean eclairage, boolean lissage) {
		super(width, height);
        centerX = width / 2;
        centerY = height / 2;

        clearCanvas();

        this.method = method;
        this.aretesVisibles = aretesVisibles;
        this.pointsVisibles = pointsVisibles;
        this.colorDisplay = colors;
        this.eclairage = eclairage;
        this.lissage = lissage;
        repere.attach(this);
	}

    public boolean isEclairage() {
        return eclairage;
    }

    public void setEclairage(boolean eclairage) {
        this.eclairage = eclairage;
    }

	/**
	 * efface ce qui est affiché sur le canvas
	 */
    private void clearCanvas() {
        graphicsContext = getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, getWidth(), getHeight());
    }

    public void setAretesVisibles(boolean hasAreteVisible) {
        this.aretesVisibles = hasAreteVisible;
    }

    public void setPointsVisibles(boolean hasVisiblePoint) {
        this.pointsVisibles = hasVisiblePoint;
    }

    /**
     * Dessine le repere sur le canvas
     */
    @Override
    public void update(Repere value) {
        clearCanvas();
        GraphicUtils.drawRepere(value, this, centerX, centerY, method, colorDisplay, true, aretesVisibles, pointsVisibles, eclairage, lissage);
        Paint fill = graphicsContext.getFill();
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillText(method.getName(), 5, 15);
        graphicsContext.setFill(fill);
        //lw.close();
    }

    public void setColorDisplay(boolean hasColor) {
        colorDisplay = hasColor;
    }

	public GraphicsContext getGc() {
		return graphicsContext;
	}
    
}
