import java.util.*;

public class ExactKCenter {
  private int[][] distances;
  private int numVertices;
  private int k;
  private int minMaxDistance;
  private int[] bestCenters;

  public ExactKCenter(int[][] distances, int k) {
    this.distances = distances;
    this.numVertices = distances.length;
    this.k = k;
    this.minMaxDistance = Integer.MAX_VALUE;
    this.bestCenters = new int[k];
  }

  public int[] solve(long timeLimitMillis) {
    Thread solverThread = new Thread(() -> {
      int[] combination = new int[k];
      generateCombinations(combination);
    });

    solverThread.start();
    try {
      solverThread.join(timeLimitMillis);
      if (solverThread.isAlive()) {
        solverThread.interrupt();
        solverThread.join();
        // Indicate that it did not complete within the time limit
        return new int[] { -1 };
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    return bestCenters;
  }

  private void generateCombinations(int[] combination) {
    int[] indices = new int[k];
    for (int i = 0; i < k; i++) {
      indices[i] = i;
    }

    while (indices[0] <= numVertices - k) {
      if (Thread.currentThread().isInterrupted()) {
        return;
      }

      for (int i = 0; i < k; i++) {
        combination[i] = indices[i];
      }

      int maxDistance = findMaxDistance(combination);
      if (maxDistance < minMaxDistance) {
        minMaxDistance = maxDistance;
        System.arraycopy(combination, 0, bestCenters, 0, k);
      }

      int t = k - 1;
      while (t != 0 && indices[t] == numVertices - k + t) {
        t--;
      }
      indices[t]++;
      for (int i = t + 1; i < k; i++) {
        indices[i] = indices[i - 1] + 1;
      }
    }
  }

  private int findMaxDistance(int[] centers) {
    int maxDistance = 0;
    for (int i = 0; i < numVertices; i++) {
      int minDistance = Integer.MAX_VALUE;
      for (int center : centers) {
        minDistance = Math.min(minDistance, distances[i][center]);
      }
      maxDistance = Math.max(maxDistance, minDistance);
    }
    return maxDistance;
  }

  public static void main(String[] args) {
    // Example usage
    int[][] distances = {
        { 0, 10, 20, 30 },
        { 10, 0, 25, 35 },
        { 20, 25, 0, 15 },
        { 30, 35, 15, 0 }
    };
    int k = 2;
    ExactKCenter solver = new ExactKCenter(distances, k);
    int[] centers = solver.solve(5000); // Time limit: 5000 milliseconds (5 seconds)
    System.out.println(Arrays.toString(centers));
  }
}