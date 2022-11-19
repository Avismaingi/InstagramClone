package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView switchButton;
    Button submit;
    int submitFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseUser.logOut();

        switchButton = (TextView) findViewById(R.id.switchButton);
        submit = (Button) findViewById(R.id.submit);
        EditText usernameText = (EditText) findViewById(R.id.username);
        EditText passwordText = (EditText) findViewById(R.id.password);


        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (submitFlag == 0) {
                    submitFlag = 1;
                    submit.setText("Login");
                    switchButton.setText("Or Sign Up");
                } else {
                    submitFlag = 0;
                    submit.setText("Sign Up");
                    switchButton.setText("Or Login");
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();
                if (username.equals("") || password.equals("")) {
                    Toast.makeText(MainActivity.this, "Field incomplete", Toast.LENGTH_SHORT).show();
                } else {
                    if (submitFlag == 0) {
                        ParseUser user = new ParseUser();
                        user.setUsername(username);
                        user.setPassword(password);

                        user.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    // OK
                                    Log.i("Sign Up OK!", "We did it");
                                } else {
                                    e.printStackTrace();
                                    Toast.makeText(MainActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else if (submitFlag == 1) {
                        ParseUser.logInInBackground(username, password, new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if (e == null && user != null) {
                                    Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), ListPage.class);
                                    intent.putExtra("username", username);
                                    startActivity(intent);
                                } else {
                                    e.printStackTrace();
                                    Toast.makeText(MainActivity.this, "Invalid username/password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });


//        passwordText.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
//                    String username = usernameText.getText().toString();
//                    String password = passwordText.getText().toString();
//                    if (username.equals("") || password.equals("")) {
//                        Toast.makeText(MainActivity.this, "Field incomplete", Toast.LENGTH_SHORT).show();
//                    } else {
//                        if (submitFlag == 0) {
//                            ParseUser user = new ParseUser();
//                            user.setUsername(username);
//                            user.setPassword(password);
//
//                            user.signUpInBackground(new SignUpCallback() {
//                                @Override
//                                public void done(ParseException e) {
//                                    if (e == null) {
//                                        // OK
//                                        Log.i("Sign Up OK!", "We did it");
//                                    } else {
//                                        e.printStackTrace();
//                                        Toast.makeText(MainActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                        } else if (submitFlag == 1) {
//                            ParseUser.logInInBackground(username, password, new LogInCallback() {
//                                @Override
//                                public void done(ParseUser user, ParseException e) {
//                                    if (e == null && user != null) {
//                                        Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        e.printStackTrace();
//                                        Toast.makeText(MainActivity.this, "Invalid username/password", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                        }
//                    }
//                }
//                return false;
//            }
//        });
    }
}