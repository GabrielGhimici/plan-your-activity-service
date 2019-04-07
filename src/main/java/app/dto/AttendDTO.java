package app.dto;

public class AttendDTO {

    private long id;

    private String name;

    public AttendDTO(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AttendDTO)) return false;

        AttendDTO attendDTO = (AttendDTO) o;

        if (getId() != attendDTO.getId()) return false;
        return getName() != null ? getName().equals(attendDTO.getName()) : attendDTO.getName() == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }
}