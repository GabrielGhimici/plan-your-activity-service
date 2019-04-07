package app.response;

public class Team
{

    private long id;

    private boolean leader;

    private String name;

    public Team()
    {
        this.leader = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isLeader() {
        return leader;
    }

    public void setLeader(boolean leader) {
        this.leader = leader;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}