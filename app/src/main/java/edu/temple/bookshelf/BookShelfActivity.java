package edu.temple.bookshelf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

public class BookShelfActivity extends AppCompatActivity {

    ArrayList<HashMap> BookShelf;
    HashMap<String, String> Books;

    Resources res;
    String[] book_names;
    String[] book_authors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookshelf);

        res = getResources();

        book_names = res.getStringArray(R.array.book_names);
        book_authors = res.getStringArray(R.array.book_authors);

        Books = new HashMap<>();


    }
}
