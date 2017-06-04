package com.hayanesh.hive.store;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hayanesh.hive.R;
import com.zhukic.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.net.URL;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Hayanesh on 29-May-17.
 */

public class BookAdapter extends SectionedRecyclerViewAdapter<BookAdapter.SubheaderViewHolder,BookAdapter.ItemViewHolder> {

    public Context mcontext;
    public List<Book> books;
    class ItemViewHolder extends RecyclerView.ViewHolder{

        ImageView bCover,bPresent;
        TextView bTitle,bAuthor,bYear;
        public ItemViewHolder(View itemView) {
            super(itemView);
            bCover = (ImageView)itemView.findViewById(R.id.thumbnail);
            bPresent = (ImageView)itemView.findViewById(R.id.overflow);
            bTitle = (TextView)itemView.findViewById(R.id.title);
            bAuthor = (TextView)itemView.findViewById(R.id.author);
            bYear = (TextView)itemView.findViewById(R.id.year);
        }
    }
    class SubheaderViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        public SubheaderViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.section_name);
        }
    }

    public BookAdapter(Context mcontext,List<Book> books) {
        this.mcontext = mcontext;
        this.books = books;
    }

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(ItemViewHolder holder, int position) {
        final Book book = books.get(position);
        try {
            Glide.with(mcontext).load(new URL(book.getCover())).asBitmap().into(holder.bCover);
        }catch (Exception e)
        {
            Toast.makeText(mcontext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        holder.bTitle.setText(book.getBook_title());
        holder.bAuthor.setText(book.getAuthor());
        holder.bYear.setText(book.getYear());
        if(book.getPresent())
        {
            holder.bPresent.setVisibility(View.VISIBLE);
        }else {
            holder.bPresent.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public SubheaderViewHolder onCreateSubheaderViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_header,parent,false);
        return new SubheaderViewHolder(view);
    }

    @Override
    public void onBindSubheaderViewHolder(SubheaderViewHolder subheaderHolder, int nextItemPosition) {
        final Book book = books.get(nextItemPosition);
        final Typeface customtypeface3 = Typeface.createFromAsset(mcontext.getAssets(),"fonts/CormorantGaramond-Regular.ttf");
        subheaderHolder.title.setText(book.getCategory());
        subheaderHolder.title.setTypeface(customtypeface3);
    }

    @Override
    public int getItemSize() {
        return books.size();
    }

    @Override
    public boolean onPlaceSubheaderBetweenItems(int position) {
        final Book book = books.get(position);
        final Book nxtbook = books.get(position+1);

        return !book.getCategory().equals(nxtbook.getCategory());
    }
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
