package Matrixes;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Variables.Operations;
import javafx.util.Pair;

public class SparseMatrix<T extends Operations<T>> {

    private final Map<Pair<Integer, Integer>, T> sparseMatrix;

    private final GenerateEquation generateEquation;
    private int counter = 0;
    private final T typeElement;


    public Map<Pair<Integer, Integer>, T> getSparseMatrix() {
        return this.sparseMatrix;
    }

    public T getTypeElement() {
        return this.typeElement;
    }

    public Map<Pair<Integer, Integer>, Integer> generateIndexes() {
        Map<Pair<Integer, Integer>, Integer> indexes = new HashMap<>(Map.of());
        int t = 0, n = 0, counter = 0;
        while (t + n <= this.generateEquation.getSize()) {
            indexes.put(new Pair<>(t, n), counter);
            if (n + t == this.generateEquation.getSize()) {
                n = 0;
                t++;
            } else {
                n++;
            }
            counter++;
        }

        return indexes;
    }

    public SparseMatrix(GenerateEquation generateEquation, T typeElement) {
        this.sparseMatrix = new HashMap<>(Map.of());
        this.generateEquation = generateEquation;
        this.typeElement = typeElement;
    }

    public void fillMatrix() {
        int t = 0, n = 0;
        Map<Pair<Integer, Integer>, Integer> indexes = generateIndexes();
        ArrayList<ArrayList<ArrayList<Integer>>> possibleChanges = new ArrayList<>();
        ArrayList<Map<ArrayList<Integer>, Long>> groupedVotes = new ArrayList<>();

        while (t + n <= this.generateEquation.getSize()) {
            possibleChanges.add(new ArrayList<>());
            ArrayList<String> possibleVotes = this.generateEquation.GenerateVoters(t, n);

            for (int i = 0; i < this.generateEquation.getSize(); i++) {
                for (int j = 0; j < this.generateEquation.getSize(); j++) {

                    ArrayList<String> tempElements = new ArrayList<>();
                    ArrayList<String> copyOfPossibleVotes = new ArrayList<>(possibleVotes);
                    copyOfPossibleVotes.remove(possibleVotes.get(i));
                    copyOfPossibleVotes.remove(possibleVotes.get(j));
                    if (i == j) continue;
                    tempElements.add(generateEquation.transitionFunction.get(new Pair<>(possibleVotes.get(i), possibleVotes.get(j))).getValue());
                    tempElements.addAll(copyOfPossibleVotes);
                    tempElements.add(generateEquation.transitionFunction.get(new Pair<>(possibleVotes.get(i), possibleVotes.get(j))).getKey());

                    possibleChanges.get(this.counter).add(generateEquation.sumVotes(tempElements));
                }
            }
            counter++;

            if (n + t == this.generateEquation.getSize()) {
                n = 0;
                t++;
            } else {
                n++;
            }
        }

        for (ArrayList<ArrayList<Integer>> el : possibleChanges) {
            groupedVotes.add(generateEquation.groupVotes(el));
        }

        for (int i = 0; i < groupedVotes.size(); i++) {
            for (Map.Entry<ArrayList<Integer>, Long> entry : groupedVotes.get(i).entrySet()) {
                T toMatrix = typeElement.initializeToSparseMatrix(entry.getValue(), generateEquation.getVotesQuantity(groupedVotes.get(0)));
                toMatrix.reverseSign();
                sparseMatrix.put(new Pair<>(i, indexes.get(new Pair<>(entry.getKey().get(0), entry.getKey().get(1)))), toMatrix);
            }
        }

        for (int i = 0; i < generateEquation.getSize() * generateEquation.getSize(); i++) {
            T temp = typeElement.initializeWithOne();
            temp.reverseSign();
            if (this.sparseMatrix.containsKey(new Pair<>(i, i)) && !this.sparseMatrix.get(new Pair<>(i, i)).returnValue().equals(temp.returnValue())) {
                T toAdd = typeElement.initializeWithOne();
                toAdd.add(this.sparseMatrix.get(new Pair<>(i, i)));
                this.sparseMatrix.put(new Pair<>(i, i), toAdd);
            } else sparseMatrix.put(new Pair<>(i, i), typeElement.initializeWithOne());
        }

        this.sparseMatrix.put(new Pair<>(counter - 1, counter - 1), typeElement.initializeWithOne());
        this.sparseMatrix.put(new Pair<>(counter - 1, counter), typeElement.initializeWithOne());
    }

    public Integer countRows() {
        return generateIndexes().size();
    }

    public Integer countColumns() {
        return countRows() + 1;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        DecimalFormat df = new DecimalFormat("+0.0000;-0.0000");
        for (int i = 0; i < counter; i++) {
            for (int j = 0; j < counter + 1; j++) {
                if (this.sparseMatrix.containsKey(new Pair<>(i, j)))
                    result.append(df.format(this.sparseMatrix.get(new Pair<>(i, j)).returnValue())).append(" ");
                else result.append("+0,0000 ");
            }
            result.append("\n");
        }
        return result.toString();
    }

}
