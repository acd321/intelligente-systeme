package decisiontree;

import java.util.HashSet;
import java.util.stream.IntStream;

public class Feature {
    private String name = null;
    private HashSet<FeatureValue> values = new HashSet<FeatureValue>();

    // column represents that this feature is within the data
    public Feature(String[][] data, int column) {
        // first row in the data has all features
        this.name = data[0][column];
        // initialize Set of featureValues for this feature
        IntStream.range(1, data.length).forEach(row -> values.add(new FeatureValue(data[row][column])));
        // find and set occurences of each one of featureValues for this Feature
        values.stream().forEach(featureValue ->  {
            int counter = 0;
            for (int row = 1; row < data.length; row++)
                if (featureValue.getName() == data[row][column]) featureValue.setOccurences(++counter);
        });
    }

    public String getName() {
        return name;
    }

    public HashSet<FeatureValue> getValues() {
        return values;
    }

    public String toString() {
        return name;
    }
}
