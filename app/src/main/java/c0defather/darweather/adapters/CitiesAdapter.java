package c0defather.darweather.adapters;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import c0defather.darweather.R;
import c0defather.darweather.models.City;

/**
 * Created by kuanysh on 4/24/18.
 * Extension of RecyclerView.Adapter to be used in recycler view.
 */

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.ViewHolder> {
    private List<City> cities = new ArrayList<>();

    /**
     * View holder for a city.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * TextView to show name of city.
         */
        public TextView tvName;
        /**
         * TextView to show temperature in city.
         */
        public TextView tvTemp;

        /**
         * Default constructor.
         * @param name TextView that shows name of city.
         * @param temp TextView that shows temperature in city.
         */
        public ViewHolder(TextView name, TextView temp) {
            super((View) name.getParent());
            tvName = name;
            tvTemp = temp;
        }
    }

    /**
     * Creates new view in recycler view.
     */
    @Override
    public CitiesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_item, parent, false);
        TextView tvName = viewGroup.findViewById(R.id.placeName);
        TextView tvTemp = viewGroup.findViewById(R.id.temp);
        ViewHolder vh = new ViewHolder(tvName, tvTemp);
        return vh;
    }

    /**
     * Replace the content of a view.
     * @param holder view holder.
     * @param position position in list.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(cities.get(position).getName());
        holder.tvTemp.setText(cities.get(position).getWeather());
    }

    /**
     * Return size of list of cities.
     * @return
     */
    @Override
    public int getItemCount() {
        return cities.size();
    }

    /**
     * Update list of cities to calculate diff.
     * @param newList new list of cities.
     */
    public void updateList(List<City> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(cities, newList));
        cities.clear();
        cities.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    /**
     * Adds item to list of cities.
     * @param city city to be added.
     */
    public void addItem(City city) {
        List<City> newList = new ArrayList<>();
        newList.addAll(cities);
        if (!newList.contains(city))
            newList.add(city);
        updateList(newList);
    }

    /**
     * Get list of list of cities in adapter.
     * @return list of cities in adapter.
     */
    public List<City> getCities() {return cities;}
}