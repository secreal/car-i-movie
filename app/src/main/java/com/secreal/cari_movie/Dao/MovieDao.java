package com.secreal.cari_movie.Dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.secreal.cari_movie.Dao.Movie;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table MOVIE.
*/
public class MovieDao extends AbstractDao<Movie, Long> {

    public static final String TABLENAME = "MOVIE";

    /**
     * Properties of entity Movie.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Detail = new Property(2, String.class, "Detail", false, "DETAIL");
        public final static Property Image = new Property(3, String.class, "image", false, "IMAGE");
        public final static Property Umur = new Property(4, String.class, "umur", false, "UMUR");
        public final static Property Trailer1 = new Property(5, String.class, "trailer1", false, "TRAILER1");
        public final static Property Trailer2 = new Property(6, String.class, "trailer2", false, "TRAILER2");
        public final static Property Tahun = new Property(7, int.class, "tahun", false, "TAHUN");
        public final static Property Durasi = new Property(8, int.class, "durasi", false, "DURASI");
    };

    private DaoSession daoSession;


    public MovieDao(DaoConfig config) {
        super(config);
    }
    
    public MovieDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'MOVIE' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'NAME' TEXT NOT NULL ," + // 1: name
                "'DETAIL' TEXT NOT NULL ," + // 2: Detail
                "'IMAGE' TEXT NOT NULL ," + // 3: image
                "'UMUR' TEXT NOT NULL ," + // 4: umur
                "'TRAILER1' TEXT NOT NULL ," + // 5: trailer1
                "'TRAILER2' TEXT," + // 6: trailer2
                "'TAHUN' INTEGER NOT NULL ," + // 7: tahun
                "'DURASI' INTEGER NOT NULL );"); // 8: durasi
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'MOVIE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Movie entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getName());
        stmt.bindString(3, entity.getDetail());
        stmt.bindString(4, entity.getImage());
        stmt.bindString(5, entity.getUmur());
        stmt.bindString(6, entity.getTrailer1());
 
        String trailer2 = entity.getTrailer2();
        if (trailer2 != null) {
            stmt.bindString(7, trailer2);
        }
        stmt.bindLong(8, entity.getTahun());
        stmt.bindLong(9, entity.getDurasi());
    }

    @Override
    protected void attachEntity(Movie entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Movie readEntity(Cursor cursor, int offset) {
        Movie entity = new Movie( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // name
            cursor.getString(offset + 2), // Detail
            cursor.getString(offset + 3), // image
            cursor.getString(offset + 4), // umur
            cursor.getString(offset + 5), // trailer1
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // trailer2
            cursor.getInt(offset + 7), // tahun
            cursor.getInt(offset + 8) // durasi
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Movie entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.getString(offset + 1));
        entity.setDetail(cursor.getString(offset + 2));
        entity.setImage(cursor.getString(offset + 3));
        entity.setUmur(cursor.getString(offset + 4));
        entity.setTrailer1(cursor.getString(offset + 5));
        entity.setTrailer2(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setTahun(cursor.getInt(offset + 7));
        entity.setDurasi(cursor.getInt(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Movie entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Movie entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
