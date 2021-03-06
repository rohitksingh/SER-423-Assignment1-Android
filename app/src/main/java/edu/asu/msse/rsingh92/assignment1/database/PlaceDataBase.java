package edu.asu.msse.rsingh92.assignment1.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import edu.asu.msse.rsingh92.assignment1.R;

/*
 * Copyright 2020 Rohit Kumar Singh,
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Rohit Kumar Singh rsingh92@asu.edu
 *
 * @version April 2016
 */
public class PlaceDataBase extends SQLiteOpenHelper {

    private static final String TAG = "PlaceDataBase";

    private static String dbName = "place";
    private static final int DATABASE_VERSION = 1;
    private String dbPath;
    private SQLiteDatabase crsDB;

    private Context context;

    public PlaceDataBase(Context context) {
        super(context, dbName, null, DATABASE_VERSION);
        this.context = context;
        dbPath = context.getFilesDir().getPath()+"/";
    }

    public SQLiteDatabase openDB() throws SQLException {

        String myPath = dbPath + dbName + ".db";

        if(checkDB()) {
            crsDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        }else{

            try {
                this.copyDB();
                crsDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            }catch(Exception ex) {
                Log.d(TAG, "openDB1: unable to copy and open db");
            }
        }
        return crsDB;
    }

    public void createDB() throws IOException {
        this.getReadableDatabase();
        try {
            copyDB();
        } catch (IOException e) {
            android.util.Log.w(this.getClass().getSimpleName(),
                    "createDB Error copying database " + e.getMessage());
        }
    }

    /**
     * Does the database exist and has it been initialized? This method determines whether
     * the database needs to be copied to the data/data/pkgName/files directory by
     * checking whether the file exists. If it does it checks to see whether the db is
     * uninitialized or whether it has the course table.
     * @return false if the database file needs to be copied from the assets directory, true
     * otherwise.
     */
    private boolean checkDB(){

        SQLiteDatabase checkDB = null;
        boolean crsTabExists = false;
        try{
            String path = dbPath + dbName + ".db";
            File aFile = new File(path);
            if(aFile.exists()){
                checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
                if (checkDB!=null) {

                    Cursor tabChk = checkDB.rawQuery("SELECT name FROM sqlite_master where type='table' and name='place';", null);
                    if(tabChk!=null){
                        Cursor c= checkDB.rawQuery("SELECT * FROM place", null);
                        if(c.getCount()!=0){
                            crsTabExists = true;
                        }
                    }
                }
            }
        }catch(SQLiteException e){
            android.util.Log.w("CourseDB->checkDB",e.getMessage());
        }

        if(checkDB != null){
            checkDB.close();
        }

        return crsTabExists;
    }

    public void copyDB() throws IOException{
        try {

            if(!checkDB()){
                InputStream ip =  context.getResources().openRawResource(R.raw.place);

                File aFile = new File(dbPath);
                if(!aFile.exists()){
                    aFile.mkdirs();
                }
                String op=  dbPath  +  dbName +".db";

                OutputStream output = new FileOutputStream(op);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = ip.read(buffer))>0){
                    output.write(buffer, 0, length);
                }
                output.flush();
                output.close();
                ip.close();
            }
        } catch (IOException e) {
            Log.d(TAG, "copyDB: "+e.getMessage());
        }
    }

    @Override
    public synchronized void close() {
        if(crsDB != null)
            crsDB.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
