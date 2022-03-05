package com.example.appcollege.Class;

public class UserData
{
    private String name, email, category, number, address, programme,
            courseID, courseName, feeStatus, feeAmount, sub1, sub2, sub3, sub4, image, key;

    public UserData()
    {

    }

    public UserData(String name, String email, String category, String number, String address,
                    String programme, String courseID, String courseName, String sub1, String sub2, String sub3, String sub4, String image, String key)
    {
        this.name = name;
        this.email = email;
        this.category = category;
        this.number = number;
        this.address = address;
        this.programme = programme;
        this.courseID = courseID;
        this.courseName = courseName;
        this.sub1 = sub1;
        this.sub2 = sub2;
        this.sub3 = sub3;
        this.sub4 = sub4;
        this.image = image;
        this.key = key;
    }

    public UserData(String name, String email, String category, String number, String address,
                    String programme, String courseID, String courseName, String sub1, String sub2, String sub3, String sub4,
                    String feeStatus, String feeAmount, String image, String key)
    {
        this.name = name;
        this.email = email;
        this.category = category;
        this.number = number;
        this.address = address;
        this.programme = programme;
        this.courseID = courseID;
        this.courseName = courseName;
        this.sub1 = sub1;
        this.sub2 = sub2;
        this.sub3 = sub3;
        this.sub4 = sub4;
        this.feeStatus = feeStatus;
        this.feeAmount = feeAmount;
        this.image = image;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNumber()
    {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProgramme()
    {
        return programme;
    }

    public void setProgramme(String programme)
    {
        this.programme = programme;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }


    public String getFeeStatus() {
        return feeStatus;
    }

    public void setFeeStatus(String feeStatus) {
        this.feeStatus = feeStatus;
    }

    public String getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(String feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getSub1() {
        return sub1;
    }

    public void setSub1(String sub1) {
        this.sub1 = sub1;
    }

    public String getSub2() {
        return sub2;
    }

    public void setSub2(String sub2) {
        this.sub2 = sub2;
    }

    public String getSub3() {
        return sub3;
    }

    public void setSub3(String sub3) {
        this.sub3 = sub3;
    }

    public String getSub4() {
        return sub4;
    }

    public void setSub4(String sub4) {
        this.sub4 = sub4;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }
}
