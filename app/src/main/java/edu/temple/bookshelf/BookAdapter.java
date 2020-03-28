package edu.temple.bookshelf;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class BookAdapter extends ArrayAdapter<HashMap<String, String>> {

    private ArrayList<HashMap<String, String>> books;

    private Context context;

    public BookAdapter(Context context, ArrayList<HashMap<String, String>> books){
        super(context, 0, books);
        this.books = books;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View bookListItem = convertView;

        if(bookListItem == null){
            bookListItem = LayoutInflater.from(context).inflate(R.layout.list_book_item, parent, false);
        }

        HashMap<String, String> book = getItem(position);

        TextView bookName = bookListItem.findViewById(R.id.list_book_name);
        TextView bookAuthor = bookListItem.findViewById(R.id.list_book_author);

        Iterator i = book.keySet().iterator();

        String key = (String) i.next();
        String value = book.get(key);

        bookName.setText(key);
        bookAuthor.setText(value);

        return bookListItem;
    }

}
