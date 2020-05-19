import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.paint.*;
import javafx.geometry.*;

/*
 * Gomoku
 * @author Vivek Kapur
 */

public class Gomoku extends Application{
  //field that holds the array
  private GridInfo[][] arr;
  //fields that hold values
  private static int rows;
  private static int columns;
  private static int numberToWin;
  private static int rowPlace;
  private static int columnPlace;
  //field that represents if it is blacks turn
  private boolean player1 = true;
  //creates a new gridpane
  private GridPane gridPane = new GridPane();
  //fields that stores backgrounds
  private Background black = new Background(new BackgroundFill(Color.BLACK, new CornerRadii(100, true), new Insets(1))); 
  private Background white = new Background(new BackgroundFill(Color.WHITE, new CornerRadii(100, true), new Insets(1)));
  private Background empty = new Background(new BackgroundFill(Color.GREEN, null, new Insets(1)));
  
  //class that stores all of the buttons 
  public class GridInfo extends Button{
    //field that stores row number
    private int rowNumber;
    //field that stores column number
    private int columnNumber;
    /**
     * GridInfo constructor
     * @param rowNumber number of rows
     * @param columnNumber number of columns
     */
    public GridInfo(int rowNumber, int columnNumber){
      this.rowNumber = rowNumber;
      this.columnNumber = columnNumber;
    }
    
    /**
     * method that gets row number
     * @return rowNumber the current row number
     */
    public int getRowNumber(){
      return rowNumber;
    }
    
    /**
     * method that gets column number
     * @return columnNumber the current column number
     */
    public int getColumnNumber(){
      return columnNumber;
    }
  }
   
    /**
     * method that makes the board and sets the win condition
     * @param primaryStage the primary stage of the GUI
     */
  public void start(Stage primaryStage){
    if(getParameters().getRaw().size() == 0){
      numberToWin = 5;
      rows = 19;
      columns = 19;
    }
    if(getParameters().getRaw().size() == 1){
      numberToWin = Integer.parseInt(getParameters().getRaw().get(0));
      rows = 19;
      columns = 19;
    }
    if(getParameters().getRaw().size() == 2){
      numberToWin = 5;
      rows = Integer.parseInt(getParameters().getRaw().get(0));
      columns = Integer.parseInt(getParameters().getRaw().get(1));
    }
    if(getParameters().getRaw().size() == 3){
      numberToWin = Integer.parseInt(getParameters().getRaw().get(0));
      rows = Integer.parseInt(getParameters().getRaw().get(1));
      columns = Integer.parseInt(getParameters().getRaw().get(2));
    }
    
    arr = new GridInfo[rows][columns];
    //creates a border pane
    BorderPane pane = new BorderPane();
    //sets background of gridpane
    gridPane.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
    gridPane.setGridLinesVisible(true);
    //creates a scene with the pane
    Scene scene = new Scene(pane);
    //creates a double array of appropriate dimentions
    for(int i = 0; i < arr.length; i++){
      for(int j = 0; j < arr[0].length; j++){
        arr[i][j] = new GridInfo(i, j);
        arr[i][j].setBackground(empty);
        arr[i][j].setPrefSize(50, 50);
        gridPane.add(arr[i][j], j, i);
        arr[i][j].setOnAction(new ButtonAction());
      }
    }
    pane.setCenter(gridPane);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
  
  //class that handles button presses
  public class ButtonAction implements EventHandler<ActionEvent>{  
    /**
     * method that handles a button being clicked
     * @param e an ActionEvent for the button pressed
     */
    public void handle(ActionEvent e){
      GridInfo b = (GridInfo)e.getSource();
      //if victory has not been achieved
      if(!victory(arr, b.getRowNumber(), b.getColumnNumber())){
        //if there is no four-four violation
        if(!fourFour(arr, b.getRowNumber(), b.getColumnNumber())){
          //if there is no three-three violation
          if(!threeThree(arr, b.getRowNumber(), b.getColumnNumber())){
            //if it is player one's turn make the button black
            if(player1)
              b.setBackground(black);
            //otherwise make it white
            else
              b.setBackground(white);
            b.setDisable(true);
            b.setOpacity(1);
            //switch turns
            player1 = !player1;
          }
          else
            System.out.println("You cannot place that here!");
        }
        else
          System.out.println("You cannot place that here!");
      }
      else{
        if(player1)
          b.setBackground(black);
        else
          b.setBackground(white);
        for(int i = 0; i < arr.length; i++){
          for(int j = 0; j < arr[0].length; j++){
          arr[i][j].setDisable(true);
          }
        }
        if(player1)
          System.out.println("Black Wins!");
        else
          System.out.println("White Wins!");
      }
    }
  }
  
  /**
   * method that returns the number of pieces of the same color in a row in a certain alignment
   * @param arr array of the buttons
   * @param rowPlace current row
   * @param columnPlace current column
   * @param alignment alignment to check 
   * @return number of pieces of the same color in a row
   */
  public int numberInLine(GridInfo[][] arr, int rowPlace, int columnPlace, String alignment){
    //initiates the number in a row at one and stores how many in a row
    int numberInARow = 1;
    //stores if the location has the same piece as defined by the turn
    boolean isSamePiece1 = true;
    boolean isSamePiece2 = true;
    if(alignment == "vertical"){
      //checks all locations above
      for(int i = rowPlace - 1; i >= 0 && isSamePiece1; i--){
        if(player1 && arr[i][columnPlace].getBackground() == black)
          numberInARow += 1;
        else if(!player1 && arr[i][columnPlace].getBackground() == white)
          numberInARow += 1;
        else
          isSamePiece1 = false;
      }
      //checks all locations below
      for(int i = rowPlace + 1; i < arr.length && isSamePiece2; i++){
        if(player1 && arr[i][columnPlace].getBackground() == black)
          numberInARow += 1;
        else if(!player1 && arr[i][columnPlace].getBackground() == white)
          numberInARow += 1;
        else
          isSamePiece2 = false;
      }
    }
    if(alignment == "horizontal"){
      //checks all locations left
      for(int j = columnPlace - 1; j >= 0 && isSamePiece1; j--){
        if(player1 && arr[rowPlace][j].getBackground() == black)
          numberInARow += 1;
        else if(!player1 && arr[rowPlace][j].getBackground() == white)
          numberInARow += 1;
        else
          isSamePiece1 = false;
      }
      //checks all locations right
      for(int j = columnPlace + 1; j < arr[0].length && isSamePiece2; j++){
        if(player1 && arr[rowPlace][j].getBackground() == black)
          numberInARow += 1;
        else if(!player1 && arr[rowPlace][j].getBackground() == white)
          numberInARow += 1;
        else
          isSamePiece2 = false;
      }
    }
    if(alignment == "diagonal left"){
      //checks all locations above and left
      for(int i = rowPlace - 1, j = columnPlace - 1; i >= 0 && j >=0 && isSamePiece1; i--, j--){
        if(player1 && arr[i][j].getBackground() == black)
          numberInARow += 1;
        else if(!player1 && arr[i][j].getBackground() == white)
          numberInARow += 1;
        else
          isSamePiece1 = false;
      }
      //checks all locations below and right
      for(int i = rowPlace + 1, j = columnPlace + 1; i < arr.length && j < arr[0].length && isSamePiece2; i++, j++){
        if(player1 && arr[i][j].getBackground() == black)
          numberInARow += 1;
        else if(!player1 && arr[i][j].getBackground() == white)
          numberInARow += 1;
        else
          isSamePiece2 = false;
      }
    }
    if(alignment == "diagonal right"){
       //checks all locations above and right
      for(int i = rowPlace - 1, j = columnPlace + 1; i >= 0 && j < arr[0].length && isSamePiece1; i--, j++){
        if(player1 && arr[i][j].getBackground() == black)
          numberInARow += 1;
        else if(!player1 && arr[i][j].getBackground() == white)
          numberInARow += 1;
        else
          isSamePiece1 = false;
      }
       //checks all locations below and left
      for(int i = rowPlace + 1, j = columnPlace - 1; i < arr.length && j >= 0 && isSamePiece2; i++, j--){
        if(player1 && arr[i][j].getBackground() == black)
          numberInARow += 1;
        else if(!player1 && arr[i][j].getBackground() == white)
          numberInARow += 1;
        else
          isSamePiece2 = false;
      }
    }
    return numberInARow;
  }
  
  /**
   * method that returns whether both ends of a row of pieces in a certain alignment are open
   * @param arr array of the buttons
   * @param rowPlace current row
   * @param columnPlace current column
   * @param alignment alignment to check 
   * @return if both ends of a row of pieces are open
   */
  public boolean isEmpty(GridInfo[][] arr, int rowPlace, int columnPlace, String alignment){
    //stores if the ends of the row are empty or not
    boolean isEmpty1 = false;
    boolean isEmpty2 = false;
    if(alignment == "vertical"){
       //checks all locations above
      for(int i = rowPlace - 1; i >= 0 && !isEmpty1; i--){
        if(!player1 && arr[i][columnPlace].getBackground() == white)
          isEmpty1 = false;
        else if(player1 && arr[i][columnPlace].getBackground() == black)
          isEmpty1 = false;
        else if(player1 && arr[i][columnPlace].getBackground() == white)
          return false;
        else if(!player1 && arr[i][columnPlace].getBackground() == black)
          return false;
        else if(arr[i][columnPlace].getBackground() == empty)
          isEmpty1 = true;
      }
       //checks all locations below
      for(int i = rowPlace + 1; i < arr.length && !isEmpty2; i++){
        if(!player1 && arr[i][columnPlace].getBackground() == white)
          isEmpty2 = false;
        else if(player1 && arr[i][columnPlace].getBackground() == black)
          isEmpty2 = false;
        else if(player1 && arr[i][columnPlace].getBackground() == white)
          return false;
        else if(!player1 && arr[i][columnPlace].getBackground() == black)
          return false;
        else if(arr[i][columnPlace].getBackground() == empty)
          isEmpty2 = true;
      }
    }
    if(alignment == "horizontal"){
       //checks all locations left
      for(int j = columnPlace - 1; j >= 0 && !isEmpty1; j--){
        if(!player1 && arr[rowPlace][j].getBackground() == white)
          isEmpty1 = false;
        else if(player1 && arr[rowPlace][j].getBackground() == black)
          isEmpty1 = false;
         else if(player1 && arr[rowPlace][j].getBackground() == white)
          return false;
        else if(!player1 && arr[rowPlace][j].getBackground() == black)
          return false;
        else if(arr[rowPlace][j].getBackground() == empty)
          isEmpty1 = true;
      }
       //checks all locations right
      for(int j = columnPlace + 1; j < arr[0].length && !isEmpty2; j++){
        if(!player1 && arr[rowPlace][j].getBackground() == white)
          isEmpty2 = false;
        else if(player1 && arr[rowPlace][j].getBackground() == black)
          isEmpty2 = false;
        else if(player1 && arr[rowPlace][j].getBackground() == white)
          return false;
        else if(!player1 && arr[rowPlace][j].getBackground() == black)
          return false;
        else if(arr[rowPlace][j].getBackground() == empty)
          isEmpty2 = true;
      }
    }
    if(alignment == "diagonal left"){
       //checks all locations above and left
      for(int i = rowPlace - 1, j = columnPlace - 1; i >= 0 && j >=0 && !isEmpty1; i--, j--){
        if(!player1 && arr[i][j].getBackground() == white)
          isEmpty1 = false;
        else if(player1 && arr[i][j].getBackground() == black)
          isEmpty1 = false;
        else if(player1 && arr[i][j].getBackground() == white)
          return false;
        else if(!player1 && arr[i][j].getBackground() == black)
          return false;
        else if(arr[i][j].getBackground() == empty)
          isEmpty1 = true;
      }
       //checks all locations below and right
      for(int i = rowPlace + 1, j = columnPlace + 1; i < arr.length && j < arr[0].length && !isEmpty2; i++, j++){
        if(!player1 && arr[i][j].getBackground() == white)
          isEmpty2= false;
        else if(player1 && arr[i][j].getBackground() == black)
          isEmpty2 = false;
        else if(player1 && arr[i][j].getBackground() == white)
          return false;
        else if(!player1 && arr[i][j].getBackground() == black)
          return false;
        else if(arr[i][j].getBackground() == empty)
          isEmpty2 = true;
      }
    }
    if(alignment == "diagonal right"){
       //checks all locations above and right
      for(int i = rowPlace - 1, j = columnPlace + 1; i >= 0 && j < arr[0].length && !isEmpty1; i--, j++){
        if(!player1 && arr[i][j].getBackground() == white)
          isEmpty1 = false;
        else if(player1 && arr[i][j].getBackground() == black)
          isEmpty1 = false;
        else if(player1 && arr[i][j].getBackground() == white)
          return false;
        else if(!player1 && arr[i][j].getBackground() == black)
          return false;
        else if(arr[i][j].getBackground() == empty)
          isEmpty1 = true;
      }
       //checks all locations below and left
      for(int i = rowPlace + 1, j = columnPlace - 1; i < arr.length && j >= 0 && !isEmpty2; i++, j--){
        if(!player1 && arr[i][j].getBackground() == white)
          isEmpty2 = false;
        else if(player1 && arr[i][j].getBackground() == black)
          isEmpty2 = false;
        else if(player1 && arr[i][j].getBackground() == white)
          return false;
        else if(!player1 && arr[i][j].getBackground() == black)
          return false;
        else if(arr[i][j].getBackground() == empty)
          isEmpty2 = true;
      }
     }
     return (isEmpty1 && isEmpty2);
  }
  
  /**
   * method that checks if there is a four-four violation
   * @param arr array of the buttons
   * @param rowPlace current row
   * @param columnPlace current column
   * @return if the piece placement is illegal
   */
  public boolean fourFour(GridInfo[][] arr, int rowPlace, int columnPlace){
    //counts how many groups of four in a line exist 
    int c = 0;
    //stores whether or not there are two or more groups of four
    boolean twoOrMoreGroups = false;
    if(numberInLine(arr, rowPlace, columnPlace, "vertical") == numberToWin - 1){
      c++;
    }
    if(numberInLine(arr, rowPlace, columnPlace, "horizontal") == numberToWin - 1){
      c++;
    }
    if(numberInLine(arr, rowPlace, columnPlace, "diagonal left") == numberToWin - 1){
      c++;
    }
    if(numberInLine(arr, rowPlace, columnPlace, "diagonal right") == numberToWin - 1){
      c++;
    }
    if(c >= 2)
      twoOrMoreGroups = true;
    return twoOrMoreGroups;
  }
    /**
     * method that checks if there is a three-three violation
     * @param arr array of the buttons
     * @param rowPlace current row
     * @param columnPlace current column
     * @return if the piece placement is illegal
     */
   public boolean threeThree(GridInfo[][] arr, int rowPlace, int columnPlace){
    //counts how many groups of four in a line exist 
    int c = 0;
    //stores whether or not there are two or more groups of four
    boolean twoOrMoreGroups = false;
    if(numberInLine(arr, rowPlace, columnPlace, "vertical") == numberToWin - 2){
      if(isEmpty(arr, rowPlace, columnPlace, "vertical"))
        c++;
    }
    if(numberInLine(arr, rowPlace, columnPlace, "horizontal") == numberToWin - 2){
       if(isEmpty(arr, rowPlace, columnPlace, "horizontal"))
         c++;
    }
    if(numberInLine(arr, rowPlace, columnPlace, "diagonal left") == numberToWin - 2){
       if(isEmpty(arr, rowPlace, columnPlace, "diagonal left"))
         c++;
    }
    if(numberInLine(arr, rowPlace, columnPlace, "diagonal right") == numberToWin - 2){
      if(isEmpty(arr, rowPlace, columnPlace, "diagonal right"))
        c++;
    }
    if(c >= 2)
      twoOrMoreGroups = true;
    return twoOrMoreGroups;
  }
   
     /**
     * method that checks if a player has one the game
     * @param arr array of the buttons
     * @param rowPlace current row
     * @param columnPlace current column
     * @return if the piece placement results in victory for a player
     */
   public boolean victory(GridInfo[][] arr, int rowPlace, int columnPlace){
     if(numberInLine(arr, rowPlace, columnPlace, "vertical") == numberToWin){
       return true;
     }
     if(numberInLine(arr, rowPlace, columnPlace, "horizontal") == numberToWin){
       return true;
     }
     if(numberInLine(arr, rowPlace, columnPlace, "diagonal left") == numberToWin){
       return true;
     }
     if(numberInLine(arr, rowPlace, columnPlace, "diagonal right") == numberToWin){
       return true;
     }
     return false;
   }
   /**
    * this is the main method
    * @param args string array of argumnets
    */
   public static void main(String[] args){
     Application.launch(args);
   }
}