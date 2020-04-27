package edu.temple.bookshelf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

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

import edu.temple.audiobookplayer.AudiobookService;

public class BookShelfActivity extends AppCompatActivity implements BookListFragment.OnBookSelectionInterface, BookDetailsFragment.OnPlayBookInterface{

    // ArrayList containing Book objects.
    ArrayList<Book> Books = new ArrayList<>();
    // Fragment that shows a ListView of the books.
    BookListFragment bookList;
    // Fragment that shows the details of the selected book.
    BookDetailsFragment bookDetails;

    // Determines if one container or two containers is being shown.
    boolean singleContainer;

    RequestQueue requestQueue;
    EditText searchBar;
    TextView bookStatus;
    Button searchButton;
    ImageButton pauseButton;
    ImageButton stopButton;
    SeekBar progressBar;

    // The current selected Book.
    Book selectedBook;
    // The current played book.
    Book currentPlayingBook;

    // Intent for BookAudioService.
    Intent serviceIntent;
    boolean connected;
    AudiobookService.MediaControlBinder mediaControlBinder;
    AudiobookService.BookProgress bookProgress;

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            connected = true;
            mediaControlBinder = (AudiobookService.MediaControlBinder) service;
            mediaControlBinder.setProgressHandler(bookProgressHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connected = false;
            mediaControlBinder = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookshelf);

        serviceIntent = new Intent(BookShelfActivity.this, AudiobookService.class);
        bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);

        progressBar = findViewById(R.id.book_progress_bar);
        pauseButton = findViewById(R.id.pause_button);
        stopButton = findViewById(R.id.stop_button);
        searchBar = findViewById(R.id.search_bar);
        searchButton = findViewById(R.id.search_button);
        bookStatus = findViewById(R.id.book_status);

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connected){
                    mediaControlBinder.pause();
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connected && mediaControlBinder.isPlaying()){
                    mediaControlBinder.stop();
                    stopService(serviceIntent);
                    progressBar.setProgress(0);
                    progressBar.setMin(0);
                    progressBar.setMax(0);
                    currentPlayingBook = null;
                    bookStatus.setText("");
                }
            }
        });

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
            // Get the book that is being played.
            currentPlayingBook = savedInstanceState.getParcelable("CurrentPlayingBook");

            if(currentPlayingBook != null) {
                progressBar.setMin(0);
                progressBar.setMax(currentPlayingBook.getBookLength());
                String status = "Now Playing: " + currentPlayingBook.getTitle() + " by " + currentPlayingBook.getAuthor();
                bookStatus.setText(status);
            }

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

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // The url for the Book API. The end of the url will be what the user enters.
                String search_url = "https://kamorris.com/lab/abp/booksearch.php?search=" + searchBar.getText().toString();

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
                                        int bookLength = jsonObject.getInt("duration");
                                        Book book = new Book(bookId, bookLength, bookTitle, bookAuthor, coverURL);
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
        savedInstanceState.putParcelable("CurrentPlayingBook", currentPlayingBook);
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

        }else {
            // If two containers, simply update the current BookDetailsFragment.
            bookDetails.displayBook(Books.get(index));
        }
    }

    @Override
    public void playBook(Book book){
        mediaControlBinder.play(book.getBookId());
        progressBar.setMin(0);
        progressBar.setMax(book.getBookLength());
        currentPlayingBook = book;
        startService(serviceIntent);
        String status = "Now Playing: " + book.getTitle() + " by " + book.getAuthor();
        bookStatus.setText(status);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    Handler bookProgressHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            bookProgress = (AudiobookService.BookProgress) msg.obj;

            if(bookProgress != null) {
                if (bookProgress.getProgress() < currentPlayingBook.getBookLength()) {
                    progressBar.setProgress(bookProgress.getProgress());
                }
                if (bookProgress.getProgress() == currentPlayingBook.getBookLength()) {
                    mediaControlBinder.stop();
                    progressBar.setProgress(0);
                    progressBar.setMin(0);
                    progressBar.setMax(0);
                    currentPlayingBook = null;
                    bookStatus.setText("");
                }
            }

            progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(fromUser){
                        mediaControlBinder.seekTo(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            return false;
        }
    });
}
