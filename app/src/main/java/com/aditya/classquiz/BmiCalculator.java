package com.aditya.classquiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BmiCalculator extends AppCompatActivity {
    long user_id;
    EditText Height,Weight;
    Button Calculate;
    TextView Message;
    FitBitDatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_calculator);
        Intent home_intent=getIntent();
        dbHelper=new FitBitDatabaseHelper(BmiCalculator.this);
        user_id=home_intent.getLongExtra("id",0l);
        Height=findViewById(R.id.Height);
        Weight=findViewById(R.id.Weight);
        Calculate=findViewById(R.id.Calculate);
        Message=findViewById(R.id.underweight);
        Message.setVisibility(View.GONE);
        Message=findViewById(R.id.overweight);
        Message.setVisibility(View.GONE);
        Message=findViewById(R.id.normal);
        Message.setVisibility(View.GONE);
        Message=findViewById(R.id.obese);
        Message.setVisibility(View.GONE);
        Calculate.setOnClickListener(v->{
            String height_str=Height.getText().toString();
            String weight_str=Weight.getText().toString();
            if(height_str.isEmpty()||weight_str.isEmpty()){
                AlertDialog.Builder build=new AlertDialog.Builder(BmiCalculator.this);
                build.setCancelable(false);
                build.setTitle("Invalid details");
                if(height_str.isEmpty())
                    build.setMessage("You have not entered your height");
                else
                    build.setMessage("You have not entered your weight");
                build.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                build.create().show();
            }
            else{
                float height=Float.parseFloat(height_str);
                float weight=Float.parseFloat(weight_str);
                float bmi=weight/(height*height);
                if(bmi<18.5){
                    Message=findViewById(R.id.underweight);
                    Message.setVisibility(View.VISIBLE);
                    Message=findViewById(R.id.overweight);
                    Message.setVisibility(View.GONE);
                    Message=findViewById(R.id.normal);
                    Message.setVisibility(View.GONE);
                    Message=findViewById(R.id.obese);
                    Message.setVisibility(View.GONE);
                }
                else if(bmi>=18.5&&bmi<25){
                    Message=findViewById(R.id.normal);
                    Message.setVisibility(View.VISIBLE);
                    Message=findViewById(R.id.underweight);
                    Message.setVisibility(View.GONE);
                    Message=findViewById(R.id.overweight);
                    Message.setVisibility(View.GONE);
                    Message=findViewById(R.id.obese);
                    Message.setVisibility(View.GONE);
                }
                else if(bmi>=25&&bmi<30){
                    Message=findViewById(R.id.overweight);
                    Message.setVisibility(View.VISIBLE);
                    Message=findViewById(R.id.normal);
                    Message.setVisibility(View.GONE);
                    Message=findViewById(R.id.obese);
                    Message.setVisibility(View.GONE);
                    Message=findViewById(R.id.underweight);
                    Message.setVisibility(View.GONE);
                }
                else{
                    Message=findViewById(R.id.obese);
                    Message.setVisibility(View.VISIBLE);
                    Message=findViewById(R.id.underweight);
                    Message.setVisibility(View.GONE);
                    Message=findViewById(R.id.overweight);
                    Message.setVisibility(View.GONE);
                    Message=findViewById(R.id.normal);
                    Message.setVisibility(View.GONE);
                }
                dbHelper.updateUserBmi(user_id,bmi);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent home_intent=new Intent(BmiCalculator.this,Home.class);
        home_intent.putExtra("id",user_id);
        startActivity(home_intent);
    }
}