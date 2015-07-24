package com.example.tictactoe;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class GameActivity extends Activity {

    private TicTacToeGame mGame;
    private Button mBoardButtons[];
    private TextView mInfoTextView;
    private Button startButton;
    private Button regret;
    private Button back;
    private Boolean mGameOver;
    private String level;
    private int cnt;
    private int meLastMove=9;
    private int comLastMove=9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initViews();
        setListener();
        startNewGame();
    }

    public void initViews(){
        mGame = new TicTacToeGame();
        mBoardButtons = new Button[mGame.BOARD_SIZE];
        mBoardButtons[0] = (Button) findViewById(R.id.one);
        mBoardButtons[1] = (Button) findViewById(R.id.two);
        mBoardButtons[2] = (Button) findViewById(R.id.three);
        mBoardButtons[3] = (Button) findViewById(R.id.four);
        mBoardButtons[4] = (Button) findViewById(R.id.five);
        mBoardButtons[5] = (Button) findViewById(R.id.six);
        mBoardButtons[6] = (Button) findViewById(R.id.seven);
        mBoardButtons[7] = (Button) findViewById(R.id.eight);
        mBoardButtons[8] = (Button) findViewById(R.id.nine);
        mInfoTextView = (TextView) findViewById(R.id.information);
        startButton = (Button) findViewById(R.id.button_restart);
        regret = (Button) findViewById(R.id.btn_regret);
        back = (Button) findViewById(R.id.btn_back);

        Bundle bundle = getIntent().getExtras();
        level = bundle.getString("KEY_LEVEL");
        cnt = 2;
    }

    private void setListener(){
        startButton.setOnClickListener(myListener);
        regret.setOnClickListener(myListener);
        back.setOnClickListener(myListener);
    }

    private void startNewGame() {
        startButton.setText(R.string.restart);
        mGameOver = false;
        mGame.clearBoard();
        for (int i = 0; i < mBoardButtons.length; i++) {
            mBoardButtons[i].setText("");
            mBoardButtons[i].setEnabled(true);
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
        }
        if(level.equals("hard")){
            setMove(TicTacToeGame.COMPUTER_PLAYER, 4);
            mInfoTextView.setTextColor(Color.rgb(0, 0, 0));
            mInfoTextView.setText(R.string.uTurn);
        }
        else{
            mInfoTextView.setText(level);
        }
    }


    //---Handles clicks on the game board buttons
    private class ButtonClickListener implements View.OnClickListener {
        int location;
        public ButtonClickListener(int location) {
            this.location = location;
        }
        @Override
        public void onClick(View v) {
            if (mGameOver == false) {
                if (mBoardButtons[location].isEnabled()) {
                    meLastMove = location;
                    setMove(TicTacToeGame.HUMAN_PLAYER, location);
                    int winner = mGame.checkForWinner();
                    if (winner == 0) {
                        mInfoTextView.setText(R.string.cTurn);
                        int move;
                        if(level.equals("hard")){
                            move = mGame.getAdvanceMove(cnt, 1);
                            cnt++;
                        }
                        else {
                            move = mGame.getAdvanceMove(9, 0);
                        }
                            comLastMove = move;
                            setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                            winner = mGame.checkForWinner();
                    }
                    if (winner == 0) {
                        mInfoTextView.setTextColor(Color.rgb(0, 0, 0));
                        mInfoTextView.setText(R.string.uTurn);
                    } else if (winner == 1) {
                        mInfoTextView.setTextColor(Color.rgb(0, 0, 200));
                        mInfoTextView.setText(R.string.tie);
                        mGameOver = true;
                    } else if (winner == 2) {
                        mInfoTextView.setTextColor(Color.rgb(0, 200, 0));
                        mInfoTextView.setText(R.string.uWin);
                        mGameOver = true;
                    } else {
                        mInfoTextView.setTextColor(Color.rgb(200, 0, 0));
                        mInfoTextView.setText(R.string.cWin);
                        mGameOver = true;
                    }
                }
            }
        }
    }

    private View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_restart:
                    cnt=2;
                    startNewGame();
                    break;
                case R.id.btn_regret:
                    if(meLastMove==9){
                        mInfoTextView.setText(R.string.noMove);
                    }
                    else{
                        mBoardButtons[meLastMove].setText("");
                        mBoardButtons[meLastMove].setEnabled(true);
                        mBoardButtons[comLastMove].setText("");
                        mBoardButtons[comLastMove].setEnabled(true);
                        mGame.regret(meLastMove, comLastMove);
                    }
                    break;
                case R.id.btn_back:
                    finish();
                    break;
            }
        }
    };

    private void setMove(char player, int location) {
        mGame.setMove(player, location);
        mBoardButtons[location].setEnabled(false);
        mBoardButtons[location].setText(String.valueOf(player));
        if (player == TicTacToeGame.HUMAN_PLAYER)
            mBoardButtons[location].setTextColor(Color.rgb(0, 200, 0));
        else
            mBoardButtons[location].setTextColor(Color.rgb(200, 0, 0));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
