package com.example.record_trail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class MyPageFragment extends Fragment {

    View v;
    Button bookmarkBtn;
    Button collectingPostsBtn;
    Button goalBtn;
    Button feedbackBtn;
    Intent intent;
    ImageView imagev;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        v = inflater.inflate(R.layout.fragment_mypage, container, false);

        imagev = v.findViewById(R.id.imageView);
        imagev.setImageResource(R.drawable.logo);

        // 즐겨찾기
        bookmarkBtn = v.findViewById(R.id.btn_bookmark);
        bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getActivity(), BookmarkActivity.class);
                startActivity(intent);
            }
        });

        // 내가 작성한 글 모아보기
        collectingPostsBtn = v.findViewById(R.id.btn_collecting_posts);
        collectingPostsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getActivity(), ReviewActivity.class);
                startActivity(intent);
            }
        });

        // 목표 설정
        goalBtn = v.findViewById(R.id.btn_goal);
        goalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getActivity(), GoalActivity.class);
                startActivity(intent);
            }
        });

        // 피드백 작성
        feedbackBtn = v.findViewById(R.id.btn_feedback);
        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("피드백을 보내주시겠습니까?")
                        .setMessage("여러분의 피드백은 추후에 레코드 트레일 기능 구현에 큰 힘이 됩니다!")
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent email_intent = new Intent();
                                email_intent.setAction(Intent.ACTION_SEND);
                                email_intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"recordtrail@gmail.com"}); // 받는 사람 이메일
                                email_intent.putExtra(Intent.EXTRA_SUBJECT, "[Record Trail] 피드백 사항"); // 메일 제목
                                email_intent.setType("message/rfc822");
                                startActivity(Intent.createChooser(email_intent, "send email.."));
                            }
                        })
                        .show();
            }
        });

        return v;
    }
}