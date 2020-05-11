package com.app.hardik.studypdf;

public class Transactions {
    private String name;
    private String pdf;
    private String value;

    //Default Constructor
    public Transactions(){

    }

    //Constructors
    public Transactions(String name, String pdf, String value) {
        this.name = name;
        this.pdf = pdf;
        this.value = value;
    }

    //Getters

    public String getName() {
        return name;
    }

    public String getPdf() {
        return pdf;
    }

    public String getValue() {
        return value;
    }

    //Setters

    public void setName(String name) {
        this.name = name;
    }
    public void setPdf(String pdf) {
        this.pdf = pdf;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public String toString(){
        return this.name + " Bought " + pdf + " At " + value;
    }

}
