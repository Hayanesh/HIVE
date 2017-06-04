package com.hayanesh.hive.store;

import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.hayanesh.hive.R;

import java.util.ArrayList;
import java.util.List;

public class Book_test extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Book> books;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_test);



      //  prepareData();
        recyclerView = (RecyclerView)findViewById(R.id.book_recyclerView);
        BookAdapter adapter = new BookAdapter(this,books);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        adapter.setGridLayoutManager(gridLayoutManager);
        recyclerView.setLayoutManager(gridLayoutManager);
        //recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(10),false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }


   /* public void prepareData()
    {
        int book_covers[] = new int[]
                {
                        R.drawable.sample_book_cover,
                        R.drawable.book1,
                        R.drawable.book2,
                        R.drawable.book3
                };
        Book b1 = new Book(1,book_covers[0],"Harry Potter","R.K Rowling","","2010",false,"Cloud Computing");
        Book b2 = new Book(2,book_covers[1],"Harry Potter","R.K Rowling","","2010",false,"Cloud Computing");
        Book b3 = new Book(3,book_covers[2],"Harry Potter","R.K Rowling","","2010",true,"Database Management");
        Book b4 = new Book(4,book_covers[3],"Harry Potter","R.K Rowling","","2010",false,"Database Management");

        books = new ArrayList<>();
        books.add(b1);
        books.add(b2);
        books.add(b1);
        books.add(b2);
        books.add(b3);
        books.add(b4);

        books.add(b3);
        books.add(b4);
    }*/
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
