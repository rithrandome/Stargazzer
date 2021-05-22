package com.example.stargazzer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class DisplayAssetActivity extends AppCompatActivity {

    ApiInterface apiInterface;

    TextView description, title;
    ImageView imageView;
    String descriptionString, idString, titleString, imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_asset);

        description = findViewById(R.id.description_text);
        title = findViewById(R.id.title);
        imageView = findViewById(R.id.image);

        Intent intent = getIntent();
        idString = intent.getStringExtra("itemID");
        descriptionString = intent.getStringExtra("itemDescription");
        titleString = intent.getStringExtra("itemTitle");

        title.setText(titleString);
        getAssetInfo(idString);
        description.setText(descriptionString);


    }

    public void loadImageView(String url, ImageView imageView){

        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .override(Target.SIZE_ORIGINAL)
                .fitCenter()
                .centerCrop()
                .into(imageView);
    }

    public void getAssetInfo(String id){

        apiInterface = ApiClient.getLibraryRetrofit().create(ApiInterface.class);

        Call<AssetItem> call = apiInterface.getAssetInfo(id);

        call.enqueue(new Callback<AssetItem>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<AssetItem> call, Response<AssetItem> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }

                AssetItem assetItem = response.body();
                assert assetItem != null;

                imageUrl = assetItem.getCollection().getItems().get(1).getHref();
                imageUrl = imageUrl.replace("http","https");



                System.out.println(imageUrl);
                loadImageView(imageUrl, imageView);

            }
            @EverythingIsNonNull
            @Override
            public void onFailure(Call<AssetItem> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });



    }
}