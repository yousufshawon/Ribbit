package com.yousuf.shawon.ribbit.ribbit;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.yousuf.shawon.ribbit.ribbit.utility.Utility;

public class LoginActivity extends AppCompatActivity {

    protected TextView mSignUpTextView;
    protected EditText mUsername, mPassword;
    protected Button mLoginButton;
    protected Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        res = getResources();

        iniUI();
    }



    private void iniUI(){

        mUsername = (EditText) findViewById(R.id.usernameField);
        mPassword = (EditText) findViewById(R.id.passwordField);
        mLoginButton = (Button) findViewById(R.id.loginButton);

        mSignUpTextView = (TextView) findViewById(R.id.signUpText);

        addAction();
    }


    private void addAction(){

        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignUpActivity();
            }
        });



        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processLogin();
            }
        });

    }



    private void processLogin(){

        String username = mUsername.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if( username.isEmpty() || password.isEmpty() ){
            String dialogTitle = res.getString(R.string.login_error_title);
            String dialogMessage = res.getString(R.string.login_error_message);
            Utility.showSimpleAlertDialog(this, dialogTitle, dialogMessage );
        }else{
            // Send data to server in background

            sendBackgroundLoginRequest( username, password);

        }


    }



    private void sendBackgroundLoginRequest(String username, String password){
        setProgressBarVisibility(true);
        setSupportProgressBarIndeterminateVisibility(true);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if( e == null ){
                    // success
                    startActivityAndClear(MainActivity.class);
                  //  setProgressBarIndeterminateVisibility(false);

                }else {
                    String dialogTitle = res.getString(R.string.signup_error_title);
                    Utility.showSimpleAlertDialog(LoginActivity.this, dialogTitle, e.getMessage() );
                }
            }
        });


    }


    private void startSignUpActivity(){
        mStartActivity(SignUpActivity.class);
    }


    private void mStartActivity(Class<? extends Activity> mStartActivityClass){

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
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
