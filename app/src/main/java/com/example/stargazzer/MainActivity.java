package com.example.stargazzer;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

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

public class MainActivity extends YouTubeBaseActivity implements DatePickerDialog.OnDateSetListener, YouTubePlayer.OnInitializedListener {

    TextView explanation, title, date;
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    FloatingActionButton datePickerButton;
    ImageView apod;
    String setDate = null;
    Date dateString;
    Calendar c = Calendar.getInstance();
    String year, month, dayOfMonth;
    String explanationString, url, titleString, mediaType;


    private APIInterface APIInterface;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        textView = findViewById(R.id.text2);
        datePickerButton = findViewById(R.id.date_picker_actions);
        date = findViewById(R.id.date_text);
        explanation = findViewById(R.id.explanation_text);
        apod = findViewById(R.id.apod);
        youTubePlayerView = findViewById(R.id.youtube_player);

        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        APIInterface = ServiceGenerator.getRetrofit().create(APIInterface.class);

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

        Picasso.get()
                .load(url)
                .fit()
                .centerCrop()
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

    private void getAPOD(){



        Call<PicInfo> call = APIInterface.getAPOD();


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

                dateString = picInfo.getDate();
                explanationString = picInfo.getExplanation();
                titleString = picInfo.getTitle();
                mediaType = picInfo.getMedia_type();
                url = picInfo.getUrl();



                if(mediaType.toLowerCase().equals("image")){
                    apod.setVisibility(View.VISIBLE);
                    youTubePlayerView.setVisibility(View.INVISIBLE);
                    loadImageView(url, apod);
                }
                else if(mediaType.toLowerCase().equals("vid eo")){
                    apod.setVisibility(View.INVISIBLE);
                    youTubePlayerView.setVisibility(View.VISIBLE);

                    onInitializedListener = new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                            Log.e("url",url.replace("https://www.youtube.com/embed/","").replace("?rel=0",""));
                            youTubePlayer.loadVideo(url.replace("https://www.youtube.com/embed/","").replace("?rel=0",""));
                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                        }
                    };


                    youTubePlayerView.initialize(YoutubeConfig.getApiKey(), onInitializedListener);

                }

                date.setText(String.valueOf(dateString));
                explanation.setText(explanationString);
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<PicInfo> call, Throwable t) {
                 Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getPicInfo(String setDate){


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

                dateString= picInfo.getDate();
                explanationString = picInfo.getExplanation();
                titleString = picInfo.getTitle();
                mediaType = picInfo.getMedia_type();
                url = picInfo.getUrl();

                if(mediaType.toLowerCase().equals("image")){

                    apod.setVisibility(View.VISIBLE);
                    youTubePlayerView.setVisibility(View.INVISIBLE);
                    loadImageView(url, apod);
                }
                else if(mediaType.toLowerCase().equals("video")) {
                    apod.setVisibility(View.INVISIBLE);
                    youTubePlayerView.setVisibility(View.VISIBLE);

                    if(youTubePlayer != null)
                        youTubePlayer.release();

                    youTubePlayerView.initialize(YoutubeConfig.getApiKey(), (YouTubePlayer.OnInitializedListener) youTubePlayerView.getContext());
                }

                date.setText(String.valueOf(dateString));
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
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putString("url",url);
        outState.putString("explanation",explanationString);
        outState.putString("title",titleString);
        outState.putString("mediaType", mediaType);
        outState.putString("year", year);
        outState.putString("month", month);
        outState.putString("dayOfMonth", dayOfMonth);
        outState.putString("date",String.valueOf(dateString));

    }
}