package com.java.model;

import java.util.List;

public class Student {
    private String name;
    private List<String> books;

    public Student(String name, List<String> books) {
        this.name = name;
        this.books = books;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getBooks() {
        return books;
    }

    public void setBooks(List<String> books) {
        this.books = books;
    }
}
