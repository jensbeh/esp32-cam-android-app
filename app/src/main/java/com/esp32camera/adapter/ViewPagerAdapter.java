package com.esp32camera.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.esp32camera.MainPresenter;
import com.esp32camera.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

// videoView handle example - https://abhiandroid.com/ui/videoview

public class ViewPagerAdapter extends PagerAdapter {
    private MainPresenter mainPresenter;
    private List<String> galleryItems;
    private View view;
    private MediaController mediaController;

    public ViewPagerAdapter(MainPresenter mainPresenter, List<String> galleryItems) {
        this.mainPresenter = mainPresenter;
        this.galleryItems = galleryItems;
    }

    @Override
    public int getCount() {
        return galleryItems.size();
    }

    /**
     * method gets the object as key and checks if the view/page to be shown is equals to the object, if yes then show the page
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    /**
     * method loads the view and sets the view as a key for a page at specific position
     * here the view content can be load and set like pictures or texts
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        if (galleryItems.get(position).endsWith(".jpg")) {
            this.view = LayoutInflater.from(mainPresenter.getActivity()).inflate(R.layout.raw_gallery_view_pager_picture_item, null);

            ImageView imageView = (ImageView) view.findViewById(R.id.imageViewVP);
            Bitmap bitmap = BitmapFactory.decodeFile(galleryItems.get(position));
            imageView.setImageBitmap(bitmap);

        } else if (galleryItems.get(position).endsWith(".mp4")) {
            this.view = LayoutInflater.from(mainPresenter.getActivity()).inflate(R.layout.raw_gallery_view_pager_video_item, null);

            mediaController = new MediaController(mainPresenter.getActivity());
            VideoView videoViewVP = (VideoView) view.findViewById(R.id.videoViewVP);
            videoViewVP.setMediaController(mediaController);
            mediaController.show();
            videoViewVP.setVideoPath(galleryItems.get(position));
        }


        TextView tv_itemName = (TextView) view.findViewById(R.id.tv_itemName);
        FloatingActionButton fab_backButton = (FloatingActionButton) view.findViewById(R.id.fab_backButton);
        TextView tv_itemCount = (TextView) view.findViewById(R.id.tv_itemCount);

        tv_itemName.setText(galleryItems.get(position).substring(galleryItems.get(position).lastIndexOf("/") + 1));

        fab_backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.navigateToGalleryFragment();
            }
        });

        tv_itemCount.setText(position + 1 + "/" + galleryItems.size());
        container.addView(view, 0);
        return view;
    }

    /**
     * method to remove the item/view from the container where all views from the pages are saved
     */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }

    /**
     * method to hide the media controller
     * is called if page changed
     */
    public void hideMediaController() {
        if (mediaController != null)
            mediaController.hide();
    }
}
