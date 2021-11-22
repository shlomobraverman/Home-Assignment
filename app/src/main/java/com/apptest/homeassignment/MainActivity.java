package com.apptest.homeassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private static JSONObject json;
    public static ArrayList<HashMap> images =  new ArrayList<>();
    RecyclerView recyclerView;
    LinearLayoutManager Manager;
    static Adapter adapter;
    static String query;
    static int count = 1;
    static int total_pages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SearchView searchView = findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query_new) {
                try {
                    query = query_new;
                    count = 1;
                    images.clear();
                    String url = "https://api.unsplash.com/search/photos?page=" + count + "&client_id=c99a7e7599297260b46b7c9cf36727badeb1d37b1f24aa9ef5d844e3fbed76fe&query=";
                    url += URLEncoder.encode(query, "UTF-8");
                    json = get_json(url);
                    if (json.has("error")){
                        return false;
                    }
                    total_pages = (int) json.get("total_pages");
                    show_images();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public static void get_more_data() {
        try {
            if (count >= total_pages || count < 1){
                return;
            }
            String url = "https://api.unsplash.com/search/photos?page=" + count + "&client_id=c99a7e7599297260b46b7c9cf36727badeb1d37b1f24aa9ef5d844e3fbed76fe&query=";
            url += URLEncoder.encode(query, "UTF-8");
            json = get_json(url);
            if (json.has("error")){
                return;
            }
            JSONArray check_result = (JSONArray) json.get("results");
            if (check_result.length() != 0) {
                Parserjson parser =  new Parserjson(json);
                ArrayList<HashMap> updating = parser.get_results();
                int len = images.size();
                images.addAll(updating);
                if (adapter != null){
                    try{
                        adapter.notifyItemRangeInserted(len, updating.size());
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONObject get_json(String url) throws Exception
    {
        String[] urls = {url};
        return new SearchableActivity().execute(urls).get();
    }
    public void show_images() throws Exception{
        Parserjson parser =  new Parserjson(json);
        images =  parser.get_results();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        Manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(Manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
        adapter = new Adapter(images, this, new ClickListener() {
            @Override
            public void onPositionClicked(int position) {
            }

            @Override
            public void onLongClicked(int position) {
            }
        });
        recyclerView.setAdapter(adapter);
    }
}

