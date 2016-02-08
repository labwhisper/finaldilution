package com.druidpyrcel.biotech.finaldilution.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "COMPOUND".
 */
public class CompoundDao extends AbstractDao<Compound, String> {

    public static final String TABLENAME = "COMPOUND";

    public CompoundDao(DaoConfig config) {
        super(config);
    }

    ;


    public CompoundDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /**
     * Creates the underlying database table.
     */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"COMPOUND\" (" + //
                "\"SHORT_NAME\" TEXT PRIMARY KEY NOT NULL ," + // 0: shortName
                "\"MOLAR_MASS\" REAL NOT NULL );"); // 1: molarMass
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"COMPOUND\"";
        db.execSQL(sql);
    }

    /**
     * @inheritdoc
     */
    @Override
    protected void bindValues(SQLiteStatement stmt, Compound entity) {
        stmt.clearBindings();

        String shortName = entity.getShortName();
        if (shortName != null) {
            stmt.bindString(1, shortName);
        }
        stmt.bindDouble(2, entity.getMolarMass());
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }

    /** @inheritdoc */
    @Override
    public Compound readEntity(Cursor cursor, int offset) {
        Compound entity = new Compound( //
                cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // shortName
                cursor.getDouble(offset + 1) // molarMass
        );
        return entity;
    }

    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Compound entity, int offset) {
        entity.setShortName(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setMolarMass(cursor.getDouble(offset + 1));
    }

    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(Compound entity, long rowId) {
        return entity.getShortName();
    }

    /** @inheritdoc */
    @Override
    public String getKey(Compound entity) {
        if(entity != null) {
            return entity.getShortName();
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
     * Properties of entity Compound.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property ShortName = new Property(0, String.class, "shortName", true, "SHORT_NAME");
        public final static Property MolarMass = new Property(1, double.class, "molarMass", false, "MOLAR_MASS");
    }
    
}
