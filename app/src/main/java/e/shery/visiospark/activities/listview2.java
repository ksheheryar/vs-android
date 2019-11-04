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
import static e.shery.visiospark.R.layout.listview5;

public class listview2 extends ArrayAdapter {

    private AppCompatActivity context;
    private ArrayList eventName;
    private ArrayList university;
    private ArrayList position;
    private ArrayList teams;
    public listview2(AppCompatActivity context, ArrayList eventName,ArrayList position, ArrayList university,ArrayList teams)
    {
        super(context,listview3,eventName);
        this.eventName = eventName;
        this.position = position;
        this.university = university;
        this.teams = teams;
        this.context = context;
    }

    public Object geteventName(int position) {
        return this.eventName.get(position);
    }

    public Object getTeams(int position) {
        return this.teams.get(position);
    }

    public Object getUniversity(int position) {
        return this.university.get(position);
    }

    public Object getPosition(int position) {
        return this.position.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        String event = (String) geteventName(position);
        String postion = (String) getPosition(position);
        String uniName = (String ) getUniversity(position);
        String team = (String ) getTeams(position);

        if (convertView == null){
            LayoutInflater inflater=context.getLayoutInflater();
            convertView=inflater.inflate(listview5,parent,false);
        }

        if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.parseColor("#b6c0c1"));
        }

        TextView e = convertView.findViewById(R.id.listText5_1);
        TextView p = convertView.findViewById(R.id.listText5_2);
        TextView n = convertView.findViewById(R.id.listText5_3);
        TextView t = convertView.findViewById(R.id.listText5_4);
        e.setText(event);
        p.setText(postion);
        n.setText(uniName);
        t.setText(team);
        return convertView;
    }
}
