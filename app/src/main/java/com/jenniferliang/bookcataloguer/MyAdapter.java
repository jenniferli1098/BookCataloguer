package com.jenniferliang.bookcataloguer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<ListItem> listItems;
    private Context mContext;
    private int id;
    private List<String> isbns;
    DBHelper mydb;

    public MyAdapter(List<ListItem>listItems, List<String> isbns, int id, Context context){
        this.mContext = context;
        this.listItems = listItems;
        this.id = id;
        this.isbns = isbns;
        mydb = new DBHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item,parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        ListItem listItem = listItems.get(position);
        holder.textViewTitle.setText(listItem.getTitle());
        holder.textViewAuthor.setText(listItem.getAuthor());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String isbn = isbns.get(position);
                Cursor r = mydb.getBook(id,isbn);
                r.moveToFirst();
                openBookInfo(r);

            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewTitle;
        public  TextView textViewAuthor;
        public LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAuthor = itemView.findViewById(R.id.viewauthor);
            textViewTitle = itemView.findViewById(R.id.viewtitle);
            linearLayout = itemView.findViewById(R.id.LinearLayout);
        }
    }

    public void openBookInfo(Cursor cursor){
        Bundle bundle = new Bundle();
        bundle.putString("title",cursor.getString(cursor.getColumnIndexOrThrow("title")));
        bundle.putString("author",cursor.getString(cursor.getColumnIndexOrThrow("author")));
        bundle.putString("isbn",cursor.getString(cursor.getColumnIndexOrThrow("isbn")));
        bundle.putString("publishDate",cursor.getString(cursor.getColumnIndexOrThrow("publishDate")));
        bundle.putString("coverUrl",cursor.getString(cursor.getColumnIndexOrThrow("coverUrl")));
        Intent intent = new Intent(mContext, DisplayBookInfoActivity.class);
        intent.putExtra("id",id);
        intent.putExtras(bundle);

        mContext.startActivity(intent);
    }
}
