package br.com.zelius.simplecontentprovider;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import br.com.zelius.simplecontentprovider.data.DBContract;
import br.com.zelius.simplecontentprovider.data.DBHelper;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDatabase();
    }

    private void initDatabase() {
        SQLiteDatabase db = new DBHelper(this).getWritableDatabase();

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (!c.moveToFirst()) {
            Log.e("DATABASE", "DB was not created.");
        }

        c.close();
    }

    // Insert
    public void ButtonClick_Insert(View v) {
        AlarmModel randomAlarm = AlarmModel.getRandomAlarm();

        ContentValues values = new ContentValues();
        values.put(DBContract.AlarmsEntry.COLUMN_NAME, randomAlarm.name);
        values.put(DBContract.AlarmsEntry.COLUMN_TIME, randomAlarm.time.toString());

        getContentResolver().insert(DBContract.AlarmsEntry.CONTENT_URI, values);

        Toast.makeText(this, "Alarm inserted!", Toast.LENGTH_SHORT).show();
    }

    // Read
    public void ButtonClick_Read(View v) {
        Cursor cursor = getContentResolver().query(
                DBContract.AlarmsEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assert cursor != null;

        String[] results = new String[cursor.getCount()];
        int count = 0;
        while (cursor.moveToNext()) {
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(cursor.getString(2));
                results[count] = "[" + cursor.getInt(0) + "] " +
                        cursor.getString(1) + " | " +
                        new SimpleDateFormat("H:mm").format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            count++;
        }

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.list, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Alarms");
        ListView lv = (ListView) convertView.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, results);
        lv.setAdapter(adapter);
        alertDialog.show();


        cursor.close();
    }

    // Delete
    public void ButtonClick_Delete(View v) {
        getContentResolver().delete(
                DBContract.AlarmsEntry.CONTENT_URI,
                null,
                null
        );

        Toast.makeText(this, "Alarms deleted!", Toast.LENGTH_SHORT).show();
    }


    private static class AlarmModel {
        public int id;
        public String name;
        public Timestamp time;
        public Timestamp date_creation;

        static final String[] RANDOM_ALARMS = {"Wake Up", "Lunch", "Sleep", "Drink Water", "That Party", "Game time", "Meeting", "Take your Pills"};
        static final String[] RANDOM_TIMES = {"06:00", "12:00", "22:00", "14:00", "18:30", "19:30", "15:15", "21:30"};

        public AlarmModel(String name, Timestamp time) {
            this.name = name;
            this.time = time;
        }

        public static AlarmModel getRandomAlarm() {
            Random random = new Random();
            int randomPosition = random.nextInt(RANDOM_ALARMS.length);
            Timestamp time = Timestamp.valueOf("2020-01-02 " + RANDOM_TIMES[randomPosition] + ":00.000000000");

            return new AlarmModel(RANDOM_ALARMS[randomPosition], time);
        }
    }

}
