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
import java.util.HashMap;

// Custom Adapter BookAdapter extends ArrayAdapter that accepts HashMap Objects.
public class BookAdapter extends ArrayAdapter<HashMap<Integer, Book>> {

    // The ArrayList containing all the books.
    private ArrayList<HashMap<Integer, Book>> books;

    // The passed context.
    private Context context;

    public BookAdapter(Context context, ArrayList<HashMap<Integer, Book>> books){
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

        // Get the book HashMap from the books array.
        HashMap<Integer, Book> bookMap = books.get(position);

        // Get reference to the textViews.
        TextView bookName = bookListItem.findViewById(R.id.list_book_name);
        TextView bookAuthor = bookListItem.findViewById(R.id.list_book_author);

        // Get the Book object using the position as the key, which is the index of HashMap in the ArrayList.
        Book book = bookMap.get(position);

        // If Book Object book is not null.
        if(book != null) {
            // Get the title and author using the getters.
            String title = book.getName();
            String author = book.getAuthor();
            // Set the text accordingly.
            bookName.setText(title);
            bookAuthor.setText(author);
        }
        // Return the inflated view.
        return bookListItem;
    }

}
