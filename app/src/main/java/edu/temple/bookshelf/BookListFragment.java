package edu.temple.bookshelf;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class BookListFragment extends Fragment {
    // the fragment initialization parameter for ArrayList of HashMaps.
    private static final String ARRAY_LIST_BOOKS = "books";

    // ArrayList of HashMaps that contain the values of name and author.
    private ArrayList<HashMap<Integer, Book>> books;

    // The parent activity that implements the OnBookSelectionInterface.
    // Will receive the context of parent.
    private OnBookSelectionInterface parentActivity;

    public BookListFragment() {
        // Required empty public constructor
    }

    public static BookListFragment newInstance(ArrayList<HashMap<Integer, Book>> books) {
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        // ArrayList and HashMap are both Serializable.
        args.putSerializable(ARRAY_LIST_BOOKS, books);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Though its a unchecked cast, the data structure we placed in arguments is serializable.
            books = (ArrayList<HashMap<Integer, Book>>) getArguments().getSerializable(ARRAY_LIST_BOOKS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate((R.layout.fragment_book_list), container, false);
        // Get reference to the listView.
        ListView bookList = v.findViewById(R.id.list_view_books);
        // Create new BookAdapter.
        BookAdapter adapter = new BookAdapter((Context) parentActivity, books);

        bookList.setAdapter(adapter);

        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Upon selection of a book, call bookSelect method in parent with the index.
                parentActivity.bookSelect(position);
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBookSelectionInterface) {
            parentActivity = (OnBookSelectionInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBookSelectionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        parentActivity = null;
    }

    public interface OnBookSelectionInterface {
        void bookSelect(int index);
    }
}
