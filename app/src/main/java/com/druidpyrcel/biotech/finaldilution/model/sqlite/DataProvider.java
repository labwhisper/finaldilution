package com.druidpyrcel.biotech.finaldilution.model.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteException;

import com.druidpyrcel.biotech.finaldilution.model.Component;
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
    }

    public void updateSolution(Solution solution) {
        if (solution == null) {
            return;
        }
        openWritableDb();
        daoSession.getSolutionDao().update(solution);
    }

    public Solution getSolution(String solutionName) {
        if (solutionName.trim().length() == 0 || solutionName.trim().equals("")) {
            return null;
        }

        openReadableDb();
        return daoSession.getSolutionDao().load(solutionName);
    }

    public List<Solution> getAllSolutions() {
        openReadableDb();
        return daoSession.getSolutionDao().loadAll();
    }

    public Compound getCompound(String shortName) {
        if (shortName.trim().length() == 0 || shortName.trim().equals("")) {
            return null;
        }

        openReadableDb();
        return daoSession.getCompoundDao().load(shortName);
    }

    public List<Compound> getAllCompounds() {
        openReadableDb();
        return daoSession.getCompoundDao().loadAll();
    }

    public long addConcentration(Concentration concentration) {
        if (concentration == null) {
            //TODO Change to exception
            return -1;
        }
        openWritableDb();
        return daoSession.getConcentrationDao().insert(concentration);
    }

    public Concentration getConcentrationById(long concentrationId) {
        if (concentrationId < 0) {
            return null;
        }
        return daoSession.getConcentrationDao().load(concentrationId);
    }

    public void addComponent(Component component) {
        if (component == null) {
            return;
        }
        openWritableDb();
        daoSession.getComponentDao().insert(component);
    }


}
