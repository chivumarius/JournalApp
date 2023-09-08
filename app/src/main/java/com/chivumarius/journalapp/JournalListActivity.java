package com.chivumarius.journalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chivumarius.journalapp.model.Journal;
import com.chivumarius.journalapp.ui.JournalRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

import util.JournalUser;

public class JournalListActivity extends AppCompatActivity {

    // ▼ "DECLARATION" OF "ATTRIBUTES" ▼

    // ▼ "FIREBASE AUTHENTICATION" ▼
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    // ▼ "GETTING FIRESTORE INSTANCE"
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    // ▼ "STORAGE REFERENCE" ▼
    private StorageReference storageReference;
    private List<Journal> journalList;


    // ▼ "DECLARATION" OF "WIDGETS IDS" ▼
    private RecyclerView recyclerView;
    private JournalRecyclerAdapter journalRecyclerAdapter;

    private CollectionReference collectionReference = db.collection("Journal");
    private TextView noPostsEntry;





    //▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀
    // ▼ "ON CREATE()" METHOD ▼
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);



        // (1) ▼ "INITIALIZING" OF "ATTRIBUTES" &  "REFERENCES" ▼

        // ▼ "INITIALIZATION" OF "FIREBASE AUTHENTICATION" ▼
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();



        // ▼ "INITIALIZATION" OF "WIDGETS IDS" ▼
        noPostsEntry = findViewById(R.id.list_no_posts);
        recyclerView = findViewById(R.id.recyclerView);

        // ▼ SETTING "HAS FIXED SIZE()" ▼
        recyclerView.setHasFixedSize(true);

        // ▼ SETTING "LAYOUT MANAGER" ▼
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // ▼ POST "ARRAYLIST" TO "JOURNAL LIST" ▼
        journalList = new ArrayList<>();
    }





    //▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀
    // (2) ▼ ADDING THE MENU ▼
    // ▼ "ON CREATE OPTION MENU()" METHOD ▼
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // ▼ GETTING THE "MENU" ▼
        getMenuInflater().inflate(R.menu.menu, menu);

        // ▼ RETURNING ▼
        return super.onCreateOptionsMenu(menu);
    }




    //▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀
    // ▼ "ON OPTIONS ITEM SELECTED()" METHOD ▼
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // ▼ "SWITCH STATEMENT" ▼
        switch (item.getItemId()){

            // "CASE 1": "ACTION ADD"
            case R.id.action_add:

                // ▼ GOING TO "ADD JOURNAL ACTIVITY" ▼
                if (user != null && firebaseAuth != null){
                    // ▼ "REDIRECTING" TO "ADD JOURNAL ACTIVITY" ▼
                    startActivity(new Intent(
                            JournalListActivity.this,
                            AddJournalActivity.class
                    ));
                }
                break;


            // "CASE 2": "SIGN OUT"
            case R.id.action_signout:

                // ▼ IF THE "USER" IS "AUTHENTICATED" ▼
                if (user != null && firebaseAuth != null){

                    // ▼ "SIGNING OUT" THE "USER" ▼
                    firebaseAuth.signOut();

                    // ▼ "REDIRECTING" TO "MAIN ACTIVITY" ▼
                    startActivity(new Intent(
                            JournalListActivity.this,
                            MainActivity.class
                    ));
                }
                break;

        }

        //▼ RETURNING ▼
        return super.onOptionsItemSelected(item);
    }





    //▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀
    // (3) ▼ GETTING ALL POSTS ▼
    // ▼ "ON START()" METHOD ▼
    @Override
    protected void onStart() {
        super.onStart();

        // ▼ "GETTING ALL DOCUMENTS" FROM THE "COLLECTION"" ▼
        collectionReference.whereEqualTo(
                    "userId",
                    JournalUser.getInstance().getUserId()
                )
                .get()


                // ▼ "ON SUCCESS LISTENER" METHOD ▼
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                    // ▼ "ON SUCCESS" METHOD ▼
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        // ▼ IF THE "QUERY DOCUMENT SNAPSHOTS" IS NOT "EMPTY" ▼
                        if (!queryDocumentSnapshots.isEmpty()){
                            // ▼ FOR EACH "QUERY DOCUMENT SNAPSHOTS" ▼
                            for (QueryDocumentSnapshot journals : queryDocumentSnapshots){
                                // ▼ CREATING A "JOURNAL" ▼
                                Journal journal = journals.toObject(Journal.class);

                                // ▼ ADDING THE "JOURNAL" TO THE "JOURNAL LIST" ▼
                                journalList.add(journal);
                            }


                            // ▼ "JOURNAL RECYCLER ADAPTER" ▼
                            journalRecyclerAdapter = new JournalRecyclerAdapter(
                                    JournalListActivity.this, journalList);

                            // ▼ SETTING THE "ADAPTER" TO THE "RECYCLER VIEW" ▼
                            recyclerView.setAdapter(journalRecyclerAdapter);

                            // ▼ REFRESHING THE "RECYCLER VIEW" ▼
                            journalRecyclerAdapter.notifyDataSetChanged();

                        } else {
                            // ▼ NO POSTS ▼
                            noPostsEntry.setVisibility(View.VISIBLE);
                        }


                    }
                })


                // ▼ "ON FAILURE LISTENER" METHOD ▼
                .addOnFailureListener(new OnFailureListener() {

                    // ▼ "ON FAILURE" METHOD ▼
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // ▼ SHOWING THE "ERROR TOAST MESSAGE" ▼
                        Toast.makeText(JournalListActivity.this, "Opps! Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}