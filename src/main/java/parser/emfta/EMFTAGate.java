package parser.emfta;

import java.util.Set;

public class EMFTAGate {
    public enum EMFTAGateType {
        OR, AND, XOR, PRIORITY_AND, PRIORITY_OR, INHIBIT, INTERMEDIATE, ORMORE, ORLESS
    }

    private String description;
    private Set<Integer> eventIndices;
    private EMFTAGateType type;
    // TODO Field Nb Occurences?


    public EMFTAGate(String description, Set<Integer> eventIndices, EMFTAGateType type) {
        this.description = description;
        this.eventIndices = eventIndices;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EMFTAGate gate = (EMFTAGate) o;

        if (description != null ? !description.equals(gate.description) : gate.description != null) return false;
        if (eventIndices != null ? !eventIndices.equals(gate.eventIndices) : gate.eventIndices != null) return false;
        return type == gate.type;
    }

    @Override
    public int hashCode() {
        int result = description != null ? description.hashCode() : 0;
        result = 31 * result + (eventIndices != null ? eventIndices.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    public String getDescription() {
        return description;
    }

    public Set<Integer> getEventIndices() {
        return eventIndices;
    }

    public EMFTAGateType getType() {
        return type;
    }
}
