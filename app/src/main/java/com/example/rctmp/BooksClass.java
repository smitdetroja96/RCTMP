package com.example.rctmp;

import java.io.Serializable;
import java.util.Comparator;

public class BooksClass implements Serializable {

    String authors;
    int biblionumber;
    String callnumber,description,isbn,materialType,name,publisher;
    int quantity;
    String subjects,url;

    public BooksClass(String authors, int biblionumber, String callnumber, String description, String isbn, String materialType, String name, String publisher, int quantity, String subjects, String url) {
        this.authors = authors;
        this.biblionumber = biblionumber;
        this.callnumber = callnumber;
        this.description = description;
        this.isbn = isbn;
        this.materialType = materialType;
        this.name = name;
        this.publisher = publisher;
        this.quantity = quantity;
        this.subjects = subjects;
        this.url = url;
    }

    public BooksClass() {
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public int getBiblionumber() {
        return biblionumber;
    }

    public void setBiblionumber(int biblionumber) {
        this.biblionumber = biblionumber;
    }

    public String getCallnumber() {
        return callnumber;
    }

    public void setCallnumber(String callnumber) {
        this.callnumber = callnumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static Comparator<BooksClass> TitleComparator = new Comparator<BooksClass>() {
        @Override
        public int compare(BooksClass o1,BooksClass o2) {
            return o1.name.compareTo(o2.name);
        }
    };
    public static Comparator<BooksClass> AuthorComparator = new Comparator<BooksClass>() {
        @Override
        public int compare(BooksClass o1,BooksClass o2) {
            return o1.authors.compareTo(o2.authors);
        }
    };
}
