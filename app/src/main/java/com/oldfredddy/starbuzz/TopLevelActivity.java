package com.oldfredddy.starbuzz;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class TopLevelActivity extends Activity {

    private SQLiteDatabase db;
    private Cursor favoriteCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_level);
        setupOptionsListView();
        setupFavoritesListView();

    }

    private void setupOptionsListView() {
        //Создать OnItemClickListener
        final Toast toast1 = Toast.makeText(this, "Привет", Toast.LENGTH_LONG);
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View itemView, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(TopLevelActivity.this, DrinkCategoryActivity.class);
                    startActivity(intent);
                }
                if (position == 1) {
                    toast1.show();
                }
            }
        };
        //Добавить слушателся к списковому представлению
        ListView listView = findViewById(R.id.list_options);
        listView.setOnItemClickListener(itemClickListener);
    }

    private void setupFavoritesListView() {
        //Заполнение списка list_favorites из курсора
        ListView listFavorites = findViewById(R.id.list_favorites);
        try {
            SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
            db = starbuzzDatabaseHelper.getReadableDatabase();
            favoriteCursor = db.query("DRINK",
                    new String[]{"_id", "NAME"},
                    "FAVORITE = 1",
                   null, null, null, null);

            CursorAdapter favoriteAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    favoriteCursor,
                    new String[]{"NAME"},
                    new int[]{android.R.id.text1}, 0);
            listFavorites.setAdapter(favoriteAdapter);
        } catch (SQLiteException e) {
            Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT).show();
        }

        //Переход к DrinkActivity при выборе напитка
        listFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View v, int position, long id) {
                Intent intent = new Intent(TopLevelActivity.this, DrinkActivity.class);
                intent.putExtra(DrinkActivity.EXTRA_DRINKID, (int) id);
                startActivity(intent);
            }
        });
    }

    //Закрытие курсора и базы в методе omDestroy()

    @Override
    protected void onDestroy() {
        super.onDestroy();
        favoriteCursor.close();
        db.close();
    }
}
