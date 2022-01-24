package com.esp32camera.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.esp32camera.MainPresenter;
import com.esp32camera.R;
import com.esp32camera.home.notification.NotificationPresenter;
import com.esp32camera.model.Notification;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;

public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private MainPresenter mainPresenter;
    private OnItemClickListener onItemClickListener;

    private NotificationPresenter notificationPresenter;

    /**
     * Initialize the data which the Adapter need.
     */
    public NotificationRecyclerViewAdapter(Context context, MainPresenter mainPresenter, NotificationPresenter notificationPresenter) {
        this.context = context;
        this.mainPresenter = mainPresenter;
        this.notificationPresenter = notificationPresenter;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Notification notification);

        void onItemLongClick(View view, Notification notification);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private final CardView cardView_notification;
        private final TextView tv_camera_name_notification;
        private final TextView tv_timestamp_notification;
        private final ConstraintLayout cl_notification_item_background;
        private final ImageView iv_check_circle_notification;
        private final ImageView iv_motionPicture;
        private boolean longClick;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            cardView_notification = (CardView) view.findViewById(R.id.cardView_notification);
            tv_camera_name_notification = (TextView) view.findViewById(R.id.tv_camera_name_notification);
            tv_timestamp_notification = (TextView) view.findViewById(R.id.tv_timestamp_notification);
            cl_notification_item_background = (ConstraintLayout) view.findViewById(R.id.cl_notification_item_background);
            iv_check_circle_notification = (ImageView) view.findViewById(R.id.iv_check_circle_notification);
            iv_motionPicture = (ImageView) view.findViewById(R.id.iv_motionPicture);
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.raw_notification_card, viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Get element from your local data at this position and replace the
        // contents of the view with that element

        viewHolder.tv_camera_name_notification.setText(mainPresenter.getNotificationItems().get(position).getCameraName());

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy '-' HH:mm:ss 'Uhr'");
        String currentDateAndTime = sdf.format(mainPresenter.getNotificationItems().get(position).getTimeStamp());
        viewHolder.tv_timestamp_notification.setText(currentDateAndTime);

        if (!notificationPresenter.getSelectedItems().contains(mainPresenter.getNotificationItems().get(position))) {
            // if item is NOT selected -> set unselected
            viewHolder.cl_notification_item_background.setBackgroundColor(Color.TRANSPARENT);
            viewHolder.iv_check_circle_notification.setVisibility(View.GONE);
        } else {
            // if item is selected -> set selected
            viewHolder.cl_notification_item_background.setBackgroundColor(Color.parseColor("#008FFF"));
            viewHolder.iv_check_circle_notification.setVisibility(View.VISIBLE);
        }

        if (mainPresenter.getNotificationItems().get(position).getPictureBmp() != null) {
            if (viewHolder.iv_motionPicture.getVisibility() == View.GONE) {
                viewHolder.iv_motionPicture.setVisibility(View.VISIBLE);
            }
            // set image to imageView
            Glide.with(context)
                    .load(mainPresenter.getNotificationItems().get(position).getPictureBmp())
                    .into(viewHolder.iv_motionPicture);
        } else {
            viewHolder.iv_motionPicture.setVisibility(View.GONE);
        }

        viewHolder.cardView_notification.setOnClickListener(v -> {
            if (!notificationPresenter.getSelectedItems().isEmpty()) {
                handleItemSelection(viewHolder, position);
            }
        });

        viewHolder.cardView_notification.setOnLongClickListener(v -> {
            handleItemSelection(viewHolder, position);
            return true;
        });
    }

    private void handleItemSelection(ViewHolder viewHolder, int position) {
        if (!notificationPresenter.getSelectedItems().contains(mainPresenter.getNotificationItems().get(position))) {
            // if item is NOT selected
            notificationPresenter.setSelectedItem(mainPresenter.getNotificationItems().get(position));
            viewHolder.cl_notification_item_background.setBackgroundColor(Color.parseColor("#008FFF"));
            viewHolder.iv_check_circle_notification.setVisibility(View.VISIBLE);

            if (!notificationPresenter.getSelectedItems().isEmpty()) {
                // show delete button when item is selected
                notificationPresenter.showDeleteButton();
            }

        } else {
            // if item is selected
            notificationPresenter.removeSelectedItem(mainPresenter.getNotificationItems().get(position));
            viewHolder.cl_notification_item_background.setBackgroundColor(Color.TRANSPARENT);
            viewHolder.iv_check_circle_notification.setVisibility(View.GONE);

            if (notificationPresenter.getSelectedItems().isEmpty()) {
                // hide delete button when NO item is selected
                notificationPresenter.hideDeleteButton();
            }
        }
    }

    // Return the size of your data (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mainPresenter.getNotificationItems().size();
    }

    public Notification getItem(int position) {
        return mainPresenter.getNotificationItems().get(position);
    }
}