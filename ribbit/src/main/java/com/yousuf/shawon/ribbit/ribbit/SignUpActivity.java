package com.yousuf.shawon.ribbit.ribbit;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.yousuf.shawon.ribbit.ribbit.utility.Utility;

public class SignUpActivity extends AppCompatActivity {

    protected EditText mUsername, mPassword, mEmail;
    protected Button mSignUpButton;
    protected Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        res = getResources();
        iniUI();
    }


    private void iniUI(){

        mUsername = (EditText) findViewById(R.id.usernameField);
        mPassword = (EditText) findViewById(R.id.passwordField);
        mEmail = (EditText) findViewById(R.id.emailField);
        mSignUpButton = (Button) findViewById(R.id.signupButton);

        setUIProperty();
    }


    private void setUIProperty(){

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                processSignUp();
            }
        });
    }


    private void processSignUp(){

        String email = mEmail.getText().toString().trim();
        String username = mUsername.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if( email.isEmpty() || username.isEmpty() || password.isEmpty() ){
            String dialogTitle = res.getString(R.string.signup_error_title);
            String dialogMessage = res.getString(R.string.signup_error_message);
            Utility.showSimpleAlertDialog(this, dialogTitle, dialogMessage );
        }else{
            // Send data to server in background

            sendBackgroundRequest(email, username, password);

        }

    }


    private void sendBackgroundRequest(String email, String username, String password){

        ParseUser newUser = new ParseUser();

        newUser.setEmail(email);
        newUser.setUsername(username);
        newUser.setPassword(password);

        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                // if e is null than sign up is successful,
                if(e == null){
                    // success

                    startActivityAndClear(MainActivity.class);


                }else {
                    // an error occur
                    String dialogTitle = res.getString(R.string.signup_error_title);
                    Utility.showSimpleAlertDialog(SignUpActivity.this, dialogTitle, e.getMessage() );
                }
            }
        });
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
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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
