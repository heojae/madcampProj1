package com.bihcomp.bih.testapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class GalleryLibrary extends AppCompatActivity {

    private Context mContext;

    public GridView selectedGridView;
    public GridView allGridView;

    public final int[] Scrollposition = new int[2];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_library);
        mContext = this;



        getSupportActionBar().setTitle("이미지 선택");

        // 하단 갤러리뷰
        GridView gv = (GridView)findViewById(R.id.gallery_view);
        final GridViewAdapter ga[] = new GridViewAdapter[1];
        GridViewAdapter gacontext = new GridViewAdapter(this);
        ga[0] = gacontext;
        gv.setAdapter(ga[0]);

        // 상단 갤러리뷰
        GridView gvsel = (GridView)findViewById(R.id.gallery_view_selected);
        final SelectedGridViewAdapter gasel[] = new SelectedGridViewAdapter[1];
        SelectedGridViewAdapter gaselcontext = new SelectedGridViewAdapter(this);
        gasel[0] = gaselcontext;
        gvsel.setAdapter(gasel[0]);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView parent, View v, int position, long id){

                // 이미지 선택 시 리스트 추가
                String beforestring = readFromSettingFile("GALLERY_SELECTED_IMAGE_LIST");
                String afterstring = "";
                if (beforestring.equals("") && !beforestring.contains(ga[0].getImgPath(position)))
                {
                    afterstring = ga[0].getImgPath(position);
                    writeToSettingFile("GALLERY_SELECTED_IMAGE_LIST", afterstring);
                }
                else if (!beforestring.contains(ga[0].getImgPath(position)))
                {
                    afterstring = beforestring + "///SPLIT///" + ga[0].getImgPath(position);
                    writeToSettingFile("GALLERY_SELECTED_IMAGE_LIST", afterstring);
                }

                int index = allGridView.getFirstVisiblePosition() * 120;

                SelectedGridViewAdapter tgasel = new SelectedGridViewAdapter(GalleryLibrary.this);
                GridViewAdapter tga = new GridViewAdapter(GalleryLibrary.this);
                gasel[0] = tgasel;
                ga[0] = tga;
                selectedGridView.setAdapter(tgasel);
                allGridView.setAdapter(tga);
                tgasel.notifyDataSetInvalidated();
                tga.notifyDataSetInvalidated();
                gridViewSetting();

            }
        });

        gvsel.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView parent, View v, int position, long id){

                // 이미지 선택 시 리스트 제거
                String beforestring = readFromSettingFile("GALLERY_SELECTED_IMAGE_LIST");
                String removestring = "";

                Toast.makeText(GalleryLibrary.this,beforestring,Toast.LENGTH_LONG);
                Log.d("removetext:beforestring", beforestring);

                Log.d("removetext:removestring", gasel[0].getImgPath(position));
                removestring = beforestring.replace(gasel[0].getImgPath(position), "");
                removestring = removestring.replace("///SPLIT//////SPLIT///", "///SPLIT///");
                if (removestring.indexOf("///SPLIT///") == 0)
                    removestring = removestring.replaceFirst("///SPLIT///", "");

                Toast.makeText(GalleryLibrary.this,removestring,Toast.LENGTH_LONG);
                Log.d("removetext:removestring", removestring);

                writeToSettingFile("GALLERY_SELECTED_IMAGE_LIST", removestring);

                gasel[0].removeItem(position);
                gasel[0].notifyDataSetInvalidated();
                gridViewSetting();

                GridViewAdapter tga = new GridViewAdapter(GalleryLibrary.this);
                ga[0] = tga;
                allGridView.setAdapter(tga);
                tga.notifyDataSetInvalidated();

            }
        });



        selectedGridView = (GridView)findViewById(R.id.gallery_view_selected);
        allGridView = (GridView)findViewById(R.id.gallery_view);

        Scrollposition[0] = allGridView.getScrollX();
        Scrollposition[1] = allGridView.getScrollY();

        gridViewSetting();




    }

    /**==========================================
     *              하단 Adapter class
     * ==========================================*/
    public class GridViewAdapter extends BaseAdapter {
        private String imgData;
        private String geoData;
        private ArrayList<String> thumbsDataList;
        private ArrayList<String> thumbsIDList;
        private ArrayList<String> thumbsStateList;

        public GridViewAdapter(Context c){
            mContext = c;
            thumbsDataList = new ArrayList<String>();
            thumbsIDList = new ArrayList<String>();
            thumbsStateList = new ArrayList<String>();
            getThumbInfo(thumbsIDList, thumbsDataList);
        }

        public final void callImageViewer(int selectedIndex){
            Intent i = new Intent(mContext, ImagePopup.class);
            String imgPath = getImageInfo(imgData, geoData, thumbsIDList.get(selectedIndex));
            i.putExtra("filename", imgPath);
            startActivityForResult(i, 1);
        }

        public boolean deleteSelected(int sIndex){
            return true;
        }

        public int getCount() {
            return thumbsIDList.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {





            ImageView imageView;
            if (convertView == null){
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(480, 270));
                imageView.setAdjustViewBounds(false);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(2, 2, 2, 2);
            }else{
                imageView = (ImageView) convertView;
            }
            BitmapFactory.Options bo = new BitmapFactory.Options();
            bo.inSampleSize = 8;
            Bitmap bmp = BitmapFactory.decodeFile(thumbsDataList.get(position), bo);
            Bitmap resized = Bitmap.createScaledBitmap(bmp, 200, 200, true);


            if (thumbsStateList.get(position).equals("yes")){
                // 최종 Bitmap
                Bitmap resultimage = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);

                // 단색 Bitmap
                Bitmap image = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
                image.eraseColor(android.graphics.Color.rgb(52, 73, 94));

                Paint paint = new Paint();
                paint.setAlpha(100);

                Canvas canvas = new Canvas(resultimage);
                canvas.drawBitmap(resized, new Matrix(), null);
                canvas.drawBitmap(image, new Matrix(), paint);

                Bitmap crop = Bitmap.createBitmap(resultimage, 6, 49, 188, 102);
                //crop = Bitmap.createScaledBitmap(crop, 200, 200, true);

                Canvas canvas2 = new Canvas(resultimage);
                canvas2.drawColor(android.graphics.Color.rgb(52, 73, 94));
                canvas2.drawBitmap(crop, 6,49, null);
                imageView.setImageBitmap(resultimage);
            } else{
                imageView.setImageBitmap(resized);
            }

            return imageView;
        }

        private void getThumbInfo(ArrayList<String> thumbsIDs, ArrayList<String> thumbsDatas){
            String[] proj = {MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.SIZE};

            Cursor imageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    proj, null, null, null);

            if (imageCursor != null && imageCursor.moveToFirst()){
                String title;
                String thumbsID;
                String thumbsImageID;
                String thumbsData;
                String data;
                String imgSize;

                int thumbsIDCol = imageCursor.getColumnIndex(MediaStore.Images.Media._ID);
                int thumbsDataCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                int thumbsImageIDCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
                int thumbsSizeCol = imageCursor.getColumnIndex(MediaStore.Images.Media.SIZE);
                int num = 0;

                // 문자열 파싱
                String pathname = readFromSettingFile("GALLERY_SELECTED_IMAGE_LIST");
                String[] pathnames = pathname.split("///SPLIT///");


                do {

                    // 문자열 내에 단어가 포함될 경우에만 추가
                    int imgData = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    String imageDataPath = imageCursor.getString(imgData);

                    thumbsID = imageCursor.getString(thumbsIDCol);
                    thumbsData = imageCursor.getString(thumbsDataCol);
                    thumbsImageID = imageCursor.getString(thumbsImageIDCol);
                    imgSize = imageCursor.getString(thumbsSizeCol);
                    num++;
                    if (thumbsImageID != null){
                        boolean isItSelected = false;
                        for (String str:pathnames)
                        {
                            if (str.equals(imageDataPath))
                            {
                                isItSelected = true;
                                break;
                            }
                        }
                        thumbsStateList.add(isItSelected?"yes":"no");
                        thumbsIDs.add(thumbsID);
                        thumbsDatas.add(thumbsData);
                    }
                }while (imageCursor.moveToNext());
            }
            imageCursor.close();
            return;
        }

        private String getImageInfo(String ImageData, String Location, String thumbID){
            String imageDataPath = null;
            String[] proj = {MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.SIZE};
            Cursor imageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    proj, "_ID='" + thumbID + "'", null, null);

            if (imageCursor != null && imageCursor.moveToFirst()){
                if (imageCursor.getCount() > 0){
                    int imgData = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    imageDataPath = imageCursor.getString(imgData);
                }
            }
            imageCursor.close();
            return imageDataPath;
        }

        public String getImgPath(int selectedIndex){
            String imgPath = getImageInfo(imgData, geoData, thumbsIDList.get(selectedIndex));
            return imgPath;
        }

        public void removeItem(int position) {

            thumbsDataList.remove(position);
            thumbsIDList.remove(position);
            notifyDataSetChanged();
        }
    }





    /**==========================================
     *              상단 Adapter class
     * ==========================================*/
    public class SelectedGridViewAdapter extends BaseAdapter {
        private String imgData;
        private String geoData;
        private ArrayList<String> thumbsDataList;
        private ArrayList<String> thumbsIDList;

        public SelectedGridViewAdapter(Context c){
            mContext = c;
            thumbsDataList = new ArrayList<String>();
            thumbsIDList = new ArrayList<String>();
            getThumbInfo(thumbsIDList, thumbsDataList);
        }

        public final void callImageViewer(int selectedIndex){
            Intent i = new Intent(mContext, ImagePopup.class);
            String imgPath = getImageInfo(imgData, geoData, thumbsIDList.get(selectedIndex));
            i.putExtra("filename", imgPath);
            startActivityForResult(i, 1);
        }

        public boolean deleteSelected(int sIndex){
            return true;
        }

        public int getCount() {
            return thumbsIDList.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null){
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(270, 270));
                imageView.setAdjustViewBounds(false);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setPadding(2, 2, 2, 2);
            }else{
                imageView = (ImageView) convertView;
            }
            BitmapFactory.Options bo = new BitmapFactory.Options();
            bo.inSampleSize = 8;
            Bitmap bmp = BitmapFactory.decodeFile(thumbsDataList.get(position), bo);
            Bitmap crop = Bitmap.createBitmap(bmp, bmp.getWidth()/8, 0, bmp.getWidth()/4*3, bmp.getHeight());
            Bitmap resized = Bitmap.createScaledBitmap(crop, 95, 95, true);
            imageView.setImageBitmap(resized);

            return imageView;
        }

        private void getThumbInfo(ArrayList<String> thumbsIDs, ArrayList<String> thumbsDatas){
            String[] proj = {MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.SIZE};

            Cursor imageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    proj, null, null, null);

            if (imageCursor != null && imageCursor.moveToFirst()){
                String title;
                String thumbsID;
                String thumbsImageID;
                String thumbsData;
                String data;
                String imgSize;

                int thumbsIDCol = imageCursor.getColumnIndex(MediaStore.Images.Media._ID);
                int thumbsDataCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                int thumbsImageIDCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
                int thumbsSizeCol = imageCursor.getColumnIndex(MediaStore.Images.Media.SIZE);
                int num = 0;

                // 문자열 파싱
                String pathname = readFromSettingFile("GALLERY_SELECTED_IMAGE_LIST");
                String[] pathnames = pathname.split("///SPLIT///");


                do {
                    thumbsID = imageCursor.getString(thumbsIDCol);
                    thumbsData = imageCursor.getString(thumbsDataCol);
                    thumbsImageID = imageCursor.getString(thumbsImageIDCol);
                    imgSize = imageCursor.getString(thumbsSizeCol);
                    num++;
                    if (thumbsImageID != null){

                        // 문자열 내에 단어가 포함될 경우에만 추가
                        int imgData = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                        String imageDataPath = imageCursor.getString(imgData);

                        for (String str:pathnames)
                        {
                            if (str.equals(imageDataPath))
                            {
                                thumbsIDs.add(thumbsID);
                                thumbsDatas.add(thumbsData);
                                break;
                            }
                        }
                    }
                }while (imageCursor.moveToNext());
            }
            imageCursor.close();
            return;
        }

        private String getImageInfo(String ImageData, String Location, String thumbID){
            String imageDataPath = null;
            String[] proj = {MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.SIZE};
            Cursor imageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    proj, "_ID='" + thumbID + "'", null, null);

            if (imageCursor != null && imageCursor.moveToFirst()){
                if (imageCursor.getCount() > 0){
                    int imgData = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    imageDataPath = imageCursor.getString(imgData);
                }
            }
            imageCursor.close();
            return imageDataPath;
        }

        public String getImgPath(int selectedIndex){
            String imgPath = getImageInfo(imgData, geoData, thumbsIDList.get(selectedIndex));
            return imgPath;
        }

        public void removeItem(int position) {

            thumbsDataList.remove(position);
            thumbsIDList.remove(position);
            notifyDataSetChanged();
        }
    }


    // 수평 그리드뷰 설정
    private void gridViewSetting() {

        int dataCount = 0;
        String pathname = readFromSettingFile("GALLERY_SELECTED_IMAGE_LIST");
        String[] pathnames = pathname.split("///SPLIT///");

        // this is size of your list with data
        int size = pathnames.length;
        // Calculated single Item Layout Width for each grid element .. for me it was ~100dp
        int width = 100 ;

        // than just calculate sizes for layout params and use it
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;

        int totalWidth = (int) (width * size * density);
        int singleItemWidth = (int) (width * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                totalWidth, LinearLayout.LayoutParams.MATCH_PARENT);

        selectedGridView.setLayoutParams(params);
        selectedGridView.setColumnWidth(singleItemWidth);
        selectedGridView.setHorizontalSpacing(2);
        selectedGridView.setStretchMode(GridView.STRETCH_SPACING);
        selectedGridView.setNumColumns(size);

    }











    private void writeToSettingFile(String title, String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput(title + ".cfg", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("WriteFile", "Exception: File write failed (" + title + ")");
        }
    }

    private String readFromSettingFile(String title) {
        String ret = "";
        try {
            InputStream inputStream = this.openFileInput(title + ".cfg");
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("ReadFile", "Exception: File not found (" + title + ")");
        } catch (IOException e) {
            Log.e("ReadFile", "Exception: Can not read file (" + title + ")");
        }
        return ret;
    }

}

