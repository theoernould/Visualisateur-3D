package io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import errors.NoAsciiFileException;
import maths.Point;
import maths.Polygon;
import maths.Repere;
import controllers.MainController;

public class PlyReader {

    /**
     * Pour régler le problème
     * de use utility classe
     */
    private PlyReader() {}

    /**
     * Methode pour lire les fichier ply
     * @param fileName
     * @return
     * @throws IOException
     * @throws NoAsciiFileException
     */
    public static Repere readFile(String fileName) throws IOException, NoAsciiFileException {
        long befTime = 0;
        befTime = System.nanoTime();
        List<String> lines = getLines(fileName);

        Repere repere = Repere.getInstance(0, 0, 0);
        String name = new File(fileName).getName();
        repere.setName(name);

        AtomicInteger nbPoints = new AtomicInteger(0);
        AtomicInteger cpt = new AtomicInteger(0);

        setHeaderElements(lines, cpt, repere, nbPoints);

        addPointsFromLines(repere, lines, cpt, nbPoints);

        addPolygonsFromLines(repere, lines, cpt);

        long aftTime = System.nanoTime();
        if(MainController.cons != null) // Pour faire passer les tests
            MainController.cons.println("Récupération du fichier " + fileName + " réalisé en " + (aftTime - befTime) / 1000000 + " ms.");

        return repere;
    }

    private static void addPolygonsFromLines(Repere repere, List<String> lines, AtomicInteger cpt) {
        List<Point> points = repere.getPoints();
        int inCpt = cpt.get();
        while (inCpt < lines.size()) {
            String line = lines.get(inCpt).trim();
            if (line != null && !line.equals("")) {
                double[] pointsIdx = Arrays.stream(lines.get(inCpt).trim().split(" ")).mapToDouble(Double::parseDouble).toArray();
                Polygon polygon = getPolygonFromPoints(repere, points, pointsIdx);
                repere.addPolygon(polygon);
            }
            inCpt++;
        }
        cpt.set(inCpt);
    }

    private static void addPointsFromLines(Repere repere, List<String> lines, AtomicInteger cpt, AtomicInteger nbPoints) {
        List<Point> points = new ArrayList<>();
        Point point = null;
        int pointIdx = 0;
        String line = "";
        int inCpt = cpt.get();
        while (pointIdx < nbPoints.get() && inCpt < lines.size()) {
            line = lines.get(inCpt).trim();
            if (line != null && !line.equals("")) {
                double[] options = Arrays.stream(lines.get(inCpt).trim().split(" "))
                        .mapToDouble(Double::parseDouble)
                        .toArray();
                point = getPointFromOptions(options, repere.isColored());
                points.add(point);
                pointIdx++;
            }
            inCpt++;
        }
        repere.addPoints(points);
        cpt.set(inCpt);
    }

    private static void setHeaderElements(List<String> lines, AtomicInteger cpt, Repere repere, AtomicInteger nbPoints) throws NoAsciiFileException {
        String line;
        int inCpt = cpt.get();
        do {
            line = lines.get(inCpt);
            line = line.trim();
            if (line != null && !line.equals("")) {
                String[] options = line.split(" ");
                setHeaderElement(repere, options, nbPoints);
            }
            inCpt++;
        } while (!line.equals("end_header"));
        cpt.set(inCpt);
    }

    private static Polygon getPolygonFromPoints(Repere repere, List<Point> points, double[] pointsIdx) {
        Polygon polygon = new Polygon(repere);
        repere.setFaceNbPoints(Math.max((int) pointsIdx[0], repere.getFaceNbPoints()));
        for (int j = 1; j < pointsIdx.length; j++) {
            points.get((int) pointsIdx[j]).addPolygon(polygon);
            polygon.addPoint((int) pointsIdx[j]);
        }
        polygon.initializeColor();
        return polygon;
    }

    private static Point getPointFromOptions(double[] options, boolean hasColor) {
        double xAxis = options[0];
        double yAxis = options[1];
        double zAxis = options[2];
        int red = 255;
        int green = 255;
        int blue = 255;
        if (hasColor) {
            red = (int) options[3];
            green = (int) options[4];
            blue = (int) options[5];
        }
        return new Point(xAxis, yAxis, zAxis, red, green, blue);
    }

    private static void setHeaderElement(Repere repere, String[] options, AtomicInteger nbPoints) throws NoAsciiFileException {
        switch (options[0]) {
            case "comment":
                if(options.length >= 3) {
                    switch(options[1]) {
                        case "description:":
                            options = Arrays.copyOfRange(options, 2, options.length);
                            String description = String.join(" ", options);
                            repere.setDescription(description);
                            break;
                        case "auteur:":
                            options = Arrays.copyOfRange(options, 2, options.length);
                            String auteur = String.join(" ", options);
                            repere.setAuteur(auteur);
                            break;
                        case "dateCreation:":
                            LocalDate date = LocalDate.parse(options[2]);
                            repere.setDate(date);
                            break;
                    }
                }
                break;
            case "property":
                if(options.length >= 3) {
                    if(options[1].equals("uchar")) {
                        String str = options[2];
                        boolean hasColor = str.equals("red") || str.equals("green") || str.equals("blue");
                        repere.setColored(hasColor);
                    }
                }
                break;
            case "format":
                if (!options[1].equalsIgnoreCase("ascii"))
                    throw new NoAsciiFileException("Le fichier choisi n'est pas en ASCII");
                break;
            case "element":
                if(options[1].equals("vertex")) nbPoints.set(Integer.parseInt(options[2]));
                break;
            default:
                break;
        }
    }

    /**
     * On récupère les lignes des fichiers
     * @param fileName
     * @return
     * @throws IOException
     */
    private static List<String> getLines(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        return Files.readAllLines(path);
    }
}