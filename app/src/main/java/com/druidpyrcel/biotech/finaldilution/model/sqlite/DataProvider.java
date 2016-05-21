package com.druidpyrcel.biotech.finaldilution.model.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteException;
import android.util.Log;

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
    private static final String TAG = "Data Provider";
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private AsyncSession asyncSession;
    private List<AsyncOperation> completedOperations;

    public DataProvider(Context context) {
        super(context, DATABASE_NAME, null);
        completedOperations = new CopyOnWriteArrayList<AsyncOperation>();
        Log.d(TAG, "Data provider created");
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
        Log.d(TAG, "Db opened for read");
    }

    public void openWritableDb() throws SQLiteException {
        mDatabase = getWritableDatabase();
        daoMaster = new DaoMaster(mDatabase);
        daoSession = daoMaster.newSession();
        asyncSession = daoSession.startAsyncSession();
        asyncSession.setListener(this);
        Log.d(TAG, "Db opened for write");
    }

    //TODO Close DB somewhere on exit!
    public void closeDbConnections() {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
        close();
        Log.d(TAG, "All database connection closed");
    }


    public void addSolution(Solution solution) {
        if (solution == null) {
            return;
        }
        openWritableDb();
        daoSession.getSolutionDao().insert(solution);
        daoSession.clear();
        Log.d(TAG, "Solution " + solution.getName() + " added");
    }

    public void updateSolution(Solution solution) {
        if (solution == null) {
            return;
        }
        openWritableDb();
        daoSession.getSolutionDao().update(solution);
        daoSession.clear();
        Log.d(TAG, "Solution " + solution.getName() + " updated");
    }

    public Solution getSolution(String solutionName) {
        if (solutionName.trim().length() == 0 || solutionName.trim().equals("")) {
            return null;
        }

        openReadableDb();
        Solution solution = daoSession.getSolutionDao().load(solutionName);
        daoSession.clear();
        Log.d(TAG, "Solution " + solution.getName() + " fetched");
        return solution;
    }

    public List<Solution> getAllSolutions() {
        openReadableDb();
        List<Solution> solutions = daoSession.getSolutionDao().loadAll();
        daoSession.clear();
        Log.d(TAG, "All " + solutions.size() + " solutions fetched");
        return solutions;
    }


    public void addCompound(Compound compound) {
        if (compound == null) {
            return;
        }
        openWritableDb();
        daoSession.getCompoundDao().insert(compound);
        Log.d(TAG, "Compound " + compound.getShortName() + " added");
        daoSession.clear();
    }

    public Compound getCompound(String shortName) {
        if (shortName.trim().length() == 0 || shortName.trim().equals("")) {
            return null;
        }

        openReadableDb();
        Compound compound = daoSession.getCompoundDao().load(shortName);
        daoSession.clear();
        Log.d(TAG, "Compound " + compound.getShortName() + " fetched");
        return compound;
    }

    public void removeCompound(Compound compound) {
        if (compound == null) {
            return;
        }
        openWritableDb();
        daoSession.getCompoundDao().delete(compound);
        Log.d(TAG, "Compound " + compound.getShortName() + " deleted");
        daoSession.clear();
    }

    public List<Compound> getAllCompounds() {
        openReadableDb();
        List<Compound> compounds = daoSession.getCompoundDao().loadAll();
        daoSession.clear();
        Log.d(TAG, "All " + compounds.size() + " compounds fetched");
        return compounds;
    }

    public long addConcentration(Concentration concentration) {
        if (concentration == null) {
            //TODO Change to exception
            return -1;
        }
        openWritableDb();
        long concenRowId = daoSession.getConcentrationDao().insert(concentration);
        daoSession.clear();
        Log.d(TAG, "Concentration " + concentration.getAmount() + " added");
        return concenRowId;
    }

    public Concentration getConcentrationById(long concentrationId) {
        if (concentrationId < 0) {
            return null;
        }
        openReadableDb();
        Concentration concentration = daoSession.getConcentrationDao().load(concentrationId);
        daoSession.clear();
        Log.d(TAG, "Concentration " + concentration.getAmount() + " fetched");
        return concentration;
    }

    public void removeConcentration(Concentration concentration) {
        if (concentration == null) {
            return;
        }
        openWritableDb();
        daoSession.getConcentrationDao().delete(concentration);
        daoSession.clear();
        Log.d(TAG, "Concentration " + concentration.getAmount() + " deleted");
    }

    public void addComponent(Component component) {
        if (component == null) {
            return;
        }
        openWritableDb();
        try {
            daoSession.getComponentDao().insert(component);
            //TODO replace with Component.tostring
            Log.d(TAG, "Component " + component.getCompound().getShortName()
                    + ", " + component.getSolutionName() + " added");
            daoSession.clear();
        } catch (SQLiteConstraintException e) {
            //Tried to add the other the same component...
            //this code shouldn't be achieved.
        }
    }

    public void updateComponent(Component component) {
        if (component == null) {
            return;
        }
        openWritableDb();
        try {
            daoSession.getConcentrationDao().update(component.getDesiredConcentration());
            if (component.getAvailableConcentration() != null) {
                daoSession.getConcentrationDao().update(component.getAvailableConcentration());
            }
            daoSession.getComponentDao().update(component);
            //daoSession.getComponentDao().update(component);
            //TODO replace with Component.tostring
            Log.d(TAG, "Component " + component.getCompound().getShortName()
                    + ", " + component.getSolutionName() + " updated");
            daoSession.clear();
        } catch (SQLiteConstraintException e) {
            //Tried to add the other the same component...
            //this code shouldn't be achieved.
        }
    }

    public void removeComponent(Component component) {
        if (component == null) {
            return;
        }
        openWritableDb();
        Log.d(TAG, "Concentration " + component.getDesiredConcentration().getAmount() + " deleted");
        daoSession.getConcentrationDao().delete(component.getDesiredConcentration());
        if (component.getFromStock()) {
            Log.d(TAG, "Concentration " + component.getAvailableConcentration().getAmount() + " deleted");
            daoSession.getConcentrationDao().delete(component.getAvailableConcentration());
        }
        daoSession.getComponentDao().delete(component);
        //TODO replace with Component.tostring
        String compoundName = "";
        if( component.getCompound() != null) {
            compoundName = component.getCompound().getShortName();
        }
        Log.d(TAG, "Component " + compoundName
                + ", " + component.getSolutionName() + " deleted");
        daoSession.clear();
    }

    public Component getComponentWithCompound(Solution solution, Compound compound) {
        if (solution == null || compound == null) {
            return null;
        }
        openReadableDb();
        //get Component by solution Name and compound shortName
        ComponentDao dao = daoSession.getComponentDao();
        WhereCondition condition = dao.queryBuilder().and(
                ComponentDao.Properties.SolutionName.eq(solution.getName()),
                ComponentDao.Properties.CompoundShortName.eq(compound.getShortName()));
        QueryBuilder<Component> queryBuilder = dao.queryBuilder().where(condition);
        Component component = queryBuilder.unique();
        daoSession.clear();
        if (component != null) {
            Log.d(TAG, "Component " + component.getCompound().getShortName()
                    + ", " + component.getSolutionName() + " fetched");
        }
        return component;
    }

}
