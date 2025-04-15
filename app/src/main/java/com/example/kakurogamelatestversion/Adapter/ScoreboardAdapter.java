package com.example.kakurogamelatestversion.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kakurogamelatestversion.Models.Player;
import com.example.kakurogamelatestversion.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ScoreboardAdapter extends FirestoreRecyclerAdapter<Player, ScoreboardAdapter.PlayerViewHolder> {

    public ScoreboardAdapter(@NonNull FirestoreRecyclerOptions<Player> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PlayerViewHolder holder, int position, @NonNull Player model) {
        holder.txtName.setText(model.getFirstName() + " " + model.getLastName());
        holder.txtScore.setText("Score: " + model.getScore());

        if (model.getImgUrl() != null && !model.getImgUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext()).load(model.getImgUrl()).circleCrop().into(holder.playerImage);
        } else {
            holder.playerImage.setImageResource(R.drawable.ic_profile_placeholder);
        }
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scoreboard, parent, false);
        return new PlayerViewHolder(view);
    }

    static class PlayerViewHolder extends RecyclerView.ViewHolder {
        ImageView playerImage;
        TextView txtName, txtScore;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            playerImage = itemView.findViewById(R.id.playerImage);
            txtName = itemView.findViewById(R.id.txtName);
            txtScore = itemView.findViewById(R.id.txtScore);
        }
    }
}
