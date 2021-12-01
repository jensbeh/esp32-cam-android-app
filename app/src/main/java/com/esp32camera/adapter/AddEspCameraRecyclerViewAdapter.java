package com.esp32camera.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.esp32camera.MainPresenter;
import com.esp32camera.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AddEspCameraRecyclerViewAdapter extends RecyclerView.Adapter<AddEspCameraRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private MainPresenter mainPresenter;
    private List<String> espCamerasIp;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, String ipAddress);

        void onItemLongClick(View view, String ipAddress);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private final TextView tv_rv_ipAddress;
        private final Button button_rv_connect;
        private boolean longClick;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            tv_rv_ipAddress = (TextView) view.findViewById(R.id.tv_rv_ipAddress);
            button_rv_connect = (Button) view.findViewById(R.id.button_rv_connect);
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

    /**
     * Initialize the data which the Adapter need.
     */
    public AddEspCameraRecyclerViewAdapter(Context context, MainPresenter mainPresenter, List<String> espCamerasIp) {
        this.context = context;
        this.mainPresenter = mainPresenter;
        this.espCamerasIp = espCamerasIp;
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.raw_add_esp_camera_item, viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Get element from your local data at this position and replace the
        // contents of the view with that element
        int pos = position;
        viewHolder.tv_rv_ipAddress.setText(espCamerasIp.get(position));
        viewHolder.button_rv_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.onNewEspCameraConnected(espCamerasIp.get(pos));
            }
        });
    }

    // Return the size of your data (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return espCamerasIp.size();
    }

    public String getItem(int position) {
        return espCamerasIp.get(position);
    }
}