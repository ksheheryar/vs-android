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
import static e.shery.visiospark.R.layout.listview4;

public class listview1 extends ArrayAdapter {

    private AppCompatActivity context;
    private ArrayList name;
    private ArrayList teams;
    private ArrayList fee;
    public listview1(AppCompatActivity context, ArrayList name,ArrayList teams, ArrayList fee)
    {
        super(context,listview3,name);
        this.name = name;
        this.teams = teams;
        this.fee = fee;
        this.context = context;
    }

    public Object getName(int position) {
        return this.name.get(position);
    }

    public Object getTeams(int position) {
        return this.teams.get(position);
    }

    public Object getfee(int position) {
        return this.fee.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        String uniName = (String) getName(position);
        String team = (String) getTeams(position);
        String payment = (String) getfee(position);

        if (convertView == null){
            LayoutInflater inflater=context.getLayoutInflater();
            convertView=inflater.inflate(listview4,parent,false);
        }

        if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.parseColor("#b6c0c1"));
        }

        TextView t = convertView.findViewById(R.id.listText4_1);
        TextView s = convertView.findViewById(R.id.listText4_2);
        TextView v = convertView.findViewById(R.id.listText4_3);
        t.setText(uniName);
        s.setText(team);
        v.setText(payment);
        return convertView;
    }
}
