package com.example.rctmp;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class HistoryBooksClass implements Serializable {
    String biblionumber;
    String title;
    String author;
    String checkInDate;

    public String getBiblionumber() {
        return biblionumber;
    }

    public void setBiblionumber(String biblionumber) {
        this.biblionumber = biblionumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public HistoryBooksClass() {
    }

    public HistoryBooksClass(String bib, String tit, String aut, String che){
        biblionumber = bib;
        title = tit;
        author = aut;
        checkInDate = che;
    }

    public static Comparator<HistoryBooksClass> TitleComparator = new Comparator<HistoryBooksClass>() {
        @Override
        public int compare(HistoryBooksClass o1,HistoryBooksClass o2) {
            return o1.title.compareTo(o2.title);
        }
    };
    public static Comparator<HistoryBooksClass> AuthorComparator = new Comparator<HistoryBooksClass>() {
        @Override
        public int compare(HistoryBooksClass o1,HistoryBooksClass o2) {
            return o1.author.compareTo(o2.author);
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
