package infogain;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Builder extends JFrame {

    static String[][] ZAHLUNGSART = {{"Kundenart", "Zahlungsgeschwindigkeit", "Kauffrequenz", "Herkunft", "Zahlungsmittel: Rechnung?"},
            {"Neukunde",       "niedrig",   "niedrig",      "Inland",    "false"},
            {"Neukunde",       "niedrig",   "niedrig",      "Ausland",   "false"},
            {"Stammkunde",     "niedrig",   "niedrig",      "Inland",    "true"},
            {"Normalkunde",    "mittel",    "niedrig",      "Inland",    "true"},
            {"Normalkunde",    "hoch",      "hoch",         "Inland",    "true"},
            {"Normalkunde",    "hoch",      "hoch",         "Ausland",   "false"},
            {"Stammkunde",     "hoch",      "hoch",         "Ausland",   "true"},
            {"Neukunde",       "mittel",    "niedrig",      "Inland",    "false"},
            {"Neukunde",       "hoch",      "hoch",         "Inland",    "true"},
            {"Normalkunde",    "mittel",    "hoch",         "Inland",    "true"},
            {"Neukunde",       "mittel",    "hoch",         "Ausland",   "true"},
            {"Stammkunde",     "mittel",    "niedrig",      "Ausland",   "true"},
            {"Stammkunde",     "niedrig",   "hoch",         "Inland",    "true"},
            {"Normalkunde",    "mittel",    "niedrig",      "Ausland",   "false"}};

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

    // data sets
    static Map<String, String[][]> datas = Collections.unmodifiableMap(new HashMap<String, String[][]>() {
        private static final long SerialVersionUID = 1L;
        {
            put("WEATHER", WEATHER);
            put("ZAHLUNGSART", ZAHLUNGSART);
        }
    });

    private static final String ANSI_RESET  = "\u001B[0m";
    private static final String GREEN  = "\u001B[92m";
    private static final String BLUE   = "\u001B[34m";

    // take first item in datas to use in the Application
    static String dataKey = datas.keySet().iterator().next();

    public static void main(String[] args) throws  Exception {
        Builder builder = new Builder();
        JTree tree = null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        boolean flag = true;
        while (flag) {
            System.out.println(GREEN + "What do you want to do (build tree, choose dataset, exit) ?" + ANSI_RESET);
            String command = bufferedReader.readLine();
            switch (command) {
                case "build tree":
                    // create a DataSet
                    DataSet dataSet = new DataSet(dataKey, datas.get(dataKey));
                    // first node in the tree will be whats returned from getSplitOnAttribute
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(dataSet.getSplitOnAttribute().getName());
                    // build decision tree
                    builder.processDataSet(dataSet, node, "");
                    if (tree != null) builder.remove(tree);
                    tree = new JTree(node);
                    builder.add(tree);
                    builder.setSize(350, 350);
                    builder.setTitle(dataKey + " DATASET");
                    builder.setVisible(true);
                    break;
                case "choose dataset":
                    System.out.println(GREEN + "Choose dataset (" + datas.keySet() + ") ?" + ANSI_RESET);
                    String value = bufferedReader.readLine();
                    if (datas.containsKey(value)) dataKey = value;
                    else System.out.println(GREEN + "Please enter a valid dataset name" + ANSI_RESET);
                    break;
                case "exit":
                    flag = false;
                    break;
            }
        }
        System.exit(0);
    }

    void processDataSet(DataSet dataSet, DefaultMutableTreeNode node, String featureValueName) {
        if (dataSet.toString() != null) System.out.println(dataSet);
        if (dataSet.getEntropy() != 0) {
            System.out.println(BLUE + "Best attribute to split on is " + dataSet.getSplitOnAttribute() + " " + dataSet.getSplitOnAttribute().getAttributeValues() + ANSI_RESET);
            HashMap<String, DataSet> attributeDataSets = new HashMap<String, DataSet>();
            // create new data set for each attribute value on getSplitOnAttribute
            // store in attributeDataSets
            dataSet.getSplitOnAttribute().getAttributeValues().forEach(attributeValue ->
                    attributeDataSets.put(attributeValue.getName(), dataSet.createDataSet(dataSet.getSplitOnAttribute(), attributeValue, dataSet.getData())));
            processDataSets(attributeDataSets, node);
        } else {
            String[][] data = dataSet.getData();
            String decision = "[" + data[0][data[0].length - 1] + " = " + data[1][data[0].length - 1] + "]";
            node.add(new DefaultMutableTreeNode(featureValueName + "  : " + decision));
            System.out.println("Decision => " + decision);
        }
    }

    void processDataSets(HashMap<String, DataSet> dataSets, DefaultMutableTreeNode node) {
        dataSets.keySet().forEach(dataSet -> {
            // for each data set create a new node and add it to node
            if (dataSets.get(dataSet).getEntropy() != 0) {
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(dataSet + "  :  [" + dataSets.get(dataSet).getSplitOnAttribute().getName() + "]");
                node.add(newNode);
                // processDataSet on new node
                processDataSet(dataSets.get(dataSet), newNode, dataSet);
            } else processDataSet(dataSets.get(dataSet), node, dataSet);
        });

    }
}
