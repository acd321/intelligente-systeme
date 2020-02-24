package decisiontree;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class DataSet {
    private String[][] data = null;
    private double entropy = 0;
    // list of features contained within that data
    private ArrayList<Feature> features = null;

    public DataSet(String[][] data) {
        this.data = data;
        // calculates and set entropy for this DataSet
        new Feature(data, data[0].length - 1).getValues().stream().forEach(featureValue ->
                entropy += minusPlog2((double) featureValue.getOccurences() / (data.length - 1)));
    }

    public String[][] getData() {
        return data;
    }

    public double getEntropy() {
        return entropy;
    }

    public ArrayList<Feature> getFeatures() {
        return features;
    }

    // return data contained within this DataSet
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int row = 0; row < data.length; row++) {
            for (int column = 0; column < data[row].length; column++) {
                stringBuffer.append(data[row][column]);
                IntStream.range(0, 24 - data[row][column].length()).forEach(i -> stringBuffer.append(" "));
            }
            stringBuffer.append("\n");
            if (row == 0) {
                IntStream.range(0, 108).forEach(i -> stringBuffer.append("-"));
                stringBuffer.append("\n");
            }
        }
        return stringBuffer.toString();
    }

    // calculate entropy
    double minusPlog2(double p) {
        double returnValue = 0;
        if (p != 0) returnValue = (-1) * p * Math.log(p) / Math.log(2);
        return returnValue;
    }
}
