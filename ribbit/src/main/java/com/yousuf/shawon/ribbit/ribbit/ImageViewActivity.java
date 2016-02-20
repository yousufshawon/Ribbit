package com.yousuf.shawon.ribbit.ribbit;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.yousuf.shawon.ribbit.ribbit.utility.Log;

import java.util.Timer;
import java.util.TimerTask;

public class ImageViewActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        ImageView imageView = (ImageView)findViewById(R.id.imageView);

        Uri imageUri = getIntent().getData();

        Picasso.with(this).load(imageUri.toString()).into(imageView);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                finish();
            }
        }, 10*1000);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        Log.i(TAG, "selected id: " + id);
        Log.i(TAG, "home id: " + android.R.id.home);
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

//
//        if(id == android.R.id.home){
//            Log.i(TAG, "home selected");
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
