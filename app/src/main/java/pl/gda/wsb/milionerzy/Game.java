package pl.gda.wsb.milionerzy;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class Game extends AppCompatActivity {
    Handler handler = new Handler();
    Button yes, no, giveUpButton;
    TextView question, a, b, c, d;
    ImageView fifty, change;
    TableLayout levels;
    ArrayList<String> lines = new ArrayList<String>();
    ArrayList<ArrayList<String>> questions_cloned = new ArrayList<ArrayList<String>>();
    ArrayList<Question> questions = new ArrayList<Question>();
    ArrayList<Question> drawn_quests = new ArrayList<Question>();
    int questions_line = 0;
    int i = 0;
    int answ= 0;
    ColorDrawable[] color = {new ColorDrawable(Color.rgb(63,81,181)), new ColorDrawable(Color.GREEN)};
    ColorDrawable[] color_red = {new ColorDrawable(Color.rgb(63,81,181)), new ColorDrawable(Color.RED)};
    ArrayList<TextView> tviews = new ArrayList<TextView>();
    ArrayList<TextView> tviews_const = new ArrayList<TextView>();
    boolean is_changed = false;
    boolean is_first_game = true;
    int child=11;
    int curr_best = 0;
    private int best=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        giveUpButton = findViewById(R.id.giveUpButton);
        question= findViewById(R.id.questionTextView);
        a = findViewById(R.id.ATextView);
        b = findViewById(R.id.BTextView);
        c = findViewById(R.id.CTextView);
        d = findViewById(R.id.DTextView);
        fifty = findViewById(R.id.fiftyImageView);
        change = findViewById(R.id.changeImageView);
        levels = findViewById(R.id.levelsTable);
        yes = findViewById(R.id.yesButton);
        no = findViewById(R.id.noButton);

        if(is_first_game) {
            readQuestions();
            is_first_game = false;
            tviews_const.add(a);
            tviews_const.add(b);
            tviews_const.add(c);
            tviews_const.add(d);
            tviews = (ArrayList)tviews_const.clone();
        }
        else {
            draw();
            is_changed=false;
            fifty.setClickable(true);
            fifty.setVisibility(View.VISIBLE);
            change.setClickable(true);
            change.setVisibility(View.VISIBLE);
            giveUpButton.setClickable(true);
            int k=1;
            for (TextView odp : tviews){
                odp.setVisibility(View.VISIBLE);
                odp.setClickable(true);
                odp.setBackgroundColor(getResources().getColor(R.color.def));
                k++;
            }

        }
        i = 0;
        answ = 0;
        child=11;
        assignQuest();

    }

    public void readQuestions(){

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("questions.txt")));

            String mLine;
            while ((mLine = reader.readLine()) != null)
            {
                if (questions_line==6)
                {
                    questions.add(new Question(lines.get(0),lines.get(1),lines.get(2),lines.get(3),lines.get(4),lines.get(5)));
                    lines.clear();
                    questions_line=0;
                }
                else
                {
                    lines.add(mLine);
                    questions_line++;
                }
            }
            questions_cloned = (ArrayList)questions.clone();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),"Error reading file!",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {

                }
            }
        }
        //losowanie pytan
        int nums = 1;
        Random r = new Random();
        while (nums<=13)//12 + 1 pytan
        {
            Question elem = questions.get(r.nextInt(questions.size()));
            drawn_quests.add(elem);
            questions.remove(elem);
            nums++;
        }

    }

    public void draw(){
        drawn_quests.clear();
        questions = (ArrayList)questions_cloned.clone();
        int nums = 1;
        Random r = new Random();
        while (nums<=13)//pytań 12 + 1 (w razie zamiany pytania) (2 łatwe, 6 średnich i 4 trudne) + 1 z każdej -> 15
        {
            Question elem = questions.get(r.nextInt(questions.size()));;
            drawn_quests.add(elem);
            questions.remove(elem);
            nums++;
        }
    }

    public void playAgain(View view){
        draw();
        yes.setVisibility(View.INVISIBLE);
        no.setVisibility(View.INVISIBLE);
        answ=0;
        i=0;
        child=11;
        int k=1;

        assignQuest();
        for (TextView odp : tviews_const){
            odp.setVisibility(View.VISIBLE);
            odp.setClickable(true);
            odp.setBackgroundColor(getResources().getColor(R.color.def));
            k++;
        }
        //czyszczenie tablicy
        for (int c = 0; c < levels.getChildCount(); c++) {
            levels.getChildAt(c).setBackgroundColor(getResources().getColor(R.color.table));
        }
        is_changed=false;
        fifty.setClickable(true);
        fifty.setVisibility(View.VISIBLE);
        change.setClickable(true);
        change.setVisibility(View.VISIBLE);
        giveUpButton.setClickable(true);
    }

    public void assignQuest() {
        question.setText((CharSequence) drawn_quests.get(i).getQuest());
        a.setText((CharSequence) drawn_quests.get(i).getA());
        b.setText((CharSequence) drawn_quests.get(i).getB());
        c.setText((CharSequence) drawn_quests.get(i).getC());
        d.setText((CharSequence) drawn_quests.get(i).getD());
    }

    public void win_actions(View view){
        question.setText("Wygrałeś MILION złotych! Gratulacje!");
        curr_best = 1000000;
        a.setVisibility(View.INVISIBLE);
        b.setVisibility(View.INVISIBLE);
        c.setVisibility(View.INVISIBLE);
        d.setVisibility(View.INVISIBLE);

        handler.postDelayed(new Runnable() {
            public void run() {
                question.setText("Czy chcesz zagrać jeszcze raz?");
                yes.setVisibility(View.VISIBLE);
                no.setVisibility(View.VISIBLE);
            }
        }, 5000);   //10 seconds
    }

    public void prize(){

        if (answ > 1 && answ < 7) {
            question.setText("Wygrałeś 1000 zł! Gratulacje!");
            if(curr_best < 1000)
                curr_best = 1000;
        } else if (answ >= 7 && i < 12){
            question.setText("Wygrałeś 40 000 zł ! Gratulacje!");
            if(curr_best < 40000)
                curr_best = 40000;
        }
        else
            question.setText("Wygrałeś 0 złotych...");
    }

    //czynnosci wykonane po wcisnieciu danego przycisku
    public void abcd(TextView ans, View view){
        TransitionDrawable trans = new TransitionDrawable(color);
        if(drawn_quests.get(i).getAnswer().contains(ans.getText())) {
            ans.setBackgroundDrawable(trans);
            trans.startTransition(3000);

            i++;
            answ++;
            handler.postDelayed(new Runnable() {
                public void run() {
                    ans.setBackgroundColor(getResources().getColor(R.color.def));
                    levels.getChildAt(child).setBackgroundColor(getResources().getColor(R.color.green));
                    child--;

                    if (is_changed) {

                        if (i < drawn_quests.size()) {
                            assignQuest();
                            for (TextView odp : tviews_const) {
                                odp.setVisibility(View.VISIBLE);
                            }

                        } else {
                            win_actions(view);
                        }
                    }
                    else {

                        if (i < drawn_quests.size()-1) {
                            assignQuest();
                            for (TextView odp : tviews_const) {
                                odp.setVisibility(View.VISIBLE);
                            }

                        } else {
                            win_actions(view);
                        }
                    }
                }
            }, 3500);
        }
        else {//przegrana
            trans = new TransitionDrawable(color_red);
            ans.setBackgroundDrawable(trans);
            trans.startTransition(5000);
            prize();

            handler.postDelayed(new Runnable() {
                public void run() {

                    //podswietlenie poprawnej odpowiedzi
                    for (TextView odp : tviews_const) {
                        if (drawn_quests.get(i).getAnswer().contains(odp.getText())) {
                            odp.setBackgroundColor(getResources().getColor(R.color.green));
                        }
                        odp.setClickable(false);
                    }
                    fifty.setClickable(false);
                    change.setClickable(false);
                    question.setText("Czy chcesz zagrać jeszcze raz?");
                    yes.setVisibility(View.VISIBLE);
                    no.setVisibility(View.VISIBLE);
                    giveUpButton.setClickable(false);
                }
            }, 3500);


        }

    }

    public void menu(View view){
        int high_s=0;
        Bundle from_result = getIntent().getExtras();
        if (from_result != null) {
             high_s = from_result.getInt("high_score");
        }

        if (curr_best > high_s)
        {
            Intent intent = new Intent(Game.this, MainActivity.class);
            intent.putExtra("best",curr_best);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(Game.this, MainActivity.class);
            intent.putExtra("best", high_s);
            startActivity(intent);
        }
    }

    //kolo ratunkowe 50:50
    public void use_fifty(View view) {
        ArrayList<TextView> the_rest = new ArrayList<TextView>();
        ArrayList<TextView> the_rest_drawn = new ArrayList<TextView>();
        for (TextView odp : tviews) {
            if (!drawn_quests.get(i).getAnswer().contains(odp.getText())) {
                the_rest.add(odp);
            }
        }
        int nums=1;
        Random r = new Random();
        while (nums<=2)
        {
            TextView elem = the_rest.get(r.nextInt(the_rest.size()));
            the_rest_drawn.add(elem);
            the_rest.remove(elem);
            nums++;
        }

        for (TextView odp_los : the_rest_drawn){
            odp_los.setVisibility(View.INVISIBLE);
        }
        fifty.setClickable(false);
        fifty.setVisibility(View.INVISIBLE);

    }

    //kolo ratunkowe zmiana pytania
    public void use_change(View view){
        int k=1;
        i++;
        assignQuest();
        for (TextView odp : tviews_const){
            odp.setVisibility(View.VISIBLE);
            k++;
        }

        is_changed = true;
        change = findViewById(R.id.changeImageView);
        change.setClickable(false);
        change.setVisibility(View.INVISIBLE);
    }


    //zaznaczanie odpowiedzi na pytanie
    public void answer(View view) {

        switch (view.getId()) {
            case R.id.ATextView:
                abcd(a, view);
                break;
            case R.id.BTextView:
                abcd(b, view);
                break;
            case R.id.CTextView:
                abcd(c, view);
                break;
            case R.id.DTextView:
                abcd(d, view);
                break;
            default:
                throw new RuntimeException("Unknown button ID");

        }

    }

    public void giveUp(View view){
        giveUpButton = findViewById(R.id.giveUpButton);
        for (TextView odp : tviews_const) {
            if (drawn_quests.get(i).getAnswer().contains(odp.getText())) {
                odp.setBackgroundColor(getResources().getColor(R.color.green));
            }
            odp.setClickable(false);
        }
        fifty.setClickable(false);
        change.setClickable(false);
        if (child==11)
            question.setText("Wygrałeś 0 zł! Gratulacje!");
        else {
            TableRow row = (TableRow) levels.getChildAt(child + 1);
            int content;
            content = Integer.parseInt((String) row.getTag());
            question.setText("Wygrałeś " + content + " zł! Gratulacje!");
            if (curr_best < content)
                curr_best = content;
        }

        handler.postDelayed(new Runnable() {
            public void run() {
                question.setText("Czy chcesz zagrać jeszcze raz?");
                yes.setVisibility(View.VISIBLE);
                no.setVisibility(View.VISIBLE);

            }
        }, 5000);   //10 seconds

    }
}
