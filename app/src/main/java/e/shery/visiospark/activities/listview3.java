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
import static e.shery.visiospark.R.layout.listview5;
import static e.shery.visiospark.R.layout.listview6;

public class listview3 extends ArrayAdapter {

    private AppCompatActivity context;
    private ArrayList name;
    private ArrayList member;
    private ArrayList check;
    public listview3(AppCompatActivity context, ArrayList name, ArrayList member,ArrayList check)
    {
        super(context,listview3,name);
        this.name=name;
        this.member=member;
        this.check=check;
        this.context=context;
    }

    public Object getName(int position) {
        // Get header position
        return this.name.get(position);
    }

    public Object getmember(int position) {
        // Get header position
        return this.member.get(position);
    }

    public Object getcheck(int position) {
        // Get header position
        return this.check.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        String uniName = (String) getName(position);
        String payment = (String) getmember(position);
        int c = (int) getcheck(position);

        if (convertView == null){
            LayoutInflater inflater=context.getLayoutInflater();
            convertView=inflater.inflate(listview6,parent,false);
        }

        if (c == 0) {
            convertView.setBackgroundColor(Color.parseColor("#e79396"));
        }
        else {
            convertView.setBackgroundColor(Color.parseColor("#a8ea9c"));
        }

        TextView t=convertView.findViewById(R.id.listText6_1);
        TextView v=convertView.findViewById(R.id.listText6_2);
        t.setText(uniName);
        v.setText(payment);
        return convertView;
    }
}
