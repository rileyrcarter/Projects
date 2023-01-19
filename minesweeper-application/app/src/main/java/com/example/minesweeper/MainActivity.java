package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //global variables for landing activity
    private static final int COLUMN_COUNT = 8;
    private static final int ROW_COUNT = 10;

    private int mine_count = 4;
    private int flag_count = 4;

    private int clock = 0;
    private boolean clock_running = false;

    private boolean mine_revealed = false;



    //textViews of all cells in an array
    private ArrayList<TextView> cell_tvs;

    private Cell[][] cell_data = new Cell[ROW_COUNT][COLUMN_COUNT];


    private boolean dig_mode = true;

    //instantiate null components of landing page
        //time & flag counts
        //grid cell layout
        //mode switch toggle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instantiate components of landing page
        //time_count
        //flag_count
        TextView tv_time = (TextView) findViewById(R.id.textView_time_count);
        tv_time.setText(String.valueOf(clock));

        TextView tv_flag = (TextView) findViewById(R.id.textView_flag_count);
        tv_flag.setText(String.valueOf(flag_count));


        cell_tvs = new ArrayList<TextView>();

        //grid 8x10
        GridLayout game_grid = (GridLayout) findViewById(R.id.gridLayout_game);
        game_grid.setColumnCount(COLUMN_COUNT);
        game_grid.setRowCount(ROW_COUNT);
        
        LayoutInflater li = LayoutInflater.from(this);
        for (int i = 0; i<ROW_COUNT; i++) {
            for (int j=0; j<COLUMN_COUNT; j++) {
                TextView tv = (TextView) li.inflate(R.layout.custom_cell_layout, game_grid, false);
                tv.setTextColor(Color.GREEN);
                tv.setBackgroundColor(Color.GREEN);
                tv.setOnClickListener(this::onClickTV);

                GridLayout.LayoutParams lp = (GridLayout.LayoutParams) tv.getLayoutParams();
                lp.rowSpec = GridLayout.spec(i);
                lp.columnSpec = GridLayout.spec(j);

                game_grid.addView(tv, lp);

                cell_tvs.add(tv);
            }
        }

        //MODE button
        TextView mode_button = findViewById(R.id.switch_mode);
        mode_button.setOnClickListener(this::onClickSwitchMode);

        //START NEW GAME
        generateNullGame();
        generateRandomMines();
        setAdjacentMines();

        clock_running = true;
        runTimer();

        //testSetup();

    }


    private void generateNullGame(){
        for(int r=0; r<ROW_COUNT; r++) {
            for (int c = 0; c < COLUMN_COUNT; c++) {
                cell_data[r][c] = new Cell();
            }
        }
    }
    //method to update cell_data arraylist
    private void generateRandomMines(){


        int i =0;
        while(i<mine_count)
        {
            //random x y coordinate
            Random random = new Random();
            int x = random.nextInt(ROW_COUNT);
            int y = random.nextInt(COLUMN_COUNT);

            //update mine boolean, only if no current mine
            if(!(cell_data[x][y]).getMine())
            {
                cell_data[x][y].setMine();
                i++;

            }
        }
    }

    //iterate through cell_data grid to set adjacent mine values
    private void setAdjacentMines(){


        //for each cell, calculate num adjacent mines
        for(int r=0; r<ROW_COUNT; r++)
        {
            for(int c=0; c<COLUMN_COUNT; c++) {
                //set adjacent numbers for all cells without a mine
                if (!cell_data[r][c].getMine())
                {
                    int num = cell_data[r][c].getValue();

                    //check all 8 neighboring cells
                    //row-1, col-1
                    if(validCell(r-1, c-1))
                    {
                        if(cell_data[r-1][c-1].getMine())
                        {
                            num++;
                        }
                    }

                    //row-1, col
                    if(validCell(r-1, c))
                    {
                        if(cell_data[r-1][c].getMine())
                        {
                            num++;
                        }
                    }

                    //row-1, col+1
                    if(validCell(r-1, c+1))
                    {
                        if(cell_data[r-1][c+1].getMine())
                        {
                            num++;
                        }
                    }

                    //row, col-1
                    if(validCell(r, c-1))
                    {
                        if(cell_data[r][c-1].getMine())
                        {
                            num++;
                        }
                    }

                    //row, col+1
                    if(validCell(r, c+1))
                    {
                        if(cell_data[r][c+1].getMine())
                        {
                            num++;
                        }
                    }

                    //row+1, col-1
                    if(validCell(r+1, c-1))
                    {
                        if(cell_data[r+1][c-1].getMine())
                        {
                            num++;
                        }
                    }

                    //row+1, col
                    if(validCell(r+1, c))
                    {
                        if(cell_data[r+1][c].getMine())
                        {
                            num++;
                        }
                    }

                    //row+1, col+1
                    if(validCell(r+1, c+1))
                    {
                        if(cell_data[r+1][c+1].getMine())
                        {
                            num++;
                        }
                    }

                    //for every cell: update count in cell_data
                    cell_data[r][c].setValue(num);
                }
            }
        }
    }

    public void testSetup(){
        //iterate game_data and add to views
        for (int n=0; n<cell_tvs.size(); n++) {

            int i = n/COLUMN_COUNT;
            int j = n%COLUMN_COUNT;

            //place mines
            if(cell_data[i][j].getMine())
            {
                cell_tvs.get(n).setText(R.string.mine);
            }
            else
            {
                int val = cell_data[i][j].getValue();

                (cell_tvs.get(n)).setText(String.valueOf(val));
            }
        }
    }

    private boolean validCell(int i, int j)
    {
        if((i < ROW_COUNT) && (j < COLUMN_COUNT) && (i>=0) && (j>=0))
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    //private method to locate cell index
    private int findIndexOfCellTextView(TextView tv) {
        for (int n=0; n<cell_tvs.size(); n++) {
            if (cell_tvs.get(n) == tv)
                return n;
        }
        return -1;
    }

    //function to process user click, return flag or dig
    public void onClickTV(View view){
        TextView tv = (TextView) view;
        int n = findIndexOfCellTextView(tv);
        int i = n/COLUMN_COUNT;
        int j = n%COLUMN_COUNT;

        //case 1: FLAG MODE
        if(!dig_mode)
        {
            if(flag_count>0)
            {
                //change cell image
                tv.setText(R.string.flag);

                //update flag count
                TextView flag_text = (TextView) findViewById(R.id.textView_flag_count);
                flag_count--;
                flag_text.setText(String.valueOf(flag_count));

                //update cell_data structure
                cell_data[i][j].setFlag();
            }
        }

        //case 2: DIG MODE
            //case 2A: MINE CELL: GAME OVER
            //case 2B: INT CELL: ADJ MINE VALUES
            //case 2C: NULL CELL: RECURSIVE CLEAR

        else{

            if(cell_data[i][j].getMine())
            {
                //reveal mines here
                tv.setBackgroundColor(Color.LTGRAY);
                tv.setText(R.string.mine);
                mine_revealed = true;
            }
            else
            {
                int num_mines = cell_data[i][j].getValue();

                if(num_mines == 0)
                {
                    //recursive null clear
                    transitiveClear(i, j);
                }
                else
                {
                    cell_data[i][j].setReveal();

                    tv.setBackgroundColor(Color.LTGRAY);
                    tv.setText(String.valueOf(num_mines));
                    tv.setTextColor(Color.GRAY);

                }
            }
        }
        checkGameStatus();

    }

    public void transitiveClear(int r, int c){

        int tv_index = c + (COLUMN_COUNT*r);

        //case 1: cell w adjacent mines
        //reveal value, no neighbor search
        if(cell_data[r][c].getValue() != 0)
        {
            cell_tvs.get(tv_index).setText(String.valueOf(cell_data[r][c].getValue()));
            cell_tvs.get(tv_index).setTextColor(Color.GRAY);
            cell_tvs.get(tv_index).setBackgroundColor(Color.LTGRAY);
            cell_data[r][c].setReveal();
        }
        //case 2: null cell
        //reveal cell, transitive neighbor search
        else {
            cell_tvs.get(tv_index).setTextColor(Color.LTGRAY);
            cell_tvs.get(tv_index).setBackgroundColor(Color.LTGRAY);
            cell_data[r][c].setReveal();

            //for each neighboring cell
            //if not revealed yet AND null cell, reveal

            //check all 8 neighboring cells
            //row-1, col-1
            if(validCell(r-1, c-1))
            {
                if(!cell_data[r-1][c-1].getReveal())
                {
                    transitiveClear(r-1, c-1);
                }
            }

            //row-1, col
            if(validCell(r-1, c))
            {
                if(!cell_data[r-1][c].getReveal())
                {
                    transitiveClear(r-1, c);
                }
            }

            //row-1, col+1
            if(validCell(r-1, c+1))
            {
                if(!cell_data[r-1][c+1].getReveal())
                {
                    transitiveClear(r-1, c+1);
                }
            }

            //row, col-1
            if(validCell(r, c-1))
            {
                if(!cell_data[r][c-1].getReveal())
                {
                    transitiveClear(r, c-1);
                }
            }

            //row, col+1
            if(validCell(r, c+1))
            {
                if(!cell_data[r][c+1].getReveal())
                {
                    transitiveClear(r, c+1);
                }
            }

            //row+1, col-1
            if(validCell(r+1, c-1))
            {
                if(!cell_data[r+1][c-1].getReveal())
                {
                    transitiveClear(r+1, c-1);
                }
            }

            //row+1, col
            if(validCell(r+1, c))
            {
                if(!cell_data[r+1][c].getReveal())
                {
                    transitiveClear(r+1, c);
                }
            }

            //row+1, col+1
            if(validCell(r+1, c+1))
            {
                if(!cell_data[r+1][c+1].getReveal())
                {
                    transitiveClear(r+1, c+1);
                }
            }
        }
        return;
    }

    public void onClickSwitchMode(View view){
        TextView mode_toggle = (TextView) view;

        //case: currently dig mode, switch to flag
        if(dig_mode)
        {
            mode_toggle.setText(R.string.flag);
            dig_mode = false;
        }
        //case 2: currently flag mode, switch to dig
        else
        {
            mode_toggle.setText(R.string.pick);
            dig_mode = true;
        }
    }

    private void runTimer() {
        final TextView timeView = (TextView) findViewById(R.id.textView_time_count);
        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                int minutes = (clock%3600) / 60;
                int seconds = clock%60;
                String time = String.format("%02d:%02d", minutes, seconds);
                timeView.setText(time);

                if (clock_running) {
                    clock++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    private void checkGameStatus()
    {
        //case 1: game lost
        if(mine_revealed)
        {
            gameOver(false);
        }
        //case 2: game not lost, check if complete
        else
        {
            boolean game_won = true;

            for(int r=0; r<ROW_COUNT; r++) {
                for (int c = 0; c < COLUMN_COUNT; c++) {

                    //revealed
                    if(cell_data[r][c].getReveal())
                    {

                    }
                    //not revealed
                    else
                    {
                        //NOT WIN CASE 1: not revealed and not mine location
                        if(!cell_data[r][c].getMine())
                        {
                            game_won = false;
                        }
                        //not revealed, mine location
                        else
                        {
                            //NOT WIN CASE 2: not revealed, mine location, and not flagged
                            if(!cell_data[r][c].getFlag())
                            game_won = false;
                        }
                    }

                }
            }
            if(game_won)
            {
                gameOver(true);
            }
            //cases where game_won is false
            //case 1: unrevealed cell
            //case 2: flag on non mine cell
            //case 3:
            //iterate game data
            //game won: if all revealed, except mine locations
            //game won: if flags on all mine locations mines
        }
    }
    private void gameOver(boolean result)
    {
        clock_running = false;

        String line1 = "Used " + clock + " seconds.";
        String line2;
        if(result)
        {
            line2 = "You won." + "\n" + "Good Job!";
        }
        else
        {
            line2 = "You lost.";
        }

        String message = line1 + "\n" + line2;

        //set intent to navigate to new activity
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("com.example.sendmessage.MESSAGE", message);

        startActivity(intent);

    }

}