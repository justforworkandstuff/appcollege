package com.example.appcollege.Class;

public class AppDetailsData
{
    private String description, email, number, image;

    public AppDetailsData()
    {

    }

    public AppDetailsData(String description, String email, String number)
    {
        this.description = description;
        this.email = email;
        this.number = number;
    }

    public AppDetailsData(String description, String email, String number, String image)
    {
        this.description = description;
        this.email = email;
        this.number = number;
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
