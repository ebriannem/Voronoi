import java.util.ArrayList;
import java.util.Random;
import controller.VoronoiController;
import view.VoronoiView;
import voronoidiagram.Point;
import voronoidiagram.Voronoi;


public class Main {
  public static void main(String[] args) {
    int numArgs = args.length;
    int argNums[] = new int[3];
    //Defaults
    int width = 1500;
    int height = 1000;
    int numRegions = 1000;
    //Parse inputs
    for(int i = 0; i < Math.min(3, numArgs); i++) {
      if(!args[i].matches("\\d+")){
        invalidCmdArgs();
        return;
      }
      argNums[i] = Integer.parseInt(args[i]);
    }
    switch(numArgs) {
      case(3):
        numRegions = argNums[0];
        width = argNums[1];
        height = argNums[2];
        break;
      case(2):
        width = argNums[0];
        height = argNums[1];
        numRegions = Math.min(width, height);
        break;
      case(1):
        numRegions = argNums[0];
        break;
      case(0):
        break;
      default:
        invalidCmdArgs();
        return;
    }

    //Generate
    final Random RAND = new Random();
    ArrayList<Point> sites = new ArrayList<>(numRegions);
    for (int i = 0; i < numRegions; i++) {
      double x = RAND.nextDouble() * width;
      double y = RAND.nextDouble() * height;
      sites.add(new Point(x, y));
    }
    Voronoi voronoi = new Voronoi(width, height, sites);
    Runnable r = new VoronoiController(new VoronoiView(voronoi), voronoi);
    r.run();
  }

  private static void invalidCmdArgs() {
    System.out.println("Usage:\n" +
            "\t NumRegions \n" +
            "\t Width Height \n" +
            "\t NumRegions Width Height \n");
  }
}
