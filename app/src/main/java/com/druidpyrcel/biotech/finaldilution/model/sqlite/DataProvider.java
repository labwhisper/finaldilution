package com.druidpyrcel.biotech.finaldilution.model.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteException;

import com.druidpyrcel.biotech.finaldilution.model.Component;
import com.druidpyrcel.biotech.finaldilution.model.ComponentDao;
import com.druidpyrcel.biotech.finaldilution.model.Compound;
import com.druidpyrcel.biotech.finaldilution.model.Concentration;
import com.druidpyrcel.biotech.finaldilution.model.DaoMaster;
import com.druidpyrcel.biotech.finaldilution.model.DaoSession;
import com.druidpyrcel.biotech.finaldilution.model.Solution;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.greenrobot.dao.async.AsyncOperation;
import de.greenrobot.dao.async.AsyncOperationListener;
import de.greenrobot.dao.async.AsyncSession;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;

public class DataProvider extends AssetDbHelper implements AsyncOperationListener {

    public static final String DATABASE_NAME = "FinalDilutionDB.sqlite";

    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private AsyncSession asyncSession;
    private List<AsyncOperation> completedOperations;

    public DataProvider(Context context) {
        super(context, DATABASE_NAME, null);
        completedOperations = new CopyOnWriteArrayList<AsyncOperation>();
    }


    @Override
    public void onAsyncOperationCompleted(AsyncOperation operation) {
        completedOperations.add(operation);
    }

    public void openReadableDb() throws SQLiteException {
        mDatabase = getReadableDatabase();
        daoMaster = new DaoMaster(mDatabase);
        daoSession = daoMaster.newSession();
        asyncSession = daoSession.startAsyncSession();
        asyncSession.setListener(this);
    }

    public void openWritableDb() throws SQLiteException {
        mDatabase = getWritableDatabase();
        daoMaster = new DaoMaster(mDatabase);
        daoSession = daoMaster.newSession();
        asyncSession = daoSession.startAsyncSession();
        asyncSession.setListener(this);
    }

    //TODO Close DB somewhere on exit!
    public void closeDbConnections() {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
        close();
    }


    public void addSolution(Solution solution) {
        if (solution == null) {
            return;
        }
        openWritableDb();
        daoSession.getSolutionDao().insert(solution);
        daoSession.clear();
    }

    public void updateSolution(Solution solution) {
        if (solution == null) {
            return;
        }
        openWritableDb();
        daoSession.getSolutionDao().update(solution);
        daoSession.clear();
    }

    public Solution getSolution(String solutionName) {
        if (solutionName.trim().length() == 0 || solutionName.trim().equals("")) {
            return null;
        }

        openReadableDb();
        Solution solution = daoSession.getSolutionDao().load(solutionName);
        daoSession.clear();
        return solution;
    }

    public List<Solution> getAllSolutions() {
        openReadableDb();
        List<Solution> solutions = daoSession.getSolutionDao().loadAll();
        daoSession.clear();
        return solutions;
    }


    public void addCompound(Compound compound) {
        if (compound == null) {
            return;
        }
        openWritableDb();
        daoSession.getCompoundDao().insert(compound);
        daoSession.clear();
    }

    public Compound getCompound(String shortName) {
        if (shortName.trim().length() == 0 || shortName.trim().equals("")) {
            return null;
        }

        openReadableDb();
        Compound compound = daoSession.getCompoundDao().load(shortName);
        daoSession.clear();
        return compound;
    }

    public List<Compound> getAllCompounds() {
        openReadableDb();
        List<Compound> compounds = daoSession.getCompoundDao().loadAll();
        daoSession.clear();
        return compounds;
    }

    public long addConcentration(Concentration concentration) {
        if (concentration == null) {
            //TODO Change to exception
            return -1;
        }
        openWritableDb();
        long concRowId = daoSession.getConcentrationDao().insert(concentration);
        daoSession.clear();
        return concRowId;
    }

    public Concentration getConcentrationById(long concentrationId) {
        if (concentrationId < 0) {
            return null;
        }
        Concentration concentration = daoSession.getConcentrationDao().load(concentrationId);
        daoSession.clear();
        return concentration;
    }

    public void addComponent(Component component) {
        if (component == null) {
            return;
        }
        openWritableDb();
        try {
            daoSession.getComponentDao().insert(component);
            daoSession.clear();
        } catch (SQLiteConstraintException e) {
            //Tried to add the other the same component...
            //this code shouldn't be achieved.
        }
    }

    public Component getComponentWithCompound(Solution solution, Compound compound) {
        if (solution == null || compound == null) {
            return null;
        }
        openWritableDb();
        //get Component by solution Name and compound shortName
        ComponentDao dao = daoSession.getComponentDao();
        WhereCondition condition = dao.queryBuilder().and(
                ComponentDao.Properties.SolutionName.eq(solution.getName()),
                ComponentDao.Properties.CompoundShortName.eq(compound.getShortName()));
        QueryBuilder<Component> queryBuilder = dao.queryBuilder().where(condition);
        Component component = queryBuilder.unique();
        daoSession.clear();
        return component;
    }

}
