package com.example.animalcare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.w3c.dom.Text;

import java.util.List;

public class CustomListViewAdapter extends ArrayAdapter<BlogData> {

    public CustomListViewAdapter(@NonNull Context context, @NonNull List<BlogData> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

       View myLayoutView = convertView;

       if(myLayoutView == null){
           myLayoutView = LayoutInflater.from(getContext()).inflate(R.layout.feed_list_view,parent,false);
       }

        BlogData myBlogData = getItem(position);
        TextView userName = (TextView) myLayoutView.findViewById(R.id.TextViewUserName);
        assert myBlogData != null;
        userName.setText(myBlogData.getUserName());

        TextView blogText = (TextView) myLayoutView.findViewById(R.id.TextViewBlog);
        blogText.setText(myBlogData.getBlogText());

        TextView dateTv = (TextView) myLayoutView.findViewById(R.id.TextViewDate);
        dateTv.setText(myBlogData.getBlogDate());

        ImageView blogImgView = (ImageView) myLayoutView.findViewById(R.id.blogImageView);

        blogImgView.setImageResource(R.drawable.loadingimages);

        Glide.with(getContext()).
                load(myBlogData.getImageURL())
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                //.thumbnail(0.1f).
                .placeholder(R.drawable.loadingimages).
                into(blogImgView);

        return myLayoutView;
    }
}
