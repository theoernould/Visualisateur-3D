package io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import maths.Repere;

/**
 * Classe de test pour la classe PlyFileReader
 */
public class TestPlyFileReader {

    static Repere repere = Repere.getInstance(0, 0, 0);



    @Test
    void testEndHeader() {
        IndexOutOfBoundsException exception = Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            repere = PlyReader.readFile("plyFilesTest/noEndHeader.ply");
        });

        Assertions.assertEquals("Index 44 out of bounds for length 44", exception.getMessage());
    }

    /*@Test
    void testNoVertex() {
        NoVertexEception exception = Assertions.assertThrows(NoVertexEception.class, () -> {
            repere = PlyReader.readFile("plyFilesTest/noVertex.ply");
        });

        Assertions.assertEquals("pas de vertex", exception.getMessage());
    }

    @Test
    void testNoFace() {
        NoFaceException exception = Assertions.assertThrows(NoFaceException.class, () -> {
            repere = PlyReader.readFile("plyFilesTest/noFace.ply");
        });

        Assertions.assertEquals("pas de face", exception.getMessage());
    }*/

}
