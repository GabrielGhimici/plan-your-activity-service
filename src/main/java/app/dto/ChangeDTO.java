package app.dto;

public class ChangeDTO {

    public ChangeDTO(){}

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChangeDTO)) return false;

        ChangeDTO changeDTO = (ChangeDTO) o;

        return getPassword() != null ? getPassword().equals(changeDTO.getPassword()) : changeDTO.getPassword() == null;
    }

    @Override
    public int hashCode() {
        return getPassword() != null ? getPassword().hashCode() : 0;
    }
}