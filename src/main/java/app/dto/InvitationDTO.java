package app.dto;

public class InvitationDTO {

    private long id;

    private short response;

    public InvitationDTO(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public short getResponse() {
        return response;
    }

    public void setResponse(short response) {
        this.response = response;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvitationDTO)) return false;

        InvitationDTO that = (InvitationDTO) o;

        if (getId() != that.getId()) return false;
        return getResponse() == that.getResponse();
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (int) getResponse();
        return result;
    }
}