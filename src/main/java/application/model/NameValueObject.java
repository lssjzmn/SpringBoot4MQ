package application.model;

import java.io.Serializable;

/**
 * @author lancec
 *         Date: 14-1-22
 */
public class NameValueObject<T> implements Serializable {
    private static final long serialVersionUID = 812939999870386189L;
    private String name;
    private T value;

    public NameValueObject() {

    }

    public NameValueObject(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NameValueObject)) return false;

        NameValueObject that = (NameValueObject) o;

        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
