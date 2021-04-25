package de.lunardoggo.studyassistant.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.ListView
import de.lunardoggo.studyassistant.MainActivity
import de.lunardoggo.studyassistant.R
import de.lunardoggo.studyassistant.learning.data.StudyAssistantDataSource
import de.lunardoggo.studyassistant.learning.models.StudyReminder
import de.lunardoggo.studyassistant.learning.utility.StudyReminderIntentConverter
import de.lunardoggo.studyassistant.ui.activities.StudyReminderEditorActivity
import de.lunardoggo.studyassistant.ui.adapters.StudyReminderListViewAdapter
import java.time.LocalDate

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

    private fun onStudyRemindersChanged(studyReminder: StudyReminder) {
        this.updateReminderList();
    }

    private fun onAddReminderClicked(view : View) {
        val intent = Intent(this.requireContext(), StudyReminderEditorActivity::class.java);
        this.requireActivity().startActivityForResult(intent, RequestCodes.REQUEST_STUDY_REMINDER);
    }

    private fun updateReminderList() {
        this.reminderListAdapter.clear();
        this.reminderListAdapter.addAll(this.dataSource.getStudyReminders());
    }

    companion object {
        @JvmStatic
        fun newInstance() = PlannerFragment().apply { arguments = Bundle().apply {} }
    }
}