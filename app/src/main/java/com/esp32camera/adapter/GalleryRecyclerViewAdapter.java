package com.esp32camera.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.esp32camera.MainPresenter;
import com.esp32camera.R;
import com.esp32camera.home.gallery.GalleryPresenter;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class GalleryRecyclerViewAdapter extends RecyclerView.Adapter<GalleryRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private MainPresenter mainPresenter;
    private OnItemClickListener onItemClickListener;

    private GalleryPresenter galleryPresenter;
    private List<String> galleryItems;

    /**
     * Initialize the data which the Adapter need.
     */
    public GalleryRecyclerViewAdapter(Context context, MainPresenter mainPresenter, GalleryPresenter galleryPresenter, List<String> galleryItems) {
        this.context = context;
        this.mainPresenter = mainPresenter;
        this.galleryPresenter = galleryPresenter;
        this.galleryItems = galleryItems;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, String galleryItemName);

        void onItemLongClick(View view, String galleryItemName);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private final ImageView imageView;
        private boolean longClick;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            imageView = (ImageView) view.findViewById(R.id.imageView);
        }

        @Override
        public void onClick(View view) {
            if (!longClick) {
                onItemClickListener.onItemClick(view, getItem(getAdapterPosition()));
            } else {
                longClick = false;
            }
        }

        @Override
        public boolean onLongClick(View view) {
            onItemClickListener.onItemLongClick(view, getItem(getAdapterPosition()));
            longClick = true;
            return false;
        }
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.raw_gallery_item, viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Get element from your local data at this position and replace the
        // contents of the view with that element

        Glide.with(context).load(Uri.fromFile(new File(galleryItems.get(position)))).into(viewHolder.imageView);

        viewHolder.imageView.setOnClickListener(v -> {
            Toast.makeText(mainPresenter.getActivity(), galleryItems.get(position), Toast.LENGTH_SHORT).show();
            galleryPresenter.setSelectedItem(position);
            mainPresenter.navigateToGalleryViewPagerFragment();
        });
    }

    // Return the size of your data (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return galleryItems.size();
    }

    public String getItem(int position) {
        return galleryItems.get(position);
    }
}