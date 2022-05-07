package Gausses;

import Matrixes.NormalMatrix;
import Matrixes.SparseMatrix;
import Variables.Operations;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GaussForSparseMatrix<T extends Operations<T>> {

    public Integer findRowWithMaximumElement(SparseMatrix<T> matrix, Integer fromRow) {
        Integer index = fromRow;
        T maximumElement = matrix.getSparseMatrix().get(new Pair<>(fromRow, fromRow));
        for (int i = fromRow; i < matrix.countRows(); i++) {
            if (matrix.getSparseMatrix().containsKey(new Pair<>(i, fromRow))) {
                if ((maximumElement.absolute()).compare(matrix.getSparseMatrix().get(new Pair<>(i, fromRow)).absolute()) == -1) {
                    maximumElement = maximumElement.initialize(matrix.getSparseMatrix().get(new Pair<>(i, fromRow)));
                    index = i;
                }
            }
        }
        return index;
    }


    public NormalMatrix<T> G(SparseMatrix<T> matrix, String mode) {
        List<T> results = new ArrayList<>();
        int leadingNumberIndex = 0;
        int numCols = matrix.countColumns();
        int numRows = matrix.countRows();
        while (leadingNumberIndex < numRows) {
            if (mode.equals("PG")) {
                int index = findRowWithMaximumElement(matrix, leadingNumberIndex);
                if (index != leadingNumberIndex) {
                    for (int i = 0; i < numCols; i++) {
                        Pair<Integer, Integer> pair = new Pair<>(index, i);
                        Pair<Integer, Integer> pairWithLeading = new Pair<>(leadingNumberIndex, i);
                        boolean firstCondition = matrix.getSparseMatrix().containsKey(pair);
                        boolean secondCondition = matrix.getSparseMatrix().containsKey(pairWithLeading);
                        if (firstCondition && secondCondition) {
                            T firstNumber = matrix.getTypeElement().initialize(matrix.getSparseMatrix().get(new Pair<>(index, i)));
                            matrix.getSparseMatrix().put(pair, matrix.getTypeElement().initialize(matrix.getSparseMatrix().get(pairWithLeading)));
                            matrix.getSparseMatrix().put(pairWithLeading, firstNumber);
                        } else {
                            if (!firstCondition && secondCondition) {
                                matrix.getSparseMatrix().put(new Pair<>(index, i),  matrix.getTypeElement().initialize(matrix.getSparseMatrix().get(pairWithLeading)));
                                matrix.getSparseMatrix().remove(pairWithLeading);
                            } else if (firstCondition) {
                                matrix.getSparseMatrix().put(pairWithLeading, matrix.getTypeElement().initialize(matrix.getSparseMatrix().get(pair)));
                                matrix.getSparseMatrix().remove(new Pair<>(index, i));
                            }
                        }

                    }

                }


            for (int i = leadingNumberIndex + 1; i < matrix.countRows(); i++) {
                //to juz zoptymalizowalem, ale trzeba jeszcze w srodku tego ifa coś ostro zmienić
                //po wykomentowaniu kodu ponizej sparse matrix jest wolniejsza o okolo 50ms XD czyli dalej wzglednie duzo
                //podsumowujac ten fragment wykonuje sie okolo 500ms
                if (matrix.getSparseMatrix().containsKey(new Pair<>(i, leadingNumberIndex))) {
                    T x;
                        if (matrix.getSparseMatrix().containsKey(new Pair<>(i, leadingNumberIndex))) {
                            x = matrix.getTypeElement().initialize(matrix.getSparseMatrix().get(new Pair<>(i, leadingNumberIndex)));
                            x.divide(matrix.getSparseMatrix().get(new Pair<>(leadingNumberIndex, leadingNumberIndex)));
                            for (int j = leadingNumberIndex; j < matrix.countRows() + 1; j++) {
                                if (j == leadingNumberIndex) {
                                    matrix.getSparseMatrix().remove(new Pair<>(i, leadingNumberIndex));
                                    continue;
                                }
                                if (matrix.getSparseMatrix().containsKey(new Pair<>(leadingNumberIndex, j))) {
                                    T toSubtract = matrix.getTypeElement().initialize(matrix.getSparseMatrix().get(new Pair<>(leadingNumberIndex, j)));
                                    toSubtract.multiply(x);
                                    if (matrix.getSparseMatrix().containsKey(new Pair<>(i, j))) {
                                        matrix.getSparseMatrix().get(new Pair<>(i, j)).subtract(toSubtract);
                                    } else {
                                        toSubtract.reverseSign();
                                        matrix.getSparseMatrix().put(new Pair<>(i, j), toSubtract);
                                    }
                                }
                            }
                        }
                }

            }

            leadingNumberIndex++;
        }

        for (int i = numRows - 1; i >= 0; i--) {
            int resultIndex;
            for (int j = matrix.countRows() - 1; j > i; j--) {
                T temp;
                resultIndex = matrix.countRows() - 1 - j;
                if (matrix.getSparseMatrix().containsKey(new Pair<>(i, j))) {
                    temp = matrix.getTypeElement().initialize(matrix.getSparseMatrix().get(new Pair<>(i, j)));
                    temp.multiply(matrix.getTypeElement().initialize(results.get(resultIndex)));
                } else temp = matrix.getTypeElement().initializeWithZero();
                if (matrix.getSparseMatrix().containsKey(new Pair<>(i, matrix.countRows())))
                    matrix.getSparseMatrix().get(new Pair<>(i, matrix.countRows())).subtract(temp);
                else {
                    temp.reverseSign();
                    matrix.getSparseMatrix().put(new Pair<>(i, matrix.countRows()), temp);
                }
            }
            matrix.getSparseMatrix().get(new Pair<>(i, matrix.countRows())).divide(matrix.getSparseMatrix().get(new Pair<>(leadingNumberIndex - 1, leadingNumberIndex - 1)));
            results.add(matrix.getSparseMatrix().get(new Pair<>(i, matrix.countRows())));
            leadingNumberIndex--;
        }
        Collections.reverse(results);

        return new NormalMatrix<>(1, results);
    }


    public NormalMatrix<T> GJ(SparseMatrix<T> matrix, int iter) {
        List<T> results = new ArrayList<>();
        for (int i = 0; i < matrix.countRows(); i++) {
            results.add(matrix.getTypeElement().initializeWithZero());
        }
        for (int h = 0; h < iter; h++) {
            List<T> newResult = new ArrayList<>();
            for (int i = 0; i < matrix.countRows(); i++) {
                newResult.add(matrix.getTypeElement().initializeWithZero());
                T summary;
                if (matrix.getSparseMatrix().containsKey(new Pair<>(matrix.countColumns() - 2, i))) {
                    summary = matrix.getSparseMatrix().get(new Pair<>(matrix.countColumns() - 2, i));
                } else {
                    summary = matrix.getTypeElement().initializeWithZero();
                }
                for (int j = 0; j < matrix.countColumns() - 1; j++) {
                    if (i != j) {
                        T temp2 = matrix.getTypeElement().initializeWithZero();
                        if (matrix.getSparseMatrix().containsKey(new Pair<>(i, j))) {
                            temp2 = matrix.getSparseMatrix().get(new Pair<>(i, j)).initialize(matrix.getSparseMatrix().get(new Pair<>(i, j)));
                        }
                        temp2.multiply(results.get(j));
                        summary.subtract(temp2);
                    }
                }
                if (matrix.getSparseMatrix().containsKey(new Pair<>(i, i))) {
                    summary.divide(matrix.getSparseMatrix().get(new Pair<>(i, i)));
                    newResult.set(i, summary);
                } else {
                    newResult.set(i, matrix.getTypeElement().initializeWithZero());
                }
            }
            results = newResult;
        }

        return new NormalMatrix<>(1, results);
    }

    public NormalMatrix<T> GS(SparseMatrix<T> matrix, int iter) {
        List<T> results = new ArrayList<>();
        for (int i = 0; i < matrix.countRows(); i++) {
            results.add(matrix.getTypeElement().initializeWithZero());
        }
        for (int h = 0; h < iter; h++) {
            for (int i = 0; i < matrix.countRows(); i++) {
                T summary = matrix.getTypeElement().initializeWithZero();
                for (int j = 0; j < matrix.countColumns() - 1; j++) {
                    if (i != j) {
                        T temp2 = matrix.getTypeElement().initializeWithZero();
                        if (matrix.getSparseMatrix().containsKey(new Pair<>(i, j))) {
                            temp2 = matrix.getSparseMatrix().get(new Pair<>(i, j)).initialize(matrix.getSparseMatrix().get(new Pair<>(i, j)));
                        }
                        temp2.multiply(results.get(j));
                        summary.add(temp2);
                    }
                }
                T temp = matrix.getTypeElement().initializeWithZero();
                if (matrix.getSparseMatrix().containsKey(new Pair<>(matrix.countColumns() - 2, i))) {
                    temp = matrix.getSparseMatrix().get(new Pair<>(matrix.countColumns() - 2, i));
                }
                temp.subtract(summary);
                if (matrix.getSparseMatrix().containsKey(new Pair<>(i, i))) {
                    temp.divide(matrix.getSparseMatrix().get(new Pair<>(i, i)));
                    results.set(i, temp);
                } else {
                    results.set(i, matrix.getTypeElement().initializeWithZero());
                }
            }
        }
        return new NormalMatrix<>(1, results);
    }
}
