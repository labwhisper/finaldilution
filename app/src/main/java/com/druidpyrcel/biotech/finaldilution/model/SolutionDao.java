package com.druidpyrcel.biotech.finaldilution.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "SOLUTION".
 */
public class SolutionDao extends AbstractDao<Solution, String> {

    public static final String TABLENAME = "SOLUTION";
    private DaoSession daoSession;
    ;

    public SolutionDao(DaoConfig config) {
        super(config);
    }


    public SolutionDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /**
     * Creates the underlying database table.
     */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SOLUTION\" (" + //
                "\"NAME\" TEXT PRIMARY KEY NOT NULL ," + // 0: name
                "\"VOLUME\" REAL NOT NULL );"); // 1: volume
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SOLUTION\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Solution entity) {
        stmt.clearBindings();

        String name = entity.getName();
        if (name != null) {
            stmt.bindString(1, name);
        }
        stmt.bindDouble(2, entity.getVolume());
    }

    @Override
    protected void attachEntity(Solution entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }

    /** @inheritdoc */
    @Override
    public Solution readEntity(Cursor cursor, int offset) {
        Solution entity = new Solution( //
                cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // name
                cursor.getDouble(offset + 1) // volume
        );
        return entity;
    }

    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Solution entity, int offset) {
        entity.setName(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setVolume(cursor.getDouble(offset + 1));
     }
     
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(Solution entity, long rowId) {
        return entity.getName();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(Solution entity) {
        if (entity != null) {
            return entity.getName();
        } else {
            return null;
        }
    }
    
    /** @inheritdoc */
    @Override
    protected boolean isEntityUpdateable() {
        return true;
    }

/**
     * Properties of entity Solution.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Name = new Property(0, String.class, "name", true, "NAME");
        public final static Property Volume = new Property(1, double.class, "volume", false, "VOLUME");
    }
    
}
