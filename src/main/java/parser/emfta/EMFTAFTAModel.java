package parser.emfta;

import java.util.List;

public class EMFTAFTAModel {
    private String name;
    private String description;
    private String comments;
    private int rootEventIndex;

    private List<EMFTAEvent> events;

    public EMFTAFTAModel(String name, String description, String comments, int rootEventIndex, List<EMFTAEvent> events) {
        this.name = name;
        this.description = description;
        this.comments = comments;
        this.rootEventIndex = rootEventIndex;
        this.events = events;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EMFTAFTAModel that = (EMFTAFTAModel) o;

        if (rootEventIndex != that.rootEventIndex) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (comments != null ? !comments.equals(that.comments) : that.comments != null) return false;
        return events != null ? events.equals(that.events) : that.events == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        result = 31 * result + rootEventIndex;
        result = 31 * result + (events != null ? events.hashCode() : 0);
        return result;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getComments() {
        return comments;
    }

    public int getRootEventIndex() {
        return rootEventIndex;
    }

    public List<EMFTAEvent> getEvents() {
        return events;
    }
}
