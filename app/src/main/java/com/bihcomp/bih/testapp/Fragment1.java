package com.bihcomp.bih.testapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


public class Fragment1 extends Fragment {

    private ArrayList<String> mList;
    private ListView mListView;;
    private ArrayAdapter mAdapter;

    ArrayAdapter<contactEntry> contactAdapter;
    final ArrayList<contactEntry> contactList = new ArrayList<contactEntry>();
    String contactStr = "";
    Context ctx = this.getContext();

    final String[] username = new String[1];
    final String[] userphonenumber = new String[1];
    final boolean[] alertstatus = new boolean[1];
    final View[] tempview = new View[1];


    public static Fragment1 newInstance(){
        Fragment1 fragment = new Fragment1();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // 어뎁터 설정
        contactAdapter = new contactArrayAdapter(getContext(), R.layout.listview_contact, R.id.eName, contactList);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_fragment1,container,false);
        tempview[0] = view;


        contactAdapter.clear();

        // Json 파일 읽어오기
        contactStr = readFromContactFile();

        try {
            JSONArray jarray = new JSONArray(contactStr);   // JSONArray 생성
            for(int i=0; i < jarray.length(); i++){
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                String phonenumber = jObject.getString("phonenumber");
                String name = jObject.getString("name");
                String photo = jObject.getString("photo");
                int value = jObject.getInt("value");
                //int age = jObject.getInt("photo");

                if(value == 0)
                {
                    contactEntry ne = new contactEntry(name, phonenumber, R.drawable.photo);
                    contactList.add(ne);
                }
                else if(value > 0)
                {

                }
                else if (value < 0)
                {

                }
                else
                {

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 어뎁터 설정

        contactAdapter = new contactArrayAdapter(getContext(), R.layout.listview_contact, R.id.eName, contactList);





        Button addButton = (Button) view.findViewById(R.id.add) ;
        addButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // 값 입력 창 제작
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

                alert.setTitle("새 연락처 추가");
                alert.setMessage("이름과 전화번호를 입력해주세요.");


                final LinearLayout alertlayout = new LinearLayout(getContext());
                alertlayout.setOrientation(LinearLayout.VERTICAL);

                final EditText editTextName = new EditText(getContext());
                editTextName.setHint("이름");
                alertlayout.addView(editTextName);

                final EditText editTextPhonenumber = new EditText(getContext());
                editTextPhonenumber.setHint("전화번호");
                alertlayout.addView(editTextPhonenumber);

                alert.setView(alertlayout);

                alert.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        username[0] = editTextName.getText().toString();
                        userphonenumber[0] = editTextPhonenumber.getText().toString();

                        alertstatus[0] = true;

                        String tempstr = readFromContactFile();
                        tempstr = tempstr.replace("]"," ");
                        String addstr =  ",{'name':'" + username[0] + "','phonenumber':'" + userphonenumber[0] + ",'value':0'}]";
                        tempstr = tempstr + addstr;
                        Toast.makeText(getContext(), addstr, Toast.LENGTH_SHORT).show();
                        writeToContactFile(tempstr);


                    }
                });

                alert.setNegativeButton("취소",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        alertstatus[0] = false;
                    }
                });

                alert.show();


                if(alertstatus[0]) {
                    contactAdapter = new contactArrayAdapter(getContext(), R.layout.listview_contact, R.id.eName, contactList);
                    contactEntry ne = new contactEntry(username[0], userphonenumber[0], R.drawable.photo);
                    contactList.add(ne);
                }

                // listview 갱신
                contactAdapter.notifyDataSetChanged();
            }
        }) ;











        /*


        // Fragment1 리스트 설정
        // 빈 데이터 리스트 생성.
        final ArrayList<String> items = new ArrayList<String>() ;
        // ArrayAdapter 생성. 아이템 View를 선택(single choice)가능하도록 만듦.
        final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_single_choice, items) ;

        // listview 생성 및 adapter 지정.
        final ListView listview = (ListView) view.findViewById(R.id.listview_tab1) ;

        listview.setAdapter(adapter) ;

        adapter.add("test1");
        adapter.add("test2");

        // add button에 대한 이벤트 처리.
        Button addButton = (Button) view.findViewById(R.id.add) ;
        addButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                int count;
                count = adapter.getCount();

                // 아이템 추가.
                items.add("LIST" + Integer.toString(count + 1));

                // listview 갱신
                adapter.notifyDataSetChanged();
            }
        }) ;

        // modify button에 대한 이벤트 처리.
        Button modifyButton = (Button) view.findViewById(R.id.modify) ;
        modifyButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                int count, checked ;
                count = adapter.getCount() ;

                if (count > 0) {
                    // 현재 선택된 아이템의 position 획득.
                    checked = listview.getCheckedItemPosition();
                    if (checked > -1 && checked < count) {
                        // 아이템 수정
                        items.set(checked, Integer.toString(checked+1) + "번 아이템 수정") ;

                        // listview 갱신
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }) ;

        // delete button에 대한 이벤트 처리.
        Button deleteButton = (Button) view.findViewById(R.id.delete) ;
        deleteButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                int count, checked ;
                count = adapter.getCount() ;

                if (count > 0) {
                    // 현재 선택된 아이템의 position 획득.
                    checked = listview.getCheckedItemPosition();

                    if (checked > -1 && checked < count) {
                        // 아이템 삭제
                        items.remove(checked) ;

                        // listview 선택 초기화.
                        listview.clearChoices();

                        // listview 갱신.
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }) ;
        */

        /*
        contactEntry ne1 = new contactEntry("Mia","01034567890", R.drawable.photo);
        contactList.add(ne1);
        contactEntry ne2 = new contactEntry("Aki","01062954351", R.drawable.photo);
        contactList.add(ne2);
        contactEntry ne3 = new contactEntry("Snoopy","01091534510", -1);
        contactList.add(ne3);
        contactEntry ne4 = new contactEntry("Jenny","01034964501", R.drawable.photo);
        contactList.add(ne4);
        contactEntry ne5 = new contactEntry("Kim","01043956103", -1);
        contactList.add(ne5);
        */

        // listview 생성 및 adapter 지정.
        final ListView listview = (ListView) view.findViewById(R.id.listview_tab1) ;
        listview.setAdapter(contactAdapter) ;



        return view;
    }

    private void insertadress() {
        contactAdapter = new contactArrayAdapter(getContext(), R.layout.listview_contact, R.id.eName, contactList);
        contactEntry ne = new contactEntry(username[0], userphonenumber[0], R.drawable.photo);
        contactList.add(ne);

        contactAdapter.notifyDataSetChanged();
    }

    private class contactArrayAdapter extends ArrayAdapter<contactEntry> {
        private ArrayList<contactEntry> items;
        private int rsrc;

        public contactArrayAdapter(Context ctx, int rsrcId, int txtId, ArrayList<contactEntry> data) {
            super(ctx, rsrcId, txtId, data);
            this.items = data;
            this.rsrc = rsrcId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = li.inflate(rsrc, null);
            }
            contactEntry e = items.get(position);
            if (e != null) {
                ((TextView)v.findViewById(R.id.eName)).setText(e.getName());
                ((TextView)v.findViewById(R.id.ePhoneNo)).setText(e.getPhoneNo());
                if (e.getPhotoId() != -1) {
                    ((ImageView)v.findViewById(R.id.ePhoto)).setImageResource(e.getPhotoId());
                } else {
                    ((ImageView)v.findViewById(R.id.ePhoto)).setImageResource(R.drawable.photo);
                }
            }
            return v;
        }
    }

    public void onListItemClick(ListView parent, View v, int pos, long id) {
        final int index = pos;
        final contactEntry ent = contactAdapter.getItem(pos);
        final String pn = ent.getPhoneNo();
        final int _photo = ent.getPhotoId()!=-1?ent.getPhotoId():R.drawable.photo;

        new AlertDialog.Builder(this.getContext()).setTitle(ent.getName())
                .setMessage(pn)
                .setIcon(ent.getPhotoId())
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dlg, int sumthin) {
                        contactAdapter.remove(ent);
                        contactAdapter.notifyDataSetChanged();
                    }
                })
                .setNeutralButton("Call", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dlg, int sumthin) {
                        Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+pn));
                        startActivity(i);
                    }
                })
                .show();
    }


    private void writeToContactFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getContext().openFileOutput("contact.json", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Toast.makeText(this.getContext(), "Exception: File write failed", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this.getContext(), "Exception: File not found", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this.getContext(), "Exception: Can not read file", Toast.LENGTH_SHORT).show();
        }

        return ret;
    }

}