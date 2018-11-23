package com.example.magicmirror.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.magicmirror.R;
import com.example.magicmirror.bean.FirmWare;


import java.util.ArrayList;

public class FirmwareInfoAdapter extends BaseAdapter {
    private ArrayList<FirmWare> firmwares;
    private LayoutInflater mInflator;
    private Context context;

    public FirmwareInfoAdapter(Context context){
        super();
        firmwares = new ArrayList<FirmWare>();
        this.context = context;
       mInflator = LayoutInflater.from(context);
    }

    public void addFirm(FirmWare firmWare) {
        if(!firmwares.contains(firmWare)) {
            firmwares.add(firmWare);
        }
    }

    public FirmWare getFirmware(int position) {
        return firmwares.get(position);
    }

    public void clear() {
        firmwares.clear();
    }

    @Override
    public int getCount() {
        return firmwares.size();
    }

    @Override
    public Object getItem(int position) { return firmwares.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        System.out.println("General ListView optimization code.");
        if (convertView == null) {
            if (mInflator == null) {
                System.out.println("mInflator为空");
            }
            convertView = mInflator.inflate(R.layout.item_firmware_info,null);
            viewHolder = new ViewHolder();
            viewHolder.it_name = (TextView) convertView.findViewById(R.id.it_name);
            viewHolder.it_version = (TextView) convertView.findViewById(R.id.it_version);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        FirmWare firmWare = firmwares.get(position);
        String firmversion = firmWare.getVersion_number();//版本号
        String firmname = firmWare.getVersion_name();//版本名称
        if (firmversion != null && firmversion.length() > 0)
            viewHolder.it_version.setText(firmversion);
        if (firmname != null && firmname.length() > 0)
            viewHolder.it_name.setText(firmname);
        return convertView;
    }

    /**
     * 设置Listview的高度
     */
    public void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if(listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    class ViewHolder {
        //
        TextView it_version;
        TextView it_name;
    }
}
