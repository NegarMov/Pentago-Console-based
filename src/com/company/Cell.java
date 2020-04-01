package com.company;

/**
 * The class Cell represents a cell of the board. Each cell is made of a button, the cell's
 * coordinate ((x, y)), the status of the cell (is empty, a black stone is on it or a white stone is on it)
 * and manages which player's turn it is at the moment.
 * @author Negar Movaghatian
 * @since 2020-03-30
 */
public class Cell{
    // The button which shows the cell and has features, like its
    // icon changes when clicked on
    private char character;
    // The turn of the players [0: first player(white)  1: second player(black)]
    static int turn;
    // The status of the cell [0: empty    1: white    -1: black]
    private int stat;

    /**
     * Create a new cell with the given coordinate and add a button with an empty icon
     * to it.
     */
    public Cell() {
        turn = 0;
        stat = 0;
        character = '\u2B1B';
    }

    /**
     * Update the cell and change its status to the given color.
     * @param color The color of the disk on the cell.
     */
    public void updateCell(String color) {
        if (color.equals("B")) {
            character = '\u26AA';
            stat = -1;
        }
        else {
            character = '\u26AB';
            stat = 1;
        }
    }

    /**
     * Swap the cell with another cell by swapping their icon and status.
     * @param other
     */
    public void swap(Cell other) {
        // Swap icons
        char tempIcon = this.character;
        this.character = other.character;
        other.character = tempIcon;
        // Swap stats
        int tmp = this.stat;
        this.stat = other.stat;
        other.stat = tmp;
    }

    /**
     * Get the status of the cell.
     * @return stat field.
     */
    public int getStat() {
        return stat;
    }

    public char getCharacter() {
        return character;
    }

    /**
     * Get the turn of the players at the moment.
     * @return turn field.
     */
    public static int getTurn() {
        return turn;
    }

    /**
     * The class ButtonHandler manages the ActionEvent created by
     * the cell's button.
     */
    public void addStone() {
        if (stat==0) {
            updateCell((turn==0)? "W" : "B");
            stat = (turn==0)? 1 : -1;
            turn = 1 - turn;
        }
        else
            System.out.println("Please choose another cell.");
    }
}
