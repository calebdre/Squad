package com.squad.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.squad.R;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.ChatViewHolder> {

    private final String userKey;
    List<ChatMessage> chatMessages = new ArrayList<>();
    List<String> chatMessageIds = new ArrayList<>();

    public ChatRecyclerAdapter(String lobbyId, String userKey) {
        this.userKey = userKey;
        init(lobbyId);
    }

    private void init(String lobbyId) {
        FirebaseDatabase.getInstance()
                .getReference("lobbies/" + lobbyId + "/messages")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        ChatMessage chatMessage = ChatMessage.create(dataSnapshot);
                        chatMessageIds.add(dataSnapshot.getKey());
                        chatMessages.add(chatMessage);
                        notifyItemInserted(chatMessages.size() - 1);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        String userId = dataSnapshot.getKey();

                        // [START_EXCLUDE]
                        int userIndex = chatMessageIds.indexOf(userId);
                        if (userIndex > -1) {
                            // Remove data from the list
                            chatMessageIds.remove(userIndex);
                            chatMessages.remove(userIndex);

                            // Update the RecyclerView
                            notifyItemRemoved(userIndex);
                        }
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.view_squad_chat_item, parent, false);
        return new ChatViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        ChatMessage message = chatMessages.get(position);
        holder.message.setText(message.message());
        holder.name.setText(message.name());
        PrettyTime p = new PrettyTime();
        String ago = p.format(new Date(message.time()));
        holder.time.setText(ago);

        Context context = holder.container.getContext();
        ViewGroup.MarginLayoutParams f = (ViewGroup.MarginLayoutParams) holder.container.getLayoutParams();
        if(userKey.equals(message.userId())) {
            f.setMargins(400, 30,0 , 30);
            holder.container.requestLayout();
            holder.container.setBackground(context.getDrawable(R.drawable.outgoing_bubble));
        }else {
            f.setMargins(40, 30,0 , 30);
            holder.container.requestLayout();
            holder.container.setBackground(context.getDrawable(R.drawable.incoming_bubble));
        }
    }

    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }


    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder{

        LinearLayout container;
        TextView name;
        TextView message;
        TextView time;

        public ChatViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.chat_item_name);
            container = (LinearLayout) itemView.findViewById(R.id.container);
            message = (TextView) itemView.findViewById(R.id.chat_item_message);
            time = (TextView) itemView.findViewById(R.id.chat_item_time);
        }
    }
}
