package com.jenniferliang.bookcataloguer;

public class ListItem {

    private String title;
    private String author;

    public ListItem (String a, String b) {
        title = a;
        author = b;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }
}
