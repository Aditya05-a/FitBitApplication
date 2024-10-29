package com.aditya.classquiz;

import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Resources extends AppCompatActivity {
    TextView youtubeLink1,youtubeLink2,youtubeLink3;
    ImageButton contactButton1, contactButton2, contactButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resources);
        getIntent();
        youtubeLink1=findViewById(R.id.youtubeLink1);
        youtubeLink2=findViewById(R.id.youtubeLink2);
        youtubeLink3=findViewById(R.id.youtubeLink3);
        contactButton1=findViewById(R.id.contactButton1);
        contactButton2=findViewById(R.id.contactButton2);
        contactButton3=findViewById(R.id.contactButton3);

        contactButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone_number="+918431641864";
                Intent intent=new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+phone_number));
                if (ContextCompat.checkSelfPermission(Resources.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Resources.this, new String[]{Manifest.permission.CALL_PHONE},1);
                    startActivity(intent);
                }
            }
        });
        contactButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone_number="+919483129823";
                Intent intent=new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+phone_number));
                if (ContextCompat.checkSelfPermission(Resources.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent);
                }
            }
        });
        contactButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone_number="+919019965949";
                Intent intent=new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+phone_number));
                if (ContextCompat.checkSelfPermission(Resources.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent);
                }
            }
        });
        youtubeLink1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=hI2TZW9H5iA"));
                startActivity(intent);
            }
        });
        youtubeLink2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=qyMA-vN3QwI"));
                startActivity(intent);
            }
        });
        youtubeLink3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=StA5DOel7Pw"));
                startActivity(intent);
            }
        });
    }


}