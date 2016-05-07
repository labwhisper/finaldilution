package com.druidpyrcel.biotech.finaldilution.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "COMPONENT".
*/
public class ComponentDao extends AbstractDao<Component, Long> {

    public static final String TABLENAME = "COMPONENT";
    private DaoSession daoSession;;
    private Query<Component> solution_ComponentsQuery;
    private String selectDeep;

    public ComponentDao(DaoConfig config) {
        super(config);
    }
    
    public ComponentDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"COMPONENT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"FROM_STOCK\" INTEGER NOT NULL ," + // 1: fromStock
                "\"SOLUTION_NAME\" TEXT NOT NULL ," + // 2: solutionName
                "\"COMPOUND_SHORT_NAME\" TEXT NOT NULL ," + // 3: compoundShortName
                "\"DES_CONC_ID\" INTEGER NOT NULL ," + // 4: desConcId
                "\"AVAIL_CONC_ID\" INTEGER NOT NULL );"); // 5: availConcId
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_COMPONENT_SOLUTION_NAME_COMPOUND_SHORT_NAME ON COMPONENT" +
                " (\"SOLUTION_NAME\",\"COMPOUND_SHORT_NAME\");");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"COMPONENT\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Component entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getFromStock() ? 1L : 0L);
        stmt.bindString(3, entity.getSolutionName());
        stmt.bindString(4, entity.getCompoundShortName());
        stmt.bindLong(5, entity.getDesConcId());
        stmt.bindLong(6, entity.getAvailConcId());
    }

    @Override
    protected void attachEntity(Component entity) {
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
    public Component readEntity(Cursor cursor, int offset) {
        Component entity = new Component( //
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
                cursor.getShort(offset + 1) != 0, // fromStock
                cursor.getString(offset + 2), // solutionName
                cursor.getString(offset + 3), // compoundShortName
                cursor.getLong(offset + 4), // desConcId
                cursor.getLong(offset + 5) // availConcId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Component entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setFromStock(cursor.getShort(offset + 1) != 0);
        entity.setSolutionName(cursor.getString(offset + 2));
        entity.setCompoundShortName(cursor.getString(offset + 3));
        entity.setDesConcId(cursor.getLong(offset + 4));
        entity.setAvailConcId(cursor.getLong(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Component entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Component entity) {
        if (entity != null) {
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
    
    /** Internal query to resolve the "components" to-many relationship of Solution. */
    public List<Component> _querySolution_Components(String solutionName) {
        synchronized (this) {
            if (solution_ComponentsQuery == null) {
                QueryBuilder<Component> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.SolutionName.eq(null));
                solution_ComponentsQuery = queryBuilder.build();
            }
        }
        Query<Component> query = solution_ComponentsQuery.forCurrentThread();
        query.setParameter(0, solutionName);
        return query.list();
    }

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getSolutionDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getCompoundDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T2", daoSession.getConcentrationDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T3", daoSession.getConcentrationDao().getAllColumns());
            builder.append(" FROM COMPONENT T");
            builder.append(" LEFT JOIN SOLUTION T0 ON T.\"SOLUTION_NAME\"=T0.\"NAME\"");
            builder.append(" LEFT JOIN COMPOUND T1 ON T.\"COMPOUND_SHORT_NAME\"=T1.\"SHORT_NAME\"");
            builder.append(" LEFT JOIN CONCENTRATION T2 ON T.\"DES_CONC_ID\"=T2.\"_id\"");
            builder.append(" LEFT JOIN CONCENTRATION T3 ON T.\"AVAIL_CONC_ID\"=T3.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }

    protected Component loadCurrentDeep(Cursor cursor, boolean lock) {
        Component entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Solution solution = loadCurrentOther(daoSession.getSolutionDao(), cursor, offset);
         if(solution != null) {
            entity.setSolution(solution);
        }
        offset += daoSession.getSolutionDao().getAllColumns().length;

        Compound compound = loadCurrentOther(daoSession.getCompoundDao(), cursor, offset);
         if(compound != null) {
            entity.setCompound(compound);
        }
        offset += daoSession.getCompoundDao().getAllColumns().length;

        Concentration desiredConcentration = loadCurrentOther(daoSession.getConcentrationDao(), cursor, offset);
         if(desiredConcentration != null) {
            entity.setDesiredConcentration(desiredConcentration);
        }
        offset += daoSession.getConcentrationDao().getAllColumns().length;

        Concentration availableConcentration = loadCurrentOther(daoSession.getConcentrationDao(), cursor, offset);
         if(availableConcentration != null) {
            entity.setAvailableConcentration(availableConcentration);
        }

        return entity;
    }
    
    public Component loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();

        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);

        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }

    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<Component> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Component> list = new ArrayList<Component>(count);

        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<Component> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    
    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Component> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
    
/**
     * Properties of entity Component.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
    public final static Property Id = new Property(0, Long.class, "id", true, "_id");
    public final static Property FromStock = new Property(1, boolean.class, "fromStock", false, "FROM_STOCK");
    public final static Property SolutionName = new Property(2, String.class, "solutionName", false, "SOLUTION_NAME");
    public final static Property CompoundShortName = new Property(3, String.class, "compoundShortName", false, "COMPOUND_SHORT_NAME");
    public final static Property DesConcId = new Property(4, long.class, "desConcId", false, "DES_CONC_ID");
    public final static Property AvailConcId = new Property(5, long.class, "availConcId", false, "AVAIL_CONC_ID");
    }
 
}
