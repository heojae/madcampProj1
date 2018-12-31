package com.bihcomp.bih.testapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.bihcomp.bih.testapp.Fragment2;
import com.bihcomp.bih.testapp.R;

public class ImagePopup extends FragmentActivity implements OnClickListener {
    private Context mContext = null;
    //private final int imgWidth = 960;
    //private final int imgHeight = 540;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_popup);
        mContext = this;

        // 전송메시지
        Intent i = ((FragmentActivity) mContext).getIntent();
        Bundle extras = i.getExtras();
        String imgPath = extras.getString("filename");

        // 완성된 이미지 보여주기
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inSampleSize = 2;
        ImageView iv = (ImageView) findViewById(R.id.imageView);
        Bitmap bm = BitmapFactory.decodeFile(imgPath, bfo);

        float widthOrigin = bm.getWidth();
        float heightOrigin = bm.getHeight();
        int widthFixed;
        int heightFixed;
        float scale;

        if (widthOrigin > heightOrigin) {
            widthFixed = 960;
            scale = widthFixed / widthOrigin;
            heightFixed = (int) (heightOrigin * scale);
        } else {
            heightFixed = 960;
            scale = heightFixed / heightOrigin;
            widthFixed = (int) (widthOrigin * scale);
        }

        Bitmap resized = Bitmap.createScaledBitmap(bm, widthFixed, heightFixed, true);
        iv.setImageBitmap(resized);
    }

    /*
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_popup, container, false);
        mContext = getActivity();

        // 전송메시지
        Intent i = ((FragmentActivity) mContext).getIntent();
        Bundle extras = i.getExtras();
        String imgPath = extras.getString("filename");

        // 완성된 이미지 보여주기
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inSampleSize = 2;
        ImageView iv = (ImageView) getView().findViewById(R.id.imageView);
        Bitmap bm = BitmapFactory.decodeFile(imgPath, bfo);
        Bitmap resized = Bitmap.createScaledBitmap(bm, imgWidth, imgHeight, true);
        iv.setImageBitmap(resized);

        // 리스트로 가기 버튼
        Button btn = (Button) getView().findViewById(R.id.btn_back);
        btn.setOnClickListener(this);

        return view;
    }*/

    /* (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    public void onClick(View v) {

    }
}
