package decisiontree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.IntStream;

public class Driver {

    static String[][] WEATHER = {{"outlook", "temperature", "humidity", "windy", "play"},
            {"sunny",       "hot",  "high",     "FALSE",    "no"},
            {"sunny",       "hot",  "high",     "TRUE",     "no"},
            {"overcast",    "hot",  "high",     "FALSE",    "yes"},
            {"rainy",       "mild", "high",     "FALSE",    "yes"},
            {"rainy",       "cool", "normal",   "FALSE",    "yes"},
            {"rainy",       "cool", "normal",   "TRUE",     "no"},
            {"overcast",    "cool", "normal",   "TRUE",     "yes"},
            {"sunny",       "mild", "high",     "FALSE",    "no"},
            {"sunny",       "cool", "normal",   "FALSE",    "yes"},
            {"rainy",       "mild", "normal",   "FALSE",    "yes"},
            {"sunny",       "mild", "normal",   "TRUE",     "yes"},
            {"overcast",    "mild", "high",     "TRUE",     "yes"},
            {"overcast",    "hot",  "normal",   "FALSE",    "yes"},
            {"rainy",       "mild", "high",     "TRUE",     "no"}};

    static String[][] CONTACT_LENSES= {{"age", "spectacle-prescrip", "astigmatism", "tear-prod-rate", "prescription"},
            {"young",           "myope",        "no",     "reduced",    "none"},
            {"young",           "myope",        "no",     "normal",     "soft"},
            {"young",           "myope",        "yes",    "reduced",    "none"},
            {"young",           "myope",        "yes",    "normal",     "hard"},
            {"young",           "hypermetrope", "no",     "reduced",    "none"},
            {"young",           "hypermetrope", "no",     "normal",     "soft"},
            {"young",           "hypermetrope", "yes",    "reduced",    "none"},
            {"young",           "hypermetrope", "yes",    "normal",     "hard"},
            {"pre-presbyopic",  "myope",        "no",     "reduced",    "none"},
            {"pre-presbyopic",  "myope",        "no",     "normal",     "soft"},
            {"pre-presbyopic",  "myope",        "yes",    "reduced",    "none"},
            {"pre-presbyopic",  "myope",        "yes",    "normal",     "hard"},
            {"pre-presbyopic",  "hypermetrope", "no",     "reduced",    "none"},
            {"pre-presbyopic",  "hypermetrope", "no",     "normal",     "soft"},
            {"pre-presbyopic",  "hypermetrope", "yes",    "reduced",    "none"},
            {"pre-presbyopic",  "hypermetrope", "yes",    "normal",     "none"},
            {"presbyopic",      "myope",        "no",     "reduced",    "none"},
            {"presbyopic",      "myope",        "no",     "normal",     "none"},
            {"presbyopic",      "myope",        "yes",    "reduced",    "none"},
            {"presbyopic",      "myope",        "yes",    "normal",     "hard"},
            {"presbyopic",      "hypermetrope", "no",     "reduced",    "none"},
            {"presbyopic",      "hypermetrope", "no",     "normal",     "soft"},
            {"presbyopic",      "hypermetrope", "yes",    "reduced",    "none"},
            {"presbyopic",      "hypermetrope", "yes",     "normal",    "none"}};

    public static void main(String[] args) {
        Driver driver = new Driver();
        HashMap<String, String[][]> datas = new HashMap<String, String[][]>();
        datas.put("WEATHER", WEATHER);
        datas.put("CONTACT LENSES", CONTACT_LENSES);

        // iterate over keys in each one of the data sets
        datas.keySet().forEach(data -> {
            // calculate info gain for each feature in a data set
            HashMap<Feature, Double> featuresInfoGain = new HashMap<Feature, Double>();
            // instatntiate a data set given a two dimensional string array
            DataSet dataSet = new DataSet(datas.get(data));
            // iterate through each one column of two dimensional string array
            IntStream.range(0, datas.get(data)[0].length - 1).forEach(column -> {
                // instantiate a feature for that column
                Feature feature = new Feature(datas.get(data), column);
                // feature will have more than one feature value
                // data set for each one feature values
                ArrayList<DataSet> dataSets = new ArrayList<DataSet>();
                feature.getValues().stream().forEach(featureValue ->
                        dataSets.add(driver.createDataSet(featureValue, column, datas.get(data))));

                // sum of all features entropies
                double sum = 0;
                for (int i = 0; i < dataSets.size(); i ++)
                    sum += ((double) (dataSets.get(i).getData().length - 1) / (datas.get(data).length - 1)) * dataSets.get(i).getEntropy();
                // store in featuresInfoGain
                featuresInfoGain.put(feature, dataSet.getEntropy() - sum);
            });

            // print data set
            System.out.println("<" + data + " DATASET>:\n" + dataSet);
            // info gain display table
            System.out.println(driver.generateInfoGainDisplayTable(featuresInfoGain));
            // split on feature
            System.out.println("Best feature to split on is " + driver.determineSplitOnFeature(featuresInfoGain) + "\n");
            System.out.println("\n\n");
        });
    }

    Feature determineSplitOnFeature(HashMap<Feature, Double> featuresInfoGain) {
        Feature splitOnFeature = null;
        Iterator<Feature> iterator = featuresInfoGain.keySet().iterator();
        while (iterator.hasNext()) {
            Feature feature = iterator.next();
            if (splitOnFeature == null) splitOnFeature = feature;
            if (featuresInfoGain.get(splitOnFeature) < featuresInfoGain.get(feature)) splitOnFeature = feature;
        }
        return splitOnFeature;
    }

    StringBuffer generateInfoGainDisplayTable(HashMap<Feature, Double> featuresInfoGain) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Feature                Information Gain\n");
        IntStream.range(0, 38).forEach(i -> stringBuffer.append("-"));
        stringBuffer.append("\n");
        Iterator<Feature> iterator = featuresInfoGain.keySet().iterator();
        while (iterator.hasNext()) {
            Feature feature = iterator.next();
            stringBuffer.append(feature);
            IntStream.range(0, 21 - feature.getName().length()).forEach(i -> stringBuffer.append(" "));
            stringBuffer.append(String.format("%.8f", featuresInfoGain.get(feature)) + "\n");
        }
        return stringBuffer;
    }

    DataSet createDataSet(FeatureValue featureValue, int column, String[][] data) {
        String[][] returnData = new String[featureValue.getOccurences() + 1][data[0].length];
        returnData[0] = data[0];
        int counter = 1;
        for (int row = 1; row < data.length; row++)
            if (data[row][column] == featureValue.getName()) returnData[counter++] = data[row];
            return new DataSet(deleteColumn(returnData, column));
    }

    String[][] deleteColumn(String[][] data, int deleteColumn) {
        String returnData[][] = new String[data.length][data[0].length - 1];
        for (int row = 0; row < data.length; row++) {
            int columnCounter = 0;
            for (int column = 0; column < data[0].length; column++)
                if (column != deleteColumn) returnData[row][columnCounter++] = data[row][column];
        }
        return returnData;
    }
}
