package causality;

import javafx.beans.property.SimpleBooleanProperty;

public class ExogenousVariable extends Variable {
    public ExogenousVariable(String name) {
        super(name);
        this.bindableProperty = new SimpleBooleanProperty();
    }
}
