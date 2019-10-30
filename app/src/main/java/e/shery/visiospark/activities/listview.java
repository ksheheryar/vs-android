package e.shery.visiospark.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import e.shery.visiospark.R;

import static e.shery.visiospark.R.layout.listview3;

public class listview extends ArrayAdapter {

    private AppCompatActivity context;
    private ArrayList name;
    private ArrayList fee;
    public listview(AppCompatActivity context, ArrayList name, ArrayList fee)
    {
        super(context,listview3,name);
        this.name=name;
        this.fee=fee;
        this.context=context;
    }

    public Object getName(int position) {
        // Get header position
        return this.name.get(position);
    }

    public Object getfee(int position) {
        // Get header position
        return this.fee.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        String uniName = (String) getName(position);
        String payment = (String) getfee(position);

        if (convertView == null){
            LayoutInflater inflater=context.getLayoutInflater();
            convertView=inflater.inflate(listview3,parent,false);
        }

        if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.parseColor("#b6c0c1"));
        }

        TextView t=convertView.findViewById(R.id.listText3_1);
        TextView v=convertView.findViewById(R.id.listText3_2);
        t.setText(uniName);
        v.setText(payment);
        return convertView;
    }
}
