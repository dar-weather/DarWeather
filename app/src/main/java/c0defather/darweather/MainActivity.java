package c0defather.darweather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import c0defather.darweather.adapters.CitiesAdapter;
import c0defather.darweather.helpers.API;
import c0defather.darweather.helpers.CacheHelper;
import c0defather.darweather.models.City;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kuanysh on 4/24/18.
 *
 * MainActivity that shows list of cities with weather data.
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText editText;
    private RecyclerView recyclerView;
    private CitiesAdapter viewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    /**
     * Initialize views.
     */
    private void initViews() {
        editText = findViewById(R.id.editText);
        recyclerView = findViewById(R.id.recyclerView);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        viewAdapter = new CitiesAdapter();
        recyclerView.setAdapter(viewAdapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String newText = editable.toString();
                CacheHelper.getUserInput().setValue(newText);
                CacheHelper.getUserInput().setDate(System.currentTimeMillis());
                if (newText != null && newText.length() > 2) {
                    Observable.just(newText)
                            .delay(300, TimeUnit.MILLISECONDS)
                            .map(API::getCities)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(autocompleteObserver);
                } else if (viewAdapter.getItemCount() > 0) {
                    viewAdapter.updateList(new ArrayList<>());
                }
            }
        });
    }

    /**
     * Initialize cache.
     */
    private void installCache() {
        try {
            CacheHelper.init(getApplicationContext());
            if (System.currentTimeMillis() - CacheHelper.getUserInput().getDate() < 60 * 60 * 1000) {
                editText.setText(CacheHelper.getUserInput().getValue());
            }
        } catch (IOException e) {
            Toast.makeText(this,"Failed to install cache", Toast.LENGTH_SHORT).show();
        } catch (ClassNotFoundException e) {
            Toast.makeText(this,"Cache file violated", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        installCache();
    }
    @Override
    public void onPause() {
        super.onPause();
        try {
            CacheHelper.flushData(true);
        } catch (IOException e) {
            Log.e(TAG, "Failed to save cache data");
        }
    }

    private Observer<List<City>> autocompleteObserver = new Observer<List<City>>() {

        @Override
        public void onError(Throwable e) {
            Log.d("onError", "onError");
        }

        @Override
        public void onComplete() {
            Log.d("onCompleted", "Completed");
        }

        @Override
        public void onSubscribe(Disposable d) {
            Log.d("onSubscribe", "Subscribed");
        }

        @Override
        public void onNext(List<City> cities) {
            Log.d("onNext", cities.toString());
            List<City> intersect = new ArrayList<>();
            for (City city: cities) {
                if (viewAdapter.getCities().contains(city))
                    intersect.add(city);
            }
            viewAdapter.updateList(intersect);
            Observable.fromIterable(cities)
                    .map(API::getWeather)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(weatherObserver);

        }
    };

    private Observer<City> weatherObserver = new Observer<City>() {
        @Override
        public void onError(Throwable e) {
            Log.e("onError", "onError");
        }

        @Override
        public void onComplete() {
            Log.d("onCompleted", "Completed");
        }

        @Override
        public void onSubscribe(Disposable d) {
            Log.d("onSubscribe", "Subscribed");
        }

        @Override
        public void onNext(City city) {
            if (city.getWeather() != null && city.getWeather().length() > 0)
                viewAdapter.addItem(city);
        }
    };
}
