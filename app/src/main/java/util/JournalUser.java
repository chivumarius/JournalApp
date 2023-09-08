package util;


import android.app.Application;



public class JournalUser extends Application {

    // ▼ "DECLARATION" OF "ATTRIBUTES" ▼
    private String username;
    private String userId;


    // ▼ "CREATING" AN "INSTANCE" OF "JOURNAL USER" ▼
    private static JournalUser instance;




    // ▼ "SINGLETON DESIGN PATTER" ▼
    // ▼ "FULL CONSTRUCTOR" ▼
    public static JournalUser getInstance(){
        // ▼ "CHECKING" IF "INSTANCE" IS "NULL" ▼
        if (instance == null){
            // ▼ "CREATING" A "NEW INSTANCE" OF "JOURNAL USER" ▼
            instance = new JournalUser();
        }

        // ▼ "OTHERWISE" ▼
        return instance;
    }


    // ▼ "EMPTY CONSTRUCTOR" ▼
    public JournalUser(){

    }



    // ▼ "GETTER" → FOR "USERNAME" ▼
    public String getUsername(){
        return username;
    }


    // ▼ "SETTER" → FOR "USERNAME" ▼
    public void setUsername(String username) {
        this.username = username;
    }



    // ▼ "GETTER" → FOR "USER ID" ▼
    public String getUserId() {
        return userId;
    }


    // ▼ "SETTER" → FOR "USER ID" ▼
    public void setUserId(String userId) {
        this.userId = userId;
    }
}

