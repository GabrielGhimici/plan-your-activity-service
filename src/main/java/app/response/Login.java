package app.response;

import lombok.Data;

@Data
public class Login {

    public Login(){}

    private String userName;

    public Login(String user)
    {
        this.userName=user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}