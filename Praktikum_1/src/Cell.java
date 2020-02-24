public class Cell {

    // Koordinaten
    public int i, j;

    // Vorgänger Zelle für den Pfad
    public Cell source;

    // Heuristik Kosten für die aktuelle Zelle
    public int heuristicCost;

    // F = G + H
    // F steht für die zu berechnenden Kosten
    // G Kosten vom aktuellen Knoten bis zum Zielknoten
    // H gibt den Wert der geringsten Kosten vom aktuellen Knoten zum Zielknoten an
    public int finalCost;

    // wenn Zelle Teil vom Pfad ist
    public boolean solution;

    public Cell(int i, int j) {
        this.i = i;
        this.j = j;
    }

    @Override
    public String toString() {
        return "[" + i + ", " + j + "]";
    }
}
