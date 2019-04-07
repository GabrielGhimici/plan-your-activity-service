package app.dto;

import app.response.AttendantPOJO;

import java.util.Arrays;
import java.util.Date;

public class EventDTO {

    private String description;

    private String start_date;

    private String start_time;

    private String finish_date;

    private String finish_time;

    private AttendantPOJO[] attendants;

    public EventDTO(){}

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

    public void setFinish_date(String finish_date) {
        this.finish_date = finish_date;
    }

    public String getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(String finish_time) {
        this.finish_time = finish_time;
    }

    public AttendantPOJO[] getAttendants() {
        return attendants;
    }

    public void setAttendants(AttendantPOJO[] attendants) {
        this.attendants = attendants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventDTO)) return false;

        EventDTO eventDTO = (EventDTO) o;

        if (getDescription() != null ? !getDescription().equals(eventDTO.getDescription()) : eventDTO.getDescription() != null)
            return false;
        if (getStart_date() != null ? !getStart_date().equals(eventDTO.getStart_date()) : eventDTO.getStart_date() != null)
            return false;
        if (getStart_time() != null ? !getStart_time().equals(eventDTO.getStart_time()) : eventDTO.getStart_time() != null)
            return false;
        if (getFinish_date() != null ? !getFinish_date().equals(eventDTO.getFinish_date()) : eventDTO.getFinish_date() != null)
            return false;
        if (getFinish_time() != null ? !getFinish_time().equals(eventDTO.getFinish_time()) : eventDTO.getFinish_time() != null)
            return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(getAttendants(), eventDTO.getAttendants());
    }

    @Override
    public int hashCode() {
        int result = getDescription() != null ? getDescription().hashCode() : 0;
        result = 31 * result + (getStart_date() != null ? getStart_date().hashCode() : 0);
        result = 31 * result + (getStart_time() != null ? getStart_time().hashCode() : 0);
        result = 31 * result + (getFinish_date() != null ? getFinish_date().hashCode() : 0);
        result = 31 * result + (getFinish_time() != null ? getFinish_time().hashCode() : 0);
        result = 31 * result + Arrays.hashCode(getAttendants());
        return result;
    }
}