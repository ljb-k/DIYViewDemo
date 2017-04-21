package com.example.diyviewdemo;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FrameLayout frameLayout;
    private Button btn_undo;
    private Button btn_redo;
    private Button btn_recover;
    private TuyaView tuyaView;//自定义涂鸦板
    private Button btn_paintcolor;
    private Button btn_paintsize;
    private Button btn_paintstyle;
    private SeekBar sb_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListener();


    }

    private void initListener() {
        btn_undo.setOnClickListener(this);
        btn_redo.setOnClickListener(this);
        btn_recover.setOnClickListener(this);
        btn_paintcolor.setOnClickListener(this);
        btn_paintsize.setOnClickListener(this);
        btn_paintstyle.setOnClickListener(this);
        sb_size.setOnSeekBarChangeListener(new MySeekChangeListener());


    }

    private void initData() {
        //虽然此时获取的是屏幕宽高，但是我们可以通过控制framlayout来实现控制涂鸦板大小
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        int screenWidth = defaultDisplay.getWidth();
        int screenHeight = defaultDisplay.getHeight();
        tuyaView = new TuyaView(this, screenWidth, screenHeight);
        frameLayout.addView(tuyaView);
        tuyaView.requestFocus();
        tuyaView.selectPaintSize(sb_size.getProgress());

    }

    private void initView() {
        frameLayout = (FrameLayout) findViewById(R.id.fl_boardcontainer);
        btn_undo = (Button) findViewById(R.id.btn_last);
        btn_redo = (Button) findViewById(R.id.btn_redo);
        btn_recover = (Button) findViewById(R.id.btn_recover);
        btn_paintcolor = (Button) findViewById(R.id.btn_paintcolor);
        btn_paintsize = (Button) findViewById(R.id.btn_paintsize);
        btn_paintstyle = (Button) findViewById(R.id.btn_paintstyle);
        sb_size = (SeekBar) findViewById(R.id.sb_size);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_last://撤销
                tuyaView.undo();
                break;
            case R.id.btn_redo://重做
                tuyaView.redo();
                break;
            case R.id.btn_recover://恢
                tuyaView.recover();
                break;
            case R.id.btn_paintcolor:
                sb_size.setVisibility(View.GONE);
                showPaintColorDialog(v);
                break;
            case R.id.btn_paintsize:
                sb_size.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_paintstyle:
                sb_size.setVisibility(View.GONE);
                showMoreDialog(v);
                break;


        }


    }



    class MySeekChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            tuyaView.selectPaintSize(seekBar.getProgress());
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            tuyaView.selectPaintSize(seekBar.getProgress());
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    private int select_paint_color_index = 0;
    private int select_paint_style_index = 0;


    private void showMoreDialog(View v) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("选择画笔或橡皮擦：");
        alertDialogBuilder.setSingleChoiceItems(R.array.paintstyle, select_paint_style_index, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                select_paint_style_index = which;
                tuyaView.selectPaintStyle(which);
                dialog.dismiss();
                //Paint.Style.
            }
        });
        alertDialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.create().show();


    }

    private void showPaintColorDialog(View v) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("选择画笔颜色：");
        alertDialogBuilder.setSingleChoiceItems(R.array.paintcolor, select_paint_color_index, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                select_paint_color_index = which;
                tuyaView.selectPaintColor(which);
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.create().show();


    }
}
