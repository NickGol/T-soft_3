package polynomial;

import com.sun.istack.internal.NotNull;

import java.util.*;

import static java.lang.Math.abs;

public class CalculatePolynomial {
    /**
     * points - Container to save X and Y coordinates
     */
    Map<Double, Double> points = new TreeMap<>();

    public static void main(String[] args) {
    }

    /**
     * This is only function that you can use from outside.
     * This function makes approximation by a second-order polynomial.
     * @see "https://studbooks.net/718642/tehnika/approksimatsiya_polinomom_vtorogo_poryadka"
     * @param pointsXY Map container of points
     * @return array of polynomial coefficients
     */
    public double[] loadData( Map<Double, Double> pointsXY ) {
        points = pointsXY;
        if(points.isEmpty())
            return new double[] {0,0,0};
        if(points.size() == 1) {
            List<Double> arrayY = new ArrayList<>(points.values());
            return new double[]{0, 0, arrayY.get(0)};
        }
        if(points.size() == 2) {
            return findCoefficientsForTwoPoints(points);
        }
        double[][] matrixX = createMatrixX(points);
        double[] matrixY = createMatrixY(points);
        double[][] matrixT = transposeMatrix(matrixX);
        double[][] matrixMultiplyTX = multiplicationMatrix(matrixT, matrixX);
        double[] matrixMultiplyTY = multiplicationMatrix(matrixT, matrixY);
        //determinationMatrix(matrixMultiplyTX);
        return findCoefficients(matrixMultiplyTX, matrixMultiplyTY);
    }

    /**
     * calculates polynomial coefficients in case of two points
     * @param pointsXY
     * @return array of polynomial coefficients
     */
    private double[] findCoefficientsForTwoPoints(Map<Double, Double> pointsXY) {
        List<Double> arrayX = new ArrayList<>(points.keySet());
        List<Double> arrayY = new ArrayList<>(points.values());
        double coefficient1 = -(arrayY.get(0)-arrayY.get(1))/(arrayX.get(1)-arrayX.get(0));
        double coefficient2 = -(arrayX.get(0)*arrayY.get(1)-arrayX.get(1)*arrayY.get(0))/(arrayX.get(1)-arrayX.get(0));
        return new double[]{0, coefficient1, coefficient2};
    }

    /**
     * Preparation of input signal matrix
     * @param pointsXY
     * @return input signal matrix
     */
    private double[][] createMatrixX(Map<Double, Double> pointsXY) {
        double[][] matrixX = new double [pointsXY.size()][3];
        int i = 0;
        for(Double x : pointsXY.keySet()) {
            matrixX[i][0] = x*x;
            matrixX[i][1] = x;
            matrixX[i++][2] = 1;
        }
        //System.out.println( Arrays.deepToString(matrixX) );
        return matrixX;
    }

    /**
     * Preparation of output signal matrix
     * @param pointsXY
     * @return output signal matrix
     */
    private double[] createMatrixY(Map<Double, Double> pointsXY) {
        double[] matrixY = new double [pointsXY.size()];
        int i = 0;
        for(Double y : pointsXY.values()) {
            matrixY[i++] = y;
        }
        //System.out.println( Arrays.toString(matrixY) );
        return matrixY;
    }

    /**
     * Transpose of the matrix
     * @param originalMatrix
     * @return Transposed matrix
     */
    private double[][] transposeMatrix(double[][] originalMatrix) {
        double[][] matrixT = new double[originalMatrix[0].length][originalMatrix.length];

        int columnIndex = 0;
        for(double[] rowData : originalMatrix) {
            int rowIndex = 0;
            for(double element : rowData) {
                matrixT[rowIndex++][columnIndex] = element;
            }
            columnIndex++;
        }
        //System.out.println( Arrays.deepToString(matrixT) );
        return matrixT;
    }

    /**
     * Matrix multiplication
     * @param matrixOne
     * @param matrixTwo
     * @return Multiplied matrices
     */
    private double[][] multiplicationMatrix(double[][] matrixOne, double[][] matrixTwo) {

        if( matrixOne[0].length != matrixTwo.length) {
            throw new RuntimeException("Ошибка. Матрицы не согласованы.");
        }
        double[][] matrixResult = new double[matrixOne.length][matrixTwo[0].length];
        int columnIndex = 0;
        int rowIndex = 0;
        double buff = 0;
        for(double [] rowData : matrixOne) {
            for( columnIndex=0; columnIndex<matrixTwo[0].length; columnIndex++) {
                for (int i = 0; i < matrixTwo.length; i++) {
                    buff += rowData[i] * matrixTwo[i][columnIndex];
                }
                matrixResult[rowIndex][columnIndex] = buff;
                buff = 0;
                //rowIndex++;
            }
            rowIndex++;
        }
        //System.out.println( Arrays.deepToString(matrixResult) );
        return  matrixResult;
    }

    /**
     * Multiplying a matrix with a column
     * @param matrixOne Matrix
     * @param matrixTwo Column
     * @return Multiplied matrices
     */
    private double[] multiplicationMatrix(double[][] matrixOne, double[] matrixTwo) {

        if( matrixOne[0].length != matrixTwo.length) {
            throw new RuntimeException("Ошибка. Матрицы не согласованы.");
        }
        double[] matrixResult = new double[matrixOne.length];
        int rowIndex = 0;
        double buff = 0;
        for(double [] rowData : matrixOne) {
            for (int i = 0; i < matrixTwo.length; i++) {
                buff += rowData[i] * matrixTwo[i];
            }
            matrixResult[rowIndex++] = buff;
            buff = 0;
        }
        //System.out.println( Arrays.toString(matrixResult) );
        return matrixResult;
    }

    /**
     * Calculation of the determinant of the matrix size 3x3
     * @param matrix
     * @return determinant of the matrix
     */
    private double determinationMatrix (@NotNull double[][] matrix) {
        if( (matrix.length != matrix[0].length) || matrix.length!=3) {
            throw new RuntimeException("Ошибка. Ожидаемый размер матрицы 3х3.");
        }
        int matrixSize = matrix.length;
        double determinant = 0, buff = 1;
        for(int step = 0; step < 3; step++) {
            for (int i = 0; i < 3; i++) {
                buff *= matrix[i][ (step+i)%matrixSize ];
            }
            determinant += buff;
            buff = 1;
        }
        for(int step = 2; step < 5; step++) {
            for (int i = 2; i >= 0; i--) {
                buff *= matrix[i][ (step-i)%matrixSize ];
            }
            determinant -= buff;
            buff = 1;
        }
        //System.out.println(determinant);
        return determinant;
    }

    /**
     * Calculation of polynomial coefficients
     * @param matrixMultiplyTX
     * @param matrixMultiplyTY
     * @return polynomial coefficients
     */
    private double[] findCoefficients(@NotNull double[][] matrixMultiplyTX, @NotNull double[] matrixMultiplyTY) {

        double determinantMain=0;
        double[] determinants = new double[matrixMultiplyTY.length];
        double[] coefficients = new double[matrixMultiplyTY.length];
        double[][] matrixCopy;
        determinantMain = determinationMatrix(matrixMultiplyTX);
        for( int step=0; step<matrixMultiplyTY.length; step++) {
            matrixCopy = Arrays.stream(matrixMultiplyTX).map(double[]::clone).toArray(double[][]::new);
            for (int i = 0; i < matrixMultiplyTY.length; i++) {
                matrixCopy[i][step] = matrixMultiplyTY[i];
                determinants[step] = determinationMatrix(matrixCopy);
                coefficients[step] = determinants[step]/determinantMain;
            }
        }
        //System.out.println( Arrays.toString(coefficients) );
        return coefficients;
    }
}
