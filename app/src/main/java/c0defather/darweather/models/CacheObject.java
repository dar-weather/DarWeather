package c0defather.darweather.models;

import java.io.Serializable;

/**
 * Created by kuanysh on 4/24/18.
 *
 * Class represents pair of date and value to store in cache file.
 */

public class CacheObject<T> implements Serializable {
    private long date;
    private T value;

    /**
     * Default constructor for CacheObject.
     * @param date date in milliseconds when value was modified.
     * @param value value stored.
     */
    public CacheObject(long date, T value) {
        this.date = date;
        this.value = value;
    }

    /**
     * Get date when cache was modified.
     * @return date when cache was modified.
     */
    public long getDate() {
        return date;
    }

    /**
     * Set date when cache was modified.
     * @param date date in milliseconds.
     */
    public void setDate(long date) {
        this.date = date;
    }

    /**
     * Get value stored.
     * @return value.
     */
    public T getValue() {
        return value;
    }

    /**
     * Set value stored.
     * @param value value stored.
     */
    public void setValue(T value) {
        this.value = value;
    }

    /**
     * Check if instances are equal.
     * @param o Second instance of CacheObject.
     * @return true if instances are equal; otherwise, false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CacheObject that = (CacheObject) o;

        if (date != that.date) return false;
        return value != null ? value.equals(that.value) : that.value == null;
    }

    /**
     * Needed for hash data structures.
     * @return hash code of CacheObject instance.
     */
    @Override
    public int hashCode() {
        int result = (int) (date ^ (date >>> 32));
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
