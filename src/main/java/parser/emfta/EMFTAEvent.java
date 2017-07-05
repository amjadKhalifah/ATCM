package parser.emfta;

public class EMFTAEvent {
    public enum EMFTAEventType {
        Basic, External, Undevelopped, Conditioning, Intermediate
    }

    private EMFTAEventType type;
    private String name;
    private String description;
    private double probability;
    // optional; can be null
    private EMFTAGate gate;
    // TODO referenceCount? RelatedObject?

    public EMFTAEvent(EMFTAEventType type, String name, String description, double probability, EMFTAGate gate) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.probability = probability;
        this.gate = gate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EMFTAEvent that = (EMFTAEvent) o;

        if (Double.compare(that.probability, probability) != 0) return false;
        if (type != that.type) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        return gate != null ? gate.equals(that.gate) : that.gate == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = type != null ? type.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        temp = Double.doubleToLongBits(probability);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (gate != null ? gate.hashCode() : 0);
        return result;
    }

    public EMFTAEventType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getProbability() {
        return probability;
    }

    public EMFTAGate getGate() {
        return gate;
    }
}
