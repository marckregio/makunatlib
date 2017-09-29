package com.marckregio.firebasemakunat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marckregio.firebasemakunat.model.SampleModel;

/**
 * Created by eCopy on 9/29/2017.
 */

public class FirebaseDatabaseActivity extends AppCompatActivity {

    private FirebaseDatabase fbDatabase;
    private static boolean enabledPersistence = false;

    private String sample = "sample";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!enabledPersistence) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true); // enable offline store
            enabledPersistence = true;
        }
        fbDatabase = FirebaseDatabase.getInstance();

        setValue("Title","Descriptionssss");
    }

    public void setValue(String title, String description){
        DatabaseReference dbRef = fbDatabase.getReference(sample);
        String userId = dbRef.push().getKey(); //unique id
        SampleModel model = new SampleModel(title, description);

        dbRef.child(userId).setValue(model);
        dbRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SampleModel model = dataSnapshot.getValue(SampleModel.class);
                Log.v("FIREBASE", model.getTitle() + "---" + model.getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
