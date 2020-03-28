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
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARRAY_LIST_BOOKS = "books";

    private ArrayList<HashMap<String, String>> books;

    private OnBookSelectionInterface parentActivity;

    public BookListFragment() {
        // Required empty public constructor
    }

    public static BookListFragment newInstance(ArrayList<HashMap<String, String>> books) {
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARRAY_LIST_BOOKS, books);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            books = (ArrayList<HashMap<String, String>>) getArguments().getSerializable(ARRAY_LIST_BOOKS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate((R.layout.fragment_book_list), container, false);

        ListView bookList = v.findViewById(R.id.list_view_books);

        BookAdapter adapter = new BookAdapter((Context) parentActivity, books);

        bookList.setAdapter(adapter);

        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
        void bookSelect(int position);
    }
}
