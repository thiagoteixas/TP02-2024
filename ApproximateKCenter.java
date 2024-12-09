import java.util.*;

public class ApproximateKCenter {
  private int[][] distances;
  private int numVertices;
  private int k;

  public ApproximateKCenter(int[][] distances, int k) {
    this.distances = distances;
    this.numVertices = distances.length;
    this.k = k;
  }

  public int[] solve() {
    int[] centers = new int[k];
    boolean[] isCenter = new boolean[numVertices];
    Arrays.fill(centers, -1);

    int firstCenter = 0;
    centers[0] = firstCenter;
    isCenter[firstCenter] = true;

    for (int i = 1; i < k; i++) {
      int nextCenter = -1;
      int maxDistance = -1;
      for (int j = 0; j < numVertices; j++) {
        if (!isCenter[j]) {
          int minDistance = Integer.MAX_VALUE;
          for (int center : centers) {
            if (center != -1) {
              minDistance = Math.min(minDistance, distances[j][center]);
            }
          }
          if (minDistance > maxDistance) {
            maxDistance = minDistance;
            nextCenter = j;
          }
        }
      }
      centers[i] = nextCenter;
      isCenter[nextCenter] = true;
    }
    return centers;
  }
}
