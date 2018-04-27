package c0defather.darweather.models;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by kuanysh on 4/24/18.
 *
 * Represents City.
 */

public class City implements Serializable, Comparable{
    private String name;
    private String weather;

    /**
     * Get name of city.
     * @return name of city.
     */
    public String getName() {
        return name;
    }

    /**
     * Set name of city.
     * @param name name of city.
     * @return current City instance.
     */
    public City setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get weather in city.
     * @return weather in city.
     */
    public String getWeather() {
        return weather;
    }

    /**
     * Set weather in city.
     * @param weather weather represented in string.
     * @return current City instance.
     */
    public City setWeather(String weather) {
        this.weather = weather;
        return this;
    }

    /**
     * Get string representation of City instance.
     * @return string representation of City instance.
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Check if instances are equal.
     * @param o another City instance.
     * @return true if cities are equal; otherwise, false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        return name != null ? name.equals(city.name) : city.name == null;
    }

    /**
     * Needed for hash data structures.
     * @return hash code of City instance.
     */
    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    /**
     * Compares to another instance of City.
     * @param o another instance of City.
     * @return 0 if equal or 1 if greater; otherwise, -1.
     */
    @Override
    public int compareTo(@NonNull Object o) {
        City other = (City)o;
        if (other.getName().equals(getName()))
            return getWeather().compareTo(other.getWeather());
        return getName().compareTo(other.getName());
    }
}
