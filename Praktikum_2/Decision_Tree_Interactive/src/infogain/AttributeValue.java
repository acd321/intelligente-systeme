package infogain;

public class AttributeValue {
    private String name;
    private int occurences;

    public AttributeValue(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getOccurences() {
        return occurences;
    }

    public void setOccurences(int occurences) {
        this.occurences = occurences;
    }

    // compare attribute values
    public int hashCode() {
        return name.hashCode();
    }

    public boolean equals(Object object) {
        boolean returnValue = true;
        if (object == null || (getClass() != object.getClass())) returnValue = false;
        if (name == null)  if (((AttributeValue) object).name != null) returnValue = false;
        else if (!name.equals(((AttributeValue) object).name)) returnValue = false;
        return returnValue;
    }

    public String toString() {
        return name;
    }
}
