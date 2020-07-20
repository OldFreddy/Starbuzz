package com.oldfredddy.starbuzz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DrinkActivity extends Activity {
    public static final String EXTRA_DRINKID = "drinkId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        //Получить напиток из данных интента
        int drinkId = (int) getIntent().getExtras().get(EXTRA_DRINKID);
        System.out.println(drinkId);
        //        Drink drink = Drink.drinks[drinkId];

        //Создание курсора
        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
        try {
            SQLiteDatabase db = starbuzzDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query("DRINK",
                    new String[]{"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID"},
                    "_id = ?",
                    new String[]{Integer.toString(drinkId)},
                    null, null, null);

            //Переход к первой записи курсора
            if (cursor.moveToFirst()) {
                //получение данных напитка из курсора
                String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(1);
                int photoId = cursor.getInt(2);


                //Заполнение названия напитка
                TextView name = findViewById(R.id.name);
                System.out.println(nameText);
                name.setText(nameText);

                //Заполнение описания напитка
                TextView description = findViewById(R.id.description);
                description.setText(descriptionText);

                //Заполнение изображения напитка
                ImageView photo = findViewById(R.id.photo);
                photo.setImageResource(photoId);
                photo.setContentDescription(nameText);
            }
            cursor.close();
            db.close();
        } catch (SQLiteException e) {
            // Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT).show();
            Toast toast = Toast.makeText(this, "БАЗА НЕДОСТУПНА!", Toast.LENGTH_SHORT);
            toast.show();
        }


    }
}
