package edu.temple.bookshelf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

public class BookShelfActivity extends AppCompatActivity implements BookListFragment.OnBookSelectionInterface{

    final int NUM_BOOKS = 10;

    ArrayList<HashMap<String, String>> Books = new ArrayList<>();

    Resources res;

    BookListFragment bookList;

    BookDetailsFragment bookDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookshelf);

        addBooks();

        System.out.println(Books);

        bookList = BookListFragment.newInstance(Books);

        getSupportFragmentManager().beginTransaction().replace(R.id.container1, bookList).commit();
    }

    void addBooks(){

        res = getResources();

        String[] book_names = res.getStringArray(R.array.book_names);
        String[] book_authors = res.getStringArray(R.array.book_authors);

        for(int i = 0; i < NUM_BOOKS; i++){
            HashMap<String, String> newMap = new HashMap<>();
            newMap.put(book_names[i], book_authors[i]);
            Books.add(newMap);
        }
    }

    @Override
    public void bookSelect(int position){
        HashMap<String, String> book = Books.get(position);

        bookDetails = BookDetailsFragment.newInstance(book);

        getSupportFragmentManager().beginTransaction().replace(R.id.container1, bookDetails)
                .addToBackStack(null)
                .commit();

        bookDetails.displayBook(book);
    }
}
