package model;

import java.io.Serializable;

public class Tag implements Serializable {
    private String key;
    private String value;

    public Tag(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public boolean equals(Tag tag) {
        return key.equals(tag.getKey()) && value.equals(tag.getValue());
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }
}
