package com.example.tacademy.sampledragdrop;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {//implements View.OnLongClickListener {

    ListView listView;
    ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(mAdapter);

        TextView tv = (TextView) findViewById(R.id.text_item1);
        tv.setOnLongClickListener(mDrag);

        findViewById(R.id.text_item2).setOnLongClickListener(mDrag);
        findViewById(R.id.text_item3).setOnLongClickListener(mDrag);

        listView.setOnDragListener(new View.OnDragListener() {
            Drawable old;
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        View source = (View)event.getLocalState();
                        source.setVisibility(View.INVISIBLE);
                        return true;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        old = v.getBackground();
                        v.setBackgroundColor(Color.GREEN);
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED:
                        v.setBackgroundDrawable(old);
                        return true;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        break;
                    case DragEvent.ACTION_DROP:
                        int x = (int)event.getX();
                        int y = (int)event.getY();
                        ClipData data = event.getClipData();
                        String text = data.getItemAt(0).getText().toString();
                        int position = listView.pointToPosition(x, y);
                        if (position != ListView.INVALID_POSITION) {
                            mAdapter.insert(text, position);
                        } else {
                            mAdapter.add(text);
                        }
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:
                        View source2 = (View)event.getLocalState();
                        source2.setVisibility(View.VISIBLE);
                        return true;

                }
                return false;
            }
        });
    }
    View.OnLongClickListener mDrag =new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            String text = (String) v.getTag();
            ClipData.Item item = new ClipData.Item(text);
            ClipData data = new ClipData(text, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
            View.DragShadowBuilder builder = new View.DragShadowBuilder(v);
            v.startDrag(data, builder, v, 0);
            return true;
        }
    };

}