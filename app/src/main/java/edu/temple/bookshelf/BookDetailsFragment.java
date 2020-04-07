package edu.temple.bookshelf;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class BookDetailsFragment extends Fragment {

    // the fragment initialization parameters, the HashMap and the Integer key.
    private static final String BOOK_OBJECT = "book_obj";

    // Contains the name and author of the book.
    private Book book;

    // The two textViews for displaying name and author.
    private TextView bookName;
    private TextView bookAuthor;
    private ImageView bookCover;

    public BookDetailsFragment() {
        // Required empty public constructor
    }

    public static BookDetailsFragment newInstance(Book book) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(BOOK_OBJECT, book);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            book = getArguments().getParcelable(BOOK_OBJECT);
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
        bookCover = v.findViewById(R.id.book_cover);

        // As long as the passed HashMap isn't null.
        if(book != null){
            // Call the method displayBook.
            displayBook(book);
        }

        return v;
    }

    // Displays the book details in the textViews.
    public void displayBook(Book book){

        if(book != null) {
            bookAuthor.setText(book.getAuthor());
            bookName.setText(book.getTitle());
            Picasso.get().load(book.getCoverURL()).into(bookCover);
        }

    }
}
