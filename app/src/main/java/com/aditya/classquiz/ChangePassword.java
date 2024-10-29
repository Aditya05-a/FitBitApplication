package com.aditya.classquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {

    long user_id;
    SharedPreferences sh;
    EditText Password;
    Button SetPassword;
    FirebaseFirestore login_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);
        Intent change_intent=getIntent();
        login_db=FirebaseFirestore.getInstance();
        user_id=change_intent.getLongExtra("id",0l);
        sh=getSharedPreferences("login_details",MODE_PRIVATE);
        Password=findViewById(R.id.Password);
        SetPassword=findViewById(R.id.SetPassword);
        SetPassword.setOnClickListener(v->{
            String password=Password.getText().toString();
            if(!password.equals(sh.getString("password "+user_id,""))){
                login_db.collection("login_details")
                        .whereEqualTo("user_id",user_id)
                        .get()
                        .addOnSuccessListener(querySnap->{
                            DocumentSnapshot docSnap=querySnap.getDocuments().get(0);
                            DocumentReference docRef=docSnap.getReference();
                            Map<String,Object>updates = new HashMap<>();
                            updates.put("password",password);
                            docRef.update(updates)
                                    .addOnSuccessListener(val->{
                                        Log.d("ChangePassword","Update successfully applied");
                                    })
                                    .addOnFailureListener(error->{
                                        Log.d("ChangePassword","Error occured during updation");
                                    });
                            SharedPreferences.Editor edit=sh.edit();
                            edit.putString("password "+user_id,password);
                            edit.apply();
                            Log.d("ChangePassword","Password updated successfully");
                        })
                        .addOnFailureListener(error->{
                            Log.w("ChangePassword","Error in retrieving documents");
                        });
            }
            else {
                Toast.makeText(ChangePassword.this, "Password not changed", Toast.LENGTH_LONG).show();
            }
//            Map<String,?> login_details=sh.getAll();
//            String password=Password.getText().toString();
//            int num=0;
//            for(Map.Entry<String,?>entry:login_details.entrySet()){
//                if(entry.getKey().startsWith("password")&&entry.getValue().equals(password)) {
//                    Toast.makeText(ChangePassword.this, "Password exists", Toast.LENGTH_LONG).show();
//                    break;
//                }
//                num++;
//            }
//            if(num==login_details.size()){
//                SharedPreferences.Editor edit=sh.edit();
//                edit.putString("password "+user_id,password);
//                edit.apply();
//            }
//            Toast.makeText(ChangePassword.this,"Password changed",Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent profile_intent=new Intent(ChangePassword.this,Profile.class);
        profile_intent.putExtra("id",user_id);
        startActivity(profile_intent);
    }
}