package app.dao;

import app.model.Users;
import app.model.Password;
import app.response.Login;

import javax.servlet.http.HttpServletRequest;

public interface LoginDao {

    boolean get(String email, String pass);

    String logout(HttpServletRequest request);
}