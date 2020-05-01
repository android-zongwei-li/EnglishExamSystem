package com.example.launchguide.viewpager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.MainActivity;
import com.example.myapplication.R;

public class ContentFragment extends Fragment {

    private int[] bgRes = {R.mipmap.guide_splash, R.mipmap.guide_splash, R.mipmap.guide_splash};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content,null);
        Button btn = view.findViewById(R.id.btn);
        RelativeLayout rl = view.findViewById(R.id.rl);

        int index = getArguments().getInt("index");
        rl.setBackgroundResource(bgRes[0]);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        btn.setVisibility(index == 2 ? View.VISIBLE : View.GONE);

        return view;
    }
}
