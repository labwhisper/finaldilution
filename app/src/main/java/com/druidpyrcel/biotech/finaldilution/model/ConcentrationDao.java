package com.druidpyrcel.biotech.finaldilution.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CONCENTRATION".
*/
public class ConcentrationDao extends AbstractDao<Concentration, Long> {

    public static final String TABLENAME = "CONCENTRATION";
    private final ConcentrationConverter typeConverter = new ConcentrationConverter();;

    public ConcentrationDao(DaoConfig config) {
        super(config);
    }

    public ConcentrationDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }
    
    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CONCENTRATION\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"AMOUNT\" REAL NOT NULL ," + // 1: amount
                "\"TYPE\" INTEGER NOT NULL );"); // 2: type
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CONCENTRATION\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Concentration entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindDouble(2, entity.getAmount());
        stmt.bindLong(3, typeConverter.convertToDatabaseValue(entity.getType()));
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }

    /** @inheritdoc */
    @Override
    public Concentration readEntity(Cursor cursor, int offset) {
        Concentration entity = new Concentration( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getDouble(offset + 1), // amount
            typeConverter.convertToEntityProperty(cursor.getInt(offset + 2)) // type
        );
        return entity;
    }

    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Concentration entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setAmount(cursor.getDouble(offset + 1));
        entity.setType(typeConverter.convertToEntityProperty(cursor.getInt(offset + 2)));
     }
     
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Concentration entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Concentration entity) {
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

/**
     * Properties of entity Concentration.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Amount = new Property(1, double.class, "amount", false, "AMOUNT");
        public final static Property Type = new Property(2, int.class, "type", false, "TYPE");
    }
    
}
