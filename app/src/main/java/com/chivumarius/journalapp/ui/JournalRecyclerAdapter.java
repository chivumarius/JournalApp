package com.chivumarius.journalapp.ui;




import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chivumarius.journalapp.R;
import com.chivumarius.journalapp.model.Journal;

import java.util.List;




public class JournalRecyclerAdapter extends RecyclerView.Adapter<JournalRecyclerAdapter.ViewHolder> {

    // ▼ "DECLARATION" OF "ATTRIBUTES" ▼
    private Context context;
    private List<Journal> journalList;



    // ▼ "CONSTRUCTOR"
    public JournalRecyclerAdapter(Context context, List<Journal> journalList) {
        this.context = context;
        this.journalList = journalList;
    }




    // ▼ "ON CREATE VIEW HOLDER" METHOD ▼
    @NonNull
    @Override
    public JournalRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.journal_row, viewGroup, false);
        return new ViewHolder(view, context);
    }




    // ▼ "BIND VIEW HOLDER" SUB-CLASS ▼
    @Override
    public void onBindViewHolder(@NonNull JournalRecyclerAdapter.ViewHolder holder, int position) {

        Journal journal = journalList.get(position);
        String imageUrl;

        holder.title.setText(journal.getTitle());
        holder.thoughts.setText(journal.getThoughts());
        holder.name.setText(journal.getUserName());
        imageUrl = journal.getImageUrl();

        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(
                journal.getTimeAdded()
                        .getSeconds()*1000);
        holder.dateAdded.setText(timeAgo);

        /**
         *  Using Glide Library to Display the images
         * */

        Glide.with(context)
                .load(imageUrl)
                //.placeholder()
                .fitCenter()
                .into(holder.image);
    }



    // ▼ "GET ITEM COUNT" METHOD ▼
    @Override
    public int getItemCount() {
        return journalList.size();
    }




    // ▼ "VIEW HOLDER" SUB-CLASS ▼
    public class ViewHolder extends RecyclerView.ViewHolder{

        // ▼ "DECLARATION" OF "ATTRIBUTES" ▼
        public TextView title, thoughts, dateAdded, name;
        public ImageView image;
        public ImageView shareButton;
        String userId;
        String username;



        // ▼ "CONSTRUCTOR"
        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);

            context = ctx;

            // ▼ "INITIALIZATION" OF "WIDGETS IDS"
            //      → FROM "JOURNAL_ROW. XML" FILE ▼
            title = itemView.findViewById(R.id.journal_title_list);
            thoughts = itemView.findViewById(R.id.journal_thought_list);
            dateAdded = itemView.findViewById(R.id.journal_timestamp_list);

            image = itemView.findViewById(R.id.journal_image_list);
            name = itemView.findViewById(R.id.journal_row_username);

            shareButton = itemView.findViewById(R.id.journal_row_share_button);


            // ▼ "SET ON CLICK LISTENER" → FOR "SHARE BUTTON" ▼
            shareButton.setOnClickListener(new View.OnClickListener() {

                // ▼ "ON CLICK" METHOD ▼
                @Override
                public void onClick(View view) {
                    // SHARING THE POST !!
                }
            });
        }
    }

}
