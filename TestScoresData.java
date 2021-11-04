import java.io.*;
import java.util.Scanner;
import java.util.*;

public class TestScoresData{
  //maximum number of columns out of mean test scores and covariate data
  public static int numColumns = 0;

  public static void main (String[] args) throws FileNotFoundException {
    //sets up files and scanners
    //this file with economic status also contains urban vs. rural data
    File econ = new File("es.csv");
    File scores = new File("ts.csv");
    Scanner sc = new Scanner(econ).useDelimiter(",");
    Scanner sc1 = new Scanner(scores).useDelimiter(",");

    //sets max number of columns, to be the size of the array
    numColumns = countColumns(sc);
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

//counts the number of columns within the file
  public static int countColumns (Scanner sc) throws FileNotFoundException {
    int columns = 0;
    //takes the first line, usually the header, and counts number of commas
    String currentLine = sc.nextLine();
    for (int i=0; i < currentLine.length();i++){
      //identifies all instances of a comma
      if (currentLine.substring(i, i+1).equals(",")){
        columns++;
      }
    }
    //columns are separated by commas - need to add extra at the end
    columns++;
    return columns;
  }
}
