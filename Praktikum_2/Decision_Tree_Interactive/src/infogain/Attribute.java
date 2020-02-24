package infogain;

import java.util.HashSet;
import java.util.stream.IntStream;

public class Attribute {
    private String name = null;

    // Set of attributeValues
    private HashSet<AttributeValue> attributeValues = new HashSet<AttributeValue>();

    // column that this attribute is in data
    // data in data set
    public Attribute(String[][] data, int column) {
        // first row in the data has all attributes
        this.name = data[0][column];
        // initialize Set of attributeValues for this attribute
        IntStream.range(1, data.length).forEach(row -> attributeValues.add(new AttributeValue(data[row][column])));
        // find and set occurences of each one of attributeValues for this Attribute
        attributeValues.stream().forEach(attributeValue ->  {
            int counter = 0;
            for (int row = 1; row < data.length; row++)
                if (attributeValue.getName() == data[row][column]) attributeValue.setOccurences(++counter);
        });
    }

    public String getName() {
        return name;
    }

    public HashSet<AttributeValue> getAttributeValues() {
        return attributeValues;
    }

    public String toString() {
        return name;
    }
}
