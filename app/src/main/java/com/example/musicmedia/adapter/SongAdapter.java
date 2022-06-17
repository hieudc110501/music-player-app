package com.example.musicmedia.adapter;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicmedia.R;
import com.example.musicmedia.model.Song;

import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder>{
    public List<Song> songs=new ArrayList<>();

    private IClickItemListener mIClickItemListener;


    public interface IClickItemListener {
        void onClickItemSong(Song song);
    }

    public SongAdapter(IClickItemListener listener) {
        this.mIClickItemListener = listener;
    }

    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_song,parent,false);
        return new SongHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SongHolder holder, int position) {
        Song currentSong=songs.get(position);
        holder.txtSongName.setText(currentSong.getName());
        holder.txtSingerName.setText(currentSong.getSinger());
        /*if (currentSong.getCheck() == 1) {
            holder.btnListSongFavorite.setImageResource(R.drawable.ic_favorite2);
        } else {
            holder.btnListSongFavorite.setImageResource(R.drawable.ic_favorite1);
        }*/
      /*  holder.btnListSongFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIClickItemListener.onClickItemSong(currentSong);
            }
        });

        holder.buttonViewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup=new PopupMenu(holder.buttonViewOptions.getContext(),holder.buttonViewOptions);
                popup.inflate(R.menu.menu_download);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.download:
                                Toast.makeText(view.getContext(), "Download chỉ phù hợp cho online", Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public void setSongs(List<Song> songs){
        this.songs=songs;
        notifyDataSetChanged();
    }
    public Song getSongAt(int position){
        return songs.get(position);
    }
    class SongHolder extends RecyclerView.ViewHolder{
        private TextView txtSongName;
        private TextView txtSingerName;
        private RelativeLayout layoutItem;
        private ImageView btnListSongFavorite;
        private TextView buttonViewOptions;
        public SongHolder(View itemView){
            super(itemView);
            layoutItem=itemView.findViewById(R.id.relativeLayoutMusic);
            txtSongName=itemView.findViewById(R.id.txt_song_name);
            txtSingerName=itemView.findViewById(R.id.txt_singer_name);
            btnListSongFavorite=itemView.findViewById(R.id.img_button);
            buttonViewOptions=itemView.findViewById(R.id.textViewOptions);
        }
    }

}
