package com.aditya.classquiz;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Profile extends AppCompatActivity implements SensorEventListener{
    long user_id;
    String name,gender,mobile;
    int age, num_steps;
    float bmi;
    float[] lastValues = new float[3];
    float THRESHOLD = 11.0f;
    TextView Name,Mobile,Age,Gender,Bmi,Steps;
    Button Calculate;
    SensorManager sm;
    Sensor accelerometer;
    FitBitDatabaseHelper dbHelper;
    SharedPreferences sh;
    boolean isFirst=true;
    long lastUpdate=0l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        Intent profile_intent=getIntent();
        if(profile_intent!=null)
            user_id=profile_intent.getLongExtra("id",0l);
        dbHelper=new FitBitDatabaseHelper(Profile.this);
        sh=getSharedPreferences("login_details",MODE_PRIVATE);
        Name=findViewById(R.id.Name);
        Age=findViewById(R.id.Age);
        Mobile=findViewById(R.id.Mobile);
        Gender=findViewById(R.id.Gender);
        Bmi=findViewById(R.id.Bmi);
        Steps=findViewById(R.id.Steps);
        getUserDetails();
        if(this.mobile.equals(sh.getString("password "+user_id,"")))
            changePasswordNotification();
        sm=(SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Name.setText(" "+this.name);
        Age.setText(" "+this.age);
        Mobile.setText(" "+this.mobile);
        if(this.gender.equals("M"))
            Gender.setText(" Male");
        else
            Gender.setText(" Female");
        if(bmi!=0)
            Bmi.setText(" "+this.bmi+" ");
        Calculate=findViewById(R.id.Calculate);
        Calculate.setOnClickListener(v->{
            Intent bmi_intent=new Intent(Profile.this, BmiCalculator.class);
            bmi_intent.putExtra("id",this.user_id);
            startActivity(bmi_intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        sm.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sm.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        long curr_time=System.currentTimeMillis();
        if(curr_time-lastUpdate>10) {
            lastUpdate=curr_time;
            if (isFirst) {
                isFirst = false;
                this.num_steps = dbHelper.getUserStepCount(user_id);
                Steps.setText("  " + this.num_steps);
                return;
            }
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                // Basic step detection algorithm (this can be improved)
                float deltaX = Math.abs(x - lastValues[0]);
                float deltaY = Math.abs(y - lastValues[1]);
                float deltaZ = Math.abs(z - lastValues[2]);

                if ((deltaX > THRESHOLD) || (deltaY > THRESHOLD) || (deltaZ > THRESHOLD)) {
                    num_steps++;
                    Steps.setText("  " + num_steps);
                    dbHelper.updateUserStepCount(user_id, num_steps);
                }

                lastValues[0] = x;
                lastValues[1] = y;
                lastValues[2] = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void getUserDetails(){
        Cursor cursor=dbHelper.getUserById(this.user_id);
        if(cursor!=null&&cursor.moveToFirst()) {
            this.name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            this.age = cursor.getInt(cursor.getColumnIndexOrThrow("age"));
            this.mobile = cursor.getString(cursor.getColumnIndexOrThrow("mobile"));
            this.gender=cursor.getString(cursor.getColumnIndexOrThrow("gender"));
            this.bmi=cursor.getFloat(cursor.getColumnIndexOrThrow("bmi"));
            this.num_steps=cursor.getInt(cursor.getColumnIndexOrThrow("steps_taken"));
        }
        else
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
    }

    public void changePasswordNotification(){
        Intent change_pass_intent=new Intent(Profile.this,ChangePassword.class);
        change_pass_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        change_pass_intent.putExtra("id",user_id);
        PendingIntent pend_intent=PendingIntent.getActivity(Profile.this,0,change_pass_intent,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
        NotificationManager nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("pass_channel", "Change Password", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Channel for Password Notifications");
            nm.createNotificationChannel(channel);
        }
        NotificationCompat.Builder build=new NotificationCompat.Builder(Profile.this,"pass_channel")
                .setSmallIcon(R.drawable.notifications)
                .setContentText("Please change your password as it is the same as your mobile number")
                .setContentTitle("Warning")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pend_intent);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(Profile.this);
        if (ActivityCompat.checkSelfPermission(Profile.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return ;
        }
        notificationManagerCompat.notify(1, build.build());
    }
}