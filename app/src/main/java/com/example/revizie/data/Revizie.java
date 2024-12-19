package com.example.revizie.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.Objects;

@Entity(tableName = "revizii")
public class Revizie {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private int price;
    private Date date;
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Revizie(long id, int price, Date date, String name) {
        this.id = id;
        this.price = price;
        this.date = date;
        this.name = name;
    }

    @Ignore
    public Revizie(int price, Date date, String name) {
        this.price = price;
        this.date = date;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Revizie revizie = (Revizie) o;
        return price == revizie.price && Objects.equals(date, revizie.date) && Objects.equals(name, revizie.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, date, name);
    }

    @Override
    public String toString() {
        return "Revizie{" +
                "id=" + id +
                ", price=" + price +
                ", date=" + date +
                ", name='" + name + '\'' +
                '}';
    }
}
