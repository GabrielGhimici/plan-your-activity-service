package app.response;

import lombok.Data;

import java.util.Date;

@Data
public class Details {

    private String name;

    private String email;

    private String born;

    private boolean permanentAccount;

    private Team team;

    public Details()
    {
        this.team = new Team();
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

    public String getBorn() {
        return born;
    }

    public void setBorn(String born) {
        this.born = born;
    }

    public boolean getPermanentAccount() {
        return permanentAccount;
    }

    public void setPermanentAccount(boolean firstLogin) {
        this.permanentAccount = firstLogin;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}