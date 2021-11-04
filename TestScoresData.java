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
    Scanner sc2 = new Scanner(econ).useDelimiter(",");
    Scanner sc3 = new Scanner(scores).useDelimiter(",");
    ruralUrbanTestScores(sc2, sc3);
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
      //identify the index here -- change this into a method
      int scoresIndex = Arrays.asList(scoresLine).indexOf("cs_mn_avg_ol");
      int esIndex = Arrays.asList(economicLine).indexOf("perfrl");
      //System.out.println(scoresIndex);
      //System.out.println(esIndex);

      //catches gaps in the code
      if (scoresLine.length > 11){
        if (economicLine.length > 22){
          testScores.add(scoresLine[11]); //need to correctly get the column value here
          economicStatus.add(economicLine[22]);
        }
        //System.out.println("This works");

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
  System.out.println("Below Average Test Performance, Proportion of Students who Qualify for Free or Reduced Price Lunch " + belowES*100 + "%");
  System.out.println("Above Average Test Performance, Proportion of Students who Qualify for Free or Reduced Price Lunch " + aboveES*100 + "%");
  }

  public static void ruralUrbanTestScores(Scanner sc, Scanner sc1) throws FileNotFoundException {
    ArrayList<String> ruralScores = new ArrayList<String>();
    ArrayList<String> urbanScores = new ArrayList<String>();
    double avgRural = 0.0;
    double avgUrban = 0.0;

    String[] ruralUrbanLine = new String[numColumns];
    String[] scoresLine = new String[numColumns];


    //reads data and determines if a given place is rural or urban
    while (sc1.hasNext()){
      String currRuUrLine = sc.nextLine();
      String currScoresLine = sc1.nextLine();
      ruralUrbanLine = currRuUrLine.split(",",0);
      scoresLine = currScoresLine.split(",",0);

      if (scoresLine.length > 11){
        if (ruralUrbanLine.length > 22){
          if (ruralUrbanLine[12].equals("City")){
            urbanScores.add(scoresLine[11]);
            //System.out.println(scoresLine[11]);
            //System.out.println(ruralUrbanLine[12]);
          }
          else if (ruralUrbanLine[12].equals("Rural")){
            ruralScores.add(scoresLine[11]);
            //System.out.println(ruralUrbanLine[12]);
          }
        }
      }
    }
    //calculates the average test scores for both regions
    for (int i = 0; i < ruralScores.size();i++){
      avgRural += Double.parseDouble(ruralScores.get(i));
    }
    for (int i = 0; i < urbanScores.size();i++){
      avgUrban += Double.parseDouble(urbanScores.get(i));
    }
    avgRural = avgRural/ruralScores.size();
    avgUrban = avgUrban/urbanScores.size();
    System.out.println("Rural Average Performance: " + avgRural);
    System.out.println("Urban Average Performance: " + avgUrban);
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

//public static int identifyColumnIndex (Scanner sc) throws FileNotFoundException
}
