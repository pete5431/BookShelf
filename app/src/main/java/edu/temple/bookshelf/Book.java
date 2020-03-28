package edu.temple.bookshelf;

// The Book Class will hold details about the actual book.

public class Book {

    // The name of the book.
    private String name;
    // The author of the book.
    private String author;

    // Accepts a name and author as constructors.
    public Book(String name, String author){
        this.name = name;
        this.author = author;
    }

    // For getting the name.
    public String getName(){
        return this.name;
    }

    // For getting the author.
    public String getAuthor(){
        return this.author;
    }
}
