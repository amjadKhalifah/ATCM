package attacker_attribution;

import java.util.Set;

public class User {
    private String ID;
    private String name;
    private String role;
    private int score;
    private Set<String> attacks;

    public User(String ID, String name, String role, int score, Set<String> attacks) {
        this.ID = ID;
        this.name = name;
        this.role = role;
        this.score = score;
        this.attacks = attacks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (score != user.score) return false;
        if (ID != null ? !ID.equals(user.ID) : user.ID != null) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (role != null ? !role.equals(user.role) : user.role != null) return false;
        return attacks != null ? attacks.equals(user.attacks) : user.attacks == null;
    }

    @Override
    public int hashCode() {
        int result = ID != null ? ID.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + score;
        result = 31 * result + (attacks != null ? attacks.hashCode() : 0);
        return result;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Set<String> getAttacks() {
        return attacks;
    }

    public void setAttacks(Set<String> attacks) {
        this.attacks = attacks;
    }
}
