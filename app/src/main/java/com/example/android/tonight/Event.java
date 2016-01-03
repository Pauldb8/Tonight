package com.example.android.tonight;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by onetec on 15-12-15.
 */
public class Event implements Serializable{
    private int id;
    private String name;
    private Date start_date;
    private Date end_date;
    private int max_people;
    private double price;
    private String picture_url;
    private String city;

    private String description;

    //Pour formater la date en Sam. 21 Jan 2015
    private SimpleDateFormat fDateEvent = new SimpleDateFormat("EEE d MMM yyyy");
    private SimpleDateFormat fHourEvent = new SimpleDateFormat("hh:mm");

    public Event(){}

    //public constructor used with multiple event
    public Event(int id, String name, Date start_date, Date end_date, int max_people,
                 double price, String picture_url, String city){
        this.id = id;
        this.name = name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.max_people = max_people;
        this.price = price;
        this.picture_url = picture_url;
        this.city = city;
    }

    //public constructor used with only one specific event
    public Event(int id, String name, Date start_date, Date end_date, int max_people,
                 double price, String picture_url, String description, String city){
        this.id = id;
        this.name = name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.max_people = max_people;
        this.price = price;
        this.picture_url = picture_url;
        this.description = description;
        this.city = city;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public Date getStart_date() {
        return start_date;
    }

    public String getStartDateFormatted(){
        return fDateEvent.format(this.start_date);
    }
    public String getEndDateFormatted(){
        return fDateEvent.format(this.end_date);
    }

    public String getStartHour(){
        return fHourEvent.format(this.start_date);
    }
    public String getEndHour(){
        return fHourEvent.format(this.end_date);
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public int getMax_people() {
        return max_people;
    }

    public void setMax_people(int max_people) {
        this.max_people = max_people;
    }

    public double getPrice() {
        return price;
    }

    public String getPriceFormatted(){
        if(this.price == 0)
            return "FREE";
        else
            return String.valueOf(this.getPrice());
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String toString(){
        return "Event(" + this.id + ", " + this.name + ", " + this.start_date + ", "
                + this.end_date + ", " + this.max_people + ", " + this.price + ", "
                + this.picture_url + ", " + this.city + ")";
    }
}
