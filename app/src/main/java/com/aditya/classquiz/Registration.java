package com.aditya.classquiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.aditya.classquiz.models.LoginDetails;
import com.google.firebase.firestore.FirebaseFirestore;

public class Registration extends AppCompatActivity {

    EditText Name, Age, Mobile;
    RadioGroup GenderGroup;
    SharedPreferences sh;
    String name, mobile, gender;
    RadioButton Gender;
    Button Submit;
    int age, gid;
    long user_id;
    FitBitDatabaseHelper dbHelper;
    FirebaseFirestore login_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntent();
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
        sh=getSharedPreferences("login_details",MODE_PRIVATE);
        login_db=FirebaseFirestore.getInstance();
        dbHelper=new FitBitDatabaseHelper(Registration.this);
        Name=findViewById(R.id.Name);
        Age=findViewById(R.id.Age);
        Mobile=findViewById(R.id.Mobile);
        GenderGroup=findViewById(R.id.GenderGroup);
        Submit=findViewById(R.id.Submit);
        Submit.setOnClickListener(v->{
            name=String.valueOf(Name.getText());
            age =Integer.parseInt(Age.getText().toString());
            mobile=String.valueOf(Mobile.getText());
            gid=GenderGroup.getCheckedRadioButtonId();
            Gender=findViewById(gid);
            gender=Gender.getText().toString();
            if(age<=100&&mobile.length()==10){
                AlertDialog.Builder build=new AlertDialog.Builder(Registration.this);
                build.setCancelable(false);
                build.setTitle("Confirm submission");
                build.setMessage("Are you sure you want to submit?");
                build.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(gender.equals("Male"))
                            gender="M";
                        else
                            gender="F";
                        user_id= dbHelper.addUser(name,age,mobile,gender);
                        LoginDetails user_login=new LoginDetails(user_id,name,mobile);
                        login_db.collection("login_details")
                                .add(user_login)
                                .addOnSuccessListener(docRef->{
                                    Log.d("Registration","Document Snapshot added with ID: "+docRef.getId());
                                })
                                .addOnFailureListener(error->{
                                    Log.w("Registration","Error adding document snapshot");
                                });
                        SharedPreferences.Editor edit=sh.edit();
                        edit.putLong("user_id "+user_id,user_id);
                        edit.putString("user_name "+user_id,name);
                        edit.putString("password "+user_id,mobile);
                        edit.apply();
                        sendSms(mobile,name,mobile);
                        Intent login_intent=new Intent(Registration.this,MainActivity.class);
                        startActivity(login_intent);
                    }
                });
                build.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                build.create().show();
            }
            else{
                AlertDialog.Builder build=new AlertDialog.Builder(Registration.this);
                build.setCancelable(false);
                build.setTitle("Invalid credentials");
                build.setMessage("Check whether you have entered all details correctly");
                build.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        if(age<=100)
                            Age.setText("");
                        if(mobile.length()!=10)
                            Mobile.setText("");
                    }
                });
                AlertDialog alert= build.create();
                alert.show();
            }
        });
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }

    public void sendSms(String mobile,String username,String password){
        SmsManager details_sms=SmsManager.getDefault();
        String message="New Account Created\nUsername: "+
                username+"\nPassword: "+password;
        details_sms.sendTextMessage("+91"+mobile,"+15551294567",message,null,null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent login_intent=new Intent(Registration.this,MainActivity.class);
        startActivity(login_intent);
    }
}