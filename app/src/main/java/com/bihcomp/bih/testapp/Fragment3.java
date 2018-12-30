package com.bihcomp.bih.testapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.bihcomp.bih.testapp.MyAdapter;
import com.bihcomp.bih.testapp.TitleChild;
import com.bihcomp.bih.testapp.TitleCreator;
import com.bihcomp.bih.testapp.TitleParent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Fragment3 extends Fragment {

    RecyclerView recyclerView;

    public static Fragment3 newInstance(){
        Fragment3 fragment = new Fragment3();
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((MyAdapter)recyclerView.getAdapter()).onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_fragment3,container,false);


        recyclerView = (RecyclerView) view.findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        MyAdapter adapter = new MyAdapter(getContext(),initData());
        adapter.setParentClickableViewAnimationDefaultDuration();
        adapter.setParentAndIconExpandOnClick(true);

        recyclerView.setAdapter(adapter);


        return view;
    }

    private List<ParentObject> initData() {
        // 데이터 생성
        // Json 파일 읽어오기
        String contactStr = readFromContactFile();
        ArrayList<String> titleArray = new ArrayList<String>();
        ArrayList<String> valueArray = new ArrayList<String>();
        ArrayList<Pair<String, String>> titleValue= new ArrayList<Pair<String, String>>();

        try {
            JSONArray jarray = new JSONArray(contactStr);   // JSONArray 생성
            for(int i=0; i < jarray.length(); i++){
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                String phonenumber = jObject.getString("phonenumber");
                String name = jObject.getString("name");
                String photo = jObject.getString("photo");
                int value = jObject.getInt("value");

                if(value == 0)
                {
                    Pair<String, String> tempPair = new Pair<String, String>(name,""+value);
                    titleValue.add(tempPair);

                    titleArray.add(name);
                    valueArray.add(""+value);
                }
                else if(value > 0)
                {
                    Pair<String, String> tempPair = new Pair<String, String>(name,""+value);
                    titleValue.add(tempPair);

                    titleArray.add(name);
                    valueArray.add(""+value);
                }
                else if(value < 0)
                {
                    Pair<String, String> tempPair = new Pair<String, String>(name,""+value);
                    titleValue.add(tempPair);

                    titleArray.add(name);
                    valueArray.add(""+value);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("JSONArray", "Parsing Error Currupt");
        }



        titleValue.sort(new Comparator<Pair<String, String>>() {
            private final Collator collator = Collator.getInstance();
            @Override
            public int compare(Pair<String, String> o1, Pair<String, String> o2) {
                return collator.compare(o1.first, o2.first);
            }
        });

        titleArray.sort(new Comparator<String>() {
            private final Collator collator = Collator.getInstance();
            @Override
            public int compare(String o1, String o2) {
                return collator.compare(o1, o2);
            }
        });


        // 리스트 생성

        TitleCreator titleCreator = TitleCreator.get(getContext(), titleArray);
        List<TitleParent> titles = titleCreator.getAll();
        List<ParentObject> parentObject = new ArrayList<>();
        for(TitleParent title:titles)
        {
            String foundvalue = "";
            for (int i = 0; i < titleArray.size(); i++)
            {
                if(titleValue.get(i).first.matches(title.getTitle()))
                {
                    foundvalue = titleValue.get(i).second;
                    break;
                }
            }
            List<Object> childList = new ArrayList<>();
            String contents = readFromTextFile(title.getTitle());
            if (contents.length() == 0)
            {
                contents = "내역이 존재하지 않습니다.";
                contents = "거래내역 : " + contents;
            }
            else
            {
                contents = "거래내역 :\n" + contents.replace("[split]", "\n");
            }


            childList.add(new TitleChild(contents,"최종금액 : " + foundvalue + "원"));
            title.setChildObjectList(childList);
            parentObject.add(title);
        }
        return parentObject;
    }



    private void writeToContactFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getContext().openFileOutput("contact.json", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("WriteFile", "Exception: File write failed (contact.json)");
        }
    }

    private String readFromContactFile() {
        String ret = "";
        try {
            InputStream inputStream = getContext().openFileInput("contact.json");
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
            Log.e("ReadFile", "Exception: File not found (contact.json)");
        } catch (IOException e) {
            Log.e("ReadFile", "Exception: Can not read file (contact.json)");
        }
        return ret;
    }

    private void writeToTextFile(String title, String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getContext().openFileOutput(title + ".txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("WriteFile", "Exception: File write failed (" + title + ")");
        }
    }

    private String readFromTextFile(String title) {
        String ret = "";
        try {
            InputStream inputStream = getContext().openFileInput(title + ".txt");
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