package nz.ac.arastudent.eyeballmazeassignment2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import nz.ac.arastudent.eyeballmazeassignment2.model.IGame;
import nz.ac.arastudent.eyeballmazeassignment2.model.Model;

public class ProgrammaticalActivity extends MainActivity {
    public IGame myModel = new Model();
    SharedPreferences sharedPreferences = null;
    public Button[][] buttons = new Button[6][4];
    ToggleButton soundToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programmatical);

        Toolbar toolbar = findViewById(R.id.game_toolbar);
        setSupportActionBar(toolbar);

        TextView textView = findViewById(R.id.GoalCounter);
        textView.setText("0");

        TextView movesLeft = findViewById(R.id.movesLeft);
        movesLeft.setText(myModel.getMovesLeft().toString());

        startSong(R.raw.gtasa);
        this.initialiseGame();

    }

    private void createButtons(){
        Context context = getApplicationContext();
        GridLayout gridLayout = findViewById(R.id.GameLayout);

        for(int y = 0; y < this.buttons.length; y++) {
            for (int x = 0; x < this.buttons[y].length; x++) {
                Button btn = new Button(context);
                String id = (Integer.toString(y) + Integer.toString(x));
                btn.setId(Integer.parseInt(id));


                ViewGroup.LayoutParams params = buttons[y][x].getLayoutParams();
                btn.setLayoutParams(new TableRow.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                params.width = R.integer.buttonSize;
                params.height = R.integer.buttonSize;
               // buttons[y][x].setLayoutParams(gridLayout.LayoutParams(x, y) );
                buttons[y][x] = btn;
            }
        }
    }
    protected void startSong(int song) {
        MediaPlayer gameSong = MediaPlayer.create(this, song);
        gameSong.setAudioStreamType(AudioManager.STREAM_MUSIC);
        gameSong.setLooping(true);
        gameSong.start();

        soundToggle = findViewById(R.id.soundToggle);
        soundToggle.isChecked();
        soundToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soundToggle.isChecked()) {
                    unmute();
                } else {
                    mute();
                }
            }
        });
    }
    protected void mute() {
        //mute audio
        AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
    }


    protected void unmute() {
        //unmute audio
        AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        amanager.setStreamMute(AudioManager.STREAM_MUSIC, false);
    }


    private void readALevel() {
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.level);

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            int j = 0;
            while ((line = bufferedReader.readLine()) != null) {
                String[] line2 = line.split(",");
                for (int i=0; i<line2.length; i++) {
                    line2[i] = line2[i].replace("\"", "");
                    myModel.setMazeCharacter(i, j, line2[i]);
                }
                j++;
            }
            //don't forget to close!
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        myModel.setMoveCount("0");
        myModel.setMovesLeft("10");
    }

    private void loadLevel() {
        this.sharedPreferences = getSharedPreferences("nz.ac.arastudent.eyeballmazeassignment2.savedLevel.txt", Context.MODE_PRIVATE);
        String map = sharedPreferences.getString("theMap", "None");
        System.out.println(map);
        String[] rows = map.split(":");

        int y = 0;
        for (String aRow : rows){
            String[] blocks = aRow.split(",");

            int x = 0;
            for (String aBlock : blocks){
                System.out.println(aBlock + "()");
                myModel.setMazeCharacter(x, y, aBlock);
                x++;
            }
            y++;
        }

        myModel.setMoveCount(sharedPreferences.getString("movesLeft", "None"));
        myModel.setGoalCount(sharedPreferences.getString("goalsLeft", "None"));
        myModel.setMovesLeft(sharedPreferences.getString("movesLeft", "None"));
        myModel.setMoveCount(sharedPreferences.getString("moveCount", "None"));
        Toast.makeText(ProgrammaticalActivity.this, "Game Loaded!", Toast.LENGTH_SHORT).show();
    }

    private void saveLevel() {
        String[][] currentState = myModel.getGameMap();
        String myMap = "";

        for (int y = 0; y < currentState.length; ++y) {
            for (int x = 0; x < currentState[y].length; ++x) {
                String pos = currentState[y][x];
                myMap += pos;
                if (x != currentState[y].length) {
                    myMap += ",";
                }
            }
            if (y != currentState.length - 1){
                myMap += ":";
            }
        }

        this.sharedPreferences = getSharedPreferences("nz.ac.arastudent.eyeballmazeassignment2.savedLevel.txt", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        //editor.clear();

        editor.putString("theMap", myMap);
        editor.apply();
        editor.putString("goalsLeft", myModel.getGoalCount());
        editor.apply();
        editor.putString("movesLeft", myModel.getMovesLeft().toString());
        editor.apply();
        editor.putString("moveCount", myModel.getMoveCount());
        editor.apply();


        Toast.makeText(ProgrammaticalActivity.this, "Game Saved!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.options, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_load){
            loadLevel();
            this.myModel.updateMaze();
            updateGame();
        }

        if(id == R.id.action_save){
            saveLevel();
        }

        if(id == R.id.action_restart){
            readALevel();
            this.myModel.updateMaze();
            updateGame();
        }

        if(id == R.id.action_help){
            Intent helpIntent = new Intent(ProgrammaticalActivity.this, HelpActivity.class);
            startActivity(helpIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateGame(){
        for(int y = 0; y < this.buttons.length; y++){
            for(int x = 0; x < this.buttons[y].length; x++){
                Button aButton = this.buttons[y][x];
                aButton.setText(this.myModel.getItem(x, y));
            }
        }
        TextView textView = findViewById(R.id.GoalCounter);
        textView.setText(myModel.getGoalCount());

        TextView moveCounter = findViewById(R.id.MoveCounter);
        moveCounter.setText(myModel.getMoveCount());

        TextView movesLeft = findViewById(R.id.movesLeft);
        movesLeft.setText(myModel.getMovesLeft().toString());
    }

    public void checkMove(int x, int y){
        this.myModel.updateMaze();
        Integer[] currentPos = this.myModel.getPlayerLocation();
        int currentX = currentPos[0];
        int currentY = currentPos[1];
        String direction = "";
        int distance = 0;
        if (x == currentX && y == currentY){
            Toast.makeText(getApplicationContext(),
                    "You are already on this position", Toast.LENGTH_SHORT).show();
        }
        // else if (y < currentY && x < currentX || y > currentY && x > currentX) {
        //   Toast.makeText(getApplicationContext(),
        //         "You can only move left, right or forward", Toast.LENGTH_SHORT).show();
        //}
        else if (y < currentY && x < currentX || y < currentY && x > currentX || y > currentY && x < currentX || y > currentY && x > currentX) {
            Toast.makeText(getApplicationContext(),
                    "You can't move diagonal, only left, right or forward.", Toast.LENGTH_SHORT).show();
        }
        else {
            if (y < currentY) {
                direction = "W";
                distance = currentY - y;
            } else if (y > currentY) {
                direction = "S";
                distance = y - currentY;
            } else if (x < currentX) {
                direction = "A";
                distance = currentX - x;
            } else if (x > currentX) {
                direction = "D";
                distance = x - currentX;
            }

            //check move isn't backwards
            String isBackwards = this.myModel.makeMove(direction, distance);

            if (isBackwards != "") {
                Toast.makeText(getApplicationContext(),
                        isBackwards, Toast.LENGTH_SHORT).show();
            }
            //check if complete
            if (myModel.isComplete()){
                if(Integer.parseInt(myModel.getGoalCount()) == 0) {
                    gameWonDialog();
                } else if (myModel.getMovesLeft() == 0) {
                    gameLostDialog();
                }
            }
            updateGame();
        }
    }

    public void initialiseGame(){
        this.myModel.updateMaze();

        TextView textView = findViewById(R.id.GoalCounter);
        textView.setText(myModel.getGoalCount());

        TextView moveCounter = findViewById(R.id.MoveCounter);
        moveCounter.setText(myModel.getMoveCount());

        TableLayout table = findViewById(R.id.mainTable);
        for(int y = 0; y < this.buttons.length; y++){
            TableRow row = new TableRow(this);
            for(int x = 0; x < this.buttons[y].length; x++){
//                this.buttons[y][x] = new Button(this);
//                Button aButton = this.buttons[x][y];
//                aButton.setText(this.myModel.getItem(x, y));
//
//                final int weirdX = x;
//                final int weirdY = y;
                Button btn = new Button(this);
                btn.setText(this.myModel.getItem(x, y));

                TableRow.LayoutParams tr = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                tr.weight = 0;
                btn.setLayoutParams(tr);
                btn.setText(this.myModel.getItem(x, y));
                btn.setWidth(150);
                btn.setHeight(150);
                String id = Integer.toString(y) + Integer.toString(x);
                btn.setId(Integer.parseInt(id));
                row.addView(btn);

                final int weirdX = x;
                final int weirdY = y;
                btn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view){
                        checkMove(weirdX, weirdY);
                    }
                });

                buttons[y][x] = btn;
                System.out.println(buttons[y][x]);
            }
            table.addView(row);
        }
    }

    public void gameWonDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.gameWon)
                .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        readALevel();
                        myModel.updateMaze();
                        updateGame();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();

        MediaPlayer gameSong = MediaPlayer.create(this, R.raw.winner);
        gameSong.setAudioStreamType(AudioManager.STREAM_MUSIC);
        gameSong.start();
    }

    public void gameLostDialog() {
        MediaPlayer gameSong = MediaPlayer.create(this, R.raw.loser);
        gameSong.setAudioStreamType(AudioManager.STREAM_MUSIC);
        gameSong.start();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.gameLost)
                .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        readALevel();
                        myModel.updateMaze();
                        updateGame();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
