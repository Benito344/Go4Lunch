package com.behague.benjamin.go_4_lunch.models.objects;

/**
 * Created by Benjamin BEHAGUE on 28/03/2018.
 */

public class User {

    private String uid;
    private String username;
    private String uemail;
    private String urlPicture;
    private String restau;

    public User() { }

    public User(String uid, String username, String uemail, String urlPicture, String restau) {
        this.uid = uid;
        this.username = username;
        this.uemail = uemail;
        this.urlPicture = urlPicture;
        this.restau = restau;
    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    public String getUemail() { return uemail; }
    public String getUrlPicture() { return urlPicture; }
    public String getRestau() { return restau; }

    // --- SETTERS ---
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setUemail(String uemail) { this.uemail = uemail; }
    public void setUrlPicture(String urlPicture) { this.urlPicture = urlPicture; }
    public void setRestau(String restau) { this.restau = restau; }
}
