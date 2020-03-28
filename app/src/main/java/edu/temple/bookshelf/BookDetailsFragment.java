package edu.temple.bookshelf;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

public class BookDetailsFragment extends Fragment {

    // the fragment initialization parameters, the HashMap and the Integer key.
    private static final String HASH_MAP_BOOK = "book_map";
    private static final String BOOK_ID = "book_id";

    // Contains the name and author of the book.
    private HashMap<Integer, Book> book;
    // The key to get the name and author from the HashMap.
    private int bookId;

    // The two textViews for displaying name and author.
    private TextView bookName;
    private TextView bookAuthor;

    public BookDetailsFragment() {
        // Required empty public constructor
    }

    public static BookDetailsFragment newInstance(HashMap<Integer, Book> book, int id) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle args = new Bundle();
        // HashMap is Serializable.
        args.putSerializable(HASH_MAP_BOOK, book);
        args.putInt(BOOK_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Even though unchecked cast, HashMap is serializable.
            book = (HashMap<Integer, Book>) getArguments().getSerializable(HASH_MAP_BOOK);
            bookId = getArguments().getInt(BOOK_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_book_details, container, false);

        // Get reference to both textViews.
        bookName = v.findViewById(R.id.book_name);
        bookAuthor = v.findViewById(R.id.book_author);

        // As long as the passed HashMap isn't null.
        if(book != null){
            // Call the method displayBook.
            displayBook(book, bookId);
        }

        return v;
    }

    // Displays the book details in the textViews.
    public void displayBook(HashMap<Integer, Book> bookMap, int position){

        // Get the Book Object from the HashMap.
        Book book = bookMap.get(position);

        // As long as Book Object isn't null.
        if(book != null) {
            // Get the title or book name using the getter method of Book Class.
            String title = book.getName();
            // Get the author name using the getter method of Book Class.
            String author = book.getAuthor();
            // Set the text of the two textViews accordingly.
            bookName.setText(title);
            bookAuthor.setText(author);
        }
    }
}
