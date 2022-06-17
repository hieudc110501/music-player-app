package com.example.musicmedia.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicmedia.R;
import com.example.musicmedia.activity.MainActivity;
import com.example.musicmedia.fragment.FragmentOnline;
import com.example.musicmedia.model.Song;
import com.example.musicmedia.model.SongOnline;
import com.example.musicmedia.viewmodel.SongOnlineViewModel;
import com.example.musicmedia.viewmodel.SongViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class SongOnlineAdapter extends RecyclerView.Adapter<SongOnlineAdapter.SongHolder> implements Filterable {
    private List<SongOnline> songs;
    private List<SongOnline> songsOld;
    private IClickItemListener mIClickItemListener;
    private SongViewModel songViewModel;
    boolean check;

    int pos = -1;

    public interface IClickItemListener {
        void onClickItemSong(SongOnline song);
    }

    public SongOnlineAdapter(IClickItemListener listener) {
        this.mIClickItemListener = listener;
    }


    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_song, parent, false);
        return new SongHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SongHolder holder, int position) {
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
                    favorite.update("Favorite", false);
                    currentsong.setFavorite(false);
                } else {
                    favorite.update("Favorite", true);
                    currentsong.setFavorite(true);
                }
            }
        });


        holder.relativeLayout.setOnClickListener(view -> {
            this.pos = holder.getAbsoluteAdapterPosition();
            mIClickItemListener.onClickItemSong(currentsong);

        });
        holder.buttonViewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(holder.buttonViewOptions.getContext(), holder.buttonViewOptions);
                popup.inflate(R.menu.menu_download);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.download:
                                String url = currentsong.getPath();
                                String fileExtension = MimeTypeMap.getFileExtensionFromUrl(url);
                                DownloadManager manager = (DownloadManager) view.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                                request.setDescription(currentsong.getPath() + "." + fileExtension);
                                request.setTitle(currentsong.getName());

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                    request.allowScanningByMediaScanner();
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                }
                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, url);
                                manager.enqueue(request);
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
        // check favorite
        if (currentsong.getFavorite()) {
            holder.imageButton.setImageResource(R.drawable.ic_favorite2);
        } else {
            holder.imageButton.setImageResource(R.drawable.ic_favorite1);
        }
    }

    public SongOnline getPreviousItem(int id) {
        int previousSongID = getItemCount() - 1;
        if (id > 0) previousSongID = id - 1;
        pos = previousSongID;
        return songs.get(previousSongID);
    }

    public SongOnline getNextItem(int id) {
        int nextSongID = 0;
        if (id < getItemCount() - 1) nextSongID = id + 1;
        pos = nextSongID;
        return songs.get(nextSongID);
    }

    public SongOnline getPreviousFavItem(int id) {
        List<SongOnline> listFavorite = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i++) {
            if (songs.get(i).getFavorite()) {
                listFavorite.add(songs.get(i));
            }
        }
        int previousSongID = listFavorite.size() - 1;
        if (id > 0) previousSongID = id - 1;
        pos = previousSongID;
        return listFavorite.get(previousSongID);
    }

    public SongOnline getNextFavItem(int id) {
        List<SongOnline> listFavorite = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i++) {
            if (songs.get(i).getFavorite()) {
                listFavorite.add(songs.get(i));
            }
        }
        int nextSongID = 0;
        if (id < listFavorite.size() - 1) nextSongID = id + 1;
        pos = nextSongID;
        return listFavorite.get(nextSongID);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public void setSongs(List<SongOnline> mListSong) {
        this.songs = mListSong;
        songsOld = new ArrayList<>(mListSong);
    }

    public void clear() {
        songs.clear();
    }

    static class SongHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout;
        TextView txtSongName;
        TextView txtSingerName;
        ImageView imageButton;
        CircleImageView circleImageView;
        private TextView buttonViewOptions;

        public SongHolder(View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.relativeLayoutMusic);
            txtSongName = itemView.findViewById(R.id.txt_song_name);
            txtSingerName = itemView.findViewById(R.id.txt_singer_name);
            imageButton = itemView.findViewById(R.id.img_button);
            circleImageView = itemView.findViewById(R.id.img_song);
            buttonViewOptions = itemView.findViewById(R.id.textViewOptions);
        }
    }

    // search
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                List<SongOnline> list = new ArrayList<>();
                if (strSearch.isEmpty()) {
                    list.addAll(songsOld);
                } else {
                    for (SongOnline song : songsOld) {
                        if (song.getName().toLowerCase().contains(strSearch.toLowerCase())) {
                            list.add(song);
                        }
                    }
                    //songs = list;
                }
                FilterResults results = new FilterResults();
                results.values = list;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                songs.clear();
                songs.addAll((Collection<? extends SongOnline>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    public int getPos() {
        return pos;
    }
}
