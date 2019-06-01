package com.MP2019.NDND;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.MP2019.NDND.circle.CircleMainActivity;
import com.MP2019.NDND.circleList.CircleInfoForDB;
import com.MP2019.NDND.circleList.CircleRegisterFormActivity;
import com.MP2019.NDND.circleList.MemberInfoForDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class CircleLIst_NavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ListView lv_searchResult, lv_myCircleList;
    ImageButton bu_createCircle, bu_search;
    ArrayAdapter<CircleInfoForDB> aad_searchResult;
    ArrayAdapter<CircleInfoForDB> aad_myCircleList;
    EditText et_searchCircle;
    FirebaseUser user;
    TextView TVuserName,TVuserEmail;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference circleRef = mRootRef.child("circle");
    private DatabaseReference userRef = mRootRef.child("user");
    static ArrayList<CircleInfoForDB> arrayDataforsearch = new ArrayList<>();
    static ArrayList<CircleInfoForDB> arrayDataForMyCircleList = new ArrayList<>();
    static ArrayList<String> arrayIndex = new ArrayList<>();
    private FirebaseAuth firebaseAuth;
    String circleName;
    String userUid_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_list__navigation_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        lv_searchResult = findViewById(R.id.LV_searchResult);
        lv_myCircleList = findViewById(R.id.LV_myCircleList);
        bu_search = findViewById(R.id.IB_searchCircle);
        bu_createCircle = findViewById(R.id.IB_createCircle);
        et_searchCircle = findViewById(R.id.ET_searchCircle);
        firebaseAuth = FirebaseAuth.getInstance();

        aad_myCircleList = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        lv_myCircleList.setAdapter(aad_myCircleList);
        //해당 동아리 메뉴 페이지로 이동
        lv_myCircleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CircleLIst_NavigationDrawer.this, "동아리 메인 페이지로 이동합니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), CircleMainActivity.class);

                String circleName=parent.getItemAtPosition(position).toString();
                //동아리 이름 넘김
                intent.putExtra("circleName",circleName);
                // 회원 id 넘김
                intent.putExtra("userID",user.getUid());
                startActivity(intent);
            }
        });

        aad_searchResult = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        lv_searchResult.setAdapter(aad_searchResult);

        lv_searchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                circleName=parent.getItemAtPosition(position).toString();

                AlertDialog.Builder ad = new AlertDialog.Builder(CircleLIst_NavigationDrawer.this);

                ad.setTitle(circleName);// 제목 설정
                ad.setMessage("가입 하시겠습니까?");// 내용 설정

                // 확인 버튼 설정
                ad.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Log.v(TAG,"Yes Btn Click");
                        dialog.dismiss();     //닫기
                        Toast.makeText(CircleLIst_NavigationDrawer.this,circleName+"\n가입요청 승인이 날 때까지 기다려주세요:) ", Toast.LENGTH_LONG).show();
                        FirebaseUser user=firebaseAuth.getCurrentUser();
                        MemberInfoForDB memberInfoForDB= new MemberInfoForDB("requestor",new Date().toString());
                        circleRef.child(circleName+"/request").child(user.getUid()).setValue(memberInfoForDB);
                    }
                });

                // 취소 버튼 설정
                ad.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  Log.v(TAG,"No Btn Click");
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });

                // 창 띄우기
                ad.show();
            }
        });

        bu_createCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CircleRegisterFormActivity.class);
                startActivity(intent);
            }
        });

        bu_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFirebaseDatabase(et_searchCircle.getText().toString());
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.circle_list__navigation_drawer, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

         if (id == R.id.nav_share) {
            AlertDialog.Builder ad = new AlertDialog.Builder(CircleLIst_NavigationDrawer.this);

            ad.setTitle("로그아웃");       // 제목 설정
            ad.setMessage(" 로그아웃 하시겠습니까?");   // 내용 설정

            // 확인 버튼 설정
            ad.setPositiveButton("네", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {

                    dialog.dismiss();     //닫기
                    Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_LONG).show();

                    firebaseAuth.signOut();
                    Intent intent = new Intent(getApplicationContext(),com.MP2019.NDND.login.MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    //finish();
                    // Event
                }
            });


            // 취소 버튼 설정
            ad.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //  Log.v(TAG,"No Btn Click");
                    dialog.dismiss();     //닫기
                    // Event
                }
            });

            // 창 띄우기
            ad.show();

        } else if (id == R.id.nav_send) {
            FirebaseUser user=firebaseAuth.getCurrentUser();

            AlertDialog.Builder ad = new AlertDialog.Builder(CircleLIst_NavigationDrawer.this);

            ad.setTitle("너동나동 어플 탈퇴");       // 제목 설정
            ad.setMessage(" 너동나동 어플을 탈퇴 하시겠습니까?");   // 내용 설정

            // 확인 버튼 설정
            ad.setPositiveButton("네", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();     //닫기
                    Toast.makeText(getApplicationContext(), "그 동안 저희 어플을 이용해 주셔서 감사합니다.", Toast.LENGTH_LONG).show();

                    //firebaseAuth.signOut();
                    user.delete();
                    Toast.makeText(getApplicationContext(), "계정이 삭제되었습니다.", Toast.LENGTH_LONG).show();

                    finish();
                    // Event
                }
            });


            // 취소 버튼 설정
            ad.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //  Log.v(TAG,"No Btn Click");
                    dialog.dismiss();     //닫기
                    // Event
                }
            });

            // 창 띄우기
            ad.show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("circlefinder", "start");
        user = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference().child("circle").orderByChild("member/" + user.getUid()).startAt("autority");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            CircleInfoForDB get;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayDataForMyCircleList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.d("circlefinder", "mycircleList" + postSnapshot.child("info").getValue().toString());
                    String key = postSnapshot.getKey();
                    if (!postSnapshot.child("member/" + user.getUid()).child("autority").getValue().toString().equals("secession")) {
                        CircleInfoForDB get = postSnapshot.child("info").getValue(CircleInfoForDB.class);
                        arrayDataForMyCircleList.add(get);
                        arrayIndex.add(key);
                    }
                }

                aad_myCircleList.clear();
                aad_myCircleList.addAll(arrayDataForMyCircleList);
                aad_myCircleList.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void getFirebaseDatabase(final String str){
        FirebaseDatabase.getInstance().getReference().child("circle").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayDataforsearch.clear();
                arrayIndex.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Log.d("circlefinder",postSnapshot.toString());
                    String key = postSnapshot.getKey();
                    if(key.contains(str)){
                        CircleInfoForDB get = postSnapshot.child("info").getValue(CircleInfoForDB.class);
                        arrayDataforsearch.add(get);
                        arrayIndex.add(key);
                    }
                }
                aad_searchResult.clear();
                aad_searchResult.addAll(arrayDataforsearch);
                aad_searchResult.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}
