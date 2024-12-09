import java.util.*;

public class Grafo {
  private int V;
  private double[][] dist;

  public Grafo(int V) {
    this.V = V;
    this.dist = new double[V][V];
  }

  public void addAresta(int u, int v, double custo) {
    dist[u][v] = custo;
    dist[v][u] = custo;
  }

  public double getDist(int u, int v) {
    return dist[u][v];
  }

  public int getNumVertices() {
    return V;
  }

  // Exact method to solve the k-center problem
  public Set<Integer> kCentrosExato(int k) {
    Set<Integer> centros = new HashSet<>();
    List<Integer> vertices = new ArrayList<>();
    for (int i = 0; i < V; i++) {
      vertices.add(i);
    }
    double minRaio = Double.MAX_VALUE;

    for (Set<Integer> subset : combinations(vertices, k)) {
      double maxDist = 0;
      for (int i = 0; i < V; i++) {
        double minDist = Double.MAX_VALUE;
        for (int centro : subset) {
          minDist = Math.min(minDist, dist[i][centro]);
        }
        maxDist = Math.max(maxDist, minDist);
      }
      if (maxDist < minRaio) {
        minRaio = maxDist;
        centros = subset;
      }
    }
    return centros;
  }

  // Function to generate all combinations of k elements from a list
  private <T> Set<Set<T>> combinations(List<T> list, int k) {
    Set<Set<T>> result = new HashSet<>();
    combinationsHelper(list, k, 0, new HashSet<T>(), result);
    return result;
  }

  private <T> void combinationsHelper(List<T> list, int k, int start, Set<T> current, Set<Set<T>> result) {
    if (current.size() == k) {
      result.add(new HashSet<>(current));
      return;
    }
    for (int i = start; i < list.size(); i++) {
      current.add(list.get(i));
      combinationsHelper(list, k, i + 1, current, result);
      current.remove(list.get(i));
    }
  }

  public Set<Integer> kCentrosAproximado(int k) {
    Set<Integer> centros = new HashSet<>();
    boolean[] isCentro = new boolean[V];
    int primeiroCentro = 0;
    centros.add(primeiroCentro);
    isCentro[primeiroCentro] = true;

    while (centros.size() < k) {
      double maxDist = 0;
      int novoCentro = -1;
      for (int i = 0; i < V; i++) {
        if (!isCentro[i]) {
          double minDist = Double.MAX_VALUE;
          for (int centro : centros) {
            minDist = Math.min(minDist, dist[i][centro]);
          }
          if (minDist > maxDist) {
            maxDist = minDist;
            novoCentro = i;
          }
        }
      }
      centros.add(novoCentro);
      isCentro[novoCentro] = true;
    }
    return centros;
  }

}