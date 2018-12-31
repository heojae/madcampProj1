package com.bihcomp.bih.testapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    android.support.v7.app.ActionBar bar;
    private FragmentManager fm;
    private ArrayList<Fragment> fList;

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2, fab3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // floatingActionButton 생성
        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Todo: Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);

        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);

        // 스와이프할 뷰페이저를 정의
        mViewPager = (ViewPager) findViewById(R.id.pager);

        // 프라그먼트 매니져 객체 정의
        fm = getSupportFragmentManager();

        // 액션바 객체 정의
        bar = getSupportActionBar();

        // 액션바 속성 정의
        bar.setDisplayShowTitleEnabled(true);   // 액션바 노출 유무
        bar.setTitle("자산관리 어플리케이션");   // 액션바 타이틀 라벨

        // 액션바에 모드 설정 = ActionBar.NAVIGATION_MODE_TABS 로 TAB 모드로 설정
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // 액션바에 추가될 탭 생성
        ActionBar.Tab tab1 = bar.newTab().setText("연락처").setTabListener(tabListener);
        ActionBar.Tab tab2 = bar.newTab().setText("갤러리").setTabListener(tabListener);
        ActionBar.Tab tab3 = bar.newTab().setText("회계장부").setTabListener(tabListener);

        // 액션바에 탭 추가
        bar.addTab(tab1);
        bar.addTab(tab2);
        bar.addTab(tab3 );

        // 각 탭에 들어갈 프라그먼트 생성 및 추가
        fList = new ArrayList<Fragment>();
        fList.add(Fragment1.newInstance());
        fList.add(Fragment2.newInstance());
        fList.add(Fragment3.newInstance());

        // 스와이프로 탭간 이동할 뷰페이저의 리스너 설정
        mViewPager.setOnPageChangeListener(viewPagerListener);

        // 뷰페이져의 아답터 생성 및 연결
        CustomFragmentPagerAdapter fragmentAdapter = new CustomFragmentPagerAdapter(fm, fList);
        mViewPager.setAdapter(fragmentAdapter);








/*


        // Fragment1 리스트 설정
        // 빈 데이터 리스트 생성.
        final ArrayList<String> items = new ArrayList<String>() ;
        // ArrayAdapter 생성. 아이템 View를 선택(single choice)가능하도록 만듦.
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, items) ;

        // listview 생성 및 adapter 지정.
        final ListView listview = (ListView) findViewById(R.id.listview_tab1) ;

//        listview.setAdapter(adapter) ;

        // add button에 대한 이벤트 처리.
        Button addButton = (Button)findViewById(R.id.add) ;
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
        Button modifyButton = (Button)findViewById(R.id.modify) ;
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
        Button deleteButton = (Button)findViewById(R.id.delete) ;
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

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                anim();
                break;
            case R.id.fab1:
                anim();
                if (mViewPager.getCurrentItem() == 0) {
                    // 값 입력 창 제작
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);

                    alert.setTitle("기존 연락처 제거");
                    alert.setMessage("이름과 전화번호를 입력해주세요.");

                    final LinearLayout alertlayout = new LinearLayout(this);
                    alertlayout.setOrientation(LinearLayout.VERTICAL);


                    final TextView textViewName = new TextView(this);
                    textViewName.setText("이름 : ");
                    alertlayout.addView(textViewName);

                    final EditText editTextName = new EditText(this);
                    editTextName.setHint("이름을 입력하세요.                    ");
                    alertlayout.addView(editTextName);

                    final TextView textViewPhonenumber = new TextView(this);
                    textViewPhonenumber.setText("전화번호 : ");
                    alertlayout.addView(textViewPhonenumber);

                    final EditText editTextPhonenumber = new EditText(this);
                    editTextPhonenumber.setHint("전화번호를 입력하세요.                    ");
                    alertlayout.addView(editTextPhonenumber);


                    final TextView textViewtemp = new TextView(this);
                    textViewtemp.setText("        ");


                    final LinearLayout wraplayout = new LinearLayout(this);
                    wraplayout.setOrientation(LinearLayout.HORIZONTAL);
                    wraplayout.addView(textViewtemp);
                    wraplayout.addView(alertlayout);

                    alert.setView(wraplayout);

                    alert.setPositiveButton("제거", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            String username = editTextName.getText().toString();
                            String userphonenumber = editTextPhonenumber.getText().toString();

                            String tempstr = readFromContactFile();

                            if (username.length() == 0 || userphonenumber.length() == 0){
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("알림");
                                builder.setMessage("이름과 전화번호를 모두 입력하세요.");
                                builder.setNegativeButton("확인",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                builder.show();
                            } else if (tempstr.contains(username) && tempstr.contains(userphonenumber)) {
                                String expectedstr1 = "{'name':'" + username + "','phonenumber':'" + userphonenumber + "','photo':'man1','value':0}";
                                String expectedstr2 = "{'name':'" + username + "','phonenumber':'" + userphonenumber + "','photo':'man2','value':0}";
                                String expectedstr3 = "{'name':'" + username + "','phonenumber':'" + userphonenumber + "','photo':'man3','value':0}";
                                String expectedstr4 = "{'name':'" + username + "','phonenumber':'" + userphonenumber + "','photo':'woman1','value':0}";
                                String expectedstr5 = "{'name':'" + username + "','phonenumber':'" + userphonenumber + "','photo':'woman2','value':0}";
                                String expectedstr6 = "{'name':'" + username + "','phonenumber':'" + userphonenumber + "','photo':'woman3','value':0}";
                                String expectedstrs[] = {expectedstr1, expectedstr2, expectedstr3, expectedstr4, expectedstr5, expectedstr6};

                                boolean change = false;

                                for (int i = 0; i < expectedstrs.length; i++) {
                                    if (tempstr.indexOf(expectedstrs[i]) > -1) {
                                        if (tempstr.indexOf(expectedstrs[i]) == 1){
                                            tempstr = tempstr.replace(expectedstrs[i], "");
                                            tempstr = tempstr.replaceFirst(",", "");
                                        } else {
                                            tempstr = tempstr.replace(expectedstrs[i], "");
                                            tempstr = tempstr.replace(",,", ",");
                                        }
                                        writeToContactFile(tempstr);
                                        change = true;
                                    }
                                }

                                if (!change) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setTitle("알림");
                                    builder.setMessage("금액이 남은 경우 삭제하실 수 없습니다.");
                                    builder.setNegativeButton("확인",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });
                                    builder.show();
                                }
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("알림");
                                builder.setMessage("이름과 전화번호가 존재하지 않습니다.");
                                builder.setNegativeButton("확인",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                builder.show();
                            }

                            mViewPager.setCurrentItem(0);
                        }
                    });

                    alert.setNegativeButton("취소",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            mViewPager.setCurrentItem(0);
                        }
                    });

                    alert.show();

                    mViewPager.refreshDrawableState();
                    mViewPager.setCurrentItem(2);
                } else if (mViewPager.getCurrentItem() == 1) {

                } else if (mViewPager.getCurrentItem() == 2) {
                    mViewPager.setCurrentItem(0);
                    Snackbar.make(this.mViewPager, "값을 추가할 연락처를 선택하세요.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                break;
            case R.id.fab2:
                anim();
                if (mViewPager.getCurrentItem() == 0) {
                    // 값 입력 창 제작
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);

                    alert.setTitle("새 연락처 추가");
                    alert.setMessage("이름과 전화번호를 입력해주세요.");

                    final LinearLayout alertlayout = new LinearLayout(this);
                    alertlayout.setOrientation(LinearLayout.VERTICAL);


                    final TextView textViewName = new TextView(this);
                    textViewName.setText("이름 : ");
                    alertlayout.addView(textViewName);

                    final EditText editTextName = new EditText(this);
                    editTextName.setHint("이름을 입력하세요.                    ");
                    alertlayout.addView(editTextName);

                    final TextView textViewPhonenumber = new TextView(this);
                    textViewPhonenumber.setText("전화번호 : ");
                    alertlayout.addView(textViewPhonenumber);

                    final EditText editTextPhonenumber = new EditText(this);
                    editTextPhonenumber.setHint("전화번호를 입력하세요.                    ");
                    alertlayout.addView(editTextPhonenumber);


                    final TextView textViewtemp = new TextView(this);
                    textViewtemp.setText("        ");


                    final LinearLayout wraplayout = new LinearLayout(this);
                    wraplayout.setOrientation(LinearLayout.HORIZONTAL);
                    wraplayout.addView(textViewtemp);
                    wraplayout.addView(alertlayout);

                    alert.setView(wraplayout);

                    alert.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String username = editTextName.getText().toString();
                            String userphonenumber = editTextPhonenumber.getText().toString();

                            if (username.length() == 0 || userphonenumber.length() == 0){
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("알림");
                                builder.setMessage("이름과 전화번호를 모두 입력하세요.");
                                builder.setNegativeButton("확인",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                builder.show();
                                mViewPager.setCurrentItem(0);
                            } else {
                                String tempstr = readFromContactFile();
                                tempstr = tempstr.replace("]","");
                                String addstr =  ",{'name':'" + username + "','phonenumber':'" + userphonenumber + "','photo':'man1','value':0}]";
                                tempstr = tempstr + addstr;
                                writeToContactFile(tempstr);
                                //Toast.makeText(getParent(), addstr, Toast.LENGTH_SHORT).show();
                                Log.d("addstr", addstr);
                                mViewPager.setCurrentItem(0);
                            }

                        }
                    });

                    alert.setNegativeButton("취소",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            mViewPager.setCurrentItem(0);
                        }
                    });
                    alert.setNeutralButton("연락처 선택", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent mIntent = new Intent(Intent.ACTION_PICK);
                            mIntent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                            startActivityForResult(mIntent, 0);

                        }
                    });

                    alert.show();

                    mViewPager.refreshDrawableState();
                    mViewPager.setCurrentItem(2);
                } else if (mViewPager.getCurrentItem() == 1) {

                } else if (mViewPager.getCurrentItem() == 2) {
                    mViewPager.setCurrentItem(0);
                    Snackbar.make(this.mViewPager, "값을 제거할 연락처를 선택하세요.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                break;
            case R.id.fab3:
                anim();
                if (mViewPager.getCurrentItem() == 0) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    EditText editTextCt = (EditText) findViewById(R.id.editTextContact);
                    LinearLayout searchLinearLayout = findViewById(R.id.searchLinearLayout);

                    if (searchLinearLayout.getVisibility() == View.VISIBLE) {
                        searchLinearLayout.setVisibility(View.GONE);
                        editTextCt.setText("");
                        editTextCt.clearFocus();
                        imm.hideSoftInputFromWindow(editTextCt.getWindowToken(), 0);
                    } else {
                        searchLinearLayout.setVisibility(View.VISIBLE);
                        imm.showSoftInput(editTextCt, 0);
                    }
                } else if (mViewPager.getCurrentItem() == 1) {

                } else if (mViewPager.getCurrentItem() == 2) {

                }
                break;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK)
        {
            Cursor cursor = getContentResolver().query(data.getData(),
                    new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
            cursor.moveToFirst();
            String sName   = cursor.getString(0);
            String sNumber = cursor.getString(1);
            cursor.close();

            String tempstr = readFromContactFile();
            tempstr = tempstr.replace("]","");
            String addstr =  ",{'name':'" + sName + "','phonenumber':'" + sNumber + "','photo':'man1','value':0}]";
            tempstr = tempstr + addstr;
            writeToContactFile(tempstr);
            //Toast.makeText(getParent(), addstr, Toast.LENGTH_SHORT).show();
            Log.d("addstr", addstr);

            Snackbar.make(this.mViewPager, sName + " 님의 연락처가 추가되었습니다.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();



        }
        mViewPager.setCurrentItem(0);
        super.onActivityResult(requestCode, resultCode, data);
    }




    public void anim() {

        if (isFabOpen) {
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            isFabOpen = false;
        } else {
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            isFabOpen = true;
        }
    }

    ViewPager.SimpleOnPageChangeListener viewPagerListener = new ViewPager.SimpleOnPageChangeListener(){
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);

            // 뷰페이저 이동시 해당 탭으로 이동
            bar.setSelectedNavigationItem(position);
        }
    };


    ActionBar.TabListener tabListener = new ActionBar.TabListener() {
        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // 해당 탭에서 벚어났을때 처리
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            // 해당 탭을 선택시 처리
            // 해당 탭으로 뷰페이저도 이동
            mViewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // 해당 탭이 다시 선택됐을때 처리
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // Json 파일 테스트용 저장
            String str =
                    "[{'name':'Pak Jeong-Hun','phonenumber':'010-3659-1044','photo':'man1','value':0}," +
                            "{'name':'Won Jung-Eun','phonenumber':'033-8015-2264','photo':'woman3','value':1000}," +
                            "{'name':'Hong Byung-Hoon','phonenumber':'052-1624-1104','photo':'man3','value':-1000}," +
                            "{'name':'Sop Kyong-Su','phonenumber':'010-4728-5356','photo':'man2','value':0}," +
                            "{'name':'Chom Kang-Dae','phonenumber':'062-4032-6077','photo':'man2','value':0}," +
                            "{'name':'Chong Minjun','phonenumber':'051-7086-4133','photo':'man3','value':0}," +
                            "{'name':'Chu Song-Ho','phonenumber':'1588-7473','photo':'man1','value':0}," +
                            "{'name':'Chu Chi-Won','phonenumber':'02-2585-1613','photo':'man1','value':0}," +
                            "{'name':'Eoh Ji-Hoon','phonenumber':'02-1265-5822','photo':'man3','value':0}," +
                            "{'name':'An Ji-Won','phonenumber':'040-9425-5912','photo':'woman1','value':0}," +
                            "{'name':'Tan Hyun-Ju','phonenumber':'1588-3958','photo':'man1','value':0}," +
                            "{'name':'Hung Min-Yung','phonenumber':'047-3833-5090','photo':'woman2','value':0}," +
                            "{'name':'Ri Mi-Sook','phonenumber':'068-5790-5717','photo':'woman3','value':0}," +
                            "{'name':'Chegal Suk-Ja','phonenumber':'1588-9206','photo':'woman3','value':0}," +
                            "{'name':'Pong Yi','phonenumber':'1588-9545','photo':'man1','value':0}," +
                            "{'name':'Chung Yu-Ni','phonenumber':'1588-7875','photo':'woman1','value':0}," +
                            "{'name':'Hwan Kyong-Ja','phonenumber':'02-4179-7747','photo':'woman2','value':0}," +
                            "{'name':'Ogum Un-Ju','phonenumber':'010-4485-3333','photo':'woman1','value':0}," +
                            "{'name':'Mangjol Hyon-Ju','phonenumber':'053-8542-5040','photo':'woman3','value':0}," +
                            "{'name':'Mae Tae-Young','phonenumber':'010-9034-0169','photo':'man1','value':0}," +
                            "{'name':'Pom Ji-Hun','phonenumber':'1588-1277','photo':'man3','value':0}," +
                            "{'name':'Ki Chuwon','phonenumber':'02-8037-5149','photo':'man2','value':0}," +
                            "{'name':'Kun Min-Jun','phonenumber':'031-9784-3958','photo':'man3','value':0}," +
                            "{'name':'Chang Kang-Dae','phonenumber':'068-8434-2971','photo':'man2','value':0}," +
                            "{'name':'Kim Min-Su','phonenumber':'059-8651-7823','photo':'man1','value':0}," +
                            "{'name':'Pang Min-Kyu','phonenumber':'054-8456-3648','photo':'man1','value':0}," +
                            "{'name':'Chu Kyong-Su','phonenumber':'064-3639-9267','photo':'man3','value':0}," +
                            "{'name':'Nang Suk-Chul','phonenumber':'067-0436-4190','photo':'man2','value':0}," +
                            "{'name':'Ru Dong-Jun','phonenumber':'010-9296-0249','photo':'man3','value':0}," +
                            "{'name':'Sung Sang-Min','phonenumber':'042-3650-9642','photo':'man2','value':0}," +
                            "{'name':'Yun Myung-Hee','phonenumber':'052-4368-7394','photo':'woman1','value':0}," +
                            "{'name':'Min Se-Yeon','phonenumber':'02-4893-7244','photo':'woman2','value':0}," +
                            "{'name':'Pom Mi-Suk','phonenumber':'033-7327-2767','photo':'man3','value':0}," +
                            "{'name':'Yang Ja-Hyun','phonenumber':'054-9152-2803','photo':'woman2','value':0}," +
                            "{'name':'Pung Da-Hee','phonenumber':'010-3425-5002','photo':'woman3','value':0}," +
                            "{'name':'Kye Eun-Ah','phonenumber':'070-5128-4608','photo':'woman1','value':0}," +
                            "{'name':'Kun Sujin','phonenumber':'02-4429-7810','photo':'woman2','value':0}," +
                            "{'name':'Pae Ae','phonenumber':'070-8311-5537','photo':'man1','value':0}," +
                            "{'name':'Ra Hwi-Hyang','phonenumber':'1588-0132','photo':'man3','value':0}," +
                            "{'name':'Sip Eun-Bi','phonenumber':'1588-9463','photo':'woman3','value':0}," +
                            "{'name':'Ogum Yong-Gi','phonenumber':'046-0213-2011','photo':'man2','value':0}," +
                            "{'name':'Chang Seong-Hyeon','phonenumber':'032-9671-4492','photo':'man3','value':0}," +
                            "{'name':'Om Young-Soo','phonenumber':'036-3070-9286','photo':'man1','value':0}," +
                            "{'name':'Hwang Seung-Woo','phonenumber':'036-4224-7732','photo':'man3','value':0}," +
                            "{'name':'Tae Song-Ho','phonenumber':'066-0719-8085','photo':'man2','value':0}," +
                            "{'name':'Pyong Sang-Chul','phonenumber':'02-0421-3789','photo':'man2','value':0}," +
                            "{'name':'Ko Myung-Hee','phonenumber':'053-3212-4003','photo':'woman2','value':0}," +
                            "{'name':'Chegal Jung-Nam','phonenumber':'1588-9258','photo':'man1','value':0}," +
                            "{'name':'Nae Kwang-Jo','phonenumber':'061-9388-5237','photo':'man3','value':0}," +
                            "{'name':'Kwok Sunghyon','phonenumber':'031-3499-3751','photo':'man2','value':0}]";


            //JSON 파일 리셋
            writeToContactFile(str);

            Snackbar.make(this.mViewPager, "연락처가 초기화되었습니다.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return true;
        }

        if (id == R.id.action_developers) {
            Snackbar.make(this.mViewPager, "개발자: 배인환, 이현준", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void writeToContactFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput("contact.json", Context.MODE_PRIVATE));
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
            InputStream inputStream = this.openFileInput("contact.json");

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

}
