package com.example.magicmirror;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.magicmirror.adapter.FirmwareInfoAdapter;
import com.example.magicmirror.bean.FirmWare;
import com.example.magicmirror.bean.FirmWareInfo;
import com.example.magicmirror.internet.GitHubService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirmwareInfoActivity extends AppCompatActivity {
    private String url="http://panhe-tech.cn/index.php/Home/Android/";
    private ListView lv_firmware;
    private List<FirmWare> firmwares = new ArrayList<>();
    private GitHubService service;
    private FirmwareInfoAdapter firmwareInfoAdapter;
    private String path;
    private int i;

    // 创建一个复杂更新进度的Handler
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0://表示没有固件
                    firmwareInfoAdapter.clear();
                    AlertDialog.Builder builder1=new AlertDialog.Builder(FirmwareInfoActivity.this).setTitle("无固件信息");
                    builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder1.create().show();
                    break;
                case 1:
                    AlertDialog.Builder builder2=new AlertDialog.Builder(FirmwareInfoActivity.this).setTitle("请求失败").setMessage("请检查网络，服务器访问失败");
                    builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder2.create().show();
                    break;

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firmware_info);
        lv_firmware = (ListView)findViewById(R.id.lv_firmwares);
        firmwareInfoAdapter = new FirmwareInfoAdapter(this);
        lv_firmware.setAdapter(firmwareInfoAdapter);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(GitHubService.class);
        lv_firmware.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FirmWare firmWare = firmwareInfoAdapter.getFirmware(position);
                if (firmWare == null) return;
                Intent intent = new Intent(FirmwareInfoActivity.this, UpdateActivity.class);
//                intent.putExtra("版本号",firmWare.getVersion_number());
                startActivity(intent);
                finish();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("访问数据");
        //                异步执行
        service.AndroidFirmWareInfoGet("1").enqueue(new Callback<FirmWareInfo>() {

           @Override
            public void onResponse(Call<FirmWareInfo> call, retrofit2.Response<FirmWareInfo> response){
                FirmWareInfo data=response.body();
               System.out.println("访问数据1");
                System.out.println(data+"数据："+data.getStatus());
                Message msg = new Message();
                switch (data.getStatus()){
                    case 0://表示没有固件信息
                        msg.what = 0;
                        handler.sendMessage(msg);
                        break;
                    case 1://表示返回成功
                        firmwareInfoAdapter.clear();
                        ArrayList<FirmWare> firmWaresInfo=data.getData();
                        System.out.println("大小："+firmWaresInfo.size());
                        for(i=0;i<firmWaresInfo.size();i++){
                            System.out.println("版本："+firmWaresInfo.get(i).getVersion_number()+" "+firmWaresInfo.get(i).getVersion_name());
                            firmwareInfoAdapter.addFirm(firmWaresInfo.get(i));
                            firmwareInfoAdapter.notifyDataSetChanged();
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Call<FirmWareInfo> call, Throwable t) {
                System.out.println("访问失败"+t.getMessage());
                Message msg1 = new Message();
                msg1.what = 1;
                handler.sendMessage(msg1);
            }

        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(FirmwareInfoActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }
        return super.onKeyDown(keyCode, event);
    }

}
