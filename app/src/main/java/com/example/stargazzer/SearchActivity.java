package com.example.stargazzer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class SearchActivity extends AppCompatActivity {

    private ApiInterface apiInterface;
    private Adapter recyclerAdapter;
    private RecyclerView recyclerView;
    private EditText searchBar;
    ArrayList<String> itemsTitles, itemsIDs, itemsDescriptions;
    private String q;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        itemsTitles = new ArrayList<>();
        itemsIDs = new ArrayList<>();
        itemsDescriptions = new ArrayList<>();

        searchBar = findViewById(R.id.search_bar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.toString().equals("")) {
                    itemsIDs.clear();
                    itemsTitles.clear();

                    recyclerAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.toString().equals("")) {
                    itemsIDs.clear();
                    itemsTitles.clear();

                    recyclerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(final Editable editable) {
                if(editable != null) {

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            performSearch(editable.toString());
                        }
                    }, 2000);

                }
                else {
                    itemsIDs.clear();
                    itemsTitles.clear();

                    recyclerAdapter.notifyDataSetChanged();
                }
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerAdapter = new Adapter(itemsTitles);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);

        recyclerAdapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(SearchActivity.this, DisplayAssetActivity.class);
                intent.putExtra("itemID",itemsIDs.get(position));
                intent.putExtra("itemTitle", itemsTitles.get(position));
                intent.putExtra("itemDescription",itemsDescriptions.get(position));
                startActivity(intent);
            }
        });
    }


    private void performSearch(String q){

        apiInterface = ApiClient.getLibraryRetrofit().create(ApiInterface.class);

        if(q == null){
            itemsIDs.clear();
            itemsTitles.clear();

            recyclerAdapter.notifyDataSetChanged();
            return;
        }

        Call<LibraryItem> call = apiInterface.performSearch(q);

        call.enqueue(new Callback<LibraryItem>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<LibraryItem> call, Response<LibraryItem> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Code: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }

                LibraryItem libraryItem = response.body();
                assert libraryItem !=null;
                for(int i = 0; i < libraryItem.getCollection().getItems().size(); i++){
                     itemsTitles.add(libraryItem.getCollection().getItems().get(i).getData().get(0).getTitle());
                     itemsIDs.add(libraryItem.getCollection().getItems().get(i).getData().get(0).getNasa_id());
                     itemsDescriptions.add(libraryItem.getCollection().getItems().get(i).getData().get(0).getDescription());
                     recyclerAdapter.notifyDataSetChanged();

                }

            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<LibraryItem> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }


}