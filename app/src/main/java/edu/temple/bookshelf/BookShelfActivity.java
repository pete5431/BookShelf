package edu.temple.bookshelf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BookShelfActivity extends AppCompatActivity implements BookListFragment.OnBookSelectionInterface{

    // ArrayList containing Book objects.
    ArrayList<Book> Books = new ArrayList<>();

    // Fragment that shows a ListView of the books.
    BookListFragment bookList;

    // Fragment that shows the details of the selected book.
    BookDetailsFragment bookDetails;

    // Determines if one container or two containers is being shown.
    boolean singleContainer;

    RequestQueue requestQueue;

    EditText search_bar;

    Button search_button;

    // The current selected Book.
    Book selectedBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookshelf);

        // Determine if the second container is present.
        singleContainer = findViewById(R.id.container2) == null;

        // Request a new queue using Volley.
        requestQueue = Volley.newRequestQueue(this);

        // If there is something saved.
        if(savedInstanceState != null) {
            // Get the saved Books ArrayList.
            Books = savedInstanceState.getParcelableArrayList("BookArray");
            // Get the selected book.
            selectedBook = savedInstanceState.getParcelable("SelectedBook");

            // If two containers are shown.
            if(!singleContainer){
                // Update the bookDetails fragment with the saved selectedBook.
                bookDetails = BookDetailsFragment.newInstance(selectedBook);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container2, bookDetails)
                        .commit();
            }
        }
        else{
            // If two containers are shown.
            if(!singleContainer){

                // Start a default bookDetails fragment.
                bookDetails = new BookDetailsFragment();
                // Do the transaction.
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container2, bookDetails)
                        .commit();
            }
        }

        // Create new instance of BookListFragment using ArrayList Books.
        bookList = BookListFragment.newInstance(Books);
        // Do the transaction and commit to container 1.
        getSupportFragmentManager().beginTransaction().replace(R.id.container1, bookList).commit();

        if(savedInstanceState != null && singleContainer && selectedBook != null){
            // Retain the bookDetails fragment by replacing the list in container1.
            bookDetails = BookDetailsFragment.newInstance(selectedBook);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container1, bookDetails)
                    .addToBackStack(null)
                    .commit();
        }

        search_bar = findViewById(R.id.search_bar);
        search_button = findViewById(R.id.search_button);

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // The url for the Book API. The end of the url will be what the user enters.
                String search_url = "https://kamorris.com/lab/abp/booksearch.php?search=" + search_bar.getText().toString();

                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, search_url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {

                                int i = 0;
                                for(; i < response.length(); i++){
                                    try{
                                        JSONObject jsonObject = response.getJSONObject(i);
                                        int bookId = jsonObject.getInt("book_id");
                                        if(Books.size() >= response.length()){
                                            if(Books.get(i).getBookId() == bookId){
                                                continue;
                                            }else{
                                                Books.remove(i);
                                            }
                                        }
                                        else if(i < Books.size() - 1 && Books.size() < response.length()){
                                            if(Books.get(i).getBookId() == bookId){
                                                continue;
                                            }else{
                                                Books.remove(i);
                                            }
                                        }
                                        String bookTitle = jsonObject.getString("title");
                                        String bookAuthor = jsonObject.getString("author");
                                        String coverURL = jsonObject.getString("cover_url");
                                        Book book = new Book(bookId, bookTitle, bookAuthor, coverURL);
                                        Books.add(i, book);
                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                // Remove any excess books.
                                if(Books.size() > response.length()){
                                    int start = i;
                                    int size = Books.size();
                                    for(; i < size; i++){
                                        Books.remove(start);
                                    }
                                }
                                Books.trimToSize();
                                bookList.notifyChanged();
                                if(singleContainer) {
                                    Fragment frag = getSupportFragmentManager().findFragmentById(R.id.container1);
                                    if (frag instanceof BookDetailsFragment) {
                                        getSupportFragmentManager().beginTransaction().replace(R.id.container1, bookList).commit();
                                    }
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error){

                            }
                        });
                requestQueue.add(jsonArrayRequest);
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList("BookArray", Books);
        savedInstanceState.putParcelable("SelectedBook", selectedBook);
    }

    @Override
    public void bookSelect(int index){

        // Save the selectedBook in the current Book ArrayList.
        selectedBook = Books.get(index);

        // If only container 1 is shown.
        if(singleContainer) {

            // Perform the Transaction. Add to backStack.
            getSupportFragmentManager().beginTransaction().replace(R.id.container1, BookDetailsFragment.newInstance(Books.get(index)))
                    .addToBackStack(null)
                    .commit();
        }else bookDetails.displayBook(Books.get(index));
        // If two containers, simply update the current BookDetailsFragment.
    }
}
