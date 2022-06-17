package com.example.musicmedia.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicmedia.R;
import com.example.musicmedia.model.SongOnline;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SongFavoriteAdapter extends RecyclerView.Adapter<SongOnlineAdapter.SongHolder> {
    private List<SongOnline> songs=new ArrayList<>();
    private SongOnlineAdapter.IClickItemListener mIClickItemListener;

    public interface IClickItemListener {
        void onClickItemSong(SongOnline song);
    }
    public SongFavoriteAdapter(SongOnlineAdapter.IClickItemListener listener) {
        this.mIClickItemListener = listener;
    }

    @NonNull
    @Override
    public SongOnlineAdapter.SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_song, parent, false);
        return new SongOnlineAdapter.SongHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SongOnlineAdapter.SongHolder holder, int position) {
        SongOnline currentsong = songs.get(position);

        holder.txtSongName.setText(currentsong.getName());
        holder.txtSingerName.setText(currentsong.getArtist());



        //load image
        Picasso.with(holder.circleImageView.getContext())
                .load(currentsong.getImage())
                .placeholder(R.drawable.ic_about)
                .into(holder.circleImageView);

        //click favorite and change data
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference favorite = db.
                collection("MySong").
                document(currentsong.getID());
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentsong.getFavorite()) {
                    favorite.update("Favorite",false);
                    currentsong.setFavorite(false);
                } else {
                    favorite.update("Favorite",true);
                    currentsong.setFavorite(true);
                }
            }
        });


        holder.relativeLayout.setOnClickListener(view -> {
            mIClickItemListener.onClickItemSong(currentsong);
        });

        // check favorite
        if (currentsong.getFavorite()) {
            holder.imageButton.setImageResource(R.drawable.ic_favorite2);
        } else {
            holder.imageButton.setImageResource(R.drawable.ic_favorite1);
        }
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public void setSongs(List<SongOnline> mListSong) {
        this.songs = mListSong;
        notifyDataSetChanged();
    }

    static class SongHolder extends RecyclerView.ViewHolder {
        private RelativeLayout relativeLayout;
        private TextView txtSongName;
        private TextView txtSingerName;
        private ImageView imageButton;
        private CircleImageView circleImageView;

        public SongHolder(View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.relativeLayoutMusic);
            txtSongName = itemView.findViewById(R.id.txt_song_name);
            txtSingerName = itemView.findViewById(R.id.txt_singer_name);
            imageButton = itemView.findViewById(R.id.img_button);
            circleImageView = itemView.findViewById(R.id.img_song);
        }
    }
}
