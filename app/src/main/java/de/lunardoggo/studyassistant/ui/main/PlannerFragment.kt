package de.lunardoggo.studyassistant.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import de.lunardoggo.studyassistant.MainActivity
import de.lunardoggo.studyassistant.R
import de.lunardoggo.studyassistant.learning.data.StudyAssistantDataSource
import de.lunardoggo.studyassistant.learning.models.Importance
import de.lunardoggo.studyassistant.learning.models.StudyReminder
import de.lunardoggo.studyassistant.learning.utility.StudyReminderIntentConverter
import de.lunardoggo.studyassistant.ui.activities.StudyReminderEditorActivity
import de.lunardoggo.studyassistant.ui.adapters.StudyReminderListViewAdapter
import java.time.LocalDate
import java.time.ZoneId

class PlannerFragment : Fragment() {

    private lateinit var reminderListAdapter : StudyReminderListViewAdapter;
    private lateinit var dataSource: StudyAssistantDataSource;
    private lateinit var reminderList : ListView;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (this.activity as MainActivity).studyRemindersChanged += ::onStudyRemindersChanged;
        val fragment = inflater.inflate(R.layout.fragment_planner, container, false);

        this.reminderListAdapter = StudyReminderListViewAdapter(this.requireContext(), R.layout.template_study_reminder_listview_item);

        this.reminderList = fragment.findViewById(R.id.reminderList);
        this.reminderList.adapter = this.reminderListAdapter;
        this.reminderList.setOnItemClickListener(::onListViewItemClicked)

        val addReminderButton : Button = fragment.findViewById(R.id.addGoalButton);
        addReminderButton.setOnClickListener(::onAddReminderClicked);

        this.dataSource = MainActivity.instance.dataSource;

        this.updateReminderList();
        return fragment;
    }

    override fun onDestroy() {
        super.onDestroy();
        (this.activity as MainActivity).studyRemindersChanged -= ::onStudyRemindersChanged;
    }


    private fun onListViewItemClicked(adapterView: AdapterView<*>?, clickedView: View?, position: Int, id: Long) {
        val reminder = this.reminderList.getItemAtPosition(position) as StudyReminder;
        this.showStudyReminderEditor(reminder, RequestCodes.REQUEST_SHOW_EDIT_STUDY_REMINDER);
    }

    private fun onStudyRemindersChanged(studyReminder: StudyReminder) {
        this.updateReminderList();
    }

    private fun onAddReminderClicked(view : View) {
        val reminder = StudyReminder();
        reminder.date = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).plusMinutes(12 * 60).toInstant();
        reminder.importance = Importance.NORMAL;
        this.showStudyReminderEditor(reminder, RequestCodes.REQUEST_SHOW_ADD_STUDY_REMINDER);
    }

    private fun showStudyReminderEditor(reminder : StudyReminder, requestCode : Int) {
        val intent = StudyReminderIntentConverter.instance.convertTo(reminder, Intent(this.requireContext(), StudyReminderEditorActivity::class.java));
        this.requireActivity().startActivityForResult(intent, requestCode);
    }

    private fun updateReminderList() {
        this.reminderListAdapter.clear();
        this.reminderListAdapter.addAll(this.dataSource.getPendingStudyReminders());
        this.reminderListAdapter.notifyDataSetChanged();
    }

    companion object {
        @JvmStatic
        fun newInstance() = PlannerFragment().apply { arguments = Bundle().apply {} }
    }
}