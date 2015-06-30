package sebi.todos;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ListActivity extends Activity {

    protected static final String LOG_TAG = "myDataBase";
    private String sql;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        doSelect();
        SQLiteDatabase db = new DatabaseHelper(this).getReadableDatabase();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String date = dateFormat.format(new Date(System.currentTimeMillis()));


        //String sql = "SELECT rowid _id,* FROM todo WHERE " + DatabaseHelper.DATE_FIELD_NAME + " = date('" + date +"')";

        if(getIntent().getBooleanExtra(MainActivity.TODAY_ALL, false))
        {
            sql = "SELECT rowid _id,* FROM todo WHERE " + DatabaseHelper.DATE_FIELD_NAME + " = '" + date + "'";
        }
        else
        {
            sql = "SELECT rowid _id,* FROM todo";
        }

        Cursor cursor = db.rawQuery(sql, null);
        String[] from = new String[] {
                DatabaseHelper.TITLE_FIELD_NAME,
                DatabaseHelper.DESCR_FIELD_NAME,
                DatabaseHelper.DATE_FIELD_NAME} ;

        int[] to = new int[] {R.id.title, R.id.descr, R.id.date};

        SimpleCursorAdapter sca = new SimpleCursorAdapter(this, R.layout.list_item, cursor, from, to, 0);

        ListView lv = (ListView) findViewById(R.id.listView);

        lv.setAdapter(sca);

    }

    private void doSelect()
    {
        SQLiteDatabase db = new DatabaseHelper(this).getReadableDatabase();

        Cursor result = db.query(DatabaseHelper.TABLE_NAME, null, null, null, null, null, DatabaseHelper.DATE_FIELD_NAME, null);

        if(result.moveToFirst()){
            int dateIdx = result.getColumnIndex(DatabaseHelper.DATE_FIELD_NAME);
            int titleIdx = result.getColumnIndex(DatabaseHelper.TITLE_FIELD_NAME);
            int descriptionIdx = result.getColumnIndex(DatabaseHelper.DESCR_FIELD_NAME);

            do {
                long date = result.getLong(dateIdx);
                String dateString = result.getString(dateIdx);
                String title = result.getString(titleIdx);
                String description = result.getString(descriptionIdx);

                Log.d(LOG_TAG, new Date(date) + " " + date + " " + dateString + " " +  title + " " + description);
            }while (result.moveToNext());
        }

        result.close();

        db.close();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
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
