package com.example.rctmp;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class HistoryBooksClass implements Serializable {
    BooksClass mybook;
    String checkInDate;

    public HistoryBooksClass(BooksClass mybook, String checkInDate) {
        this.mybook = mybook;
        this.checkInDate = checkInDate;
    }

    public HistoryBooksClass() {
    }

    public BooksClass getMybook() {
        return mybook;
    }

    public void setMybook(BooksClass mybook) {
        this.mybook = mybook;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public static Comparator<HistoryBooksClass> TitleComparator = new Comparator<HistoryBooksClass>() {
        @Override
        public int compare(HistoryBooksClass o1,HistoryBooksClass o2) {
            return o1.mybook.name.compareTo(o2.mybook.name);
        }
    };
    public static Comparator<HistoryBooksClass> AuthorComparator = new Comparator<HistoryBooksClass>() {
        @Override
        public int compare(HistoryBooksClass o1,HistoryBooksClass o2) {
            return o1.mybook.authors.compareTo(o2.mybook.authors);
        }
    };
    public static Comparator<HistoryBooksClass> DateComparator = new Comparator<HistoryBooksClass>() {
        @Override
        public int compare(HistoryBooksClass o1,HistoryBooksClass o2) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date d1 = format.parse(o1.checkInDate);
                Date d2 = format.parse(o2.checkInDate);
                return d1.compareTo(d2);
            }
            catch (Exception e){}
            return 0;
        }
    };
}
