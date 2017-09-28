package attacker_attribution;

import java.util.List;

public class User {
    private String ID;
    private String name;
    private String role;
    private List<String> attacks;

    public User(String ID, String name, String role, List<String> attacks) {
        this.ID = ID;
        this.name = name;
        this.role = role;
        this.attacks = attacks;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getAttacks() {
        return attacks;
    }

    public void setAttacks(List<String> attacks) {
        this.attacks = attacks;
    }
}
