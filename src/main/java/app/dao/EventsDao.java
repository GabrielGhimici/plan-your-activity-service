package app.dao;

import app.dto.EventDTO;
import app.dto.EventUpdateDTO;
import app.dto.InvitationDTO;
import app.response.*;

import javax.servlet.http.HttpServletRequest;

public interface EventsDao {

    EventsPOJO addEvent(EventDTO event, HttpServletRequest request);

    UserPOJO[] getUsers(HttpServletRequest request);

    EventsPOJO updateEvent(EventUpdateDTO event);

    boolean deleteEvent(EventUpdateDTO event, HttpServletRequest request);

    Invitations[] getInvitations(HttpServletRequest request);

    boolean respond(InvitationDTO inv);

    TeamPOJO[] getTeams();

    EventsDetailedPOJO[] getEvents(HttpServletRequest request);
}
