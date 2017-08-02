package io.github.alphacalculus.alphacalculus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class ChapterListAdapter extends ArrayAdapter<ChapterItem> {

    private int resourceId;

    public ChapterListAdapter(Context context, int textViewResourceId, List<ChapterItem> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChapterItem chapter = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.chapter_image);
            viewHolder.nameView = (TextView) view.findViewById(R.id.chapter_name);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        viewHolder.imageView.setImageResource(chapter.getImageId());
        viewHolder.nameView.setText(chapter.getName());
        return view;
    }

    class ViewHolder {

        ImageView imageView;

        TextView nameView;

    }

}
