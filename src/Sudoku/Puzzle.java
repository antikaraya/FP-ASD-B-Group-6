/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #1
 * 1 - 5026231033 - Ayu Alfia Putri
 * 2 - 5026231034 - Antika Raya
 * 3 - 5026231106 - Nailah Qonitah Firdausa
 */

package Sudoku;

import java.util.Random;
import java.util.Stack;

public class Puzzle{
    // All variables have package access
    // The numbers on the puzzle
    public static final int GRID_SIZE = 9;
    public static final int SUBGRID_SIZE = 3;
    int[][] numbers = new int[GRID_SIZE][GRID_SIZE];
    Cell[][] puzzleCell = new Cell[GRID_SIZE][GRID_SIZE];
    // The clues - isGiven (no need to guess) or need to guess
    boolean[][] isGiven = new boolean[GRID_SIZE][GRID_SIZE];
    // Constructor
    public Puzzle(){
        super();
    }

    // Generate a new puzzle given the number of cells to be guessed, which can be used
    //  to control the difficulty level.
    // This method shall set (or update) the arrays numbers and isGiven

    public void newPuzzle(int toGivenCells){
        numbers = new int[GRID_SIZE][GRID_SIZE];
        //sudoku generator initial value
        int initvalue = 10;
        int initcount = 0;
        do {
            for (int i=0;i<GRID_SIZE;i++){
                for (int j=0;j<GRID_SIZE;j++){
                    numbers[i][j]=0;
                    puzzleCell[i][j]= new Cell(i,j,numbers[i][j]);
                    isGiven[i][j] = false;
                }
            }
            while (initcount < initvalue){
                int rowrandoming = getRandomNumber(0, 9);
                int colrandoming = getRandomNumber(0, 9);
                int valrandoming = getRandomNumber(1, 9);
                if (numbers[rowrandoming][colrandoming] == 0){
                    if (validValue(rowrandoming, colrandoming, valrandoming)){
                        numbers[rowrandoming][colrandoming] = valrandoming;
                        initcount += 1;
                        Cell temp = new Cell(rowrandoming, colrandoming,numbers[rowrandoming][colrandoming]);
                        temp.islocked = true;
                        puzzleCell[rowrandoming][colrandoming] = temp;
                    }
                }
            }
        }
        while (!solve());
        int appearedNumber = toGivenCells;
        int limittedrowcol=9;
        int count = 0;
        int [] limitedColumn = new int[9];
        int [] limitedRow = new int[9];

        while(count<appearedNumber){
            System.out.println(count);
            int rowrandoming = getRandomNumber(0,9);
            int colrandoming = getRandomNumber(0,9);
            if(isGiven[rowrandoming][colrandoming]==false){
                if(limitedColumn[colrandoming]<limittedrowcol&&limitedRow[rowrandoming]<limittedrowcol){
                    isGiven[rowrandoming][colrandoming]= true;
                    limitedColumn[colrandoming]+=1;
                    limitedRow[rowrandoming]+=1;
                    count+=1;
                }
            }
        }
    }

    public int getRandomNumber(int min, int max){
        Random newNum = new Random();
        int newNumber = newNum.nextInt(max)+min;
        return newNumber;
    }

    public boolean validValue(int row, int col, int value){
        boolean isValid = true;
        for (int i=0;i<GRID_SIZE;i++){
            if (numbers[i][col]==value){
                isValid=false;
                break;
            }
        }
        for (int i=0;i<GRID_SIZE;i++){
            if (numbers[row][i]==value){
                isValid = false;
                break;
            }
        }

        int sgridrow = row-row%3;
        int sgridcol = col-col%3;
        for (int i=0;i<SUBGRID_SIZE;i++){
            for (int j=0;j<SUBGRID_SIZE;j++){
                if (numbers[sgridrow+i][sgridcol+j]==value){
                    isValid=false;
                    break;
                }
            }
        }
        return isValid;
    }

    public boolean solve(){
        Stack<Cell> stack = new Stack<>();
        int currentRow = 0;
        int currentColumn = 0;
        int currentValue =1;
        while(stack.size() < 81){
            if(puzzleCell[currentRow][currentColumn].islocked){
                Cell lockCell = puzzleCell[currentRow][currentColumn];
                stack.push(lockCell);
                currentColumn+=1;
                if (currentColumn>=9){
                    currentRow+=1;
                    currentColumn=0;
                    currentValue=1;
                }
                continue;
            }
            for (;currentValue <= 10 ; currentValue++){
                if (validValue(currentRow, currentColumn, currentValue)){
                    break;
                }
            }

            if(currentValue <= 9){
                Cell cell = new Cell(currentRow, currentColumn, currentValue);
                numbers[currentRow][currentColumn] = currentValue;
                stack.push(cell);
                currentColumn+=1;
                if (currentColumn>=9){
                    currentRow+=1;
                    currentColumn=0;
                }
                currentValue = 1;
            } else{
                if (stack.size() > 0){
                    // Assign to a Cell variable the top of the stack (stack.pop())
                    Cell cell = stack.pop();
                    // while the Cell is locked
                    while (cell.islocked){
                        // if stack size is greater than 0
                        if (stack.size() > 0){
                            // assign to the Cell variable the top of the stack (i.e. pop)
                            cell = stack.pop();
                        } else{
                            // print out the number of steps (time)
                            // return false (no solution found)
                            System.out.println("Total steps: ");
                            return false;
                        }
                    }

                    // assign to currentRow the row value of the Cell
                    currentRow = cell.row;
                    // assign to currentColumn the col value of the Cell
                    currentColumn = cell.col;
                    // assign to currentValue the value of the Cell + 1
                    currentValue = cell.number + 1;
                    // set the value of the board Cell at currentRow, currentColumn to 0
                    numbers[currentRow][currentColumn] =  0;
                } else{
                    // print out the number of steps (time)
                    // return false (no solution found)
                    System.out.println("Total steps: ");
                    return false;
                }
            }
        }
        return true;
    }
}
