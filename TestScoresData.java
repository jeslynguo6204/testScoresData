import java.io.*;
import java.util.Scanner;
import java.util.*;

public class TestScoresData{
  //maximum number of columns out of mean test scores and covariate data
  public static int numColumns = 0;
  //column indeces for percent students qualifying for free/reduced price lunch, average test scores, and urbanicity
  public static int econStatusColumn = 0;
  public static int testScoreColumn = 0;
  public static int ruralUrbanColumn = 0;

  public static void main (String[] args) throws FileNotFoundException {
    //sets up files
    File econ = new File("es.csv"); //this file with economic status also contains urban vs. rural data
    File scores = new File("ts.csv");
    //sets up scanners to analyze economic status and test performance
    Scanner sc = new Scanner(econ).useDelimiter(",");
    Scanner sc1 = new Scanner(scores).useDelimiter(",");

    //sets max number of columns in the files, to be the size of the array
    Scanner sc2 = new Scanner(econ);
    numColumns = countColumns(sc2);

    //sets up new scanenrs to analyze rural vs. urban test performance
    Scanner sc3 = new Scanner(econ).useDelimiter(",");
    Scanner sc4 = new Scanner(scores).useDelimiter(",");

    //scanners to be used in identify column indeces for three subcategories
    Scanner sc5 = new Scanner(econ);
    Scanner sc6 = new Scanner(scores);
    Scanner sc7 = new Scanner(econ);
    econStatusColumn = identifyColumnIndex(sc5, "perfrl");
    testScoreColumn = identifyColumnIndex(sc6, "cs_mn_avg_ol");
    ruralUrbanColumn = identifyColumnIndex(sc7, "urbanicity");

    calcEconomicStatus(sc, sc1);
    ruralUrbanTestScores(sc3, sc4);
  }

  public static void calcEconomicStatus(Scanner sc, Scanner sc1) throws FileNotFoundException{
    //holds the proportion of students eligible for free or reduced price lunch
    ArrayList<String> economicStatus = new ArrayList<String>();
    //holds the school mean test score against national U.S. average of 0.0
    ArrayList<String> testScores = new ArrayList<String>(); //parallel array to economicStatus

    //array to be updated based on each line, holds data separated between commas in indeces
    String[] economicLine = new String[numColumns];
    String[] scoresLine = new String[numColumns];

    //reads in data and adds all mean test scores and proportions to arraylists
    while (sc1.hasNext()){
      String currEconLine = sc.nextLine();
      String currScoresLine = sc1.nextLine();
      //splits current line values into an array
      economicLine = currEconLine.split(",",0);
      scoresLine = currScoresLine.split(",",0);
      //identify the index here -- change this into a method
      //int scoresIndex = Arrays.asList(scoresLine).indexOf("cs_mn_avg_ol");
      //int esIndex = Arrays.asList(economicLine).indexOf("perfrl");

      //ensures no blank data is taken or errors thrown
      if (scoresLine.length > testScoreColumn){
        if (economicLine.length > econStatusColumn){
          testScores.add(scoresLine[testScoreColumn]);
          economicStatus.add(economicLine[econStatusColumn]);
        }
      }
    }
  sc.close();
  //counter variables to get average economic status
  int belowAvCounter = 0;
  int aboveAvCounter = 0;
  double belowES = 0.0;
  double aboveES = 0.0;

  for (int i=1; i < testScores.size();i++){
    //negative performance against national mean
    if (Double.parseDouble(testScores.get(i)) <0.0){
      belowES += Double.parseDouble(economicStatus.get(i));
      belowAvCounter++;
    }
    else{
      //positive or exactly average performance against national mean
      aboveES += Double.parseDouble(economicStatus.get(i));
      aboveAvCounter++;
    }
    testScores.remove(i);
    economicStatus.remove(i);
    i--;
  }

  belowES = belowES/belowAvCounter;
  aboveES = aboveES/aboveAvCounter;
  System.out.println("Below Average Test Performance, Proportion of Students who Qualify for Free or Reduced Price Lunch " + belowES*100 + "%");
  System.out.println("Above Average Test Performance, Proportion of Students who Qualify for Free or Reduced Price Lunch " + aboveES*100 + "%");
  }

  public static void ruralUrbanTestScores(Scanner sc, Scanner sc1) throws FileNotFoundException {
    //sets up arraylists to average score performance for urban and rural schools
    ArrayList<String> ruralScores = new ArrayList<String>();
    ArrayList<String> urbanScores = new ArrayList<String>();
    double avgRural = 0.0;
    double avgUrban = 0.0;

    //array to be updated based on each line, holds data separated between commas in indeces
    String[] ruralUrbanLine = new String[numColumns];
    String[] scoresLine = new String[numColumns];

    //reads data and determines if a given place is rural or urban
    while (sc1.hasNext()){
      String currRuUrLine = sc.nextLine();
      String currScoresLine = sc1.nextLine();
      ruralUrbanLine = currRuUrLine.split(",",0);
      scoresLine = currScoresLine.split(",",0);

      if (scoresLine.length > testScoreColumn){
        if (ruralUrbanLine.length > ruralUrbanColumn){
          if (ruralUrbanLine[ruralUrbanColumn].equals("City")){
            urbanScores.add(scoresLine[testScoreColumn]);
          }
          else if (ruralUrbanLine[ruralUrbanColumn].equals("Rural")){
            ruralScores.add(scoresLine[testScoreColumn]);
          }
        }
      }
    }
    sc.close();

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

  //identifies the index of the column of a subcategory
  public static int identifyColumnIndex (Scanner sc, String identifier) throws FileNotFoundException {
    String line = sc.nextLine();
    String[] currentLine = new String[numColumns];
    currentLine = line.split(",",0);
    for (int i = 0; i < currentLine.length;i++){
      //gets the index of the column with identifier as label/header
      if (currentLine[i].equals(identifier)){
        return i;
      }
    }
    sc.close();
    //identifier not found in file
    return -1;
  }
}
