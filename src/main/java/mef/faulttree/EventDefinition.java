package mef.faulttree;

import mef.general.Attribute;

import java.util.List;

public abstract class EventDefinition extends ElementDefinition {
    private String name;
    private Role role;
    private String label;
    private List<Attribute> attributes;

    public EventDefinition(String name, Role role, String label, List<Attribute> attributes) {
        this.name = name;
        this.role = role;
        this.label = label;
        this.attributes = attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventDefinition that = (EventDefinition) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (role != that.role) return false;
        if (label != null ? !label.equals(that.label) : that.label != null) return false;
        return attributes != null ? attributes.equals(that.attributes) : that.attributes == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        return result;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public String getLabel() {
        return label;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }
}
