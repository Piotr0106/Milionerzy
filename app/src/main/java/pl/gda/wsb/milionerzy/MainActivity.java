package pl.gda.wsb.milionerzy;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button newGame, theBest;
    TextView beginText, result;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = findViewById(R.id.result);
    }

    //rozpoczecie gry - wyswietlenie pytania/pytan
    public void startGame(View view){
        startActivity(new Intent(MainActivity.this,Game.class));

        Intent myI = new Intent(MainActivity.this,Game.class);
        myI.putExtra("high_score",Integer.parseInt(result.getText().toString()));
        startActivity(myI);
    }

    public void showResult(View view){

        back = findViewById(R.id.backImageView);
        newGame = findViewById(R.id.newGameButton);
        theBest = findViewById(R.id.theBestButton);
        beginText = findViewById(R.id.beginTextView);

        newGame.setVisibility(View.INVISIBLE);
        theBest.setVisibility(View.INVISIBLE);
        beginText.setVisibility(View.VISIBLE);
        result.setVisibility(View.VISIBLE);
        back.setVisibility(View.VISIBLE);

        Bundle extras = getIntent().getExtras();
        int best = 0;
        if (extras != null) {
            best = extras.getInt("best");
        }
        result.setText(String.valueOf(best));
    }

    public void back(View view){
        newGame.setVisibility(View.VISIBLE);
        theBest.setVisibility(View.VISIBLE);
        beginText.setVisibility(View.INVISIBLE);
        result.setVisibility(View.INVISIBLE);
        back.setVisibility(View.INVISIBLE);
    }

}