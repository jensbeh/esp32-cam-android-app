package com.esp32camera.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        private final LinearLayout ll_gallery_item_background;
        private final ImageView iv_check_circle;
        private final ImageView iv_item_file_type;
        private boolean longClick;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            ll_gallery_item_background = (LinearLayout) view.findViewById(R.id.ll_gallery_item_background);
            iv_item_file_type = (ImageView) view.findViewById(R.id.iv_item_file_type);
            iv_check_circle = (ImageView) view.findViewById(R.id.iv_check_circle);
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

        // set image to imageView
        Glide.with(context)
                .load(Uri.fromFile(new File(galleryItems.get(position))))
                .into(viewHolder.imageView);

        // set photo or video icon on item
        if (galleryItems.get(position).endsWith(".jpg")) {
            viewHolder.iv_item_file_type.setImageResource(R.drawable.ic_photo_camera);
        } else if (galleryItems.get(position).endsWith(".mp4")) {
            viewHolder.iv_item_file_type.setImageResource(R.drawable.ic_videocam);
        }

        viewHolder.imageView.setOnClickListener(v -> {
            if (galleryPresenter.getSelectedItems().isEmpty()) {
                // no item is selected -> show galleryViewPager
                Toast.makeText(mainPresenter.getActivity(), galleryItems.get(position), Toast.LENGTH_SHORT).show();
                galleryPresenter.setViewPagerSelectedItem(position);
                mainPresenter.navigateToGalleryViewPagerFragment();
            } else {
                handleItemSelection(viewHolder, position);
            }

        });

        viewHolder.imageView.setOnLongClickListener(v -> {
            handleItemSelection(viewHolder, position);
            return true;
        });
    }

    private void handleItemSelection(ViewHolder viewHolder, int position) {
        if (!galleryPresenter.getSelectedItems().contains(galleryItems.get(position))) {
            // if item is NOT selected
            galleryPresenter.setSelectedItem(galleryItems.get(position));
            viewHolder.ll_gallery_item_background.setBackgroundColor(Color.parseColor("#008FFF"));
            viewHolder.iv_check_circle.setVisibility(View.VISIBLE);

            if (!galleryPresenter.getSelectedItems().isEmpty()) {
                // show delete button when item is selected
                galleryPresenter.showDeleteButton();
            }

        } else {
            // if item is selected
            galleryPresenter.removeSelectedItem(galleryItems.get(position));
            viewHolder.ll_gallery_item_background.setBackgroundColor(Color.TRANSPARENT);
            viewHolder.iv_check_circle.setVisibility(View.GONE);

            if (galleryPresenter.getSelectedItems().isEmpty()) {
                // hide delete button when NO item is selected
                galleryPresenter.hideDeleteButton();
            }
        }
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