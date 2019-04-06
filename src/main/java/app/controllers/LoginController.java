package app.controllers;

import app.dao.LoginDao;
import app.dto.AuthDTO;
import app.response.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LoginController {

    @Autowired
    private LoginDao loginDAO;

    @PostMapping(value = "/login")
    public ResponseEntity<?> auth(@RequestBody AuthDTO auth) {
        Boolean correctCredentials = loginDAO.get(auth.getEmail(),auth.getPassword());
        if (correctCredentials) {
            Login jwtUser = new Login(auth.getEmail());
            return ResponseEntity.ok("ok");
        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/service/logout")
    public ResponseEntity<?> logout (HttpServletRequest request)
    {
        return ResponseEntity.ok("OK");
    }

}
