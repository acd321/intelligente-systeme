import java.util.PriorityQueue;

public class AStarAlgorithm {

    // Kosten für diagonale, vertikale und horizontale Bewegungen
    private static final int D_COST = 14;
    private static final int V_H_COST = 10;

    private static final String ANSI_RESET  = "\u001B[0m";
    private static final String GREEN  = "\u001B[92m";
    private static final String BLUE   = "\u001B[34m";
    private static final String BG_CYAN   = "\u001B[46m";

    private Cell[][] field;

    // Wir definieren eine Priority Queue
    // die Menge der zu bewertenden Knoten
    private PriorityQueue<Cell> openList;

    // die Menge der Knoten, die bewertet wurden
    private boolean[][] closedList;

    // Start Knoten
    private int startI, startJ;

    // End Knoten
    private int endI, endJ;

    public AStarAlgorithm(int width, int height, int startI, int startJ, int endI, int endJ, int[][] blocks) {
        field = new Cell[width][height];
        closedList = new boolean[width][height];
        openList = new PriorityQueue<Cell>((Cell c1, Cell c2) -> {

            return c1.finalCost < c2.finalCost ? -1 : c1.finalCost > c2.finalCost ? 1 : 0;
        });

        setStartCell(startI, startJ);

        setEndCell(endI, endJ);

        // Initialisiere Heuristik
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                field[i][j] = new Cell(i, j);
                field[i][j].heuristicCost = Math.abs(i - endI) + Math.abs(j - endJ);
                field[i][j].solution = false;
            }
        }

        field[startI][startJ].finalCost = 0;

        // erstelle Blöcke auf der Karte
        for (int i = 0; i < blocks.length; i++) {
            addBlock(blocks[i][0], blocks[i][1]);
        }
    }

    public void addBlock(int i, int j) {
        field[i][j] = null;
    }

    public void setStartCell(int i , int j) {
        startI = i;
        startJ = j;
    }

    public void setEndCell(int i, int j) {
        endI = i;
        endJ = j;
    }

    public void checkAndUpdateCost(Cell current, Cell successor, int cost) {
        if (successor == null || closedList[successor.i][successor.j])
            return;

        int cellFinalCost = successor.heuristicCost + cost;
        boolean inOpen = openList.contains(successor);

        if (!inOpen || cellFinalCost < successor.finalCost) {
            successor.finalCost = cellFinalCost;
            successor.source = current;

            if (!inOpen)
                openList.add(successor);
        }
    }

    public void process() {
        // füge den Start Knoten zu openList
        openList.add(field[startI][startJ]);
        Cell currentCell;

        while (true) {
            currentCell = openList.poll();

            if (currentCell == null)
                break;

            closedList[currentCell.i][currentCell.j] = true;

            if (currentCell.equals(field[endI][endJ]))
                return;

            Cell successor;

            // prüfe die Nachfolger Knoten
            if (currentCell.i - 1 >= 0) {
                successor = field[currentCell.i - 1][currentCell.j];
                checkAndUpdateCost(currentCell, successor, currentCell.finalCost + V_H_COST);

                if (currentCell.j - 1 >= 0) {
                    successor = field[currentCell.i - 1][currentCell.j - 1];
                    checkAndUpdateCost(currentCell, successor, currentCell.finalCost + D_COST);
                }

                if (currentCell.j + 1 < field[0].length) {
                    successor = field[currentCell.i - 1][currentCell.j + 1];
                    checkAndUpdateCost(currentCell, successor, currentCell.finalCost + D_COST);
                }
            }

            if (currentCell.j - 1 >= 0) {
                successor = field[currentCell.i][currentCell.j - 1];
                checkAndUpdateCost(currentCell, successor, currentCell.finalCost + V_H_COST);
            }

            if (currentCell.j + 1 < field[0].length) {
                successor = field[currentCell.i][currentCell.j + 1];
                checkAndUpdateCost(currentCell, successor, currentCell.finalCost + V_H_COST);
            }

            if (currentCell.i + 1 < field.length) {
                successor = field[currentCell.i + 1][currentCell.j];
                checkAndUpdateCost(currentCell, successor, currentCell.finalCost + V_H_COST);

                if (currentCell.j - 1 >= 0) {
                    successor = field[currentCell.i + 1][currentCell.j - 1];
                    checkAndUpdateCost(currentCell, successor, currentCell.finalCost + D_COST);
                }

                if (currentCell.j + 1 < field[0].length) {
                    successor = field[currentCell.i + 1][currentCell.j + 1];
                    checkAndUpdateCost(currentCell, successor, currentCell.finalCost + D_COST);
                }
            }
        }
    }

    public void display() {
        System.out.println("SO = Source");
        System.out.println("TA = Target");
        System.out.println(" ");
        System.out.println("Field");

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (i == startI && j == startJ)
                    // Source Cell
                    System.out.print(GREEN + "SO " + ANSI_RESET + "|");
                else if (i == endI && j == endJ)
                    // Target Cell
                    System.out.print(BLUE + "TA " + ANSI_RESET + "|");
                else if (field[i][j] != null)
                    System.out.print("0  |");
                else
                    // Block Cell
                    System.out.print(BG_CYAN + "   " + ANSI_RESET + "|");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void displayScores() {
        System.out.println("Cells with costs");

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] != null)
                    System.out.printf("%-3d|", field[i][j].finalCost);
                else
                    System.out.print(BG_CYAN + "   " + ANSI_RESET + "|");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void displaySolution() {
        if (closedList[endI][endJ]) {
            // verfolge Pfad zurück
            System.out.println("Shortest Path");
            Cell current = field[endI][endJ];
            System.out.print(current);
            field[current.i][current.j].solution = true;

            while (current.source != null) {
                System.out.print(" -> " + current.source);
                field[current.source.i][current.source.j].solution = true;
                current = current.source;
            }

            System.out.println("\n");

            for (int i = 0; i < field.length; i++) {
                for (int j = 0; j < field[i].length; j++) {
                    if (i == startI && j == startJ)
                        // Source Cell
                        System.out.print(GREEN + "SO " + ANSI_RESET  + "|");
                    else if (i == endI && j == endJ)
                        // Target Cell
                        System.out.print(BLUE + "TA " + ANSI_RESET  + "|");
                    else if (field[i][j] != null)
                        System.out.print(field[i][j].solution ? GREEN + "X  " + ANSI_RESET + "|" : "0  |");
                    else
                        // Block Cell
                        System.out.print(BG_CYAN + "   " + ANSI_RESET + "|");
                }
                System.out.println();
            }
            System.out.println();

        } else
            System.out.println("Not possible");
    }

    public static void main(String[] args) {
        AStarAlgorithm aStar = new AStarAlgorithm(5, 5, 0, 0, 3, 2,
                new int[][]{
                        {0, 4}, {2, 2}, {3, 1}, {3, 3}, {2, 1}, {2, 3}
                }
        );

        aStar.display();
        aStar.process();
        aStar.displayScores();
        aStar.displaySolution();
    }

}
