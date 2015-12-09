package com.cloud;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.cloud.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_dragview)
public class DragViewActivity extends Activity {

    @ViewById
    VideoView viewHeader;
    @ViewById
    Button btn;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupListView();
    }

    @Click
    void btn(){
        MediaController mController = new MediaController(this);
        viewHeader.setMediaController(mController);
        viewHeader.setVideoURI(Uri.parse("android.resource://com.cloud/"+R.raw.video));
        viewHeader.start();
    }

    private void setupListView() {
        mListView = (ListView) findViewById(R.id.listView);
        SampleAdapter adapter = new SampleAdapter(this);
        for (int i = 0; i < 20; i++) {
            adapter.add(new SampleItem("Cloud", android.R.drawable.star_on));
        }
        mListView.setAdapter(adapter);
    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, DragViewActivity_.class);
        context.startActivity(intent);
    }

    private class SampleItem {
        public String tag;
        public int iconRes;
        public SampleItem(String tag, int iconRes) {
            this.tag = tag;
            this.iconRes = iconRes;
        }
    }

    public class SampleAdapter extends ArrayAdapter<SampleItem> {

        public SampleAdapter(Context context) {
            super(context, 0);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_simple, null);
            }
            ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
//            icon.setImageResource(getItem(position).iconRes);
            icon.setImageResource(R.mipmap.cloud);
            TextView title = (TextView) convertView.findViewById(R.id.row_title);
            title.setText(getItem(position).tag);

            return convertView;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, MainActivity_.class);
            startActivity(intent);
            this.finish();
        }
        return false;
    }
}
