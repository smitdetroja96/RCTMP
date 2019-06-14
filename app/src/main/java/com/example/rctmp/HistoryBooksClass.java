package com.example.rctmp;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class HistoryBooksClass {
    String biblionumber;
    String title;
    String author;
    String checkInDate;

    public HistoryBooksClass(String bib,String tit,String aut,String che){
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
