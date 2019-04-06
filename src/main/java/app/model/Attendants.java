package app.model;

import javax.persistence.*;

@Entity
@Table(name = "attendants")
public class Attendants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "answer", nullable = false)
    private short answer;

    @Column(name = "invited_user", nullable = false)
    private long invited_user;

    @Column(name = "event", nullable = false)
    private long event;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public short getAnswer() {
        return answer;
    }

    public void setAnswer(short answer) {
        this.answer = answer;
    }

    public long getInvited_user() {
        return invited_user;
    }

    public void setInvited_user(long invited_user) {
        this.invited_user = invited_user;
    }

    public long getEvent() {
        return event;
    }

    public void setEvent(long event) {
        this.event = event;
    }
}