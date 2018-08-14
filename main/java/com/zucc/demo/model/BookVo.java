package com.zucc.demo.model;

public class BookVo{
    private static final long serialVersionUID = 1L;

    private String isbn;
    private String book_title;
    private String book_Author;
    private String year_of_Publication;
    private String publisher;
    private String image_URL_S;
    private String image_URL_M;
    private String image_URL_L;

    public BookVo() {
    }

    public String getBook_title() {
        return book_title;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public String getBook_Author() {
        return book_Author;
    }

    public void setBook_Author(String book_Author) {
        this.book_Author = book_Author;
    }

    public String getYear_of_Publication() {
        return year_of_Publication;
    }

    public void setYear_of_Publication(String year_of_Publication) {
        this.year_of_Publication = year_of_Publication;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getImage_URL_S() {
        return image_URL_S;
    }

    public void setImage_URL_S(String image_URL_S) {
        this.image_URL_S = image_URL_S;
    }

    public String getImage_URL_M() {
        return image_URL_M;
    }

    public void setImage_URL_M(String image_URL_M) {
        this.image_URL_M = image_URL_M;
    }

    public String getImage_URL_L() {
        return image_URL_L;
    }

    public void setImage_URL_L(String image_URL_L) {
        this.image_URL_L = image_URL_L;
    }

    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

}