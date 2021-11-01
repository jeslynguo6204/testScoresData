import java.io.*;
import java.util.Scanner;
import java.util.*;

public class TestScoresData{
  public static void main (String[] args) throws FileNotFoundException {
    File f = new File("economicstatus.csv");
    Scanner sc = new Scanner(f).useDelimiter(",");
    ArrayList<String> economicStatus = new ArrayList<>();
    while (sc.hasNextLine()){
      String line = sc.nextLine();
      System.out.println(line.substring(23, 24));
    }
    sc.close();

  }

}
