package edu.temple.bookshelf;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;


public class BookDetailsFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String HASH_MAP_BOOK = "book_map";

    private HashMap<String, String> book;

    private View v;

    private TextView bookName;
    private TextView bookAuthor;

    public BookDetailsFragment() {
        // Required empty public constructor
    }

    public static BookDetailsFragment newInstance(HashMap<String, String> book) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(HASH_MAP_BOOK, book);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            book = (HashMap<String, String>) getArguments().getSerializable(HASH_MAP_BOOK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_book_details, container, false);

        bookName = v.findViewById(R.id.book_name);
        bookAuthor = v.findViewById(R.id.book_author);

        displayBook(book);

        return v;
    }

    public void displayBook(HashMap<String, String> book){

        Iterator i = book.keySet().iterator();

        String key = (String) i.next();
        String value = book.get(key);

        if(bookName != null && bookAuthor != null) {
            bookName.setText(key);
            bookAuthor.setText(value);
        }
    }
}
