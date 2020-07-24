import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


public class main {

    static double adjacency[][];
    static List<vertex> graph;
    static vertex[] graphArr;

    public static void main(String[] args) throws IOException {
        File output = new File("output.txt");
        PrintWriter fw = new PrintWriter(output);
        graph = new LinkedList<>();
        Scanner pathSc = new Scanner(System.in);
        System.out.println("Enter input path");
        String path = pathSc.nextLine();
        File inputFile = new File(path);
        Scanner sc = new Scanner(inputFile);
        String lineStr;

        while (sc.hasNextLine()) {
            lineStr = sc.nextLine();
            lineStr = lineStr.trim();
            String line[] = lineStr.split(" +");
            graph.add((new vertex(Integer.parseInt(line[0]), Integer.parseInt(line[1]), Integer.parseInt(line[2]))));
        }

        graphArr = new vertex[graph.size()];
        graph.toArray(graphArr);
        int len = graphArr.length;
        vertex[] bestSolutionArr = new vertex[len+1];

        if(len>=20000){
            List<vertex>temp;
            int bestTour;
            int k=0;
                temp = nearestNeighbour(graphArr[k]);
                temp.toArray(bestSolutionArr);
                    bestTour =   pathCostCalculateArr(bestSolutionArr);
                    System.out.println(bestTour);
            bestSolutionArr=TwoOpt(bestSolutionArr);
            bestTour = pathCostCalculateArr(bestSolutionArr);
            fw.println((int) bestTour);
            for (int i = 0; i < bestSolutionArr.length - 1 ; i++) {
                fw.println(bestSolutionArr[i].getIndex());
                fw.flush();
            }
            fw.close();
            System.exit(1);
        }

        createAdjacency();
        int bestTour = Integer.MAX_VALUE;
        List<vertex> temp;

        if(len<5000 && len>=2000){
            vertex[] tempArr = new vertex[len+1];
            for (int i = 0; i < len; i++) {
                temp = nearestNeighbour(graphArr[i]);
                temp.toArray(tempArr);
                int tempCost = pathCostCalculateArr(tempArr);
                if (tempCost < bestTour) {
                    bestTour = tempCost;
                    bestSolutionArr = tempArr;
                    System.out.println(bestTour);
                }
            }
            System.out.println(bestTour);
            bestSolutionArr = TwoOpt(bestSolutionArr);
            bestTour = pathCostCalculateArr(bestSolutionArr);}
        if(len<2000){
            vertex[] tempArr = new vertex[len+1];
            vertex[] tempArr2 = new vertex[len+1];
            for (int i = 0; i < len; i++) {
                temp = nearestNeighbour(graphArr[i]);
                temp.toArray(tempArr);
                tempArr2=TwoOpt(tempArr);
                int tempCost = pathCostCalculateArr(tempArr2);
                if (tempCost < bestTour) {
                    bestTour = tempCost;
                    bestSolutionArr = tempArr2;
                    System.out.println(bestTour);}
            }
        }
        else {
            temp=nearestNeighbour(graphArr[0]);

            temp.toArray(bestSolutionArr);
            bestSolutionArr = TwoOpt(bestSolutionArr);
            bestTour = pathCostCalculateArr(bestSolutionArr);
        }

        fw.println( bestTour);
        for (int i = 0; i < bestSolutionArr.length - 1 ; i++) {
            fw.println(bestSolutionArr[i].getIndex());
            fw.flush();
        }
        fw.close();
    }

    public static void createAdjacency() {
        int len = graphArr.length;
        adjacency = new double[len][len];
        for (int i = 0; i < len; i++) {
            for (int j = i; j < len; j++) {
                if (i == j) {
                    adjacency[i][j] = Double.MAX_VALUE;
                    adjacency[j][i] = Double.MAX_VALUE;
                } else
                    adjacency[i][j] = getDistance(graphArr[i], graphArr[j]);
                adjacency[j][i] = getDistance(graphArr[i], graphArr[j]);
            }
        }
    }

    public static vertex[] TwoOpt(vertex[] node) {
        vertex[] temp;
        double bestSolution = pathCostCalculateArr(node);
        double bestTour=0;
        boolean swap = true;
        int len = node.length;
        while (swap) {
            swap = false;
            for (int i = 1; i < len-2; i++) {
                for (int j = i + 1; j < len - 1; j++) {
                    double initial = getDistance(node[i], (node[i-1])) + getDistance(node[j+1], (node[j]));
                    double swapped = getDistance(node[i], (node[j+1])) + getDistance(node[i-1], (node[j]));
                    if (initial >= swapped) {
                        temp = swap(node, i, j);
                        bestTour = bestTour+swapped-initial;
                        if (bestTour < bestSolution) {
                            node = temp;
                            bestSolution = bestTour;
                            swap = true;
                        }
                    }
                }
            }
        }
        return node;
    }

    private static vertex[] swap(vertex[] node, int i, int j) {
        int size=node.length;
        vertex temp[]= new vertex[size];
        for (int k = 0; k <= i - 1; k++)
            temp[k]=node[k];

        int l = 0;
        for (int k = i; k <= j; k++) {
            temp[k]=node[j-l];
            l++;
        }
        for (int k = j + 1; k < size; k++)
            temp[k]= node[k];
        return temp;
    }

    public static List<vertex> nearestNeighbour(vertex start) {
        List<vertex> temp = new LinkedList<>();
        start.setVisited(true);
        temp.add(start);
        int len = graphArr.length;
        while (temp.size() != len) {
            vertex tempV = nearest(temp.get(temp.size() - 1));
            temp.add(tempV);
            graphArr[(graph.indexOf(tempV))].setVisited(true);
        }
        temp.add(start);
        for (int i = 0; i < len; i++)
            graphArr[i].setVisited(false);
        return temp;
    }

    public static vertex nearest(vertex node) {
        int min = Integer.MAX_VALUE;
        vertex minVertex = node;
        int index = graph.indexOf(node);
        int len = graphArr.length;
        for (int i = 0; i < len; i++) {
            if(len<30000){
                if (!graphArr[i].isVisited() && (int) adjacency[index][i] < min) {
                    min = (int) adjacency[index][i];
                    minVertex = graphArr[i];
                }
            }
            else{
                int distance =getDistance(graphArr[index],graphArr[i]);
                if (!graphArr[i].isVisited() && distance < min) {
                    min =  distance;
                    minVertex = graphArr[i];
                }
            }
        }
        return minVertex;
    }
    public static int pathCostCalculateArr(vertex[] x) {
        int result = 0;
        int len = x.length;
        for (int i = 0; i < len-1; i++)
            result += getDistance(x[i], x[i+1]);
        return result;
    }
    public static int getDistance(vertex a, vertex b) {
        return (int)Math.round(Math.sqrt(Math.abs(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2))));
    }
}
