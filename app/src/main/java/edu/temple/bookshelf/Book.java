package edu.temple.bookshelf;

// The Book Class will hold details about the actual book.

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {

    // The system provided id of the book.
    private int bookId;
    // The title of the book.
    private String title;
    // The author of the book.
    private String author;
    // The url for the book cover image.
    private String coverURL;

    // Accepts a name and author as constructors.
    public Book(int id, String name, String author, String url){
        this.bookId = id;
        this.title = name;
        this.author = author;
        this.coverURL = url;
    }

    protected Book(Parcel in) {
        bookId = in.readInt();
        title = in.readString();
        author = in.readString();
        coverURL = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(bookId);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(coverURL);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
