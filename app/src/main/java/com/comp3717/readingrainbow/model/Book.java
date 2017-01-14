package com.comp3717.readingrainbow.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Book implements Parcelable, Serializable {
    private String title;
    private String[] authors;
    private String[] years;
    private String owner;

    public Book(String title, String[] authors, String[] years) {
        this.title = title;
        this.authors = authors;
        this.years = years;
    }

    public Book(String title, String authors, String years, String owner) {
        this.title = title;
        this.authors = new String[] {authors};
        this.years = new String[] {years};
        this.owner = owner;
    }

    protected Book(Parcel in) {
        title = in.readString();
        authors = in.createStringArray();
        years = in.createStringArray();
        owner = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String[] getAuthors() {
        return authors;
    }

    public String getAuthorsAsString() {
        String authors = "";

        for (int i = 0; i < getAuthors().length; i++) {
            authors += getAuthors()[i];
            if (i != getAuthors().length - 1)
                authors += ", ";
        }

        return authors;
    }

    public String[] getYears() {
        return years;
    }

    public String getOwner() { return owner; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeStringArray(authors);
        dest.writeStringArray(years);
        dest.writeString(owner);
    }

    @Override
    public String toString() {
        String output = getTitle() + "\n";
        for (int i = 0; i < getAuthors().length; i++) {
            output += getAuthors()[i];
            if (i != getAuthors().length - 1)
                output += ", ";
        }
        output += "\n";
        for (int i = 0; i < getYears().length; i++) {
            output += getYears()[i];
            if (i != getYears().length - 1)
                output += ", ";
        }
        return output;
    }
}
