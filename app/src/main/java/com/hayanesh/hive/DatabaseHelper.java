package com.hayanesh.hive;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hayanesh.hive.store.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hayanesh on 27-May-17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    final static int DATABASE_VERSION = 1;

    final static String DATABASE_NAME = "hive";

    //Table
    final static String DETAILS_TABLE = "Stud_Attendance";
    final static String BOOKS_TABLE = "books_details";

    //Column
    final static String KEY_ID = "id";
    final static String KEY_NAME = "name";
    final static String KEY_NO = "no";
    final static String KEY_STATUS = "status";
    final static String KEY_SELECTED = "selected";


    //Column
    final static String BOOK_ID = "id";
    final static String BOOK_NAME = "name";
    final static String BOOK_URL = "url";
    final static String BOOK_COVER = "img_url";
    final static String BOOK_AUTHOR = "author";
    final static String BOOK_YEAR = "year";
    final static String BOOK_SUBJECT = "subject";

    final static String CREATE_TABLE_DETAILS = "CREATE TABLE " + DETAILS_TABLE + "(" + KEY_ID + " TEXT PRIMARY KEY, " + KEY_NAME + " TEXT, " + KEY_NO + " INTEGER, "+ KEY_STATUS +" BOOLEAN, " +
            KEY_SELECTED +" BOOLEAN "+")";

    final static String CREATE_TABLE_BOOKS = "CREATE TABLE " + BOOKS_TABLE + "(" + BOOK_ID + " INTEGER PRIMARY KEY, " + BOOK_NAME + " TEXT, " + BOOK_AUTHOR + " TEXT, "+ BOOK_YEAR +" TEXT, " +
            BOOK_COVER +" TEXT,"+ BOOK_URL + " TEXT,"+BOOK_SUBJECT +" TEXT "+")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DETAILS);
        db.execSQL(CREATE_TABLE_BOOKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DETAILS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + BOOKS_TABLE);
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Instering only one row
   /* public long createDetails(Student details) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, details.getId());
        values.put(KEY_NAME, details.getName());
        values.put(KEY_NO,details.getNo());
        values.put(KEY_SELECTED,details.getSelected());
        values.put(KEY_STATUS,details.getPresent());
        long details_id = db.insert(DETAILS_TABLE, null, values);
        db.close();
        return details_id;
    }*/
 /*
    public Details getDetails(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DETAILS_TABLE, new String[]{KEY_ID,
                        KEY_NAME, KEY_EMAIL,KEY_PASS, KEY_PHONE,KEY_ALTPHONE,KEY_ADDRESS,KEY_LOCALITY, KEY_CATEGORY, KEY_TYPE,KEY_PINCODE}, KEY_ID + "=?",
                new String[]{id}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Details details = new Details(cursor.getString(0),
                cursor.getString(1), cursor.getString(2),cursor.getString(3), cursor.getString(4),cursor.getString(5), cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9),
                cursor.getString(10));
        // return contact
        return details;


    }*/

    public void DeleteDetails() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DETAILS_TABLE,null,null);
    }


    public void updateStudent(Student a) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_STATUS, a.getPresent());
        // updating row
        db.update(DETAILS_TABLE, values,KEY_ID + " = ?",
                new String[]{String.valueOf(a.getId())});
    }

    public void AllSelected()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SELECTED,true);
        db.update(DETAILS_TABLE, values,null,null);
    }
    public void AllDeselected()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SELECTED,false);
        db.update(DETAILS_TABLE, values,null,null);
    }

    public void AllPresent()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STATUS,true);
        db.update(DETAILS_TABLE, values,null,null);
    }
    public void AllAbsent()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STATUS,false);
        db.update(DETAILS_TABLE, values,null,null);
    }
    public int getAbsent()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String SELECT_ABS = "SELECT * FROM "+DETAILS_TABLE +" WHERE "+KEY_STATUS+" <1";
        Cursor cursor = db.rawQuery(SELECT_ABS, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }
    public boolean CreateTableStudent(List<Student> students)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);
                ContentValues values = new ContentValues();
                values.put(KEY_NO,student.getNo());
                values.put(KEY_ID,student.getId());
                values.put(KEY_NAME,student.getName());
                values.put(KEY_SELECTED,student.getSelected());
                values.put(KEY_STATUS,student.getPresent());

                long s_id = sqLiteDatabase.insert(DETAILS_TABLE, null, values);
            }
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Student> getStudents()
    {
        ArrayList<Student> students = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String SELECT_APPLIANCES = "SELECT * FROM "+DETAILS_TABLE;
        Cursor c = db.rawQuery(SELECT_APPLIANCES,null);
        if(c.moveToFirst())
        {
            do {
                Student app = new Student();
                app.setId(c.getString(c.getColumnIndex(KEY_ID)));
                app.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                app.setNo(c.getInt(c.getColumnIndex(KEY_NO)));
                app.setPresent(c.getInt(c.getColumnIndex(KEY_STATUS))>0);
                app.setSelected(c.getInt(c.getColumnIndex(KEY_SELECTED))>0);
                students.add(app);
            }while (c.moveToNext());
        }
        return students;
    }
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }


    public boolean CreateTableBooks(List<Book> books)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            for (int i = 0; i < books.size(); i++) {
                Book book = books.get(i);
                ContentValues values = new ContentValues();
                values.put(BOOK_ID,book.getNo());
                values.put(BOOK_NAME,book.getBook_title());
                values.put(BOOK_AUTHOR,book.getAuthor());
                values.put(BOOK_YEAR,book.getAuthor());
                values.put(BOOK_COVER,book.getCover());
                values.put(BOOK_URL,book.getUrl());
                values.put(BOOK_SUBJECT,book.getCategory());

                long s_id = sqLiteDatabase.insert(BOOKS_TABLE, null, values);
            }
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Book> getBooks()
    {
        ArrayList<Book> books = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String SELECT_APPLIANCES = "SELECT * FROM "+ BOOKS_TABLE;
        Cursor c = db.rawQuery(SELECT_APPLIANCES,null);
        if(c.moveToFirst())
        {
            do {
                Book book = new Book();
                book.setNo(c.getInt(c.getColumnIndex(BOOK_ID)));
                book.setBook_title(c.getString(c.getColumnIndex(BOOK_NAME)));
                book.setAuthor(c.getString(c.getColumnIndex(BOOK_AUTHOR)));
                book.setYear(c.getString(c.getColumnIndex(BOOK_YEAR)));
                book.setCategory(c.getString(c.getColumnIndex(BOOK_SUBJECT)));
                book.setUrl(c.getString(c.getColumnIndex(BOOK_URL)));
                book.setCover(c.getString(c.getColumnIndex(BOOK_COVER)));
                books.add(book);
            }while (c.moveToNext());
        }
        return books;
    }

    public void DeleteBooks() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(BOOKS_TABLE,null,null);
    }

}

