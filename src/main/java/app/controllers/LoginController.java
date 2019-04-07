package app.controllers;

import app.dao.LoginDao;
import app.dto.AuthDTO;
import app.dto.ChangeDTO;
import app.dto.RegisterDTO;
import app.response.Details;
import app.response.Login;
import app.services.JwtFilter;
import app.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LoginController {

    @Autowired
    private LoginDao loginDAO;

    @Autowired
    private JwtService jwtService;

    @PostMapping(value = "/login")
    public ResponseEntity<?> auth(@RequestBody AuthDTO auth) {
        Boolean correctCredentials = loginDAO.get(auth.getEmail(),auth.getPassword());
        if (correctCredentials) {
            Login jwtUser = new Login(auth.getEmail());
            return ResponseEntity.ok(jwtService.getToken(jwtUser));
        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/service/logout")
    public ResponseEntity<?> logout (HttpServletRequest request)
    {
        JwtFilter.logout(loginDAO.logout(request));
        return ResponseEntity.ok("OK");
    }

    @PostMapping(value = "/service/firstLogin")
    public ResponseEntity<?> firstLogging(@RequestBody ChangeDTO change, HttpServletRequest request)
    {
        if (loginDAO.setPassword (request,change.getPassword()))
            return ResponseEntity.ok("OK");
        else
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/service/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO user, HttpServletRequest request)
    {
        if (loginDAO.set(user,request))
            return ResponseEntity.ok("OK");
        else
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/service/details")
    public ResponseEntity<?> getdescription (HttpServletRequest request)
    {
        Details d = loginDAO.getDetails(request);
        if (d != null)
        {
            return ResponseEntity.ok(d);
        }
        else
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/service/updateUser")
    public ResponseEntity<?> setdescription (@RequestBody Details user, HttpServletRequest request)
    {
        Details d = loginDAO.setDetails(user, request);
        if (d != null)
        {
            return ResponseEntity.ok(d);
        }
        else
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
