package app.bxvip.com.myview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

import app.bxvip.com.myview.R;

public class MyListViewAadapter extends BaseAdapter {
    private List<String>  names;
    private Context context;

    public MyListViewAadapter(List<String> names, Context context) {
        this.names = names;
        this.context = context;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder viewHolder;
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item,null);
            viewHolder = new MyViewHolder();
            viewHolder.textView = convertView.findViewById(R.id.name);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (MyViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(names.get(position));
        return convertView;
    }
   public static class MyViewHolder{
        TextView textView;
   }
}
