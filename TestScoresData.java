import java.io.*;
import java.util.Scanner;
import java.util.*;

public class TestScoresData{
  //maximum number of columns out of mean test scores and covariate data
  public final static int numColumns = 33;

  public static void main (String[] args) throws FileNotFoundException {
    //sets up files and scanners
    File econ = new File("es.csv");
    File scores = new File("ts.csv");
    //File ru = new File("");
    Scanner sc = new Scanner(econ).useDelimiter(",");
    Scanner sc1 = new Scanner(scores).useDelimiter(",");

    calcEconomicStatus(sc, sc1);
  }

  public static void calcEconomicStatus(Scanner sc, Scanner sc1) throws FileNotFoundException{
    //holds the proportion of students eligible for free or reduced price lunch
    ArrayList<String> economicStatus = new ArrayList<String>();
    //holds the school mean test score against national U.S. average of 0.0
    ArrayList<String> testScores = new ArrayList<String>();

    String[] economicLine = new String[numColumns];
    String[] scoresLine = new String[numColumns];

    //reads in data and adds all mean test scores and proportions
    while (sc1.hasNext()){
      //System.out.println(".");
      String currEconLine = sc.nextLine();
      String currScoresLine = sc1.nextLine();
      economicLine = currEconLine.split(",",0);
      scoresLine = currScoresLine.split(",",0);
      int scoresIndex = Arrays.asList(scoresLine).indexOf("cs_mn_avg_ol");
      int esIndex = Arrays.asList(economicLine).indexOf("perfrl");
      //System.out.println(scoresIndex);
      //System.out.println(esIndex);

      //catches gaps in the code
      if (scoresLine.length > 11){
        //System.out.println("This works");
        testScores.add(scoresLine[11]); //need to correctly get the column value here
      }
      if (economicLine.length > 22){
          economicStatus.add(economicLine[22]);
      }
    }
    sc.close();
/*
    for (int i =0; i < testScores.size();i++){
      System.out.println(testScores.get(i));
      System.out.println(economicStatus.get(i));
    }
*/

  //separate into negatives and positives
  ArrayList<String> belowAverageScores = new ArrayList<>();
  ArrayList<String> aboveAverageScores = new ArrayList<>();
  int belowAvCounter = 0;
  int aboveAvCounter = 0;
  double belowES = 0.0;
  double aboveES = 0.0;

  for (int i=1; i < testScores.size();i++){
    if (Double.parseDouble(testScores.get(i)) <0.0){
      //System.out.println(Double.parseDouble(economicStatus.get(i)));
      belowES += Double.parseDouble(economicStatus.get(i));
      belowAvCounter ++;
    }
    else{
      aboveES += Double.parseDouble(economicStatus.get(i));
      aboveAvCounter++;
    }
    testScores.remove(i);
    economicStatus.remove(i);
    i--;
  }

  //System.out.println(belowES + " " + belowAvCounter);
  //System.out.println(aboveES + " " + aboveAvCounter);
  belowES = belowES/belowAvCounter;
  aboveES = aboveES/aboveAvCounter;
  System.out.println("Below: " + belowES);
  System.out.println("Above: " + aboveES);

  }
//count columns method, add 1 for number of commas


}
