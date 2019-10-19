package e.shery.visiospark.activities;

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
    private String[] name;
    private String[] fee;
    public listview(AppCompatActivity context, String[] name, String[] fee)
    {
        super(context,listview3,name);
        this.name=name;
        this.fee=fee;
        this.context=context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater=context.getLayoutInflater();
        View view=inflater.inflate(listview3,null,true);

        TextView t=view.findViewById(R.id.listText3_1);
        TextView v=view.findViewById(R.id.listText3_2);
        t.setText(name[position]);
        v.setText(fee[position]);
        return view;
    }
}
