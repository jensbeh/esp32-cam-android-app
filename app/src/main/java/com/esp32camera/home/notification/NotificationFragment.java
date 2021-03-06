package com.esp32camera.home.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.esp32camera.MainPresenter;
import com.esp32camera.R;
import com.esp32camera.adapter.NotificationRecyclerViewAdapter;
import com.esp32camera.model.Notification;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

// reversed rv - https://stackoverflow.com/questions/32003402/add-items-to-top-of-recyclerview/48864224

public class NotificationFragment extends Fragment implements NotificationContract.View {

    private View view;
    private MainPresenter mainPresenter;
    private NotificationPresenter notificationPresenter;
    private RecyclerView rv_notification;
    private NotificationRecyclerViewAdapter notificationRecyclerViewAdapter;
    private FloatingActionButton fab_delete_selected_notification_items;

    public NotificationFragment(MainPresenter mainPresenter, NotificationPresenter notificationPresenter) {
        this.mainPresenter = mainPresenter;
        this.notificationPresenter = notificationPresenter;

        notificationPresenter.setView(this);
    }

    /**
     * First method where FragmentView is creating
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notifications, container, false);
        return view;
    }

    /**
     * Second method where the view is ready to use
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv_notification = (RecyclerView) view.findViewById(R.id.rv_notification);
        fab_delete_selected_notification_items = (FloatingActionButton) view.findViewById(R.id.fab_delete_selected_notification_items);

        setupRvNotification();

        setupOnListener();

    }

    private void setupOnListener() {
        fab_delete_selected_notification_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.deleteSelectedNotificationItems(notificationPresenter.getSelectedItems());
                notificationPresenter.deleteSelectedItems();
            }
        });
    }

    /**
     * method to setup the notification recyclerView
     */
    private void setupRvNotification() {
        rv_notification.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mainPresenter.getActivity(), LinearLayoutManager.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        layoutManager.scrollToPositionWithOffset(mainPresenter.getNotificationItems().size() - 1, 0);

        notificationRecyclerViewAdapter = new NotificationRecyclerViewAdapter(mainPresenter.getActivity(), mainPresenter, notificationPresenter);

        rv_notification.setLayoutManager(layoutManager);
        rv_notification.setAdapter(notificationRecyclerViewAdapter);


        notificationRecyclerViewAdapter.setOnItemClickListener(new NotificationRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Notification notification) {

            }

            @Override
            public void onItemLongClick(View view, Notification notification) {

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        notificationPresenter.clearSelectedItems();
    }

    /**
     * method to add new notification on motion detected
     * scrolls to the top
     */
    @Override
    public void notifyOnMotionDetected() {
        notificationRecyclerViewAdapter.notifyItemInserted(mainPresenter.getNotificationItems().size() - 1);
        rv_notification.smoothScrollToPosition(mainPresenter.getNotificationItems().size() - 1);
    }

    @Override
    public void scrollToTop() {
        rv_notification.smoothScrollToPosition(mainPresenter.getNotificationItems().size() - 1);
    }

    /**
     * method to show delete button on item selected
     */
    @Override
    public void showDeleteButton() {
        if (fab_delete_selected_notification_items.getVisibility() != View.VISIBLE)
            fab_delete_selected_notification_items.setVisibility(View.VISIBLE);
    }

    /**
     * method to hide delete button on items deleted
     */
    @Override
    public void hideDeleteButton() {
        if (fab_delete_selected_notification_items.getVisibility() != View.GONE)
            fab_delete_selected_notification_items.setVisibility(View.GONE);
    }

    /**
     * method is called when notifications are deleted and is to hide the delete button and reload the recyclerView
     */
    @Override
    public void notifyOnItemsDelete() {
        fab_delete_selected_notification_items.setVisibility(View.GONE);
        notificationRecyclerViewAdapter.notifyDataSetChanged();
    }
}