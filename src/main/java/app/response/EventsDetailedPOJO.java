package app.response;

import app.dto.AttendDTO;

import java.util.HashMap;
import java.util.Map;

public class EventsDetailedPOJO
{

    private long id;

    private String description;

    private String start_date;

    private String start_time;

    private String finish_date;

    private String finish_time;

    private String creator;

    private Map attendants;

    public EventsDetailedPOJO()
    {
        attendants = new HashMap<String,String>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getFinish_date() {
        return finish_date;
    }

    public void setFinish_date(String end_date) {
        this.finish_date = end_date;
    }

    public String getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(String end_time) {
        this.finish_time = end_time;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Map getAttendants() {
        return attendants;
    }

    public void setAttendants(Map attendants) {
        this.attendants = attendants;
    }
}