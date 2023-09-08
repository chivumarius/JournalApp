package com.chivumarius.journalapp.model;



import com.google.firebase.Timestamp;


// ▼ "MODEL CLASS" ▼
public class Journal {

    // ▼ ATTRIBUTES ▼
    private String title;
    private String thoughts;
    private String imageUrl;


    private String userId;
    private Timestamp timeAdded;
    private String userName;



    // ▼ "EMPTY CONSTRUCTOR" FOR "FIREBASE" ("MANDATORY") ▼
    public Journal() {

    }


    // ▼ "FULL CONSTRUCTOR" ▼
    public Journal(String title, String thoughts, String imageUrl, String userId, Timestamp timeAdded, String userName) {
        // ▼ THIS.PARAMETER = ATTRIBUTE ▼
        this.title = title;
        this.thoughts = thoughts;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.timeAdded = timeAdded;
        this.userName = userName;
    }





    // ▼ "GETTER" → FOR "TITLE" ▼
    public String getTitle() {
        return title;
    }


    // ▼ "SETTER" → FOR "TITLE" ▼
    public void setTitle(String title) {
        this.title = title;
    }



    // ▼ "GETTER" → FOR "THOUGHTS" ▼
    public String getThoughts() {
        return thoughts;
    }


    // ▼ "SETTER" → FOR "THOUGHTS" ▼
    public void setThoughts(String thoughts) {
        this.thoughts = thoughts;
    }



    // ▼ "GETTER" → FOR "IMAGE URL" ▼
    public String getImageUrl() {
        return imageUrl;
    }


    // ▼ "SETTER" → FOR "IMAGE URL" ▼
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    // ▼ "GETTER" → FOR "USER ID" ▼
    public String getUserId() {
        return userId;
    }


    // ▼ "SETTER" → FOR "USER ID" ▼
    public void setUserId(String userId) {
        this.userId = userId;
    }


    // ▼ "GETTER" → FOR "TIME ADDED" ▼
    public Timestamp getTimeAdded() {
        return timeAdded;
    }


    // ▼ "SETTER" → FOR "TIME ADDED" ▼
    public void setTimeAdded(Timestamp timeAdded) {
        this.timeAdded = timeAdded;
    }


    // ▼ "GETTER" → FOR "USER NAME" ▼
    public String getUserName() {
        return userName;
    }


    // ▼ "SETTER" → FOR "USER NAME" ▼
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
