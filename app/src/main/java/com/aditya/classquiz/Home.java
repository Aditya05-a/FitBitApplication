package com.aditya.classquiz;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Home extends AppCompatActivity {
    String user_name;
    long user_id;
    TextView Intro;
    Button btnProfile,btnResources,btnBmi, btnChangePassword;
    ImageButton Logout;
    FitBitDatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        Intent home_intent=getIntent();
        if(home_intent!=null){
            user_id=home_intent.getLongExtra("id",0l);
        }
        dbHelper=new FitBitDatabaseHelper(Home.this);
        user_name=home_intent.getStringExtra("user_name");
        user_id=home_intent.getLongExtra("id",0l);
        getUserName();
        Intro=findViewById(R.id.intro);
        btnProfile=findViewById(R.id.btnProfile);
        btnBmi=findViewById(R.id.btnBmi);
        btnResources=findViewById(R.id.btnResources);
        btnChangePassword=findViewById(R.id.btnChangePassword);
        Logout=findViewById(R.id.Logout);
        Intro.setText("Hello "+user_name+" !!");
        btnProfile.setOnClickListener(v->{
            Intent profile_intent=new Intent(Home.this,Profile.class);
            profile_intent.putExtra("id",this.user_id);
            startActivity(profile_intent);
        });
        btnBmi.setOnClickListener(v->{
            Intent bmi_intent=new Intent(Home.this,BmiCalculator.class);
            bmi_intent.putExtra("id",this.user_id);
            startActivity(bmi_intent);
        });
        btnResources.setOnClickListener(v->{
            Intent resource_intent=new Intent(Home.this,Resources.class);
            startActivity(resource_intent);
        });
        btnChangePassword.setOnClickListener(v->{
            Intent change_password_intent=new Intent(Home.this,ChangePassword.class);
            change_password_intent.putExtra("id",user_id);
            startActivity(change_password_intent);
        });
        Logout.setOnClickListener(v->{
            Intent login_intent=new Intent(Home.this,MainActivity.class);
            startActivity(login_intent);
        });
    }

    public void getUserName(){
        Cursor cursor=dbHelper.getUserById(this.user_id);
        if(cursor!=null&&cursor.moveToFirst())
            this.user_name=cursor.getString(cursor.getColumnIndexOrThrow("name"));
        else
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
    }
}