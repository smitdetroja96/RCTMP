package com.example.rctmp;

import java.util.Comparator;

public class SuggestionsBookCount {

    BooksClass book;
    int cnt;

    public SuggestionsBookCount() {
    }

    public SuggestionsBookCount(BooksClass book, int cnt) {
        this.book = book;
        this.cnt = cnt;
    }

    public BooksClass getBook() {
        return book;
    }

    public void setBook(BooksClass book) {
        this.book = book;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public static Comparator<SuggestionsBookCount> CountComparator = new Comparator<SuggestionsBookCount>() {
        @Override
        public int compare(SuggestionsBookCount o1,SuggestionsBookCount o2) {
            return (o1.getCnt() > o2.getCnt()) ? 1 : -1;
        }
    };

}
