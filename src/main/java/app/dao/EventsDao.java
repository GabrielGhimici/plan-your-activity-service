package app.dao;

import app.dto.EventDTO;
import app.dto.EventUpdateDTO;
import app.response.EventsPOJO;
import app.response.Invitations;
import app.response.UserPOJO;

import javax.servlet.http.HttpServletRequest;

public interface EventsDao {

    EventsPOJO addEvent(EventDTO event, HttpServletRequest request);

    UserPOJO[] getUsers(HttpServletRequest request);

    EventsPOJO updateEvent(EventUpdateDTO event);

    boolean deleteEvent(EventUpdateDTO event, HttpServletRequest request);

    Invitations[] getInvitations(HttpServletRequest request);
}
