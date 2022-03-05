package com.example.appcollege.Class;

public class CourseData
{
    private String courseID, courseName, subject2, subject3, subject4, subject5, feeAmount;

    public CourseData()
    {

    }

    public CourseData(String courseID, String courseName, String subject2, String subject3, String subject4, String subject5, String feeAmount) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.subject2 = subject2;
        this.subject3 = subject3;
        this.subject4 = subject4;
        this.subject5 = subject5;
        this.feeAmount = feeAmount;
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

    public String getSubject2() {
        return subject2;
    }

    public void setSubject2(String subject2) {
        this.subject2 = subject2;
    }

    public String getSubject3() {
        return subject3;
    }

    public void setSubject3(String subject3) {
        this.subject3 = subject3;
    }

    public String getSubject4() {
        return subject4;
    }

    public void setSubject4(String subject4) {
        this.subject4 = subject4;
    }

    public String getSubject5() {
        return subject5;
    }

    public void setSubject5(String subject5) {
        this.subject5 = subject5;
    }

    public String getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(String feeAmount) {
        this.feeAmount = feeAmount;
    }
}
