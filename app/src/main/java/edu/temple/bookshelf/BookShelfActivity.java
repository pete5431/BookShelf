package edu.temple.bookshelf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

public class BookShelfActivity extends AppCompatActivity implements BookListFragment.OnBookSelectionInterface{

    // The number of books to be added.
    final int NUM_BOOKS = 10;

    // The ArrayList that contains HashMap objects.
    // The HashMap objects contain an Integer object as the key and Book object as the value.
    // The Book Object will contain the name and author of the book.
    ArrayList<HashMap<Integer, Book>> Books = new ArrayList<>();

    // Fragment that shows a ListView of the books.
    BookListFragment bookList;

    // Fragment that shows the details of the selected book.
    BookDetailsFragment bookDetails;

    // Determines if one container or two containers is being shown.
    boolean singleContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookshelf);

        // Add the books for the ArrayList Books.
        addBooks();

        // Create new instance of BookListFragment using ArrayList Books.
        bookList = BookListFragment.newInstance(Books);

        // Do the transaction and commit to container 1.
        getSupportFragmentManager().beginTransaction().replace(R.id.container1, bookList).commit();

        // Determine if the second container is present.
        singleContainer = findViewById(R.id.container2) == null;

        // If the second container is present.
        if(!singleContainer){
            // No book selected yet, so use default constructor.
            bookDetails = new BookDetailsFragment();

            // Do the transaction.
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container2, bookDetails)
                    .commit();

        }

    }

    // Adds the books to ArrayList Books.
    void addBooks(){

        // Get reference to resources.
        Resources res = getResources();

        // Fetch the arrays for names and authors from resources.
        String[] book_names = res.getStringArray(R.array.book_names);
        String[] book_authors = res.getStringArray(R.array.book_authors);

        // Loop through the number of books to be added.
        for(int i = 0; i < NUM_BOOKS; i++){
            // Create a new Book Object with the name and author.
            Book newBook = new Book(book_names[i], book_authors[i]);
            // Create a new HashMap.
            HashMap<Integer, Book> newMap = new HashMap<>();
            // Add the key value pair, using the index as the key and the Book as the value.
            newMap.put(i, newBook);
            // Add the HashMap object to ArrayList Books.
            Books.add(newMap);
        }
    }

    @Override
    public void bookSelect(int index){
        // Get the book from the ArrayList using the passed index.
        HashMap<Integer, Book> book = Books.get(index);

        // If only container 1 is shown.
        if(singleContainer) {

            // Create new instance of BookDetailsFragment using the book and index.
            bookDetails = BookDetailsFragment.newInstance(book, index);

            // Perform the Transaction. Add to backStack.
            getSupportFragmentManager().beginTransaction().replace(R.id.container1, bookDetails)
                    .addToBackStack(null)
                    .commit();
        }
        // If two containers, simply update the current BookDetailsFragment.
        else bookDetails.displayBook(book, index);
    }
}
