package c0defather.darweather.adapters;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;

import c0defather.darweather.models.City;

/**
 * Created by kuanysh on 4/24/18.
 * Implementation of DiffUtil.Callback for RecyclerView that represents list of cities.
 */

public class DiffCallback extends DiffUtil.Callback{

    private List<City> oldCities;
    private List<City> newCities;

    /**
     * Default constructor.
     * @param oldCities old list of cities.
     * @param newCities new list of cities.
     */
    public DiffCallback(List<City> oldCities, List<City> newCities) {
        this.newCities = newCities;
        this.oldCities = oldCities;
    }

    /**
     * Get size of old cities list.
     * @return size of old cities list.
     */
    @Override
    public int getOldListSize() {
        return oldCities.size();
    }

    /**
     * Get size of new cities list.
     * @return size of new cities list.
     */
    @Override
    public int getNewListSize() {
        return newCities.size();
    }

    /**
     * Checks if two cities are same.
     * @param oldItemPosition position of old item.
     * @param newItemPosition position of new item.
     * @return true if two items are same; otherwise, false.
     */
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldCities.get(oldItemPosition).equals(newCities.get(newItemPosition));
    }

    /**
     * Checks if contents of two cities are same.
     * @param oldItemPosition position of old item.
     * @param newItemPosition position of new item.
     * @return true if contents of two items are same; otherwise, false.
     */
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        City oldCity = oldCities.get(oldItemPosition);
        City newCity = newCities.get(newItemPosition);
        if (oldCity.equals(newCity)) {
            if (oldCity == null || newCity == null) {
                return true;
            }
            if (oldCity.getWeather() == null && newCity.getWeather() == null) {
                return true;
            }
            if (oldCity.getWeather() == null || newCity.getWeather() == null) {
                return false;
            }
            return oldCity.getWeather().equals(newCity.getWeather());
        } else {
            return false;
        }
    }

    /**
     * Get payload of change.
     * @param oldItemPosition position of old item.
     * @param newItemPosition position of new item.
     * @return
     */
    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}