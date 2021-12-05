package com.esp32camera.home.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.esp32camera.MainPresenter;
import com.esp32camera.R;
import com.esp32camera.adapter.GalleryRecyclerViewAdapter;

public class GalleryFragment extends Fragment {

    private View view;
    private MainPresenter mainPresenter;
    private GalleryPresenter galleryPresenter;
    private RecyclerView rv_gallery;

    public GalleryFragment(MainPresenter mainPresenter, GalleryPresenter galleryPresenter) {
        this.mainPresenter = mainPresenter;
        this.galleryPresenter = galleryPresenter;

        galleryPresenter.setView(this);
    }

    /**
     * First method where FragmentView is creating
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_gallery, container, false);
        return view;
    }

    /**
     * Second method where the view is ready to use
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv_gallery = (RecyclerView) view.findViewById(R.id.rv_gallery);

        setupRvGallery();

        setupOnListener();
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    private void setupRvGallery() {
        rv_gallery.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mainPresenter.getActivity());

        GalleryRecyclerViewAdapter galleryRecyclerViewAdapter = new GalleryRecyclerViewAdapter(mainPresenter.getActivity(), mainPresenter, galleryPresenter, galleryPresenter.loadGalleryItems());

        rv_gallery.setLayoutManager(new GridLayoutManager(mainPresenter.getActivity(), 2));
        rv_gallery.setAdapter(galleryRecyclerViewAdapter);

        galleryRecyclerViewAdapter.setOnItemClickListener(new GalleryRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String galleryItemName) {

            }

            @Override
            public void onItemLongClick(View view, String galleryItemName) {

            }
        });
    }

    private void setupOnListener() {

    }
}