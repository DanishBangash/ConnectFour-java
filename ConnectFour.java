/**
 * @authors:Danish Bangash
 * date:20.10.2011
 */
public class ConnectFour
{
 private int player;
 private int[][] grid;
     /**
     * Constructor for objects of class ConnectFour
     * We create ConnectFour class and initialize the grid four game
     * 
     */
    public ConnectFour()  // constructor 
    {
        // we create the grid
       grid=new int[7][6];  
    for (int row=0; row<6; row++) {
        for (int column=0; column<7; column++) {
         grid[column][row] = 0;
    } 
    }
    /**
     * the first current player who drop the disk
     */
    player=1;
}
  
public int drop(int column){
    if(CheckGame()){
     return -1;
    }
    else{
     int row=0;
     for( ;row<6 && grid[column][row]!=0; row++){  };
     if(row==6){
       return -1;
      }
      else{
       grid[column][row]=player;
       player=(player%2)+1;
       return row;
     }
     }
}
 
  public boolean CheckGame(){
   boolean condition=false;
    //we check for horizontal win
    for (int row=0; row<6; row++) {
        for (int column=0; column<4; column++) {
    if (grid[column][row] != 0 && 
    grid[column][row] == grid[column+1][row] && 
    grid[column][row] == grid[column+2][row] && 
    grid[column][row] == grid[column+3][row]) {
     condition = true;
    }   
   }                                     
   }
  //we check for vertical win
  for (int row=0; row<3; row++) {
    for (int column=0; column<7; column++) {
     if (grid[column][row] != 0 && 
     grid[column][row] == grid[column][row+1] && 
     grid[column][row] == grid[column][row+2] && 
     grid[column][row] == grid[column][row+3]){
      condition = true;
    }
   }
   }
  //we check for a diagonal win (positive side)
  for (int row=0; row<4; row++) {
    for (int column=0; column<4; column++) {
     if (grid[column][row] != 0 && 
     grid[column][row] == grid[column+1][row+1] &&
     grid[column][row] == grid[column+2][row+2] &&
     grid[column][row] == grid[column+3][row+3]) {
      condition = true;
     }
   }
  }
  // we check for a diagonal win (negative side)
  for (int row=3; row<6; row++) {
    for (int column=0; column<4; column++) {
      if (grid[column][row] != 0 && 
      grid[column][row] == grid[column+1][row-1] && 
      grid[column][row] == grid[column+2][row-2] &&
      grid[column][row] == grid[column+3][row-3]) 
      {
       condition = true;
      }
    }
   }
   return condition;
}
// gives the winner name  
    public int winner(){
        if(CheckGame()){
        return (player%2)+1;
  }
  return -1;
}
//Prints the game grid
public void SeeGrid(){
    for (int row=5; row>=0; row--) {
      for (int column=0; column<7; column++) {
        System.out.print(grid[column][row] + " ");
      }
      System.out.println();
  }
}

public boolean draw(){
 boolean gameIsDraw=false;
 if((grid[0][5]!=0&&
         grid[1][5]!=0
         &&grid[2][5]!=0
         &&grid[3][5]!=0
         &&grid[4][5]!=0
         &&grid[5][5]!=0
         &&grid[6][5]!=0)&&!CheckGame()){
          System.out.println("Game is draw"); 
          gameIsDraw=true;
            }
 return gameIsDraw;           
}
}