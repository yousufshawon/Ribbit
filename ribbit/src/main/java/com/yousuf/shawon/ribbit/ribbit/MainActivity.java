package com.yousuf.shawon.ribbit.ribbit;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.parse.ParseAnalytics;
import com.parse.ParseUser;
import com.yousuf.shawon.ribbit.ribbit.adapter.SectionsPagerAdapter;
import com.yousuf.shawon.ribbit.ribbit.utility.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements ActionBar.TabListener {



    protected Uri mMediaUri;

    public static final int FILE_SIZE_LIMIT = 1024*1024*10; // 10 MB



    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    private final String TAG  = getClass().getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());


        ParseUser currentUser = ParseUser.getCurrentUser();
        if( currentUser != null ){
           // a user is logged in

            Log.i(TAG, currentUser.getUsername());

        }else {
            // if user not logged in then redirect to login Activity
            startActivityAndClear(LoginActivity.class);
        }



   /*

   */
        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(this,getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == AppConstants.PICK_PHOTO_REQUEST || requestCode == AppConstants.PICK_VIDEO_REQUEST) {
                if (data == null) {
                    Toast.makeText(this, getString(R.string.general_error), Toast.LENGTH_LONG).show();
                }
                else {
                    mMediaUri = data.getData();
                }

                Log.i(TAG, "Media URI: " + mMediaUri);
                if (requestCode == AppConstants.PICK_VIDEO_REQUEST) {
                    // make sure the file is less than 10 MB
                    int fileSize = 0;
                    InputStream inputStream = null;

                    try {
                        inputStream = getContentResolver().openInputStream(mMediaUri);
                        fileSize = inputStream.available();
                    }
                    catch (FileNotFoundException e) {
                        Toast.makeText(this, R.string.error_opening_file, Toast.LENGTH_LONG).show();
                        return;
                    }
                    catch (IOException e) {
                        Toast.makeText(this, R.string.error_opening_file, Toast.LENGTH_LONG).show();
                        return;
                    }
                    finally {
                        try {
                            inputStream.close();
                        } catch (IOException e) { /* Intentionally blank */ }
                    }

                    if (fileSize >= FILE_SIZE_LIMIT) {
                        Toast.makeText(this, R.string.error_file_size_too_large, Toast.LENGTH_LONG).show();
                        return;
                    }
                }



            }else {
                // add it to the Gallery
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(mMediaUri);
                sendBroadcast(mediaScanIntent);
            }


            Intent recipientsIntent = new Intent(this, RecipientsActivity.class);
            recipientsIntent.setData(mMediaUri);
            String fileType;
            if (requestCode == AppConstants.PICK_PHOTO_REQUEST || requestCode == AppConstants.TAKE_PHOTO_REQUEST) {
                fileType = ParseConstants.TYPE_IMAGE;
            }
            else {
                fileType = ParseConstants.TYPE_VIDEO;
            }

            recipientsIntent.putExtra(ParseConstants.KEY_FILE_TYPE, fileType);
            startActivity(recipientsIntent);

        }
        else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(this, R.string.general_error, Toast.LENGTH_LONG).show();
        }
    }



    private void startActivity(Class<? extends Activity> mStartActivityClass){

        Intent intent = new Intent(this, mStartActivityClass);
        startActivity(intent);
    }


    private void startActivityAndClear(Class<? extends Activity> mStartActivityClass){

        Intent intent = new Intent(this, mStartActivityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int itemId = item.getItemId();

        switch(itemId){

            case R.id.action_logout:
                Log.i(TAG, "user select logout");
                ParseUser.logOut();
                startActivityAndClear(LoginActivity.class);
                finish();
                break;
            case  R.id.action_edit_friends:
                startActivity(EditFriendsActivity.class);
                break;

            case R.id.action_camera:
                showCameraOption();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showCameraOption(){

        DialogStringList mDialogList = new DialogStringList(this, R.array.camera_choices);


        mDialogList.setOnSelectListener(new DialogStringList.OnSelect() {
            @Override
            public void onSelect(int index) {
                Log.i(TAG, index +  " selected");

                switch(index){
                    case 0:  // Take Picture
                        startPhotoCaptureActivity();
                        break;
                    case 1:   // TAke video
                        startVideoCaptureActivity();
                        break;
                    case 2:  // Choose picture
                        browseGalleryForPhoto();
                        break;
                    case 3:   // Choose video
                        browseGalleryForVideo();
                        break;
                }
            }
        });


    }

    private void browseGalleryForPhoto() {
        Intent choosePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
        choosePhotoIntent.setType("image/*");
        startActivityForResult(choosePhotoIntent, AppConstants.PICK_PHOTO_REQUEST);
    }

    private void browseGalleryForVideo(){
        Intent chooseVideoIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooseVideoIntent.setType("video/*");
        Toast.makeText(MainActivity.this, R.string.video_file_size_warning, Toast.LENGTH_LONG).show();
        startActivityForResult(chooseVideoIntent, AppConstants.PICK_VIDEO_REQUEST);
    }


    private void startPhotoCaptureActivity(){
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        mMediaUri = Utility.getOutputMediaFileUri(AppConstants.MEDIA_TYPE_IMAGE);
        if (mMediaUri == null) {
            // display an error
            Toast.makeText(MainActivity.this, R.string.error_external_storage,
                    Toast.LENGTH_LONG).show();
        }
        else {
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
            startActivityForResult(takePhotoIntent, AppConstants.TAKE_PHOTO_REQUEST);
        }

    }



    private void startVideoCaptureActivity(){
        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        mMediaUri = Utility.getOutputMediaFileUri(AppConstants.MEDIA_TYPE_VIDEO);
        if (mMediaUri == null) {
            // display an error
            Toast.makeText(MainActivity.this, R.string.error_external_storage,
                    Toast.LENGTH_LONG).show();
        }
        else {
            videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
            videoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);  // time is second
            videoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0); // 0 = lowest res
            startActivityForResult(videoIntent, AppConstants.TAKE_VIDEO_REQUEST);
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }



}
