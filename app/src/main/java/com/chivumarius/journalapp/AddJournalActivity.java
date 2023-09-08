package com.chivumarius.journalapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chivumarius.journalapp.model.Journal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Date;

import util.JournalUser;

public class AddJournalActivity extends AppCompatActivity {

    // ▼ "DECLARATION" OF "CONSTANT" → FOR "GETTING IMAGES" FROM "GALLERY" ▼
    private static final int GALLERY_CODE = 1;


    // ▼ "DECLARATION" OF "WIDGETS IDS" ▼
    private Button saveButton;
    private ProgressBar progressBar;
    private ImageView addPhotoButton;
    private EditText titleEditText;
    private EditText thoughsEditText;
    private TextView currentUserTextView;
    private ImageView imageView;


    // ▼ USER IS & USERNAME ▼
    private String currentUserId;
    private String currentUserName;



    // ▼ "FIREBASE AUTHENTICATION" ▼
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;


    // ▼ "FIREBASE CONNECTION" → "FIRESTORE" ▼
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;

    private CollectionReference collectionReference = db.collection("Journal");
    private Uri imageUri;





    //▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀
    // ▼ "ON CREATE" METHOD ▼
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);


        // ▼ "INITIALIZATION" OF "FIREBASE STORAGE" ▼
        storageReference = FirebaseStorage.getInstance().getReference();

        // ▼ "INITIALIZATION" OF "FIREBASE AUTHENTICATION" ▼
        firebaseAuth = FirebaseAuth.getInstance();


        // ▼ "INITIALIZATION" OF "WIDGETS IDS" ▼
        progressBar = findViewById(R.id.post_progressBar);
        titleEditText = findViewById(R.id.post_title_et);
        thoughsEditText = findViewById(R.id.post_description_et);
        currentUserTextView = findViewById(R.id.post_username_textview);

        imageView = findViewById(R.id.post_imageView);
        saveButton = findViewById(R.id.post_save_journal_button);
        addPhotoButton = findViewById(R.id.postCameraButton);


        // ▼ SETTING THE "PROGRESS BAR" AS "INVISIBLE" ▼
        progressBar.setVisibility(View.INVISIBLE);


        // ▼ GETTING THE "CURRENT USER" IF "THERE IS ANYONE" ▼
        if (JournalUser.getInstance() != null){
            currentUserId = JournalUser.getInstance().getUserId();
            currentUserName = JournalUser.getInstance().getUsername();

            currentUserTextView.setText(currentUserName);
        }



        // ▼ "SETTING" THE "STATE LISTENER" OF "AUTHENTICATION" ▼
        authStateListener = new FirebaseAuth.AuthStateListener() {

            // ▼ "ON AUTH STATE CHANGED()" METHOD ▼
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // ▼ GETTING THE "CURRENT USER"
                user = firebaseAuth.getCurrentUser();


                // ▼ "CHECKING": IF "THERE IS" A "USER" ▼
                if (user != null){

                } else {

                }
            }
        };




        // ▼ "SETTING" THE "ON CLICK LISTENER" OF "SAVE BUTTON" ▼
        saveButton.setOnClickListener(new View.OnClickListener() {

            // ▼ "ON CLICK" METHOD ▼
            @Override
            public void onClick(View view) {
                // ▼ CALLING THE "METHOD"  ▼
                SaveJournal();
            }
        });




        // ▼ "SETTING" THE "ON CLICK LISTENER" OF "ADD PHOTO BUTTON" ▼
        addPhotoButton.setOnClickListener(new View.OnClickListener() {

            // ▼ "ON CLICK" METHOD ▼
            @Override
            public void onClick(View view) {

                // ▼ "GETTING IMAGES" FROM "GALLERY" ▼
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
            }
        });

    }





    //▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀
    // ▼ "SAVE JOURNAL" METHOD ▼
    private void SaveJournal() {

        // ▼ "INITIALIZATION" OF "WIDGETS IDS" ▼
        final String title = titleEditText.getText().toString().trim();
        final String thoughts = thoughsEditText.getText().toString().trim();


        // ▼ "SETTING" THE "PROGRESS BAR" (AS "VISIBLE")
        //      → WHEN  THE "USER" CLICK ON "SAVE" BUTTON ▼
        progressBar.setVisibility(View.VISIBLE);


        // ▼ "CHECKING": IF "FIELDS ARE FILLED" ▼
        if (!TextUtils.isEmpty(title) &&
                !TextUtils.isEmpty(thoughts)
                && imageUri != null) {

            // ▼ THE "SAVING PATH" OF THE "IMAGES" IN "STORAGE FIREBASE"
            //      → IS "...../journal_images/our_image.png" ▼
            final StorageReference filepath = storageReference
                    .child("journal_images")
                    .child(
                            "my_image_" + Timestamp.now().getSeconds());


            // ▼ "UPLOADING" THE "IMAGES" TO "STORAGE" ▼
            filepath.putFile(imageUri)

                    // ▼ "ON SUCCESS"
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        // ▼ "ON SUCCESS"
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // ▼ "GETTING" THE "IMAGES URL" FROM "URI" ▼
                                    String imageUrl = uri.toString();


                                    // ▼ CREATING "OBJECT" OF "JOURNAL"
                                    // AND THE "JOURNAL" MODEL CLASS ▼
                                    Journal journal = new Journal();

                                    // ▼ SETTING THE "JOURNAL ATTRIBUTES" ▼
                                    journal.setTitle(title);
                                    journal.setThoughts(thoughts);
                                    journal.setImageUrl(imageUrl);
                                    journal.setTimeAdded(new Timestamp(new Date()));
                                    journal.setUserName(currentUserName);
                                    journal.setUserId(currentUserId);



                                    // ▼ INVOKING "COLLECTION REFERENCE"
                                    //      → FOR "STORING" THE "JOURNAL" OBJECT ▼
                                    collectionReference.add(journal)

                                            // ▼ "ON SUCCESS" METHOD ▼
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                                                // ▼ "ON SUCCESS"
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {

                                                    // ▼ SETTING THE "PROGRESS BAR" AS "INVISIBLE" ▼
                                                    progressBar.setVisibility(View.INVISIBLE);

                                                    // ▼ "REDIRECTING" TO THE "JOURNAL LIST" ACTIVITY ▼
                                                    startActivity(new Intent(AddJournalActivity.this,
                                                            JournalListActivity.class)
                                                    );
                                                    finish();
                                                }
                                            })


                                            // ▼ "ON FAILURE" METHOD ▼
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // DISPLAYING A "TOAST MESSAGE" ▼
                                                    Toast.makeText(
                                                            getApplicationContext(),
                                                            "Failed: " + e.getMessage(),
                                                            Toast.LENGTH_SHORT
                                                    ).show();
                                                }
                                            });

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {

                        // ▼ "ON FAILURE" METHOD ▼
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // ▼ SETTING THE "PROGRESS BAR" AS "INVISIBLE" ▼
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });

        } else {

            // ▼ SETTING THE "PROGRESS BAR" AS "INVISIBLE" ▼
            progressBar.setVisibility(View.INVISIBLE);
        }
    }






    //▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀
    // ▼ "ON ACTIVITY RESULT" METHOD
    //      → WHEN "USER" CLICK THE "ADD PHOTO" BUTTON
    //      → TO "SELECT" THE "IMAGES" FROM "GALLERY" ▼
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // ▼ "CHECKING" IF "USER" SELECTED THE "IMAGES" FROM "IMAGE VIEW" ▼
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK){

            // ▼ IF "USER SELECTED" THE "IMAGES" FROM "GALLERY" ▼
            if (data != null){
                // ▼ "GETTING" THE "IMAGES" FROM "URI" ▼
                imageUri = data.getData();        // Getting the actual image path

                // ▼ "SETTING" THE "IMAGES" TO "IMAGE VIEW" ▼
                imageView.setImageURI(imageUri);  // Showing the image
            }
        }
    }





    //▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀
    // ▼ "ON START" METHOD ▼
    protected void onStart() {
        super.onStart();

        // ▼ GETTING THE "CURRENT USER" IF "THERE IS ANYONE" ▼
        user = firebaseAuth.getCurrentUser();

        // ▼ "SETTING" THE "STATE LISTENER" OF "AUTHENTICATION" ▼
        firebaseAuth.addAuthStateListener(authStateListener);
    }





    //▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀
    // ▼ "ON STOP" METHOD ▼
    protected void onStop() {
        super.onStop();

        // ▼CHECKING IF "THERE IS" A "USER" ▼
        if(firebaseAuth != null){
            // ▼ "REMOVING" THE "STATE LISTENER" OF "AUTHENTICATION" ▼
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}