package com.secreal.cari_movie.extra;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.secreal.cari_movie.Dao.DaoMaster;
import com.secreal.cari_movie.Dao.DaoSession;

/**
 * Created by secreal on 5/22/2017.
 * saya ulong :)
 */

public class CariMovieContext {
    protected SQLiteDatabase db;
    protected DaoMaster daoMaster;
    //protected DaoSession daoSession;
    //protected MasterKategoriBarangDao masterKategoriBarangDao;
    protected Context ctx;

    public CariMovieContext() {
    }

    public DaoSession getDaoSession(Context _ctx)
    {
        if (_ctx != null)
            this.ctx = _ctx;
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this.ctx, "Car i-Movie", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        return daoMaster.newSession();
    }

    public void setContext(Context _ctx)
    {
        this.ctx = _ctx;
    }



}
