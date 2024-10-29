package com.aditya.classquiz;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    EditText Username, Password, Otp;
    TextView SetPassword;
    Button SignIn;
    String username, password;
    SharedPreferences sh;
    FirebaseFirestore login_db;
    long user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        getIntent();
        sh = getSharedPreferences("login_details", MODE_PRIVATE);
        login_db=FirebaseFirestore.getInstance();
        Username = findViewById(R.id.Username);
        Password = findViewById(R.id.Password);
        SignIn = findViewById(R.id.SignIn);
        SignIn.setOnClickListener(v -> {
            Map<String, ?> login_details = sh.getAll();
            username = Username.getText().toString();
            password = Password.getText().toString();
            if (login_details.containsValue(username) && login_details.containsValue(password)) {
                for (Map.Entry<String, ?> map_pair : login_details.entrySet()) {
                    if (map_pair.getKey().startsWith("user_name") && map_pair.getValue().equals(username)) {
                        user_id = Long.parseLong(map_pair.getKey().substring(10));
                        break;
                    }
                }
                Intent home_intent = new Intent(MainActivity.this, Home.class);
                home_intent.putExtra("id", sh.getLong("user_id " + user_id, 0l));
                home_intent.putExtra("username", sh.getString("user_name " + user_id, ""));
                startActivity(home_intent);
            }
            else {
                login_db.collection("login_details")
                        .whereEqualTo("user_name",username)
                        .whereEqualTo("password",password)
                        .get()
                        .addOnSuccessListener(querySnap->{
                            if(!querySnap.isEmpty()){
                                long user_id=querySnap.getDocuments().get(0).getLong("user_id");
                                SharedPreferences.Editor edit=sh.edit();
                                edit.putLong("user_id "+user_id,user_id);
                                edit.putString("user_name "+user_id,username);
                                edit.putString("password "+user_id,password);
                                edit.apply();
                                Intent home_intent=new Intent(MainActivity.this,Home.class);
                                home_intent.putExtra("id",sh.getLong("user_id "+user_id,0l));
                                home_intent.putExtra("username",sh.getString("user_name "+user_id,""));
                                startActivity(home_intent);
                            }
                            else{
                                AlertDialog.Builder build = new AlertDialog.Builder(MainActivity.this);
                                build.setCancelable(false);
                                build.setTitle("Account does not exist");
                                build.setMessage("Would you like to create an account as the account does not exist");
                                build.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent registerActivity = new Intent(MainActivity.this, Registration.class);
                                        startActivity(registerActivity);
                                    }
                                });
                                build.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Username.setText("");
                                        Password.setText("");
                                        dialogInterface.cancel();
                                    }
                                });
                                build.create().show();
                            }
                        })
                        .addOnFailureListener(error->{
                            Log.w("SignIn","Document not retrieved");
                        });
            }
        });
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unregisterReceiver(fill_otp);
//    }

//    public class OTPFillReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String otpCode = intent.getStringExtra("otp_code");
//            Otp.setText(otpCode);

//        }
//    }
}