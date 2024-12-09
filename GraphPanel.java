import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GraphPanel extends JPanel {
  private int[][] distances;
  private int numVertices;

  public GraphPanel(int[][] distances) {
    this.distances = distances;
    this.numVertices = distances.length;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    int radius = 200;
    int centerX = getWidth() / 2;
    int centerY = getHeight() / 2;
    int vertexRadius = 20;

    Point[] points = new Point[numVertices];
    for (int i = 0; i < numVertices; i++) {
      double angle = 2 * Math.PI * i / numVertices;
      int x = (int) (centerX + radius * Math.cos(angle));
      int y = (int) (centerY + radius * Math.sin(angle));
      points[i] = new Point(x, y);
      g.fillOval(x - vertexRadius / 2, y - vertexRadius / 2, vertexRadius, vertexRadius);
      g.drawString(String.valueOf(i + 1), x - vertexRadius / 2, y - vertexRadius / 2);
    }

    g.setColor(Color.BLACK);
    for (int i = 0; i < numVertices; i++) {
      for (int j = i + 1; j < numVertices; j++) {
        if (distances[i][j] < Integer.MAX_VALUE / 2) {
          g.drawLine(points[i].x, points[i].y, points[j].x, points[j].y);
        }
      }
    }
  }

  public static void visualizeGraph(int[][] distances) {
    SwingUtilities.invokeLater(() -> {
      JFrame frame = new JFrame("Graph Visualization");
      GraphPanel panel = new GraphPanel(distances);
      frame.add(panel);
      frame.setSize(800, 800);
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.setVisible(true);
    });
  }
}
