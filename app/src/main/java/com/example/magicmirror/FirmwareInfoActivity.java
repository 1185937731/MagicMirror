package com.example.magicmirror;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.magicmirror.adapter.FirmwareInfoAdapter;
import com.example.magicmirror.bean.FirmWare;
import com.example.magicmirror.bean.FirmWareInfo;
import com.example.magicmirror.internet.GitHubService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
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

                System.out.println("硬件地址："+firmWare.getPathAdd());
                AlertDialog.Builder isExit=new AlertDialog.Builder( FirmwareInfoActivity.this);
                //设置对话框标题
                isExit.setTitle("下载硬件");
                //设置对话框消息
                isExit.setMessage("你确定要下载吗？");
                // 添加选择按钮并注册监听
                isExit.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

//                        Toast.makeText(FirmwareInfoActivity.this,"正在下载",Toast.LENGTH_SHORT).show();
                        download(firmWare.getPathAdd());
                    }
                });
                isExit.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                //对话框显示
                isExit.show();

            }
        });
    }


    //通过POST
/*
    protected void download(String address) {

        //                异步执行
        ProgressDialog progressDialog = new ProgressDialog(FirmwareInfoActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("正在下载");
        progressDialog.setMessage("请稍后...");
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();
        progressDialog.setCancelable(false);
        Call<ResponseBody> call = service.AndroidGetFirm(address);
        call.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response){
                System.out.println("下载函数3");
                if (response.isSuccessful()) {

                    System.out.println("长度"+response.body().contentLength());
                    System.out.println( "server contacted and has file");

                    writeResponseBodyToDisk(response.body(),new OnDownloadListener() {
                        @Override
                        public void onDownloadSuccess(File file) {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            //下载完成进行相关逻辑操作
                            Toast.makeText(FirmwareInfoActivity.this,"下载成功",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(FirmwareInfoActivity.this,UpdateActivity.class);
                            startActivity(intent);
                            finish();

                        }

                        @Override
                        public void onDownloading(int progress) {
                            progressDialog.setProgress(progress);
                        }

                        @Override
                        public void onDownloadFailed(Exception e) {
                            //下载异常进行相关提示操作
                            Toast.makeText(FirmwareInfoActivity.this,"下载失败",Toast.LENGTH_SHORT).show();
                        }

                    });

//                    boolean writtenToDisk = writeResponseBodyToDisk.body();
//                    System.out.println("file download was a success? " + writtenToDisk);
//                    if(writtenToDisk==true){
//                        Toast.makeText(FirmwareInfoActivity.this,"下载成功",Toast.LENGTH_SHORT).show();
//                        Intent intent=new Intent(FirmwareInfoActivity.this,UpdateActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
                } else {
                    System.out.println("server contact failed");
                    Toast.makeText(FirmwareInfoActivity.this,"下载失败",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("error");
            }

        });

    }
*/
//通过Get
//    public void download(String address){
//        ProgressDialog progressDialog = new ProgressDialog(FirmwareInfoActivity.this);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        progressDialog.setTitle("正在下载");
//        progressDialog.setMessage("请稍后...");
//        progressDialog.setProgress(0);
//        progressDialog.setMax(100);
//        progressDialog.show();
//        progressDialog.setCancelable(true);
//        System.out.println("下载函数2");
//        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
//        GitHubService Fileservice = retrofit.create(GitHubService.class);
//        Call<ResponseBody> call = Fileservice.AndroidgetFirm(address);
//        call.enqueue(new Callback<ResponseBody>(){
//            @Override
//            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response){
//                System.out.println("下载函数3");
//                if (response.isSuccessful()) {
//                    System.out.println( "server contacted and has file");
//                    writeResponseBodyToDisk(response.body(),new OnDownloadListener() {
//                        @Override
//                        public void onDownloadSuccess(File file) {
//                            if (progressDialog != null && progressDialog.isShowing()) {
//                                progressDialog.dismiss();
//                            }
//                            //下载完成进行相关逻辑操作
//                            Toast.makeText(FirmwareInfoActivity.this,"下载成功",Toast.LENGTH_SHORT).show();
//                            Intent intent=new Intent(FirmwareInfoActivity.this,UpdateActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//
//                        @Override
//                        public void onDownloading(int progress) {
//                            progressDialog.setProgress(progress);
//                        }
//
//                        @Override
//                        public void onDownloadFailed(Exception e) {
//                            //下载异常进行相关提示操作
//                            Toast.makeText(FirmwareInfoActivity.this,"下载失败",Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                } else {
//                    System.out.println("server contact failed");
//                    progressDialog.dismiss();
//                }
//            }
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                System.out.println("error");
//            }
//        });}

    public void download(String address){
        ProgressDialog progressDialog = new ProgressDialog(FirmwareInfoActivity.this);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("正在下载");
        progressDialog.setMessage("loading...");
        progressDialog.setCancelable(true);
        progressDialog.show();
        System.out.println("下载函数2");
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        GitHubService Fileservice = retrofit.create(GitHubService.class);
        Call<ResponseBody> call = Fileservice.AndroidgetFirm(address);
        call.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response){
                System.out.println("下载函数3");
                if (response.isSuccessful()) {
                    System.out.println("长度"+response.body().contentLength());
                    System.out.println( "server contacted and has file");
                    boolean writtenToDisk = writeResponseBodyToDisk(response.body());
                    System.out.println("file download was a success? " + writtenToDisk);
                    if(writtenToDisk){
                        progressDialog.dismiss();
                        Toast.makeText(FirmwareInfoActivity.this,"下载成功",Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(FirmwareInfoActivity.this,UpdateActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    progressDialog.dismiss();
                    System.out.println("server contact failed");
                Toast.makeText(FirmwareInfoActivity.this,"下载失败",Toast.LENGTH_SHORT).show();
            }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("error");
            }
        });}

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        System.out.println("初始长度："+body.byteStream());
        try {

            /* todo change the file location/name according to your needs */
//            String dirName = Environment.getExternalStorageDirectory() + "/";
            String dirName = "/sdcard/";
//            File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator + "full.jpg");
            File futureStudioFile = new File(dirName);
            //不存在创建
            if (!futureStudioFile.exists()) {
                futureStudioFile.mkdir();
            }
            //下载后的文件名
            String fileName = dirName + "full_img.fex";
            System.out.println("初始长度1："+fileName);
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(fileName);
                System.out.println("FILE："+fileSize);
                while (true) {
                    int read = inputStream.read(fileReader);
                    System.out.println("FILE1");

                    if (read == -1) {
                        System.out.println("FILE2");
                        break;
                    }
                    System.out.println("FILE1: "+read);
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;

//                    int progress=(int)(fileSizeDownloaded * 1.0f / fileSize * 100);


                    System.out.println("FILE3");
                    System.out.println("file download: " + fileSizeDownloaded + " of " + fileSize);

                    // 下载中更新进度条
//                    listener.onDownloading(progress);

                    Log.d("TAG", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                outputStream.flush();
//                listener.onDownloadSuccess(futureStudioFile);
                return true;
            } catch (IOException e) {
                System.out.println("初始长度1：FALSE");
                return false;
            } finally {
                if (inputStream != null) {
                    System.out.println("初始长度1：inputStream != null");
                    inputStream.close();
                }

                if (outputStream != null) {
                    System.out.println("初始长度1：outputStream != null");
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            System.out.println("FALSE");
            return false;
        }
    }

//    private boolean writeResponseBodyToDisk(ResponseBody body,final OnDownloadListener listener) {
//        System.out.println("初始长度："+body.byteStream());
//        try {
//
//            /* todo change the file location/name according to your needs */
////            String dirName = Environment.getExternalStorageDirectory() + "/";
//            String dirName = "/sdcard/";
////            File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator + "full.jpg");
//            File futureStudioFile = new File(dirName);
//            //不存在创建
//            if (!futureStudioFile.exists()) {
//                futureStudioFile.mkdir();
//            }
//            //下载后的文件名
//            String fileName = dirName + "full_img.fex";
//            System.out.println("初始长度1："+fileName);
//            InputStream inputStream = null;
//            OutputStream outputStream = null;
//
//            try {
//                byte[] fileReader = new byte[4096];
//                long fileSize = body.contentLength();
//                long fileSizeDownloaded = 0;
//                inputStream = body.byteStream();
//                outputStream = new FileOutputStream(fileName);
//                System.out.println("FILE："+fileSize);
//                while (true) {
//                    int read = inputStream.read(fileReader);
//                    System.out.println("FILE1");
//
//                    if (read == -1) {
//                        System.out.println("FILE2");
//                        break;
//                    }
//                    System.out.println("FILE1: "+read);
//                    outputStream.write(fileReader, 0, read);
//                    fileSizeDownloaded += read;
//
//                    int progress=(int)(fileSizeDownloaded * 1.0f / fileSize * 100);
//
//
//                    System.out.println("FILE3");
//                    System.out.println("file download: " + fileSizeDownloaded + " of " + fileSize);
//
//                    // 下载中更新进度条
//                    listener.onDownloading(progress);
//
//                    Log.d("TAG", "file download: " + fileSizeDownloaded + " of " + fileSize);
//                }
//                outputStream.flush();
//                listener.onDownloadSuccess(futureStudioFile);
//                return true;
//            } catch (IOException e) {
//                System.out.println("初始长度1：FALSE");
//                return false;
//            } finally {
//                if (inputStream != null) {
//                    System.out.println("初始长度1：inputStream != null");
//                    inputStream.close();
//                }
//
//                if (outputStream != null) {
//                    System.out.println("初始长度1：outputStream != null");
//                    outputStream.close();
//                }
//            }
//        } catch (IOException e) {
//            System.out.println("FALSE");
//            return false;
//        }
//    }

//    public interface OnDownloadListener {
//        /**
//         * @param file 下载成功后的文件
//         */
//        void onDownloadSuccess(File file);
//
//        /**
//         * @param progress 下载进度
//         */
//        void onDownloading(int progress);
//
//        /**
//         * @param e 下载异常信息
//         */
//        void onDownloadFailed(Exception e);
//    }



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
                        for(int i=0;i<firmWaresInfo.size();i++){
                            System.out.println("版本："+firmWaresInfo.get(i).getVersion_number()+" "+firmWaresInfo.get(i).getVersion_name()+" "+firmWaresInfo.get(i).getPathAdd());
//                            pathget=firmWaresInfo.get(i).getPathAdd()+" ";
//                            System.out.println(pathget);
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
