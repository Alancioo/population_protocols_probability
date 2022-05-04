package Tests;

import Gausses.Gauss;
import Gausses.GaussForSparseMatrix;
import Matrixes.GenerateEquation;
import Matrixes.NormalMatrix;
import Matrixes.SparseMatrix;
import Variables.MyDouble;
import Variables.MyFloat;
import Variables.MyFractions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class TestGaussForSparseMatrix {
    private SparseMatrix<MyFloat> matrixF;
    private SparseMatrix<MyDouble> matrixD;
    private SparseMatrix<MyFractions> matrixFr;
    private GaussForSparseMatrix<MyFloat> gaussF;
    private GaussForSparseMatrix<MyDouble> gaussD;
    private GaussForSparseMatrix<MyFractions> gaussFr;
    private String res = "+0,0000 \n" +
            "+0,0000 \n" +
            "+0,0000 \n" +
            "+0,0000 \n" +
            "+1,0000 \n" +
            "+0,3333 \n" +
            "+0,0000 \n" +
            "+1,0000 \n" +
            "+1,0000 \n" +
            "+1,0000 \n";

    @Before
    public void setUp() {
        GenerateEquation test = new GenerateEquation(3);
        this.matrixF = new SparseMatrix<>(test, new MyFloat(0F));
        this.matrixD = new SparseMatrix<>(test, new MyDouble(0.0));
        this.matrixFr = new SparseMatrix<>(test, new MyFractions(0));
        this.matrixF.fillMatrix();
        this.matrixD.fillMatrix();
        this.matrixFr.fillMatrix();

        this.gaussF = new GaussForSparseMatrix<>();
        this.gaussD = new GaussForSparseMatrix<>();
        this.gaussFr = new GaussForSparseMatrix<>();
    }

    @Test
    public void testGaussPGFloat() {
        NormalMatrix<MyFloat> res = this.gaussF.G(this.matrixF, "PG");
        assertEquals("Gauss PG", this.res, res.toString());
    }

    @Test
    public void testGaussGJFloat() {
        NormalMatrix<MyFloat> res = this.gaussF.GJ(this.matrixF, 100);
        assertEquals("Gauss GJ", this.res, res.toString());
    }

    @Test
    public void testGaussGSFloat() {
        NormalMatrix<MyFloat> res = this.gaussF.GS(this.matrixF, 100);
        assertEquals("Gauss GS", this.res, res.toString());
    }


    @Test
    public void testGaussPGDouble() {
        NormalMatrix<MyDouble> res = this.gaussD.G(this.matrixD, "PG");
        assertEquals("Gauss PG", this.res, res.toString());
    }


    @Test
    public void testGaussGJDouble() {
        NormalMatrix<MyDouble> res = this.gaussD.GJ(this.matrixD, 100);
        assertEquals("Gauss GJ", this.res, res.toString());
    }

    @Test
    public void testGaussGSDouble() {
        NormalMatrix<MyDouble> res = this.gaussD.GS(this.matrixD, 100);
        assertEquals("Gauss GS", this.res, res.toString());
    }

    @Test
    public void testGaussPGFractions() {
        NormalMatrix<MyFractions> res = this.gaussFr.G(this.matrixFr, "PG");
        assertEquals("Gauss PG", this.res, res.toString());
    }


    @Test
    public void testGaussGJFractions() {
        NormalMatrix<MyFractions> res = this.gaussFr.GJ(this.matrixFr, 100);
        assertEquals("Gauss GJ", this.res, res.toString());
    }

    @Test
    public void testGaussGSFractions() {
        NormalMatrix<MyFractions> res = this.gaussFr.GS(this.matrixFr, 100);
        assertEquals("Gauss GS", this.res, res.toString());
    }

    @After
    public void tearDown() {
        this.matrixF = null;
        this.matrixD = null;
        this.res = null;
        this.gaussF = null;
        this.gaussD = null;
    }
}
