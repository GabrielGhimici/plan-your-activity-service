package app.response;

public class Invitations
{

    private long id;

    private String creator;

    private EventsPOJO event;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public EventsPOJO getEvent() {
        return event;
    }

    public void setEvent(EventsPOJO event) {
        this.event = event;
    }
}