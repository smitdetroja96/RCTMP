package com.example.rctmp;

import java.util.Comparator;

public class SuggestionsClass {

    String name_tag;

    public SuggestionsClass() {
    }

    String aging_bits;

    public SuggestionsClass(String name_tag, String aging_bits) {
        this.name_tag = name_tag;
        this.aging_bits = aging_bits;
    }

    public String getName_tag() {
        return name_tag;
    }

    public void setName_tag(String name_tag) {
        this.name_tag = name_tag;
    }

    public String getAging_bits() {
        return aging_bits;
    }

    public void setAging_bits(String aging_bits) {
        this.aging_bits = aging_bits;
    }

    public static Comparator<SuggestionsClass> AgingBitsComparator = new Comparator<SuggestionsClass>() {
        @Override
        public int compare(SuggestionsClass o1,SuggestionsClass o2) {
            return (0 - o1.getAging_bits().compareTo(o2.getAging_bits()));
        }
    };
}
