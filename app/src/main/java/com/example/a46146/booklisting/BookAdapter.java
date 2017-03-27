package com.example.a46146.booklisting;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 46146 on 2017/3/27.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private List<Book> mBookList;

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookPic;
        TextView bookTitle;
        TextView bookAuthor;
        TextView bookPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            bookPic = (ImageView) itemView.findViewById(R.id.book_pic);
            bookTitle = (TextView) itemView.findViewById(R.id.book_title);
            bookAuthor = (TextView) itemView.findViewById(R.id.book_author);
            bookPrice = (TextView) itemView.findViewById(R.id.book_price);
        }
    }

    public BookAdapter(List<Book> mBookList) {
        this.mBookList = mBookList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Book book = mBookList.get(position);
        holder.bookPic.setImageResource(R.drawable.book);
        holder.bookTitle.setText(book.getmTitle());
        holder.bookAuthor.setText("作者：" + book.getmAuthor());
        holder.bookPrice.setText("价格：" + book.getmPrice());
    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }
}
