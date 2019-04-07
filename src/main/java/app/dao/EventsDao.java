package app.dao;

import app.dto.EventDTO;
import app.response.EventsPOJO;
import app.response.UserPOJO;

import javax.servlet.http.HttpServletRequest;

public interface EventsDao {

    EventsPOJO addEvent(EventDTO event, HttpServletRequest request);

    UserPOJO[] getUsers(HttpServletRequest request);

}
