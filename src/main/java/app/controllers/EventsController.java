package app.controllers;

import app.dao.EventsDao;
import app.dto.EventDTO;
import app.dto.EventUpdateDTO;
import app.dto.InvitationDTO;
import app.response.EventsPOJO;
import app.response.Invitations;
import app.response.TeamPOJO;
import app.response.UserPOJO;
import app.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class EventsController {

    @Autowired
    private EventsDao EventsDAO;

    @Autowired
    private JwtService jwtService;

    @PutMapping(value = "/service/addEvent")
    public ResponseEntity<?> addEvents (@RequestBody EventDTO event, HttpServletRequest request)
    {
        EventsPOJO e = EventsDAO.addEvent(event,request);
        if(e != null) {
            return ResponseEntity.ok(e);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/service/updateEvent")
    public ResponseEntity<?> updateEvents (@RequestBody EventUpdateDTO event)
    {
        EventsPOJO e = EventsDAO.updateEvent(event);
        if(e != null) {

            return ResponseEntity.ok(e);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/service/deleteEvent")
    public ResponseEntity<?> deleteEvents (@RequestBody EventUpdateDTO event, HttpServletRequest request)
    {
        if(EventsDAO.deleteEvent(event, request)) {

            return ResponseEntity.ok("OK");
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/service/invitations")
    public ResponseEntity<?> showInvitations (HttpServletRequest request)
    {
        Invitations[] jo = EventsDAO.getInvitations(request);

        Map maping = new HashMap<String,String>();
        maping.put("invitations",  jo);

        return ResponseEntity.ok(maping);
    }

    @PutMapping(value = "/service/respond")
    public ResponseEntity<?> showInvitations (@RequestBody InvitationDTO inv)
    {
        if(EventsDAO.respond(inv)) {
            return ResponseEntity.ok("OK");
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/service/teams")
    public ResponseEntity<?> listOfTeams ()
    {
        TeamPOJO[] jo = EventsDAO.getTeams();

        Map maping = new HashMap<String,String>();
        maping.put("teams",  jo);

        return ResponseEntity.ok(maping);
    }

    @PostMapping(value = "/service/colleagues")
    public ResponseEntity<?> listOfUsers (HttpServletRequest request)
    {
        UserPOJO[] jo = EventsDAO.getUsers(request);

        Map maping = new HashMap<String,String>();
        maping.put("users",  jo);

        return ResponseEntity.ok(maping);
    }
}
