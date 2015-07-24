package com.example.tictactoe;

/**
 * Created by Alexpon on 7/20/2015.
 */


import java.util.Random;

public class TicTacToeGame {

    public static final char HUMAN_PLAYER = 'X';
    public static final char COMPUTER_PLAYER = 'O';
    public static final char OPEN_SPOT = ' ';
    public static final int BOARD_SIZE = 9;
    private char mBoard[] = { '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    private Random mRand;

    public TicTacToeGame() {
        // Seed the random number generator
        mRand = new Random();
        char turn = HUMAN_PLAYER;    // Human starts first
        int  win = 0;                // Set to 1, 2, or 3 when game is over
    }

    public void clearBoard(){
        for(int i=0; i<BOARD_SIZE; i++){
            mBoard[i] = OPEN_SPOT;
        }
    }

    public void setMove(char player, int location){
        mBoard[location] = player;
    }


    // Check for a winner.  Return
    //  0 if no winner or tie yet
    //  1 if it's a tie
    //  2 if X won
    //  3 if O won
    public int checkForWinner() {

        // Check horizontal wins
        for (int i = 0; i <= 6; i += 3)	{
            if (mBoard[i] == HUMAN_PLAYER &&
                    mBoard[i+1] == HUMAN_PLAYER &&
                    mBoard[i+2]== HUMAN_PLAYER)
                return 2;
            if (mBoard[i] == COMPUTER_PLAYER &&
                    mBoard[i+1]== COMPUTER_PLAYER &&
                    mBoard[i+2] == COMPUTER_PLAYER)
                return 3;
        }

        // Check vertical wins
        for (int i = 0; i <= 2; i++) {
            if (mBoard[i] == HUMAN_PLAYER &&
                    mBoard[i+3] == HUMAN_PLAYER &&
                    mBoard[i+6]== HUMAN_PLAYER)
                return 2;
            if (mBoard[i] == COMPUTER_PLAYER &&
                    mBoard[i+3] == COMPUTER_PLAYER &&
                    mBoard[i+6]== COMPUTER_PLAYER)
                return 3;
        }

        // Check for diagonal wins
        if ((mBoard[0] == HUMAN_PLAYER &&
                mBoard[4] == HUMAN_PLAYER &&
                mBoard[8] == HUMAN_PLAYER) ||
                (mBoard[2] == HUMAN_PLAYER &&
                        mBoard[4] == HUMAN_PLAYER &&
                        mBoard[6] == HUMAN_PLAYER))
            return 2;
        if ((mBoard[0] == COMPUTER_PLAYER &&
                mBoard[4] == COMPUTER_PLAYER &&
                mBoard[8] == COMPUTER_PLAYER) ||
                (mBoard[2] == COMPUTER_PLAYER &&
                        mBoard[4] == COMPUTER_PLAYER &&
                        mBoard[6] == COMPUTER_PLAYER))
            return 3;

        // Check for tie
        for (int i = 0; i < BOARD_SIZE; i++) {
            // If we find a number, then no one has won yet
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER)
                return 0;
        }
        // If we make it through the previous loop, all places are taken, so it's a tie
        return 1;
    }

    public int getAdvanceMove(int cnt, int mode){
        int move;
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER) {
                char curr = mBoard[i];
                mBoard[i] = COMPUTER_PLAYER;
                if (checkForWinner() == 3) {
                    return i;
                }
                else
                    mBoard[i] = curr;
            }
        }

        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER) {
                char curr = mBoard[i];
                mBoard[i] = HUMAN_PLAYER;
                if (checkForWinner() == 2) {
                    mBoard[i] = COMPUTER_PLAYER;
                    return i;
                }
                else
                    mBoard[i] = curr;
            }
        }

        if(mode==1){
            if (cnt==2){
                if(mBoard[1] == HUMAN_PLAYER) {
                    mBoard[2] = COMPUTER_PLAYER;
                    return 2;
                }
                else if(mBoard[3] == HUMAN_PLAYER) {
                    mBoard[6] = COMPUTER_PLAYER;
                    return 6;
                }
                else if(mBoard[5] == HUMAN_PLAYER) {
                    mBoard[2] = COMPUTER_PLAYER;
                    return 2;
                }
                else if(mBoard[7] == HUMAN_PLAYER) {
                    mBoard[6] = COMPUTER_PLAYER;
                    return 6;
                }
                else if(mBoard[0] == HUMAN_PLAYER) {
                    mBoard[8] = COMPUTER_PLAYER;
                    return 8;
                }
                else if(mBoard[2] == HUMAN_PLAYER) {
                    mBoard[6] = COMPUTER_PLAYER;
                    return 6;
                }
                else if(mBoard[6] == HUMAN_PLAYER) {
                    mBoard[2] = COMPUTER_PLAYER;
                    return 2;
                }
                else if(mBoard[8] == HUMAN_PLAYER) {
                    mBoard[0] = COMPUTER_PLAYER;
                    return 0;
                }
            }
            if (cnt==3) {
                if (mBoard[2] == COMPUTER_PLAYER && mBoard[1] == HUMAN_PLAYER) {
                    mBoard[5] = COMPUTER_PLAYER;
                    return 5;
                } else if (mBoard[2] == COMPUTER_PLAYER) {
                    mBoard[1] = COMPUTER_PLAYER;
                    return 1;
                } else if (mBoard[6] == COMPUTER_PLAYER && mBoard[5] == HUMAN_PLAYER) {
                    mBoard[7] = COMPUTER_PLAYER;
                    return 7;
                } else if (mBoard[6] == COMPUTER_PLAYER) {
                    mBoard[3] = COMPUTER_PLAYER;
                    return 3;
                } else if (mBoard[0] == COMPUTER_PLAYER && mBoard[1] == HUMAN_PLAYER) {
                    mBoard[3] = COMPUTER_PLAYER;
                    return 3;
                } else if (mBoard[0] == COMPUTER_PLAYER && mBoard[3] == HUMAN_PLAYER) {
                    mBoard[1] = COMPUTER_PLAYER;
                    return 1;
                } else if (mBoard[2] == COMPUTER_PLAYER && mBoard[1] == HUMAN_PLAYER) {
                    mBoard[5] = COMPUTER_PLAYER;
                    return 5;
                } else if (mBoard[2] == COMPUTER_PLAYER && mBoard[5] == HUMAN_PLAYER) {
                    mBoard[1] = COMPUTER_PLAYER;
                    return 1;
                } else if (mBoard[6] == COMPUTER_PLAYER && mBoard[3] == HUMAN_PLAYER) {
                    mBoard[7] = COMPUTER_PLAYER;
                    return 7;
                } else if (mBoard[6] == COMPUTER_PLAYER && mBoard[7] == HUMAN_PLAYER) {
                    mBoard[3] = COMPUTER_PLAYER;
                    return 3;
                } else if (mBoard[8] == COMPUTER_PLAYER && mBoard[5] == HUMAN_PLAYER) {
                    mBoard[7] = COMPUTER_PLAYER;
                    return 7;
                } else if (mBoard[8] == COMPUTER_PLAYER && mBoard[7] == HUMAN_PLAYER) {
                    mBoard[5] = COMPUTER_PLAYER;
                    return 5;
                }
            }
        }

        do
        {
            move = mRand.nextInt(BOARD_SIZE);
        } while (mBoard[move] == HUMAN_PLAYER || mBoard[move] == COMPUTER_PLAYER);

        mBoard[move] = COMPUTER_PLAYER;
        return move;
    }

    public void regret(int player, int computer){
        mBoard[player] = OPEN_SPOT;
        mBoard[computer] = OPEN_SPOT;
    }

}
