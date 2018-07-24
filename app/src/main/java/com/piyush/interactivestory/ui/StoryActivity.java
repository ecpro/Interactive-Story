package com.piyush.interactivestory.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.piyush.interactivestory.R;
import com.piyush.interactivestory.model.Page;
import com.piyush.interactivestory.model.Story;

public class StoryActivity extends AppCompatActivity {

    private static final String TAG = StoryActivity.class.getSimpleName();

    private Story story;
    private ImageView storyImageView;
    private TextView storyTextView;
    private Button choiceButton1;
    private Button choiceButton2;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        this.storyImageView = findViewById(R.id.storyImageView);
        this.storyTextView = findViewById(R.id.storyTextView);
        this.choiceButton1 = findViewById(R.id.page_choice_1);
        this.choiceButton2 = findViewById(R.id.page_choice_2);
        name = getIntent().getStringExtra("name");

        this.story = new Story();
        loadPage(0);

    }

    private void loadPage(int pageNumber) {
        final Page page = story.getPage(pageNumber);
        Drawable image = ContextCompat.getDrawable(this, page.getImageId());
        storyImageView.setImageDrawable(image);

        String pageText = getString(page.getTextId());
        pageText = String.format(pageText, name);
        storyTextView.setText(pageText);

        if(page.isFinal()) {
            choiceButton2.setVisibility(View.INVISIBLE);
            choiceButton1.setText(getString(R.string.replay_game));
            choiceButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadPage(0);
                }
            });
        }
        else{
            loadChoiceButton(page);
        }
    }

    private void loadChoiceButton(final Page page) {
        choiceButton1.setVisibility(View.VISIBLE);
        choiceButton2.setVisibility(View.VISIBLE);
        choiceButton1.setText(page.getChoice1().getTextId());
        choiceButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nextPage = page.getChoice1().getNextPage();
                if(page.isFinal()) loadPage(0);
                else loadPage(nextPage);
            }
        });

        choiceButton2.setText(page.getChoice2().getTextId());
        choiceButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nextPage = page.getChoice2().getNextPage();
                if(page.isFinal()) loadPage(0);
                else loadPage(nextPage);
            }
        });
    }
}
