package nz.ac.arastudent.eyeballmazeassignment2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import nz.ac.arastudent.eyeballmazeassignment2.model.IGame;
import nz.ac.arastudent.eyeballmazeassignment2.model.Model;

public class MainActivity extends AppCompatActivity {

    Button[][] buttons = new Button[6][4];

    public IGame myModel = new Model();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set tool bar
        Toolbar myToolbar = findViewById(R.id.game_toolbar);
        setSupportActionBar(myToolbar);

        TextView textView = findViewById(R.id.GoalCounter);
        textView.setText("0");


        //Set button
        buttons[0][0] = (Button)findViewById(R.id.grid00);
        buttons[0][1] = (Button)findViewById(R.id.grid10);
        buttons[0][2] = (Button)findViewById(R.id.grid20);
        buttons[0][3] = (Button)findViewById(R.id.grid30);

        buttons[1][0] = (Button)findViewById(R.id.grid01);
        buttons[1][1] = (Button)findViewById(R.id.grid11);
        buttons[1][2] = (Button)findViewById(R.id.grid21);
        buttons[1][3] = (Button)findViewById(R.id.grid31);

        buttons[2][0] = (Button)findViewById(R.id.grid02);
        buttons[2][1] = (Button)findViewById(R.id.grid12);
        buttons[2][2] = (Button)findViewById(R.id.grid22);
        buttons[2][3] = (Button)findViewById(R.id.grid32);

        buttons[3][0] = (Button)findViewById(R.id.grid03);
        buttons[3][1] = (Button)findViewById(R.id.grid13);
        buttons[3][2] = (Button)findViewById(R.id.grid23);
        buttons[3][3] = (Button)findViewById(R.id.grid33);

        buttons[4][0] = (Button)findViewById(R.id.grid04);
        buttons[4][1] = (Button)findViewById(R.id.grid14);
        buttons[4][2] = (Button)findViewById(R.id.grid24);
        buttons[4][3] = (Button)findViewById(R.id.grid34);

        buttons[5][0] = (Button)findViewById(R.id.grid05);
        buttons[5][1] = (Button)findViewById(R.id.grid15);
        buttons[5][2] = (Button)findViewById(R.id.grid25);
        buttons[5][3] = (Button)findViewById(R.id.grid35);

        this.initialiseGame();
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

        }

        if(id == R.id.action_save){

        }

        if(id == R.id.action_restart){

        }

        if(id == R.id.action_help){
            Intent helpIntent = new Intent(MainActivity.this, HelpActivity.class);
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

            this.myModel.updateMaze();


            //check move isn't backwards
            String isBackwards = this.myModel.makeMove(direction, distance);

            if (isBackwards != "") {
                Toast.makeText(getApplicationContext(),
                        isBackwards, Toast.LENGTH_SHORT).show();
            }
            //check if complete
            if (myModel.isComplete()){
                Toast.makeText(getApplicationContext(),
                        "You have solved it", Toast.LENGTH_SHORT).show();
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

        for(int y = 0; y < this.buttons.length; y++){
            for(int x = 0; x < this.buttons[y].length; x++){
                Button aButton = this.buttons[y][x];
                aButton.setText(this.myModel.getItem(x, y));
                final int weirdX = x;
                final int weirdY = y;
                aButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view){
                        checkMove(weirdX, weirdY);
                    }
                });
            }
        }
    }


}
