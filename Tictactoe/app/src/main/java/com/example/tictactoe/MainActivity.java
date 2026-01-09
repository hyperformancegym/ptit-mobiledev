package com.example.tictactoe;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    int winner =-1;
    int startGame= 0;

    Button bt1,bt2,bt3,bt4,bt5,bt6,bt7,bt8,bt9;
    Button btPlay;
    TextView showResult;
    int ActivePlayer = 1;
    ArrayList<Integer> player1 = new ArrayList<>();
    ArrayList<Integer> player2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        anhXa();

    }

    public void anhXa(){
        bt1 = findViewById(R.id.bt1);
        bt2 = findViewById(R.id.bt2);
        bt3 = findViewById(R.id.bt3);

        bt4 = findViewById(R.id.bt4);
        bt5 = findViewById(R.id.bt5);
        bt6 = findViewById(R.id.bt6);

        bt7 = findViewById(R.id.bt7);
        bt8 = findViewById(R.id.bt8);
        bt9 = findViewById(R.id.bt9);

        btPlay = findViewById(R.id.btnStart);

        showResult = findViewById(R.id.showResult);


        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(startGame==1) {
                    playAgain();
                    startGame = 0;
                    btPlay.setText("Bắt đầu");
                }
                else if(startGame==0) {
                    btPlay.setText("Chơi lại");
                    startGame=1;
                }
            }
        });


    }

    private void playAgain() {
        player1.clear();
        player2.clear();

        winner = -1;

        bt1.setText("");
        bt1.setBackgroundColor(Color.rgb(188,185,185));

        bt2.setText("");
        bt2.setBackgroundColor(Color.rgb(188,185,185));

        bt3.setText("");
        bt3.setBackgroundColor(Color.rgb(188,185,185));

        bt4.setText("");
        bt4.setBackgroundColor(Color.rgb(188,185,185));

        bt5.setText("");
        bt5.setBackgroundColor(Color.rgb(188,185,185));

        bt6.setText("");
        bt6.setBackgroundColor(Color.rgb(188,185,185));

        bt7.setText("");
        bt7.setBackgroundColor(Color.rgb(188,185,185));

        bt8.setText("");
        bt8.setBackgroundColor(Color.rgb(188,185,185));

        bt9.setText("");
        bt9.setBackgroundColor(Color.rgb(188,185,185));

        showResult.setVisibility(View.INVISIBLE);
    }


    public void btClick(View view) {
        int id = view.getId();
        int cellId =0;
        switch (id) {
            case R.id.bt1:
                cellId = 1;
                break;
            case R.id.bt2:
                cellId = 2;
                break;
            case R.id.bt3:
                cellId = 3;
                break;
            case R.id.bt4:
                cellId = 4;
                break;
            case R.id.bt5:
                cellId = 5;
                break;
            case R.id.bt6:
                cellId = 6;
                break;
            case R.id.bt7:
                cellId = 7;
                break;
            case R.id.bt8:
                cellId = 8;
                break;
            case R.id.bt9:
                cellId = 9;
                break;
            default:
                break;
        }
        if(winner==-1 && startGame==1) {
            playGame(cellId, (Button) view);
        }

    }

    private void playGame(int cellId, Button btn) {
        if(ActivePlayer==1) {
            btn.setText("X");
            btn.setBackgroundColor(Color.GREEN);
            btn.setTextColor(Color.RED);
            player1.add(cellId);
            ActivePlayer = 2;
        }
        else {
            btn.setText("O");
            btn.setBackgroundColor(Color.BLUE);
            btn.setTextColor(Color.WHITE);
            player2.add(cellId);
            ActivePlayer = 1;
        }
        checkWinner();
        if (winner == 1) {
            showResult.setText("Player 1 thắng");
            showResult.setVisibility(View.VISIBLE);
        } else if (winner == 2) {
            showResult.setText("Player 2 thắng");
            showResult.setVisibility(View.VISIBLE);
        }
        else if(winner==0){
            showResult.setText("Hòa");
            showResult.setVisibility(View.VISIBLE);
        }
    }

    private void checkWinner() {
        // 3 ke
        for (int i = 0; i < 3; i++) {
            // cot
            if(player1.contains(i+1) && player1.contains(i+4) && player1.contains(i+7)) {
                winner = 1;
            }
            if(player2.contains(i+1) && player2.contains(i+4) && player2.contains(i+7)) {
                winner = 2;
            }

            // hang
            int r = i * 3 + 1; // bat dau 1, 4, 7
            if(player1.contains(r) && player1.contains(r+1) && player1.contains(r+2)) {
                winner = 1;
            }
            if(player2.contains(r) && player2.contains(r+1) && player2.contains(r+2)) {
                winner = 2;
            }
        }

        // cheo
        if(player1.contains(1) && player1.contains(5) && player1.contains(9)) {
            winner = 1;
        }
        if(player2.contains(1) && player2.contains(5) && player2.contains(9)) {
            winner = 2;
        }
        if(player1.contains(3) && player1.contains(5) && player2.contains(7)) {
            winner = 1;
        }
        if(player2.contains(3) && player2.contains(5) && player2.contains(7)) {
            winner = 2;
        }

        // hoa
        if (player1.size() + player2.size() == 9 && winner == -1) {
            winner = 0;
        }

    }


}
