package com.labwhisper.biotech.finaldilution.solution.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.labwhisper.biotech.finaldilution.ApplicationContext
import com.labwhisper.biotech.finaldilution.R
import com.labwhisper.biotech.finaldilution.genericitem.putExtraAnItem
import com.labwhisper.biotech.finaldilution.solution.CareTaker
import com.labwhisper.biotech.finaldilution.solution.Solution
import com.labwhisper.biotech.finaldilution.solution.appmodel.StartupAppModel
import com.labwhisper.biotech.finaldilution.util.button
import com.labwhisper.biotech.finaldilution.util.listView
import com.labwhisper.biotech.finaldilution.util.textView

class StartupActivity : AppCompatActivity() {

    private lateinit var appModel: StartupAppModel

    override fun onCreate(savedInstanceState: Bundle?) {
        appModel = StartupAppModel((applicationContext as ApplicationContext).solutionGateway)
        super.onCreate(savedInstanceState)
        Log.d(TAG, "On create Startup Activity")
        setContentView(R.layout.startup)
        val newSolutionButton = button(R.id.addNewSolutionButton)
        newSolutionButton.setOnClickListener {
            SolutionNameDialog(this, appModel).forCreate().show()
        }
        appModel.solutionList.observe(this,
            Observer { solutions -> solutions?.let { refreshSolutionList(solutions) } })
    }

    override fun onResume() {
        super.onResume()
        //TODO Remove after adding observer pattern to business model
        appModel.refresh()
    }

    private fun refreshSolutionList(solutionList: List<Solution>) {

        val solutionListView = listView(R.id.solutionListView)
        val solutionListAdapter = object : ArrayAdapter<Solution>(
            this,
            R.layout.solution_list_item,
            R.id.solution_list_text1,
            solutionList
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val solution = solutionList[position]
                view.textView(R.id.solution_list_text1).text = solution.name
                view.textView(R.id.solution_list_text2).text = solution.displayString()
                return view
            }
        }
        solutionListView.adapter = solutionListAdapter
        solutionListView.setOnItemClickListener { parent, _, position, _ ->
            enterSolution(parent.adapter.getItem(position) as Solution)
        }
        registerForContextMenu(solutionListView)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?
    ) = menuInflater.inflate(R.menu.menu_solution, menu)

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        if (item == null) {
            return super.onContextItemSelected(null)
        }
        return when (item.itemId) {
            R.id.action_rename -> {
                val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
                val listView = info.targetView.parent as ListView
                val solution = listView.adapter.getItem(info.position) as Solution
                SolutionNameDialog(this, appModel).forRename(solution).show(); true
            }
            R.id.action_clone -> {
                val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
                val listView = info.targetView.parent as ListView
                val solution = listView.adapter.getItem(info.position) as Solution
                SolutionNameDialog(this, appModel).forClone(solution).show(); true
            }
            R.id.action_delete -> {
                val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
                val listView = info.targetView.parent as ListView
                val solution = listView.adapter.getItem(info.position) as Solution
                DeleteConfirmDialog(this, appModel).create(solution).show(); true
            }
            else -> false
        }
    }

    private fun enterSolution(solution: Solution) {
        val intent = Intent(this@StartupActivity, EditActivity::class.java)
        intent.putExtraAnItem(solution)
        intent.putExtra("CARE_TAKER", CareTaker<Solution>())
        startActivity(intent)
    }

    private fun deleteSolution(solution: Solution) {
        appModel.deleteSolution(solution)
    }

    companion object {
        private val TAG = "Startup Activity"
    }

}
