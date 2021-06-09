package com.example.contentprovidercontacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    ListView listView;

    ArrayList<String> contactsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fab = findViewById(R.id.fab);
        listView = findViewById(R.id.listview);

        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},1);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {

                    ContentResolver contentResolver = getContentResolver();

                    String [] projection = {ContactsContract.Contacts.DISPLAY_NAME};

                    Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,projection,null,
                            null,
                            ContactsContract.Contacts.DISPLAY_NAME);

                    if(cursor!=null){

                         contactsList = new ArrayList<String>();
                        String columnIx = ContactsContract.Contacts.DISPLAY_NAME;

                        while (cursor.moveToNext()){

                        contactsList.add(cursor.getString(cursor.getColumnIndex(columnIx)));

                        }

                        cursor.close();

                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,contactsList);
                    listView.setAdapter(adapter);

                }else {

                    Snackbar.make(v,"Permission needed", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Access", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_CONTACTS)){
                                        ActivityCompat.requestPermissions(MainActivity.this,
                                                new String[]{Manifest.permission.READ_CONTACTS},2);
                                    }else{

                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package",MainActivity.this.getPackageName(),null);
                                        intent.setData(uri);
                                        MainActivity.this.startActivity(intent);
                                    }


                                }
                            }).show();

                }
            }
        });

    }
}