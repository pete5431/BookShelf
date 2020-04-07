package edu.temple.bookshelf;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

// Custom Adapter BookAdapter extends ArrayAdapter that accepts HashMap Objects.
public class BookAdapter extends ArrayAdapter<Book> {

    // The ArrayList containing all the books.
    private ArrayList<Book> books;

    // The passed context.
    private Context context;

    public BookAdapter(Context context, ArrayList<Book> books){
        super(context, 0, books);
        this.books = books;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        View bookListItem = convertView;

        // Check if booKLisItem is null.
        if(bookListItem == null){
            // If null inflate a new view, otherwise continue using the old one.
            bookListItem = LayoutInflater.from(context).inflate(R.layout.list_book_item, parent, false);
        }

        // Get reference to the textViews.
        TextView bookName = bookListItem.findViewById(R.id.list_book_name);
        TextView bookAuthor = bookListItem.findViewById(R.id.list_book_author);

        Book book = books.get(position);

        bookName.setText(book.getTitle());
        bookAuthor.setText(book.getAuthor());

        return bookListItem;
    }

}
