package views;

import UtilsClasses.DrawMethods;
import controllers.MainController;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import maths.ArrayPoint;
import maths.Point;
import maths.Polygon;
import maths.Repere;
import maths.Vecteur;

import java.util.ArrayList;
import java.util.List;

public class GraphicUtils {

    public static Color face = Color.WHITE;
    public static Color edge = Color.BLACK;
    public static Color point = Color.BLACK;
    private static Vecteur lumiere = new Vecteur(0,1,0);

    public static void drawRepere(Repere repere, Canvas canvas, double centerX, double centerY, DrawMethods method, boolean colorDisplay, boolean onlyVisible, boolean aretesVisibles, boolean pointsVisibles, boolean lumiereActive, boolean lissage) {
        MainController.cons.println("Dessin de la vue " + method + " en cours...");
        long befTime = System.nanoTime();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(face);
        gc.setStroke(edge);
        List<Polygon> polygones = repere.getPolygones();
        switch(method) {
            case DESSUS:
                polygones.sort(new Polygon.ComparatorPolygonY());
                break;
            case DROITE:
                polygones.sort(new Polygon.ComparatorPolygonX());
                break;
            case FACE:
                polygones.sort(new Polygon.ComparatorPolygonZ());
                break;
        }
        for (Polygon p : polygones) {
            Vecteur vue = new Vecteur(0, 0, 0);
            if(!p.isHidden()) {
                double[] xs = new double[0];
                double[] ys = new double[0];
                switch(method) {
                    case DESSUS:
                        xs = p.getPointsX();
                        ys = p.getPointsZ();
                        vue.setyCoordonnee(1);
                        break;
                    case DROITE:
                        xs = p.getPointsZ();
                        ys = p.getPointsY();
                        vue.setxCoordonnee(1);
                        break;
                    case FACE:
                        xs = p.getPointsX();
                        ys = p.getPointsY();
                        vue.setzCoordonnee(1);
                        break;
                }
                for (int i = 0; i < xs.length; i++) {
                    xs[i] += centerX;
                    ys[i] *= -1.0;
                    ys[i] += centerY;
                }
                p.refreshNormale();
                if(Vecteur.produitScalaire(vue, p.getNormale())>0) {
                    Color color = colorDisplay ? (repere.isColored() ? p.getColor() : GraphicUtils.face) : GraphicUtils.face;
                    if (lumiereActive){
                        p.setLuminosity(lumiere);
                        color = p.getColorWithCos(color);
                    }
                    if (lissage && repere.getFaceNbPoints() == 3){
                    	lissage(gc,xs,ys,repere,p,lumiereActive,colorDisplay);
                    }else if(lissage && repere.getFaceNbPoints() != 3) {
                    	lissageWithoutTriangle(gc, xs, ys, repere, p, lumiereActive, colorDisplay);
                    }else{
                        gc.setFill(color);
                        gc.fillPolygon(xs, ys, p.size());
                        if(aretesVisibles) {
                            gc.setStroke(edge);
                        } else {
                            gc.setStroke(color);
                        }
                        gc.strokePolygon(xs, ys, p.size());
                        if(pointsVisibles) {
                            gc.setFill(point);
                            for(int i=0;i<xs.length;i++) {
                                gc.fillOval(xs[i] - 1.25, ys[i] - 1.25, 2.5, 2.5);
                            }
                        }
                    }
                }
            }
        }





        long aftTime = System.nanoTime();
        MainController.cons.println("Dessin de la figure réalisé en " + (aftTime-befTime)/1000000 + " ms.");
    }

    public static void showMessage(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(type.name());
        alert.setHeaderText(title);
        alert.setContentText(message);

        alert.showAndWait();
    }

    public static Image getResourceImage(String str) {
        return new Image(GraphicUtils.class.getResourceAsStream("/" + str));
    }

    public static Image getResourceImage(String str, double width, double height, boolean ratio, boolean smooth) {
        return new Image(GraphicUtils.class.getResourceAsStream("/" + str), width, height, ratio, smooth);
    }

    public static ImageView getResourceImageView(String str) {
        return new ImageView(getResourceImage(str));
    }

    public static ImageView getResourceImageView(String str, double width, double height, boolean ratio, boolean smooth) {
        return new ImageView(getResourceImage(str, width, height, ratio, smooth));
    }

    private static double[] getWeightsOfPixelInTriangle(int x, int y, double[] xs, double[] ys) {
        double div=(ys[1]-ys[2])*(xs[0]-xs[2]) + (xs[2]-xs[1])*(ys[0]-ys[2]);
        double weight1=( (ys[1]-ys[2])*(x-xs[2]) + (xs[2]-xs[1])*(y-ys[2]) ) / div;
        double weight2=( (ys[2]-ys[0])*(x-xs[2]) + (xs[0]-xs[2])*(y-ys[2]) ) / div;
        double weight3=1-weight1-weight2;

        return new double[] {weight1,weight2,weight3};
    }

    private static boolean isPointInPolygon(int x, int y,double[] xs, double[] ys){
        int i,j;
        boolean isInside = false;

        for(i = 0,j= xs.length-1;i<xs.length;j=i++){
            if ( (ys[i] >= y) != (ys[j] >= y) && x <= (xs[j] - xs[i]) *(y-ys[i]) / (ys[j] - ys[i]) + xs[i]){
                isInside = !isInside;
            }
        }
        return isInside;
    }

    private static double maxValue(double[] chars) {
        double max = chars[0];
        for (int ktr = 0; ktr < chars.length; ktr++) {
            if (chars[ktr] > max) {
                max = chars[ktr];
            }
        }
        return max;
    }

    private static double minValue(double[] chars) {
        double max = chars[0];
        for (int ktr = 0; ktr < chars.length; ktr++) {
            if (chars[ktr] < max) {
                max = chars[ktr];
            }
        }
        return max;
    }

    private static void lissage(GraphicsContext gc, double[]xs, double[]ys, Repere repere, Polygon p,boolean lumiereActive, boolean colorDisplay){
        for (int i = (int)minValue(xs);i<=(int) maxValue(xs);i++){
            for (int j = (int)minValue(ys);j<=(int)  maxValue(ys);j++){
                Color oldColor = null;
                boolean itsok = true;
                try{
                    double[] weights = getWeightsOfPixelInTriangle(i,j,xs,ys);
                    for (double d: weights){
                        if (d<0){
                            itsok = false;
                        }
                    }
                    
                    if(itsok) {
                        Color color = colorDisplay ? (repere.isColored() ? p.getColor() : GraphicUtils.face) : GraphicUtils.face;
                        
                        List<Integer> points = p.getPoints();
                        Point point1 = repere.getPoint(points.get(0));
                        Point point2 = repere.getPoint(points.get(1));
                        Point point3 = repere.getPoint(points.get(2));
                        
                        
                        Color colorPoint1 = lumiereActive ? point1.getColorWithPolygonWithLight(color) : color;
                        Color colorPoint2 = lumiereActive ? point2.getColorWithPolygonWithLight(color) : color;
                        Color colorPoint3 = lumiereActive ? point3.getColorWithPolygonWithLight(color) : color;
                        
                        double red = colorPoint1.getRed()*weights[0] + colorPoint2.getRed()*weights[1] + colorPoint3.getRed()*weights[2];
                        double green = colorPoint1.getGreen()*weights[0] + colorPoint2.getGreen()*weights[1] + colorPoint3.getGreen()*weights[2];
                        double blue = colorPoint1.getBlue()*weights[0] + colorPoint2.getBlue()*weights[1] + colorPoint3.getBlue()*weights[2];
                        oldColor = new Color(red,green,blue,1);
                        gc.getPixelWriter().setColor(i,j,oldColor);
                        
                    }
                    
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }
    
    private static double[] getWeightsOfPixelInNotTriangle(int x, int y, double[] xs, double[] ys) {
    	double normeGlobale = 0;
    	for(int i = 0 ; i < xs.length; i++) {
    		normeGlobale += Math.sqrt(Math.pow(x-xs[i], 2)+Math.pow(y-ys[i], 2));
    	}
    	double[] res = new double[xs.length];
    	for(int i = 0 ; i < xs.length; i++) {
    		res[i] = (Math.sqrt(Math.pow(x-xs[i], 2)+Math.pow(y-ys[i], 2)))/normeGlobale;
    	}
    	return res;
    }
    
    private static void lissageWithoutTriangle(GraphicsContext gc, double[]xs, double[]ys, Repere repere, Polygon p,boolean lumiereActive, boolean colorDisplay) {
    	for (int i = (int)minValue(xs);i<=(int) maxValue(xs);i++){
            for (int j = (int)minValue(ys);j<=(int)  maxValue(ys);j++){
                Color oldColor = null;
                boolean itsok = true;
                if(isPointInPolygon(i, j, xs, ys)) {
                	 try{
                         double[] weights = getWeightsOfPixelInNotTriangle(i,j,xs,ys);
                         for (double d: weights){
                             if (d<0){
                                 itsok = false;
                             }
                         }
                         
                         if(itsok) {
                             Color color = colorDisplay ? (repere.isColored() ? p.getColor() : GraphicUtils.face) : GraphicUtils.face;
                             
                             List<Integer> pointsId = p.getPoints();
                             Point point1 = repere.getPoint(pointsId.get(0));
                             Point point2 = repere.getPoint(pointsId.get(1));
                             Point point3 = repere.getPoint(pointsId.get(2));
                             
                             List<Color> colorPoints = new ArrayList<Color>();
                             for(int k = 0; k < p.size() ; k++) {
                             	colorPoints.add(lumiereActive ? point1.getColorWithPolygonWithLight(color) :color);
                             }
                             double red = 0;
                             double green = 0;
                             double blue = 0;
                             for(int k = 0; k < colorPoints.size(); k++) {
                         		red += colorPoints.get(k).getRed()*weights[k];
                         		green += colorPoints.get(k).getGreen()*weights[k];
                         		blue += colorPoints.get(k).getBlue()*weights[k];


                         	}
                             if (red>1){
                                 red = 0.99;
                             }
                             if (green>1){
                                 green = 0.99;
                             }
                             if (blue>1){
                                 blue = 0.99;
                             }
                             oldColor = new Color(red,green,blue,1);
                             gc.getPixelWriter().setColor(i,j,oldColor);
                             
                         }
                	 
               
	                }catch (Exception e){
	                    e.printStackTrace();
	                    System.exit(0);
	                }
                }
            }
        }
    }
    
    private static void ombre(GraphicsContext gc, Repere repere) {
    	// PAS IMPLEMENTE
    	
    }

}
