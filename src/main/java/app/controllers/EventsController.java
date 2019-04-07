package app.controllers;

import app.dao.EventsDao;
import app.dto.EventDTO;
import app.dto.EventUpdateDTO;
import app.response.EventsPOJO;
import app.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
}
