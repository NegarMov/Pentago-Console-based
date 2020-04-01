package com.company;

import java.util.Scanner;

/**
 * The Board class simulates a board for Pentago game. It holds a list of the board
 * cells which itself holds a stone for player 1, player 2 or is empty.
 * The first player plays white and the second player plays black.
 * @author Negar Movaghatian
 * @since 2020-03-30
 */
public class Board{

    // The list of the board's cells
    protected Cell[][] cells;
    // The name of the player at the moment
    private String player;
    // The number of stones on the board
    private int moves;
    // Shows if a block is empty or not
    private boolean[] isEmpty;

    /**
     * Creat a new board with adding 36 cells to it.
     */
    public Board() {

        cells = new Cell[6][6];
        moves = 0;
        player = "[Player 1]";
        isEmpty = new boolean[]{true, true, true, true};
        for (int i=0; i<6; i++)
            for (int j=0; j<6; j++)
                cells[i][j] = new Cell();
    }

    /**
     * Print all the components of the board.
     */
    public void printBoard() {
        System.out.println("\n\n\n\n");
        for (int i=0; i<6; i++) {
            for (int j = 0; j < 6; j++) {
                if (j == 3)
                    System.out.print(" \u2503 ");
                if (cells[i][j].getCharacter()=='\u2B1B')
                    System.out.print("\u001B[31m");
                System.out.print(cells[i][j].getCharacter() + "\u2005\u001B[0m");
            }
            System.out.println();
            if (i == 2) {
                System.out.print("\u2005");
                for (int k = 0; k < 15; k++)
                        System.out.print((k==7)? '\u254B' : "\u2501");
                System.out.println();
            }
        }
    }

    /**
     * rotate the given section 90 degrees clockwise or anticlockwise.
     * @param section The section of the board to rotate.
     * @param direction The direction of the rotation. [CW: clockwise, ACW: anticlockwise]
     */
    private void rotate(int section, String direction) {
        int[] dSection = {0, 3, 18, 21}, edge, corner;
        if (direction=="ACW") {
            edge = new int[] {1, 8, 13, 6};
            corner = new int[] {0, 2, 14, 12};
        }
        else {
            edge = new int[] {6, 13, 8, 1};
            corner = new int []{12, 14, 2, 0};
        }
        for (int i=0; i<3; i++) {
            int x = (edge[i] + dSection[section])/6, y = (edge[i] + dSection[section])%6;
            int xp = (edge[(i + 1)] + dSection[section])/6, yp = (edge[(i + 1)] + dSection[section])%6;
            cells[x][y].swap(cells[xp][yp]);
            x = (corner[i] + dSection[section])/6; y = (corner[i] + dSection[section])%6;
            xp = (corner[(i + 1)] + dSection[section])/6; yp = (corner[(i + 1)] + dSection[section])%6;
            cells[x][y].swap(cells[xp][yp]);
        }
    }

    /**
     * Check if all the cells in the given sequence have the same color.
     * @param sequence The number of cells to check.
     * @return 1 if all the cells of the 'sequence' are black, 0 if white and if they do
     *         not have the same color -1.
     */
    private int sameColor(int[] sequence) {
        // Same color and black: 1, Same color and white: 0, Not the same color: -1
        int firstColor = cells[sequence[0]/6][sequence[0]%6].getStat();
        if (firstColor==0) return -1;
        for (int i=1; i<5; i++)
            if (cells[sequence[i]/6][sequence[i]%6].getStat()!=firstColor)
                return -1;
        return (firstColor==-1)? 1 : 0;
    }

    /**
     * Check if any of the players has 5 stones in a row.
     * @return false for each player if the player doesn't have 5 stones in
     *         a row and true otherwise.
     */
    private boolean[] checkBoard() {
        boolean[] ans = {false, false}; // [0: first player, 1: second player]
        int[][] diagonalWays = {{1, 8, 15, 22, 29}, {6, 13, 20, 27, 34}, {0, 7, 14, 21, 28}, {7, 14, 21, 28, 35}
                , {11, 16, 21, 26, 31}, {4, 9, 14, 19, 24}, {5, 10, 15, 20, 25}, {10, 15, 20, 25, 30}};
        int[][] verticalWays = {{-1, 5, 11, 17, 23}, {5, 11, 17, 23, 29}};
        int[][] horizontalWays = {{-6, -5, -4, -3, -2}, {-5, -4, -3, -2, -1}};

        for (int i=0; i<8; i++) {
            int[] sequence = diagonalWays[i];
            if (sameColor(sequence)!=-1)
                ans[sameColor(sequence)] = true;
        }
        for (int i=0; i<6; i++)
            for (int j=0; j<2; j++) {
                int[] sequence = horizontalWays[j];
                for (int k=0; k<5; k++)
                    sequence[k] += 6;
                if (sameColor(sequence)!=-1)
                    ans[sameColor(sequence)] = true;
            }
        for (int i=0; i<6; i++)
            for (int j=0; j<2; j++) {
                int[] sequence = verticalWays[j];
                for (int k=0; k<5; k++)
                    sequence[k] += 1;
                if (sameColor(sequence)!=-1)
                    ans[sameColor(sequence)] = true;
            }
        return ans;
    }

    /**
     * Add a stone to the board according to the answers the player gives.
     */
    private void addCell() {
        Scanner scn = new Scanner(System.in);
        System.out.println("Which block of the board you want to put your stone on?");
        int s = scn.nextInt();
        while (s<=0 || s>4) {
            System.out.println("Please enter a number between 1 and 4.");
            s = scn.nextInt();
        }
        System.out.println("In which place?");
        int p = scn.nextInt();
        while (p<=0 || p>9) {
            System.out.println("Please enter a number between 1 and 9.");
            p = scn.nextInt();
        }
        int x = (0<p && p<4)? ((s==1 || s==2)? 0 : 3) : (3<p && p<7)? ((s==1 || s==2)? 1 : 4) : ((s==1 || s==2)? 2 : 5);
        int y = (p==1 || p==4 || p==7)? ((s==1 || s==3)? 0 : 3) : (p==2 || p==5 || p==8)? ((s==1 || s==3)? 1 : 4) : ((s==1 || s==3)? 2 : 5);
        if (cells[x][y].addStone()==-1)
            addCell();
        else
            isEmpty[s - 1] = false;
    }

    /**
     * Rotate a block according to the answers the player gives.
     */
    public void rotateBlock() {
        Scanner scn = new Scanner(System.in);
        boolean pass = isEmpty[0] || isEmpty[1] || isEmpty[2] || isEmpty[3];
        System.out.println("Which section of the board you want to rotate?");
        if (pass)
            System.out.println("(Press 5 to pass)");
        int section = scn.nextInt();
        while ((!pass && (section<=0 || section>4)) || (pass && (section<=0 || section>5))) {
            System.out.println("Please enter a number between 1 and " + ((!pass)? "4." : "5."));
            section = scn.nextInt();
        }
        if (section!=5) {
            System.out.println("In which direction you want to rotate the block?");
            System.out.println("1)Clockwise\n2)Anticlockwise");
            int direction = scn.nextInt();
            while (direction<=0 || direction>2) {
                System.out.println("Please enter a number between 1 and 2.");
                direction = scn.nextInt();
            }
            rotate(section - 1, (direction == 1) ? "CW" : "ACW");
        }
    }

    /**
     * Run a multi-player Pentago game until one or both of the players get
     * five stones in a row (vertically, horizontally or diagonal) or the board is full.
     * White begins the game.
     */
    public void runGame() {
        boolean[] flag = {false, false};
        while (!checkBoard()[0] && !checkBoard()[1] && moves<36) {
            printBoard();
            player = (Cell.getTurn()==0)? "[Player 1]" : "[Player 2]";
            System.out.println(player);

            addCell();

            if (checkBoard()[0])
                flag[0] = true;
            else if (checkBoard()[1])
                flag[1] = true;

            rotateBlock();

            moves++;
        }
        printBoard();
        System.out.println( (flag[0])? "<<White Wins!>>" : (flag[1])? "<<Black Wins!>>" :
                        (checkBoard()[0] && checkBoard()[1])? "<<Draw!>>" :
                        (checkBoard()[0])? "<<White Wins!>>" :
                        (checkBoard()[1])? "<<Black Wins!>>" : "<<Draw!>>");
    }
}