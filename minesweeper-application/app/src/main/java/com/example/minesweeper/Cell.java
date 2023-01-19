package com.example.minesweeper;

public class Cell {
    boolean mine_cell;
    boolean is_revealed;
    boolean is_flagged;
    int adjacent_mines;

    public Cell(){
        this.mine_cell = false;
        this.is_revealed = false;
        this.is_flagged = false;
        this.adjacent_mines = 0;
    }

    public boolean getMine(){
        return this.mine_cell;
    }
    public void setMine(){
        this.mine_cell = true;
    }

    public boolean getReveal(){
        return this.is_revealed;
    }
    public void setReveal(){
        this.is_revealed = true;
    }

    public boolean getFlag(){
        return this.is_flagged;
    }
    public void setFlag(){
        this.is_flagged = true;
    }

    public int getValue(){
        return this.adjacent_mines;
    }
    public void setValue(int i){
        this.adjacent_mines = i;
    }

}
