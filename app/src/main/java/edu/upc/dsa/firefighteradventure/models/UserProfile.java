package edu.upc.dsa.firefighteradventure.models;

import java.io.Serializable;

public class UserProfile implements Serializable {

    private String username;
    private String email;
    private String birthdate;
    private String profile_photo;
    private int score;
    private int ranking_position;

    public UserProfile() {

    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthdate() {
        return birthdate;
    }
    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }

    public int getRanking_position() {
        return ranking_position;
    }

    public void setRanking_position(int ranking_position) {
        this.ranking_position = ranking_position;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

}
