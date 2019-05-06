package com.example.tictactoejava;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.TabStopSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {


    Integer LINE_SIZE=3;
    String[] board = new String[LINE_SIZE*LINE_SIZE];
    Boolean playerTurn=false;

    Button[] boardButton=new Button[LINE_SIZE*LINE_SIZE];
    Button renewGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createBoard();
    }

    void createBoard(){

        boardButton[0]=findViewById(R.id.firstSpot);
        boardButton[1]=findViewById(R.id.secondSpot);
        boardButton[2]=findViewById(R.id.thirdSpot);
        boardButton[3]=findViewById(R.id.fourthSpot);
        boardButton[4]=findViewById(R.id.fifthSpot);
        boardButton[5]=findViewById(R.id.sixthSpot);
        boardButton[6]=findViewById(R.id.seventhSpot);
        boardButton[7]=findViewById(R.id.eightSpot);
        boardButton[8]=findViewById(R.id.nineSpot);
        for(int fill=0; fill<LINE_SIZE*LINE_SIZE; fill++){
            board[fill]="0";
            boardButton[fill].setId(fill);
            boardButton[fill].setBackgroundResource(android.R.drawable.btn_default);
            boardButton[fill].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    placePiece(v);
                }
            });
        }
        renewGame=findViewById(R.id.restart);
        renewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanBoardComplete();
            }
        });
    }

    void placePiece(View v){
        v.setOnClickListener(null);
        if(!playerTurn) {
            board[v.getId()] = "X";
            v.setBackgroundColor(getResources().getColor(R.color.colorPlayer1));
            playerTurn=true;
        }else{
            v.setBackgroundColor(getResources().getColor(R.color.colorPlayer2));
            board[v.getId()] = "O";
            playerTurn=false;
        }

        checkCompleteBoard();
    }

    void sendDataToDialogBox(String winner) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogue_winner);

        TextView winnerTxt = dialog.findViewById(R.id.winnerPlayer);
        TextView defaultText = dialog.findViewById(R.id.textView);

        if (winner.equals("Tie")){
            defaultText.setText("No winner it is a:");
        }
        for(int cleanBoard=0; cleanBoard<LINE_SIZE*LINE_SIZE;cleanBoard++){
            boardButton[cleanBoard].setOnClickListener(null);
        }
        winnerTxt.setText(winner);
        dialog.show();
    }

    void checkCompleteBoard(){
        String playerWinner="Empty";

        playerWinner=checkBoardColumns(playerWinner);
        if(playerWinner.equals("Empty")){
            playerWinner=checkBoardLines(playerWinner);
        }
        if(playerWinner.equals("Empty")){
            playerWinner=checkBoardDiagonalRight(playerWinner);
        }
        if(playerWinner.equals("Empty")){
            playerWinner=checkBoardDiagonalLeft(playerWinner);
        }
        if(playerWinner.equals("Empty")){
            playerWinner=checkForTie(playerWinner);
        }
        if(!playerWinner.equals("Empty")){
            if(playerWinner.equals("X")){
                playerWinner="GREEN";
            }else if(playerWinner.equals("O")){
                playerWinner="RED";
            }
            sendDataToDialogBox(playerWinner);
            playerWinner="Empty";
        }
    }

    String checkForTie(String playerWinner){
        int counter=0;

        for(int checkForTie=0; checkForTie<LINE_SIZE*LINE_SIZE; checkForTie++){
            if(board[checkForTie].equals("0")){
                counter++;
            }
        }
        if(counter==0){
            playerWinner="Tie";
        }
        return playerWinner;
    }
    String checkBoardLines(String playerWinner){
        String whichPiece;
        int counter;
        int nextLine;

        for(nextLine=0; nextLine<LINE_SIZE*LINE_SIZE;nextLine+=LINE_SIZE) {
            whichPiece = board[nextLine];
            counter = 0;
            for (int checkLines = nextLine; checkLines < LINE_SIZE+nextLine; checkLines++) {
                if (whichPiece.equals(board[checkLines]) && !whichPiece.equals("0")) {
                    counter++;
                }
            }
            if (counter == LINE_SIZE) {
                playerWinner = whichPiece;
            }
        }
        return playerWinner;
    }

    String checkBoardColumns(String playerWinner){
        String whichPiece;
        int counter;
        int nextLine;

        for(nextLine=0; nextLine<LINE_SIZE;nextLine++) {
            whichPiece = board[nextLine];
            counter = 0;
            for (int checkLines = nextLine; checkLines < LINE_SIZE*LINE_SIZE; checkLines+=LINE_SIZE) {
                if (whichPiece.equals(board[checkLines]) && !whichPiece.equals("0")) {
                    counter++;
                }
            }
            if (counter == LINE_SIZE) {
                playerWinner = whichPiece;
            }
        }
        return playerWinner;
    }
    String checkBoardDiagonalRight(String playerWinner){
        String whichPiece;
        int counter;
        whichPiece = board[LINE_SIZE-1];
        counter=0;
        for (int checkLines = LINE_SIZE-1; checkLines < LINE_SIZE*LINE_SIZE-1; checkLines+=LINE_SIZE-1) {
            if (whichPiece.equals(board[checkLines]) && !whichPiece.equals("0")) {
                counter++;
            }
        }
        if (counter == LINE_SIZE) {
            playerWinner = whichPiece;
        }
        return playerWinner;
    }
    String checkBoardDiagonalLeft(String playerWinner){
        String whichPiece;
        int counter;

        whichPiece = board[0];
        counter = 0;
        for (int checkLines = 0; checkLines < LINE_SIZE*LINE_SIZE; checkLines+=LINE_SIZE+1) {
            if (whichPiece.equals(board[checkLines]) && !whichPiece.equals("0")) {
                counter++;
            }
        }
        if (counter == LINE_SIZE) {
            playerWinner = whichPiece;
        }
        return playerWinner;
    }
    void cleanBoardComplete(){
        for(int clean=0; clean<LINE_SIZE*LINE_SIZE; clean++){
            board[clean]="0";
            playerTurn=false;
            boardButton[clean].setBackgroundResource(android.R.drawable.btn_default);
            boardButton[clean].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    placePiece(v);
                }
            });
        }
    }

}
