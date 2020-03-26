package edu.temple.bookshelf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

public class BookShelfActivity extends AppCompatActivity {

    final int NUM_BOOKS = 10;

    ArrayList<HashMap<String, String>> BookShelf = new ArrayList<>();
    HashMap<String, String> BookMap = new HashMap<>();

    Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookshelf);

        makeMap();

        BookShelf.add(BookMap);
    }

    void makeMap(){

        res = getResources();

        String[] book_names = res.getStringArray(R.array.book_names);
        String[] book_authors = res.getStringArray(R.array.book_authors);

        for(int i = 0; i < NUM_BOOKS; i++){
            BookMap.put(book_names[i], book_authors[i]);
        }
    }
}
