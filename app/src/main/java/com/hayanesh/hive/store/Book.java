package com.hayanesh.hive.store;

import com.hayanesh.hive.Student;

/**
 * Created by Hayanesh on 29-May-17.
 */

public class Book {
    public int no;
    public String cover;
    public String book_title,author;
    public String url;
    public String year;
    public boolean isPresent;
    public String category;
    public Book()
    {

    }
    public Book(int no, String cover, String book_title,String author, String url, String year, boolean isPresent,String category){
        this.no = no;
        this.cover = cover;
        this.book_title = book_title;
        this.url = url;
        this.year = year;
        this.isPresent = isPresent;
        this.author = author;
        this.category = category;
    }

    public boolean getPresent() {
        return isPresent;
    }

    public int getNo() {
        return no;
    }

    public String getCover() {
        return cover;
    }

    public String getBook_title() {
        return book_title;

    }

    public String getUrl() {
        return url;

    }

    public String getYear() {
        return year;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
