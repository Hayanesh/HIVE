package com.hayanesh.hive.store;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hayanesh.hive.DatabaseHelper;
import com.hayanesh.hive.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Book_fragment extends Fragment {

    RecyclerView recyclerView;
    List<Book> books;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swipeRefreshLayout;
    BookAdapter adapter;
    static final String PDF_FETCH_URL = "http://192.168.1.4/hive/getPdfs.php";
    DatabaseHelper databaseHelper;
    public Book_fragment() {
        // Required empty public constructor
    }

    public static Book_fragment newInstance(String param1, String param2) {
        Book_fragment fragment = new Book_fragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_book_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        databaseHelper = new DatabaseHelper(this.getActivity().getApplicationContext());
        progressDialog = new ProgressDialog(this.getContext());
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        if(databaseHelper.getBooks().isEmpty())
        {
            getPdfs();
        }
        recyclerView = (RecyclerView)view.findViewById(R.id.book_recyclerView);
        int grid_count = getResources().getInteger(R.integer.grid_size);
        adapter = new BookAdapter(getContext(),databaseHelper.getBooks());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), grid_count);
        adapter.setGridLayoutManager(gridLayoutManager);
        recyclerView.setLayoutManager(gridLayoutManager);
        //recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(10),false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                databaseHelper.DeleteBooks();
                getPdfs();
                int grid_count = getResources().getInteger(R.integer.grid_size);
                adapter.notifyDataSetChanged();
                adapter = new BookAdapter(getContext(),databaseHelper.getBooks());
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), grid_count);
                adapter.setGridLayoutManager(gridLayoutManager);
                recyclerView.setLayoutManager(gridLayoutManager);
                //recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(10),false));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);

            }
        });
    }


    private void getPdfs() {
        books = new ArrayList<>();
        progressDialog.setMessage("Fetching Pdfs... Please Wait");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PDF_FETCH_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            //Toast.makeText(MainActivity.this,obj.getString("message"), Toast.LENGTH_SHORT).show();
                            Log.d("RESPONSE",response.toString());
                            JSONArray jsonArray = obj.getJSONArray("pdf");

                            for(int i=0;i<jsonArray.length();i++){

                                //Declaring a json object corresponding to every pdf object in our json Array
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //Declaring a Pdf object to add it to the ArrayList  pdfList
                                Book pdf  = new Book();
                                int id = jsonObject.getInt("id");
                                String pdfName = jsonObject.getString("name");
                                String pdfUrl = jsonObject.getString("url");
                                String cover_img = jsonObject.getString("cover");
                                String author = jsonObject.getString("author");
                                String year = jsonObject.getString("year");
                                String sub = jsonObject.getString("subject");
                                Log.d("TAG",sub);
                                pdf.setNo(id);
                                pdf.setBook_title(pdfName);
                                pdf.setUrl(pdfUrl);
                                pdf.setAuthor(author);
                                pdf.setCategory(sub);
                                pdf.setCover(cover_img);
                                pdf.setYear(year);
                                books.add(pdf);
                            }
                            databaseHelper.CreateTableBooks(books);
                            swipeRefreshLayout.setRefreshing(false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR",error.toString());
                    }
                }
        );

        RequestQueue request = Volley.newRequestQueue(this.getContext());
        request.add(stringRequest);

    }

  /*  public void prepareData()
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
