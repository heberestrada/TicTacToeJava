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


    Integer LINE_SIZE = 3;
    String PLAYER_PIECE="X";
    String[] board = new String[LINE_SIZE * LINE_SIZE];
    Integer[] playerBoard = new Integer[LINE_SIZE * LINE_SIZE];
    Integer[] computerBoard = new Integer[LINE_SIZE * LINE_SIZE];
    Integer[] emptySpots=new Integer[LINE_SIZE];
    int firstShot=-1;
    int playerMax=0;
    int computerMax=0;
    int posPlayerMax=0;
    int center=50;
    int posComputerMax=0;
    Boolean playerTurn = false;

    Button[] boardButton = new Button[LINE_SIZE * LINE_SIZE];
    Button renewGame;

    final int COMPUTER_TURN = 1;
    final int DEPTH = 0;

    int nextAiMove;
    String gameOver="no";
    int fillEmptyBoard = 0;
    boolean startedFilling = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createBoard();
    }

    void createBoard() {

        boardButton[0] = findViewById(R.id.firstSpot);
        boardButton[1] = findViewById(R.id.secondSpot);
        boardButton[2] = findViewById(R.id.thirdSpot);
        boardButton[3] = findViewById(R.id.fourthSpot);
        boardButton[4] = findViewById(R.id.fifthSpot);
        boardButton[5] = findViewById(R.id.sixthSpot);
        boardButton[6] = findViewById(R.id.seventhSpot);
        boardButton[7] = findViewById(R.id.eightSpot);
        boardButton[8] = findViewById(R.id.nineSpot);
        for (int fill = 0; fill < LINE_SIZE * LINE_SIZE; fill++) {
            board[fill] = "0";
            boardButton[fill].setId(fill);
            boardButton[fill].setBackgroundResource(android.R.drawable.btn_default);
            boardButton[fill].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    placePiece(v);
                }
            });
        }
        renewGame = findViewById(R.id.restart);
        renewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanBoardComplete();
            }
        });
    }

    void placePiece(View v) {
        v.setOnClickListener(null);
        if (!playerTurn) {
            if(firstShot==-1){
                firstShot=v.getId();
            }
            board[v.getId()] = "X";
            v.setBackgroundColor(getResources().getColor(R.color.colorPlayer1));
            playerTurn = true;
        }
        cleanEmptyAIBoard();


        checkCompleteBoard();
        if(gameOver.equals("no")) {
            computerPlays(v);
            checkCompleteBoard();
        }


    }

    void sendDataToDialogBox(String winner) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogue_winner);

        TextView winnerTxt = dialog.findViewById(R.id.winnerPlayer);
        TextView defaultText = dialog.findViewById(R.id.textView);

        if (winner.equals("Tie")) {
            defaultText.setText("No winner it is a:");
        }
        for (int cleanBoard = 0; cleanBoard < LINE_SIZE * LINE_SIZE; cleanBoard++) {
            boardButton[cleanBoard].setOnClickListener(null);
        }
        winnerTxt.setText(winner);
        dialog.show();
    }

    void checkCompleteBoard() {
        String playerWinner = "Empty";

        playerWinner = checkBoardColumns(playerWinner, board);
        if (playerWinner.equals("Empty")) {
            playerWinner = checkBoardLines(playerWinner, board);
        }
        if (playerWinner.equals("Empty")) {
            playerWinner = checkBoardDiagonalRight(playerWinner, board);
        }
        if (playerWinner.equals("Empty")) {
            playerWinner = checkBoardDiagonalLeft(playerWinner, board);
        }
        if (playerWinner.equals("Empty")) {
            playerWinner = checkForTie(playerWinner, board);
        }
        if (!playerWinner.equals("Empty")) {
            if (playerWinner.equals("X")) {
                playerWinner = "GREEN";
            } else if (playerWinner.equals("O")) {
                playerWinner = "RED";
            }
            sendDataToDialogBox(playerWinner);
            gameOver="yes";
            playerWinner = "Empty";
        }
    }


    String checkForTie(String playerWinner, String[] boardCheck) {
        int counter = 0;

        for (int checkForTie = 0; checkForTie < LINE_SIZE * LINE_SIZE; checkForTie++) {
            if (boardCheck[checkForTie].equals("0")) {
                counter++;
            }
        }
        if (counter == 0) {
            playerWinner = "Tie";
        }
        return playerWinner;
    }

    String checkBoardLines(String playerWinner, String[] boardCheck) {
        String whichPiece;
        int counter;
        int nextLine;

        for (nextLine = 0; nextLine < LINE_SIZE * LINE_SIZE; nextLine += LINE_SIZE) {
            whichPiece = boardCheck[nextLine];
            counter = 0;
            for (int checkLines = nextLine; checkLines < LINE_SIZE + nextLine; checkLines++) {
                if (whichPiece.equals(boardCheck[checkLines]) && !whichPiece.equals("0")) {
                    counter++;
                }
            }
            if (counter == LINE_SIZE) {
                playerWinner = whichPiece;
            }
        }
        return playerWinner;
    }

    String checkBoardColumns(String playerWinner, String[] boardCheck) {
        String whichPiece;
        int counter;
        int nextLine;

        for (nextLine = 0; nextLine < LINE_SIZE; nextLine++) {
            whichPiece = boardCheck[nextLine];
            counter = 0;
            for (int checkLines = nextLine; checkLines < LINE_SIZE * LINE_SIZE; checkLines += LINE_SIZE) {
                if (whichPiece.equals(boardCheck[checkLines]) && !whichPiece.equals("0")) {
                    counter++;
                }
            }
            if (counter == LINE_SIZE) {
                playerWinner = whichPiece;
            }
        }
        return playerWinner;
    }

    String checkBoardDiagonalRight(String playerWinner, String[] boardCheck) {
        String whichPiece;
        int counter;
        whichPiece = boardCheck[LINE_SIZE - 1];
        counter = 0;
        for (int checkLines = LINE_SIZE - 1; checkLines < LINE_SIZE * LINE_SIZE - 1; checkLines += LINE_SIZE - 1) {
            if (whichPiece.equals(boardCheck[checkLines]) && !whichPiece.equals("0")) {
                counter++;
            }
        }
        if (counter == LINE_SIZE) {
            playerWinner = whichPiece;
        }
        return playerWinner;
    }

    String checkBoardDiagonalLeft(String playerWinner, String[] boardCheck) {
        String whichPiece;
        int counter;

        whichPiece = boardCheck[0];
        counter = 0;
        for (int checkLines = 0; checkLines < LINE_SIZE * LINE_SIZE; checkLines += LINE_SIZE + 1) {
            if (whichPiece.equals(boardCheck[checkLines]) && !whichPiece.equals("0")) {
                counter++;
            }
        }
        if (counter == LINE_SIZE) {
            playerWinner = whichPiece;
        }
        return playerWinner;
    }

    void cleanBoardComplete() {
        gameOver="no";
        firstShot=0;
        for (int clean = 0; clean < LINE_SIZE * LINE_SIZE; clean++) {
            board[clean] = "0";
            playerTurn = false;
            boardButton[clean].setBackgroundResource(android.R.drawable.btn_default);
            boardButton[clean].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    placePiece(v);
                }
            });
        }
    }

    void computerPlays(View v) {
        minMax();
        int aiMove=checkAiMove();
        board[aiMove] = "O";
        boardButton[aiMove].setBackgroundColor(getResources().getColor(R.color.colorPlayer2));
        boardButton[aiMove].setOnClickListener(null);
        playerTurn = false;
    }

    void cleanEmptyAIBoard() {
        for (int clean = 0; clean < LINE_SIZE * LINE_SIZE; clean++) {
            playerBoard[clean] = 0;
            computerBoard[clean] = 0;
        }
        playerMax=0;
        computerMax=0;
        startedFilling = false;
        fillEmptyBoard = 0;
    }

    void cleanEmptySpots(){
        for(int clean=0;clean<LINE_SIZE;clean++){
            emptySpots[clean]=0;
        }
    }

    void minMax() {

        int howManyX=0;
        int howManyO=0;
        int spaces=0;
        int score=0;
        int score2=0;
        center=50;
        boolean lineiSNotEmpty=false;

        //CheckLines
        for(int checkLinesAi=0; checkLinesAi<LINE_SIZE*LINE_SIZE;checkLinesAi+=LINE_SIZE) {
            for (int checkArray = checkLinesAi; checkArray < LINE_SIZE + checkLinesAi; checkArray++) {

                if (board[checkArray].equals("X")) {
                    howManyX++;
                    lineiSNotEmpty = true;
                } else if (board[checkArray].equals("O")) {
                    howManyO++;
                    lineiSNotEmpty = true;
                } else {
                    emptySpots[spaces] = checkArray;
                    spaces++;
                }
            }

            if (!lineiSNotEmpty) {
                cleanEmptySpots();
            }
            lineiSNotEmpty = false;


            for (int checkPieces = 0; checkPieces < spaces; checkPieces++) {
                if (emptySpots[checkPieces] != 0 && emptySpots[checkPieces] % 2 != 0 && emptySpots[checkPieces] != LINE_SIZE + 1) {
                    score = center * howManyX;
                    score2 = center * howManyO;
                    if (playerBoard[emptySpots[checkPieces]] < score) {
                        playerBoard[emptySpots[checkPieces]] = score;
                    }
                    if (computerBoard[emptySpots[checkPieces]] < score2) {
                        computerBoard[emptySpots[checkPieces]] = score2;
                    }
                } else if (emptySpots[checkPieces] == LINE_SIZE + 1) {
                    score = 90 * howManyX;
                    score2 = 90 * howManyO;
                    if (playerBoard[emptySpots[checkPieces]] < score) {
                        playerBoard[emptySpots[checkPieces]] = score;
                    }
                    if (computerBoard[emptySpots[checkPieces]] < score2) {
                        computerBoard[emptySpots[checkPieces]] = score2;
                    }
                } else if (emptySpots[checkPieces] == 0 || emptySpots[checkPieces] % 2 == 0) {
                    score = 75 * howManyX;
                    score2 = 75 * howManyO;
                    if (playerBoard[emptySpots[checkPieces]] < score) {
                        playerBoard[emptySpots[checkPieces]] = score;
                    }
                    if (computerBoard[emptySpots[checkPieces]] < score2) {
                        computerBoard[emptySpots[checkPieces]] = score2;
                    }
                }
            }
            spaces = 0;
            howManyO = 0;
            howManyX = 0;
        }
        //CheckColumns
        for(int checkColumnsAi=0; checkColumnsAi<LINE_SIZE;checkColumnsAi++) {
            for (int checkArray = checkColumnsAi; checkArray < LINE_SIZE + (checkColumnsAi + LINE_SIZE +1); checkArray += LINE_SIZE) {
                if (board[checkArray].equals("X")) {
                    howManyX++;
                    lineiSNotEmpty = true;
                } else if (board[checkArray].equals("O")) {
                    howManyO++;
                    lineiSNotEmpty = true;
                } else {
                    emptySpots[spaces] = checkArray;
                    spaces++;
                }
            }
            if (!lineiSNotEmpty) {
                cleanEmptySpots();
            }
            lineiSNotEmpty = false;
            for (int checkPieces = 0; checkPieces < spaces; checkPieces++) {
                if (emptySpots[checkPieces] != 0 && emptySpots[checkPieces] % 2 != 0 && emptySpots[checkPieces] != LINE_SIZE + 1) {
                    score2 = center * howManyO;
                    score = center * howManyX;
                    if (playerBoard[emptySpots[checkPieces]] < score) {
                        playerBoard[emptySpots[checkPieces]] = score;
                    }
                    if (computerBoard[emptySpots[checkPieces]] < score2) {
                        computerBoard[emptySpots[checkPieces]] = score2;
                    }
                } else if (emptySpots[checkPieces] == LINE_SIZE + 1) {
                    score2 = 90 * howManyO;
                    score = 90 * howManyX;
                    if (playerBoard[emptySpots[checkPieces]] < score) {
                        playerBoard[emptySpots[checkPieces]] = score;
                    }
                    if (computerBoard[emptySpots[checkPieces]] < score2) {
                        computerBoard[emptySpots[checkPieces]] = score2;
                    }
                } else if (emptySpots[checkPieces] == 0 || emptySpots[checkPieces] % 2 == 0) {
                    score2 = 75 * howManyO;
                    score = 75 * howManyX;
                    if (playerBoard[emptySpots[checkPieces]] < score) {
                        playerBoard[emptySpots[checkPieces]] = score;
                    }
                    if (computerBoard[emptySpots[checkPieces]] < score2) {
                        computerBoard[emptySpots[checkPieces]] = score2;
                    }
                }

            }
            howManyO = 0;
            howManyX = 0;
            spaces = 0;
        }
            cleanEmptySpots();

        //CheckDiagonalRight
            for (int checkArray = 0; checkArray < (LINE_SIZE*LINE_SIZE); checkArray+=LINE_SIZE+1) {

                if (board[checkArray].equals("X")) {
                    howManyX++;
                    lineiSNotEmpty = true;
                } else if (board[checkArray].equals("O")) {
                    howManyO++;
                    lineiSNotEmpty = true;
                } else {
                    emptySpots[spaces] = checkArray;
                    spaces++;
                }
            }
            if (!lineiSNotEmpty) {
                cleanEmptySpots();
            }
            lineiSNotEmpty = false;
            for (int checkPieces = 0; checkPieces < spaces; checkPieces++) {
                if(emptySpots[checkPieces]!=0 && emptySpots[checkPieces]%2!=0 && emptySpots[checkPieces]!=LINE_SIZE+1){
                    score2 =center*howManyO;
                    score=center*howManyX;
                    if(playerBoard[emptySpots[checkPieces]]<score) {
                        playerBoard[emptySpots[checkPieces]] = score;
                    }
                    if( computerBoard[emptySpots[checkPieces]]<score2) {
                        computerBoard[emptySpots[checkPieces]] = score2;
                    }
                }else if(emptySpots[checkPieces]==LINE_SIZE+1){
                    score2=90*howManyO;
                    score=90*howManyX;
                    if(playerBoard[emptySpots[checkPieces]]<score) {
                        playerBoard[emptySpots[checkPieces]] = score;
                    }
                    if( computerBoard[emptySpots[checkPieces]]<score2) {
                        computerBoard[emptySpots[checkPieces]] = score2;
                    }
                }else if(emptySpots[checkPieces]==0 || emptySpots[checkPieces]%2==0){
                    score2=75*howManyO;
                    score=75*howManyX;
                    if(playerBoard[emptySpots[checkPieces]]<score) {
                        playerBoard[emptySpots[checkPieces]] = score;
                    }
                    if( computerBoard[emptySpots[checkPieces]]<score2) {
                        computerBoard[emptySpots[checkPieces]] = score2;
                    }
                }
            }
        cleanEmptySpots();
        howManyO=0;
        howManyX=0;
        spaces=0;

        //CheckDiagonalLeft
        for (int checkArray = LINE_SIZE-1; checkArray < (LINE_SIZE*LINE_SIZE)-1; checkArray+=LINE_SIZE-1) {
            if (board[checkArray].equals("X")) {
                howManyX++;
                lineiSNotEmpty = true;
            } else if (board[checkArray].equals("O")) {
                howManyO++;
                lineiSNotEmpty = true;
            } else {
                emptySpots[spaces] = checkArray;
                spaces++;
            }
        }
        if (!lineiSNotEmpty) {
            cleanEmptySpots();
        }
        lineiSNotEmpty = false;
        for (int checkPieces = 0; checkPieces < spaces; checkPieces++) {
            if(emptySpots[checkPieces]!=0 && emptySpots[checkPieces]%2!=0 && emptySpots[checkPieces]!=LINE_SIZE+1){
                score2=center*howManyO;
                score=center*howManyX;
                if(playerBoard[emptySpots[checkPieces]]<score) {
                    playerBoard[emptySpots[checkPieces]] = score;
                }
                if( computerBoard[emptySpots[checkPieces]]<score2) {
                    computerBoard[emptySpots[checkPieces]] = score2;
                }
            }else if(emptySpots[checkPieces]==LINE_SIZE+1){
                score2=90*howManyO;
                score=90*howManyX;
                if(playerBoard[emptySpots[checkPieces]]<score) {
                    playerBoard[emptySpots[checkPieces]] = score;
                }
                if( computerBoard[emptySpots[checkPieces]]<score2) {
                    computerBoard[emptySpots[checkPieces]] = score2;
                }
            }else if(emptySpots[checkPieces]==0 || emptySpots[checkPieces]%2==0){
                score2=75*howManyO;
                score=75*howManyX;
                if(playerBoard[emptySpots[checkPieces]]<score) {
                    playerBoard[emptySpots[checkPieces]] = score;
                }
                if( computerBoard[emptySpots[checkPieces]]<score2) {
                    computerBoard[emptySpots[checkPieces]] = score2;
                }
            }
        }
        cleanEmptySpots();
        howManyO=0;
        howManyX=0;
        spaces=0;


        if((board[LINE_SIZE*LINE_SIZE-2].equals(PLAYER_PIECE) && board[(LINE_SIZE+LINE_SIZE-1)].equals(PLAYER_PIECE))
                || (board[LINE_SIZE*LINE_SIZE-2].equals(PLAYER_PIECE) && board[LINE_SIZE].equals(PLAYER_PIECE))
        ){
            if(board[LINE_SIZE * LINE_SIZE - 1].equals("0")) {
                playerBoard[LINE_SIZE * LINE_SIZE - 1] = 100;
            }
        }
        if((board[LINE_SIZE].equals(PLAYER_PIECE) && board[LINE_SIZE+1-LINE_SIZE].equals(PLAYER_PIECE))
                || (board[LINE_SIZE+1-LINE_SIZE].equals(PLAYER_PIECE) && board[LINE_SIZE+LINE_SIZE-1].equals(PLAYER_PIECE))
        ){
            if(board[0].equals("0")) {
                playerBoard[0] = 100;
            }
        }

        if(board[LINE_SIZE-1].equals(PLAYER_PIECE) && board[LINE_SIZE*LINE_SIZE-2].equals(PLAYER_PIECE)){
            if(board[LINE_SIZE*LINE_SIZE-1].equals("0")) {
                playerBoard[LINE_SIZE * LINE_SIZE - 1] = 100;
            }
        }
        if(board[LINE_SIZE-LINE_SIZE].equals(PLAYER_PIECE) && board[LINE_SIZE*LINE_SIZE-2].equals(PLAYER_PIECE)){
            if(board[LINE_SIZE].equals("0")) {
                playerBoard[LINE_SIZE] = 100;
            }
        }
        if(board[LINE_SIZE*(LINE_SIZE-1)].equals(PLAYER_PIECE) && board[LINE_SIZE+(LINE_SIZE-1)].equals(PLAYER_PIECE)){
            if(board[LINE_SIZE * LINE_SIZE - 1].equals("0")) {
                playerBoard[LINE_SIZE * LINE_SIZE - 1] = 100;
            }
        }
        //Diagonal Case
        if(board[LINE_SIZE-1].equals(PLAYER_PIECE) && board[LINE_SIZE*(LINE_SIZE-1)].equals(PLAYER_PIECE)){
            if(board[LINE_SIZE].equals("0")) {
                playerBoard[LINE_SIZE] = 100;
            }
        }
        if(board[LINE_SIZE-LINE_SIZE].equals(PLAYER_PIECE) && board[LINE_SIZE*LINE_SIZE-1].equals(PLAYER_PIECE)){
            if(board[LINE_SIZE].equals("0")) {
                playerBoard[LINE_SIZE] = 100;
            }
        }
    }



    int checkAiMove() {
        int move = 0;

        for(int checkMax=0; checkMax<LINE_SIZE*LINE_SIZE;checkMax++){
            if(playerBoard[checkMax]>playerBoard[playerMax]){
                playerMax=checkMax;
                if(playerBoard[checkMax]>100){
                    playerBoard[checkMax]=100;
                }
            }
            if(computerBoard[checkMax]>computerBoard[computerMax]){
                computerMax=checkMax;
                if(computerBoard[checkMax]>100){
                    computerBoard[checkMax]=100;
                }
            }
        }

        if(playerBoard[playerMax]>computerBoard[computerMax]){
            move=playerMax;
        }else if(computerBoard[computerMax]>=playerBoard[playerMax]){
            move=computerMax;
        }
        for(int removeScores=0;removeScores<LINE_SIZE*LINE_SIZE;removeScores++){
            playerBoard[removeScores]=0;
            computerBoard[removeScores]=0;
        }
        playerMax=0;
        computerMax=0;

        return move;
    }
}

