package app.dto;

public class EventUpdateDTO {

    private long id;

    private String description;

    private String start_date;

    private String start_time;

    private String finish_date;

    private String finish_time;

    public EventUpdateDTO(){}

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

    public void setFinish_date(String finish_date) {
        this.finish_date = finish_date;
    }

    public String getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(String finish_time) {
        this.finish_time = finish_time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventUpdateDTO)) return false;

        EventUpdateDTO that = (EventUpdateDTO) o;

        if (getId() != that.getId()) return false;
        if (getDescription() != null ? !getDescription().equals(that.getDescription()) : that.getDescription() != null)
            return false;
        if (getStart_date() != null ? !getStart_date().equals(that.getStart_date()) : that.getStart_date() != null)
            return false;
        if (getStart_time() != null ? !getStart_time().equals(that.getStart_time()) : that.getStart_time() != null)
            return false;
        if (getFinish_date() != null ? !getFinish_date().equals(that.getFinish_date()) : that.getFinish_date() != null)
            return false;
        return getFinish_time() != null ? getFinish_time().equals(that.getFinish_time()) : that.getFinish_time() == null;
    }

    @Override
    public int hashCode() {
        int result = getDescription() != null ? getDescription().hashCode() : 0;
        result = 31 * result + (getStart_date() != null ? getStart_date().hashCode() : 0);
        result = 31 * result + (getStart_time() != null ? getStart_time().hashCode() : 0);
        result = 31 * result + (getFinish_date() != null ? getFinish_date().hashCode() : 0);
        result = 31 * result + (getFinish_time() != null ? getFinish_time().hashCode() : 0);
        return result;
    }
}