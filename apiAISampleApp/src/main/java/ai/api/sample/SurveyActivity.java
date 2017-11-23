package ai.api.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SurveyActivity extends AppCompatActivity {
    private TextView mTitle, mQuestion;

    private ImageButton happy, sad, ok;
    private View bar2, bar3;
    private int page;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        bar2 = findViewById(R.id.bar2);
        bar3 = findViewById(R.id.bar3);
        page=0;
        mTitle = (TextView)(findViewById(R.id.title));
        mQuestion = (TextView)(findViewById(R.id.question));


        happy = (ImageButton)(findViewById(R.id.happy));
        ok = (ImageButton)(findViewById(R.id.ok));
        sad = (ImageButton)(findViewById(R.id.sad));

        final String [] questionTitles = {"Teslimatların Ulaşımı Hakkında", "CCINext Hakkında" , "Çırak Hakkında"};
        final String [] questions = {"Teslimatların elinize ulaşma süresinden memnun musunuz?",
                "CCINext'e gelen çırak özelliğini beğendiniz mi?", "Çırak özelliği hayatınızı kolaylaştırıyor mu?"};


        setQuestion(questionTitles[page], questions[page]);

        happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePage();
                setQuestion(questionTitles[page], questions[page]);
            }
        });

        sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePage();
                setQuestion(questionTitles[page], questions[page]);
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePage();
                setQuestion(questionTitles[page], questions[page]);
            }
        });
    }

    private void changePage(){
        if(page<2)
            page++;
        else {
            Intent intent = new Intent(SurveyActivity.this, MainActivity.class);
            intent.putExtra("puan",300);
            startActivity(intent);
            finish();
        }
    }

    private void setQuestion(String title, String question){
        switch (page){
            case 0: //progressBar.setProgress(33);
                break;
            case 1: bar2.setVisibility(View.VISIBLE);
                break;
            case 2: bar3.setVisibility(View.VISIBLE);
                break;
            default:
                //progressBar.setProgress(0);
                break;
        }
        mTitle.setText(title);
        mQuestion.setText(question);
    }
}
