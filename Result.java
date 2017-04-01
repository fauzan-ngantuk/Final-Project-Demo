package com.rangermerah.recyletor;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.greenfrvr.hashtagview.HashtagView;

import java.util.ArrayList;
import java.util.List;


public class Result extends AppCompatActivity {

    private final static String TAG = "Umur";

    HashtagView mHashtag;
    ImageView mImage;

    TextView labelText, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mHashtag = (HashtagView) findViewById(R.id.hashtags1);
        mImage = (ImageView) findViewById(R.id.imageResult);
        gender = (TextView) findViewById(R.id.gender);

        labelText = (TextView) findViewById(R.id.labelText);


        Intent intent = getIntent();
        String result = intent.getStringExtra("Result");
        String umur = intent.getStringExtra("Umur");
        //String recycle = intent.getStringExtra("recycle");
        Uri bitmap =  intent.getParcelableExtra("bitmap");
        //Bitmap bitmap =  intent.getParcelableExtra("bitmap");
        String gen = intent.getStringExtra("Gender");
        String acuracy = intent.getStringExtra("Acuracy");
        //mImage.setImageBitmap(bitmap);
        //mImage.setBackgroundResource(bitmap);

        mImage.setImageURI(bitmap);
        labelText.setText(result);
        if (umur.equals("Unclassify")){
            gender.setText("");
        }
        else {
            gender.setText("Gender : "+gen+" Age : "+umur+" Acuracy : "+acuracy);
        }

/*
        if (recycle.equals("true")) {
            mHashtag.setBackgroundColor(R.color.hijauDasar);
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.hijauDasar));
        } else {

            if (umur.equals("Human")) {
                mHashtag.setBackgroundColor(R.color.biruDasar);
                toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.biruDasar));
            } else {
                mHashtag.setBackgroundColor(R.color.merahDasar);
                toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.merahDasar));

            }

        }
*/
        // creating a list for tags
        List<String> tagHolder = new ArrayList<>();


       for (String s : intent.getStringArrayExtra("tag")) {
            //Do your stuff here

            // add tags to the list
            tagHolder.add("#"+s);
            // populating the hashtag view
            mHashtag.setData(tagHolder);
        }

        mHashtag.addOnTagClickListener(new HashtagView.TagsClickListener() {
            @Override
            public void onItemClicked(Object item) {
                //Log.e("aaa ",""+item);
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, item.toString());
                startActivity(intent);
            }
        });


    }

}
