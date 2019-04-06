package app.dto;

public class AuthDTO {

    private String email;
    private String password;

    public AuthDTO(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthDTO)) return false;

        AuthDTO authDTO = (AuthDTO) o;

        if (getEmail() != null ? !getEmail().equals(authDTO.getEmail()) : authDTO.getEmail() != null) return false;
        return getPassword() != null ? getPassword().equals(authDTO.getPassword()) : authDTO.getPassword() == null;
    }

    @Override
    public int hashCode() {
        int result = getEmail() != null ? getEmail().hashCode() : 0;
        result = 31 * result + (getPassword() != null ? getPassword().hashCode() : 0);
        return result;
    }
}