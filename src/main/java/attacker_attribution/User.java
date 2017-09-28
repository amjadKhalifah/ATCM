package attacker_attribution;

import java.util.Set;

public class User {
    private String ID;
    private String name;
    private String role;
    private Set<String> attacks;

    public User(String ID, String name, String role, Set<String> attacks) {
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

    public Set<String> getAttacks() {
        return attacks;
    }

    public void setAttacks(Set<String> attacks) {
        this.attacks = attacks;
    }
}
