package com.example.kakurogamelatestversion.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kakurogamelatestversion.Models.Player;
import com.example.kakurogamelatestversion.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class PlayerAdapter extends FirestoreRecyclerAdapter<Player, PlayerAdapter.ViewHolder> {

    public interface OnPlayerClickListener {
        void onPlayerClick(Player player);
    }

    private final OnPlayerClickListener listener;

    public PlayerAdapter(@NonNull FirestoreRecyclerOptions<Player> options, OnPlayerClickListener listener) {
        super(options);
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Player player) {
        holder.bind(player);
        holder.btnView.setOnClickListener(v -> listener.onPlayerClick(player));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, emailTextView;
        ImageView profileImageView;
        Button btnView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.playerName);
            emailTextView = itemView.findViewById(R.id.playerEmail);
            profileImageView = itemView.findViewById(R.id.playerImage);
            btnView = itemView.findViewById(R.id.btnView);
        }

        public void bind(Player player) {
            nameTextView.setText(player.getFirstName() + " " + player.getLastName());
            emailTextView.setText(player.getEmail());

            if (player.getImgUrl() != null && !player.getImgUrl().isEmpty()) {
                Glide.with(itemView.getContext()).load(player.getImgUrl()).circleCrop().into(profileImageView);
            } else {
                profileImageView.setImageResource(R.drawable.ic_profile_placeholder);
            }
        }
    }
}
