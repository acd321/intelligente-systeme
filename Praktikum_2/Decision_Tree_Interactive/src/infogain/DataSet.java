package infogain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.IntStream;

public class DataSet {
    private String name;

    // represents data
    private String[][] data = null;
    private double entropy = 0;
    // contains information gain for each one of the attributes in this DataSet
    private HashMap<Attribute, Double> infoGains = new HashMap<Attribute, Double>();
    // best split on Attribute
    private Attribute splitOnAttribute = null;

    public DataSet(String name, String[][] data) {
        this.name = name;
        this.data = data;
        calculateEntropy().calculateInfoGains().findSplitOnAttributes();
    }

    public String[][] getData() {
        return data;
    }

    public double getEntropy() {
        return entropy;
    }

    public HashMap<Attribute, Double> getInfoGains() {
        return infoGains;
    }

    public Attribute getSplitOnAttribute() {
        return splitOnAttribute;
    }

    public String toString() {
        return name;
    }

    // calculate entropy for this DataSet
    private DataSet calculateEntropy() {
        new Attribute(data, data[0].length - 1).getAttributeValues().stream().forEach(attributeValue ->
                entropy += minusPlog2((double) attributeValue.getOccurences() / (data.length - 1)));
        return this;
    }

    private double minusPlog2(double p) {
        double returnValue = 0;
        if (p != 0) returnValue = (-1) * p * Math.log(p) / Math.log(2);
        return returnValue;
    }

    private DataSet calculateInfoGains() {
        IntStream.range(0, data[0].length - 1).forEach(column -> {
            Attribute attribute = new Attribute(data, column);

            ArrayList<DataSet> dataSets = new ArrayList<DataSet>();

            attribute.getAttributeValues().stream().forEach(attributeValue ->
                    dataSets.add(createDataSet(attribute, attributeValue, data)));

            double sum = 0;
            for (int i = 0; i < dataSets.size(); i ++)
                // calculate sum of all attributes entropies
                // for each one attributes in the DataSet
                sum += ((double) (dataSets.get(i).getData().length - 1) / (data.length - 1)) * dataSets.get(i).getEntropy();
            // original entropy - sum
            // store in attributesInfoGain
            infoGains.put(attribute, entropy - sum);
        });
        return this;
    }

    // find best split on attribute
    private DataSet findSplitOnAttributes() {
        Iterator<Attribute> iterator = infoGains.keySet().iterator();
        while (iterator.hasNext()) {
            Attribute attribute = iterator.next();
            if (splitOnAttribute == null || infoGains.get(splitOnAttribute) < infoGains.get(attribute)) splitOnAttribute = attribute;
        }
        return this;
    }

    DataSet createDataSet(Attribute attribute, AttributeValue attributeValue, String[][] data) {
        int column = getColNumber(attribute.getName());
        String[][] returnData = new String[attributeValue.getOccurences() + 1][data[0].length];
        returnData[0] = data[0];
        int counter = 1;
        for (int row = 1; row < data.length; row++)
            if (data[row][column] == attributeValue.getName()) returnData[counter++] = data[row];
            return new DataSet(attribute.getName() + ":" + attributeValue.getName(), deleteColumn(returnData, column));
    }

    // delete a column in data
    private String[][] deleteColumn(String[][] data, int deleteColumn) {
        String returnData[][] = new String[data.length][data[0].length - 1];
        IntStream.range(0, data.length).forEach(row -> {
            int columnCounter = 0;
            for (int column = 0; column < data[0].length; column++)
                if (column != deleteColumn) returnData[row][columnCounter++] = data[row][column];
        });
        return returnData;
    }

    public int getColNumber(String colName) {
        int returnValue = -1;
        for (int column = 0; column < data[0].length - 1; column++)
            if (data[0][column] == colName) { returnValue = column; break;}
            return returnValue;
    }
}
