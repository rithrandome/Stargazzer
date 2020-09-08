package com.example.stargazzer;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class DisplayApodActivity extends YouTubeBaseActivity implements DatePickerDialog.OnDateSetListener, YouTubePlayer.OnInitializedListener {

    TextView explanation, title, date;
    YouTubePlayerView youTubePlayerView;
    FloatingActionButton datePickerButton, searchLibraryButton;
    ImageView apod;
    String setDate = null;
    Date dateString;
    Calendar c = Calendar.getInstance();
    String year, month, dayOfMonth;
    String explanationString, url, titleString, mediaType;

    private ApiInterface APIInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_apod);

        searchLibraryButton = findViewById(R.id.library_search_button);
        datePickerButton = findViewById(R.id.date_picker_actions);
        date = findViewById(R.id.date_text);
        explanation = findViewById(R.id.explanation_text);
        apod = findViewById(R.id.apod);
        youTubePlayerView = findViewById(R.id.youtube_player);
        title = findViewById(R.id.title);


        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        searchLibraryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayApodActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });


        APIInterface = ApiClient.getApodRetrofit().create(ApiInterface.class);

        getPicInfo(setDate);


    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)

        );
        datePickerDialog.show();
    }

    public void loadImageView(String url, ImageView imageView){

        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .fitCenter()
                .into(imageView);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {



        Calendar c =Calendar.getInstance();
        c.set(Calendar.YEAR, i);
        c.set(Calendar.MONTH, i1 + 1);
        c.set(Calendar.DAY_OF_MONTH, i2);

        year = String.valueOf(c.get(Calendar.YEAR));
        month = String.valueOf(c.get(Calendar.MONTH));
        dayOfMonth = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        setDate = year+"-"+month+"-"+dayOfMonth;

        getPicInfo(setDate);

    }



    private void getPicInfo(String setDate){

        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,60,0,0);

        Call<PicInfo> call = APIInterface.getPicInfo(setDate);

        call.enqueue(new Callback<PicInfo>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<PicInfo> call, Response<PicInfo> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Code: " + response.code(),Toast.LENGTH_LONG).show();
                    return;
                }

                PicInfo picInfo = response.body();
                assert picInfo != null;

                titleString = picInfo.getTitle();
                dateString= picInfo.getDate();
                explanationString = picInfo.getExplanation();
                titleString = picInfo.getTitle();
                mediaType = picInfo.getMedia_type();
                url = picInfo.getUrl();


                if(mediaType.toLowerCase().equals("image")){

                    layoutParams.addRule(RelativeLayout.BELOW, R.id.apod);
                    explanation.setLayoutParams(layoutParams);
                    apod.setVisibility(View.VISIBLE);
                    youTubePlayerView.setVisibility(View.INVISIBLE);
                    loadImageView(url, apod);
                }
                else if(mediaType.toLowerCase().equals("video")) {

                    layoutParams.addRule(RelativeLayout.BELOW, R.id.youtube_player);
                    explanation.setLayoutParams(layoutParams);
                    apod.setImageDrawable(null);
                    apod.setVisibility(View.INVISIBLE);
                    youTubePlayerView.setVisibility(View.VISIBLE);

                    if(youTubePlayer != null)
                        youTubePlayer.release();

                    youTubePlayerView.initialize(YoutubeConfig.getApiKey(), (YouTubePlayer.OnInitializedListener) youTubePlayerView.getContext());
                }

                date.setText(String.valueOf(dateString));
                title.setText(titleString);
                explanation.setText(explanationString);
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<PicInfo> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private YouTubePlayer youTubePlayer;

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        this.youTubePlayer = youTubePlayer;

        Log.e("url", url.replace("https://www.youtube.com/embed/","").replace("?rel=0",""));
        if(!b)
            youTubePlayer.loadVideo(url.replace("https://www.youtube.com/embed/","").replace("?rel=0",""));

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, "ERROR:" + youTubeInitializationResult.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        outState.putString("url",url);
        outState.putString("explanation",explanationString);
        outState.putString("title",titleString);
        outState.putString("mediaType", mediaType);
        outState.putString("year", year);
        outState.putString("month", month);
        outState.putString("dayOfMonth", dayOfMonth);
        outState.putString("setDate", setDate);


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        getPicInfo(savedInstanceState.getString("setDate"));

        url = savedInstanceState.getString("url");
        explanationString = savedInstanceState.getString("explanation");
        titleString = savedInstanceState.getString("title");
        mediaType = savedInstanceState.getString("mediaType");
        year = savedInstanceState.getString("year");
        month = savedInstanceState.getString("month");
        dayOfMonth = savedInstanceState.getString("dayOfMonth");

    }
}