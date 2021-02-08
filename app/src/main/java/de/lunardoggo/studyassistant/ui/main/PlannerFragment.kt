package de.lunardoggo.studyassistant.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import de.lunardoggo.studyassistant.R
import java.time.LocalDate

class PlannerFragment : Fragment() {

    private lateinit var calendar : CalendarView;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val fragment = inflater.inflate(R.layout.fragment_planner, container, false)
        this.calendar = fragment.findViewById(R.id.plannerCalendar);
        this.calendar.setOnDateChangeListener(::onSelectedDateChanged);

        val addGoalButton : Button = fragment.findViewById(R.id.addGoalButton);
        addGoalButton.setOnClickListener(::onAddGoalClicked);

        return fragment;
    }

    private fun onAddGoalClicked(view : View) {
    }

    private fun onSelectedDateChanged(view : CalendarView, year : Int, month : Int, dayOfMonth : Int) {
        this.updateDatePlanDisplay();
    }

    private fun updateDatePlanDisplay() {

    }

    private fun getSelectedDate() : LocalDate {
        return LocalDate.ofEpochDay(this.calendar.date);
    }

    companion object {
        @JvmStatic
        fun newInstance() = PlannerFragment().apply { arguments = Bundle().apply {} }
    }
}