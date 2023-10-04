package com.example.akasztofa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button buttonMinus;
    private Button buttonPlus;
    private TextView actualLetter;
    private Button guess;
    private ImageView lifePictures;
    private TextView guessingWord;
    private int elet;
    private int betuIndex;
    private final char[] betuk = {'A', 'Á', 'B', 'C', 'D', 'E', 'É', 'F', 'G', 'H', 'I', 'Í', 'J', 'K', 'L', 'M', 'N', 'O', 'Ó', 'Ö', 'Ő', 'P', 'Q', 'R', 'S', 'T', 'U', 'Ú', 'Ü', 'Ű', 'V', 'W', 'X', 'Y', 'Z'};
    private int betukHossz;
    private String tippeltBetuk;
    private final String[] szavak = {"ALMA", "KUTYA", "ASZTAL", "HÁZ", "VIRÁG", "AUTÓ", "ISKOLA", "AJTÓ", "KÖNYV", "ABLAK", "FA", "ORSZÁG", "EMBER", "SZÉK", "SZÁMÍTÓGÉP", "TELEVÍZIÓ", "MADÁR", "ISKOLA", "ÚT", "ÚJSÁG"};
    private List<String> actualWord;
    private String actualWordString;
    private String word;
    private AlertDialog.Builder gameOver;
    private CountDownTimer timer;
    private CountDownTimer timer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        buttonPlus.setOnClickListener(view -> {
            if (betuIndex >=  betukHossz - 1) {
                betuIndex = 0;
                actualLetter.setText(String.valueOf(betuk[betuIndex]));
            }
            else {
                betuIndex++;
                actualLetter.setText(String.valueOf(betuk[betuIndex]));
            }
            betuCheck();
        });

        buttonMinus.setOnClickListener(view -> {
            if (betuIndex == 0) {
                betuIndex = betukHossz - 1;
                actualLetter.setText(String.valueOf(betuk[betuIndex]));
            }
            else {
                betuIndex--;
                actualLetter.setText(String.valueOf(betuk[betuIndex]));
            }
            betuCheck();
        });

        guess.setOnClickListener(view -> {
            ellenorzes();
            betuCheck();
        });
    }

    public void init() {
        buttonMinus = findViewById(R.id.buttonMinus);
        buttonPlus = findViewById(R.id.buttonPlus);
        actualLetter = findViewById(R.id.actualLetter);
        guess = findViewById(R.id.guess);
        lifePictures = findViewById(R.id.lifePictures);
        guessingWord = findViewById(R.id.guessingWord);
        elet = 13;
        betuIndex = 0;
        betukHossz = Integer.parseInt(String.valueOf(betuk.length));
        timer = new CountDownTimer(500, 500) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                gameOver.setTitle("Nem sikerült kitalálni!").create();
                gameOver.show();
            }
        };

        timer2 = new CountDownTimer(500, 500) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                gameOver.setTitle("Helyes megfejtés!").create();
                gameOver.show();
            }
        };
        actualWord = new ArrayList<>();
        actualWordString = "";
        tippeltBetuk = "";
        word = String.valueOf(randomWord());
        actualWordInit(word.length());
        changeActualWord();

        gameOver = new AlertDialog.Builder(this);
        gameOver.setMessage("Szeretnél még egyet játszani?")
                .setPositiveButton("NEM", (dialogInterface, i) -> {
                    finish();
                })
                .setNegativeButton("IGEN", (dialogInterface, i) -> {
                    newGame();
                })
                .setCancelable(false)
                .create();
    }

    public String randomWord() {
        Random random = new Random();
        int wordIndex = random.nextInt(16);

        return szavak[wordIndex];
    }

    public void actualWordInit(int darab) {
        for (int i = 0; i < darab; i++) {
            actualWord.add("_ ");
        }
    }

    public void betuCheck() {
        if (tippeltBetuk.length() != 0) {
            if (tippeltBetuk.contains(String.valueOf(betuk[betuIndex])) && word.contains(String.valueOf(betuk[betuIndex]))) {
                actualLetter.setTextColor(getResources().getColor(R.color.green));
                guess.setActivated(false);
            }
            else if (tippeltBetuk.contains(String.valueOf(betuk[betuIndex])) && !word.contains(String.valueOf(betuk[betuIndex]))) {
                actualLetter.setTextColor(getResources().getColor(R.color.black));
                guess.setActivated(false);
            }
            else {
                actualLetter.setTextColor(getResources().getColor(R.color.red));
                guess.setActivated(true);
            }
        }
        else {
            actualLetter.setTextColor(getResources().getColor(R.color.red));
            guess.setActivated(true);
        }
    }

    public void ellenorzes() {
        if (actualWordString.contains("_")) {
            if (!tippeltBetuk.contains(String.valueOf(betuk[betuIndex])) && word.contains(String.valueOf(betuk[betuIndex]))) {
                Toast.makeText(this, "Jó", Toast.LENGTH_SHORT).show();
                tippeltBetuk += String.valueOf(betuk[betuIndex]);
                for (int i = 0; i < word.length(); i++) {
                    if (word.charAt(i) == betuk[betuIndex]) {
                        actualWord.set(i, String.valueOf(betuk[betuIndex]));
                    }
                }
                changeActualWord();
            }
            else if (tippeltBetuk.contains(String.valueOf(betuk[betuIndex]))) {
                Toast.makeText(this, "Már tippelt betű.", Toast.LENGTH_SHORT).show();
            }
            else {
                if (elet != 0) {
                    Toast.makeText(this, "Rossz", Toast.LENGTH_SHORT).show();
                    elet--;
                    tippeltBetuk += String.valueOf(betuk[betuIndex]);
                    kepChange();

                }
                if (elet == 0) {
                    kepChange();
                    timer.start();
                }
            }
        }
        if (!actualWordString.contains("_")) {
            timer2.start();
        }
    }

    public void changeActualWord() {
        actualWordString = "";
        for (String s : actualWord) {
            actualWordString += s;
        }

        guessingWord.setText(actualWordString);
    }

    public void kepChange() {
        switch (elet) {
            case 13:
                lifePictures.setImageResource(R.drawable.akasztofa00);
                break;
            case 12:
                lifePictures.setImageResource(R.drawable.akasztofa01);
                break;
            case 11:
                lifePictures.setImageResource(R.drawable.akasztofa02);
                break;
            case 10:
                lifePictures.setImageResource(R.drawable.akasztofa03);
                break;
            case 9:
                lifePictures.setImageResource(R.drawable.akasztofa04);
                break;
            case 8:
                lifePictures.setImageResource(R.drawable.akasztofa05);
                break;
            case 7:
                lifePictures.setImageResource(R.drawable.akasztofa06);
                break;
            case 6:
                lifePictures.setImageResource(R.drawable.akasztofa07);
                break;
            case 5:
                lifePictures.setImageResource(R.drawable.akasztofa08);
                break;
            case 4:
                lifePictures.setImageResource(R.drawable.akasztofa09);
                break;
            case 3:
                lifePictures.setImageResource(R.drawable.akasztofa10);
                break;
            case 2:
                lifePictures.setImageResource(R.drawable.akasztofa11);
                break;
            case 1:
                lifePictures.setImageResource(R.drawable.akasztofa12);
                break;
            case 0:
                lifePictures.setImageResource(R.drawable.akasztofa13);
                break;
        }
    }

    public void newGame() {
        elet = 13;
        tippeltBetuk = "";
        word = String.valueOf(randomWord());
        betuIndex = 0;
        actualLetter.setText(String.valueOf(betuk[betuIndex]));
        actualWord.clear();
        actualWordInit(word.length());
        changeActualWord();
        kepChange();
        betuCheck();
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("elet", elet);
        outState.putString("tippeltbetuk", tippeltBetuk);
        outState.putString("word", word);
        outState.putInt("betuIndex", betuIndex);
        outState.putString("actualLetterText", actualLetter.getText().toString());
        outState.putStringArrayList("actualWord", new ArrayList<>(actualWord));
    }

    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        elet = savedInstanceState.getInt("elet");
        tippeltBetuk = savedInstanceState.getString("tippeltbetuk");
        word = savedInstanceState.getString("word");
        betuIndex = savedInstanceState.getInt("betuIndex");
        actualLetter.setText(savedInstanceState.getString("actualLetterText"));
        actualWord = savedInstanceState.getStringArrayList("actualWord");
        changeActualWord();
        kepChange();
        betuCheck();
    }
}