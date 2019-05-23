package com.example.partner.GroupChatWebRTC.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.partner.Core.utils.ResourceUtils;
import com.example.partner.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PreviewFragment extends Fragment {

    public static final String PREVIEW_IMAGE = "preview_image";


    public static Fragment newInstance(int imageResourceId) {
        PreviewFragment previewFragment = new PreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PREVIEW_IMAGE, imageResourceId);
        previewFragment.setArguments(bundle);
        return previewFragment;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_item_screen_share, container, false);
        Glide.with(this)
                .load(getArguments().getInt(PREVIEW_IMAGE))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(ResourceUtils.getDimen(R.dimen.pager_image_width),
                        ResourceUtils.getDimen(R.dimen.pager_image_height))
                .into((ImageView) view.findViewById(R.id.image_preview));
        return view;
    }
}
