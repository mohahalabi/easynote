package ch.supsi.info.gruppo9.javafx.javafxpresentation;

import ch.supsi.info.gruppo9.model.ModelObject;

public interface Presentable<T extends ModelObject> {

    String present(T t);
}
