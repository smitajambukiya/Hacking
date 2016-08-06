package com.patelapp.Fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.patelapp.R;

/**
 * Created by Android on 12/30/2015.
 */
public class AboutFragment extends BaseFragment {

    View view;
    Toolbar toolbar;
    ImageView imageView;
    TextView tvTitle,tvWebsite,tvEmail,tvContacts,tvDesc;
Typeface typeface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about_us, container, false);

        findView();
        return view;
    }

    private void findView() {
        typeface = Typeface.createFromAsset(getActivity().getAssets(),"fonts/"+getString(R.string.font_guj));

        tvContacts =(TextView)view.findViewById(R.id.tvContacts);
        tvTitle = (TextView)view.findViewById(R.id.tvTitle);
        tvEmail = (TextView)view.findViewById(R.id.tvEmail);
        tvWebsite = (TextView)view.findViewById(R.id.tvWebsite);
        tvDesc = (TextView)view.findViewById(R.id.tvDesc);
        tvDesc.setText(getString(R.string.about_us));
        tvDesc.setTypeface(typeface);

        tvTitle.setText("Developed By:  \n Hiren Patel");
        tvEmail.setText("Email : hp@germaniuminc.com");
        tvWebsite.setText("Website : "+"www.germaniuminc.com");
        tvContacts.setText("Mobile : 8487808590");
     //   toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        imageView = (ImageView) view.findViewById(R.id.backdrop);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Germaniuminc Inc.");

        loadBackdrop();
    }

    private void loadBackdrop() {
        Glide.with(this).load(R.drawable.company_logo).centerCrop().into(imageView
        );
    }


}
