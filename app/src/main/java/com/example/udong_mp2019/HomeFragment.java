package com.example.udong_mp2019;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.atomic.AtomicReference;

public class HomeFragment extends Fragment {

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference userRef = mRootRef.child("user");

    TextView TV_hello;
    TextView TV_circlemain;
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        AtomicReference<String> circle= new AtomicReference<>("");
        AtomicReference<String> user= new AtomicReference<>("");

        TV_hello=(TextView)v.findViewById(R.id.tv_hello);
        TV_circlemain=(TextView) v.findViewById(R.id.tv_circlemain);

        TV_hello.setText(user+" 님 안녕하세요!");
        TV_circlemain.setText(circle+"동아리 페이지에 자주 놀러오세요~");

        Bundle bundle=getArguments();

        if(bundle!=null){
            circle.set(bundle.getString("circleName"));
            user.set(bundle.getString("userID"));

            String name=userRef.child("name/"+user).toString();

            TV_hello.setText(name+" 님 안녕하세요!");
            TV_circlemain.setText(circle+"동아리 페이지에 자주 놀러오세요~");

        }
        return v;
    }
}
