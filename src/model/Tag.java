package model;

import java.io.Serializable;

/**
 * @author Joshua Li, Dingbang Chen
 *
 */
public class Tag implements Serializable {
    private String key;
    private String value;

    public Tag(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * @return tag key
     */
    public String getKey() {
        return key;
    }

    /**
     * @return tag value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param tag
     * @return True if key and value both equal
     */
    public boolean equals(Tag tag) {
        return key.equals(tag.getKey()) && value.equals(tag.getValue());
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }
}
