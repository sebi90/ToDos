package sebi.todos;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {
    public static final String TODAY_ALL = "Today_All";
    private static final String LOG_TAG = "myDataBase";

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    private EditText editTextTitle, editTextDescription, editTextDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        editTextDate = (EditText) findViewById(R.id.editTextDate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void onClickSave(View view) {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        String dateString = editTextDate.getText().toString();
        long date = 0;

        if (!dateString.isEmpty()) {
            try {
                Date d = dateFormat.parse(dateString);
                date = d.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        doInsert(title, description, date);

    }

    public void onClickReset(View view) {
        doDelete();
    }

    public void onClickAllToday(View view) {
        Intent intent = new Intent(this, ListActivity.class);
        switch (view.getId()) {
            case R.id.buttonAll:
                intent.putExtra(TODAY_ALL, false);
                break;
            case R.id.buttonToday:
                intent.putExtra(TODAY_ALL, true);
                break;
        }
        startActivity(intent);
    }

    public void onClickCount(View view) {

        SQLiteDatabase db = new DatabaseHelper(this).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT rowid _id,* FROM todo", null);
        Toast.makeText(getApplicationContext(), cursor.getCount() + "", Toast.LENGTH_SHORT).show();
    }


    private void doInsert(String title, String description, long datum) {
        SQLiteDatabase db = new DatabaseHelper(this).getWritableDatabase();

        ContentValues vals = new ContentValues();
        if (datum == 0)
            vals.put(DatabaseHelper.DATE_FIELD_NAME, dateFormat.format(new Date(System.currentTimeMillis())));
        else
            vals.put(DatabaseHelper.DATE_FIELD_NAME, dateFormat.format(new Date(datum)).toString());

        vals.put(DatabaseHelper.TITLE_FIELD_NAME, title);
        vals.put(DatabaseHelper.DESCR_FIELD_NAME, description);

        Log.d(LOG_TAG, "Datum aus vals: " + vals.getAsString(DatabaseHelper.DATE_FIELD_NAME));

        db.insert(DatabaseHelper.TABLE_NAME, null, vals);

        db.close();
        Log.d(LOG_TAG, "Eintrag: Title: " + title + " Description: " + description + " Datum: " + datum);

    }


    private void doDelete() {
        SQLiteDatabase db = new DatabaseHelper(this).getWritableDatabase();

        int rows = db.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.DATE_FIELD_NAME, null);

        Log.d(LOG_TAG, rows + " rows deleted");
        db.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
