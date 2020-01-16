package com.jenniferliang.bookcataloguer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText usernameField;
    EditText passField;
    Button login;
    Button register;
    DBHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameField = findViewById(R.id.editText);
        passField = findViewById(R.id.editText2);
        login = findViewById(R.id.loginBtn);
        register = findViewById(R.id.registerBtn);


        mydb = new DBHelper(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("Test","register button clicked");

                String username = usernameField.getText().toString().toLowerCase();
                String password = passField.getText().toString();
                Cursor results;

                if(username.equals("") || password.equals("")) {
                    Toast.makeText(MainActivity.this, "Enter a username and password",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                try{
                    results = mydb.getUser(username);

                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "Error"+e.toString(),
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if (results.getCount()>0) {
                    Toast.makeText(MainActivity.this, "Username is taken",
                            Toast.LENGTH_LONG).show();
                    usernameField.setText("");
                    passField.setText("");
                    return;
                }



                mydb.insertUser(username, password);
                Toast.makeText(MainActivity.this, "Data is inserted",
                        Toast.LENGTH_LONG).show();
                usernameField.setText("");
                passField.setText("");

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString().toLowerCase();
                String password = passField.getText().toString();
                Cursor results;

                try{
                    results = mydb.getUser(username);
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "Error"+e.toString(),
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (results.getCount()==0) {
                    Toast.makeText(MainActivity.this, "Username does not exist",
                            Toast.LENGTH_LONG).show();
                    return;
                }


                    results.moveToFirst();
                    int id = Integer.parseInt(results.getString(0));
                    Log.d("Login", results.getString(0));

                    if (!results.getString(2).equals(password)) {
                        Toast.makeText(MainActivity.this, "Incorrect password",
                                Toast.LENGTH_LONG).show();
                        passField.setText("");

                    } else {

                        sendMessage(v, id);
                    }



            }
        });

    }

    public void sendMessage(View view, int id) {
        Intent intent = new Intent(this, DisplayNavigationActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }
}
