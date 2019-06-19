package com.example.rctmp;

import java.io.Serializable;

public class BooksClass implements Serializable {

    String authors,barcode,biblionumber,callnumber,ddc,description,materialType,name,publisher,status,subjects;


    public BooksClass(String authors, String barcode, String biblionumber, String callnumber, String ddc, String description, String materialType, String name, String publisher, String status, String subjects) {
        this.authors = authors;
        this.barcode = barcode;
        this.biblionumber = biblionumber;
        this.callnumber = callnumber;
        this.ddc = ddc;
        this.description = description;
        this.materialType = materialType;
        this.name = name;
        this.publisher = publisher;
        this.status = status;
        this.subjects = subjects;
    }

    public String getAuthors() {
        return authors;
    }

    public BooksClass() {
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getBiblionumber() {
        return biblionumber;
    }

    public void setBiblionumber(String biblionumber) {
        this.biblionumber = biblionumber;
    }

    public String getCallnumber() {
        return callnumber;
    }

    public void setCallnumber(String callnumber) {
        this.callnumber = callnumber;
    }

    public String getDdc() {
        return ddc;
    }

    public void setDdc(String ddc) {
        this.ddc = ddc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }
}
