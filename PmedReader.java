import java.io.*;
import java.util.*;
import java.io.PrintWriter;

public class PmedReader {
    public static Object[] readInstance(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String[] firstLine = br.readLine().trim().split("\\s+");
        int numVertices = Integer.parseInt(firstLine[0]);
        int numEdges = Integer.parseInt(firstLine[1]);
        int k = Integer.parseInt(firstLine[2]);

        int[][] distances = new int[numVertices][numVertices];
        for (int[] row : distances) {
            Arrays.fill(row, Integer.MAX_VALUE / 2); // Use a large value instead of Integer.MAX_VALUE to avoid overflow
        }
        for (int i = 0; i < numVertices; i++) {
            distances[i][i] = 0;
        }

        for (int i = 0; i < numEdges; i++) {
            String[] line = br.readLine().trim().split("\\s+");
            int u = Integer.parseInt(line[0]) - 1;
            int v = Integer.parseInt(line[1]) - 1;
            int distance = Integer.parseInt(line[2]);
            distances[u][v] = distance;
            distances[v][u] = distance;
        }
        br.close();

        // Apply Floyd-Warshall algorithm
        for (int kTemp = 0; kTemp < numVertices; kTemp++) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    if (distances[i][j] > distances[i][kTemp] + distances[kTemp][j]) {
                        distances[i][j] = distances[i][kTemp] + distances[kTemp][j];
                    }
                }
            }
        }

        return new Object[] { distances, k };
    }

    public static void main(String[] args) throws IOException {
        String directoryPath = "txt_files"; // Directory containing the pmed files
        File dir = new File(directoryPath);
        File[] files = dir.listFiles((d, name) -> name.startsWith("pmed") && name.endsWith(".txt"));
        String outputFile = "resultados.csv";
        PrintWriter writer = new PrintWriter(new FileWriter(outputFile));
        writer.println("nome_arquivo,tempo_exact_ms,tempo_aprox_ms");

        if (files != null) {
            List<String> results = new ArrayList<>();
            int fileCounter = 0;

            for (File file : files) {
                fileCounter++;
                String filename = file.getPath();
                try {
                    Object[] result = readInstance(filename);
                    int[][] distances = (int[][]) result[0];
                    int k = (int) result[1];

                    System.out.println("Processing file: " + filename);
                    System.out.println("k: " + k);

                    // Measure time for ExactKCenter algorithm
                    ExactKCenter exactSolver = new ExactKCenter(distances, k);
                    long startExactTime = System.currentTimeMillis();
                    int[] exactCenters = exactSolver.solve(10000); // Time limit: 5000 milliseconds (5 seconds)
                    long endExactTime = System.currentTimeMillis();
                    long exactDuration = endExactTime - startExactTime;

                    if (exactCenters[0] == -1) {
                        System.out.println("Exact solution did not finish within the time limit.");
                    } else {
                        System.out.println("Exact centers: " + Arrays.toString(exactCenters));
                    }
                    System.out.println("Exact algorithm execution time: " + exactDuration + " milliseconds");

                    // Measure time for ApproximateKCenter algorithm
                    ApproximateKCenter approximateSolver = new ApproximateKCenter(distances, k);
                    long startApproximateTime = System.currentTimeMillis();
                    int[] approximateCenters = approximateSolver.solve();
                    long endApproximateTime = System.currentTimeMillis();
                    long approximateDuration = endApproximateTime - startApproximateTime;

                    /* System.out.println("Approximate centers: " + Arrays.toString(approximateCenters)); */
                    System.out
                            .println("Approximate algorithm execution time: " + approximateDuration + " milliseconds");

                    results.add("File: " + filename + ", Exact Centers: " + Arrays.toString(exactCenters)
                            + ", Approximate Centers: " + Arrays.toString(approximateCenters));

                    writer.println(file.getName() + "," + exactDuration + "," + approximateDuration);

                    // Visualize the graph for every 5th file to reduce memory usage
                    if (fileCounter % 5 == 0) {
                        GraphPanel.visualizeGraph(distances);
                    }

                } catch (Exception e) {
                    System.err.println("Error processing file: " + filename);
                    e.printStackTrace();
                }
            }

            // Print summary of results
            System.out.println("\nSummary of Results:");
            for (String result : results) {
                System.out.println(result);
            }
        }

        writer.close();
        System.out.println("Resultados salvos em " + outputFile);
    }
}