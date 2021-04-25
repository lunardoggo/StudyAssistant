package de.lunardoggo.studyassistant.ui.adapters

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import de.lunardoggo.studyassistant.R
import de.lunardoggo.studyassistant.learning.models.Importance
import de.lunardoggo.studyassistant.learning.models.StudyReminder
import java.util.*

class StudyReminderListViewAdapter(context: Context, resource: Int) : ArrayAdapter<StudyReminder>(context, resource) {

    private val format = DateFormat.getDateFormat(this.context);
    private val inflater = LayoutInflater.from(this.context);
    private val templateResource : Int = resource;

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val reminder = this.getItem(position) as StudyReminder;
        return convertView ?: getInflatedView(parent).apply {
            setViewValues(this, reminder);
        };
    }

    private fun setViewValues(view : View, reminder : StudyReminder) {
        val viewHolder = view.tag as ViewHolder;
        viewHolder.importanceView!!.setBackgroundColor(this.getImportanceColor(reminder.importance));
        viewHolder.dateText!!.text = format.format(Date.from(reminder.date));
        viewHolder.titleText!!.text = reminder.title;
    }

    private fun getImportanceColor(importance : Importance) : Int {
        return when(importance) {
            Importance.HIGH -> this.context.getColor(R.color.lightRed);
            Importance.NORMAL -> this.context.getColor(R.color.green);
            Importance.LOW -> this.context.getColor(R.color.blue);
            else -> throw NotImplementedError();
        }
    }

    private fun getInflatedView(parent : ViewGroup) : View {
        val viewHolder = ViewHolder();
        val view = inflater.inflate(this.templateResource, parent, false);
        viewHolder.importanceView = view.findViewById(R.id.reminderImportance);
        viewHolder.titleText = view.findViewById(R.id.reminderTitle);
        viewHolder.dateText = view.findViewById(R.id.reminderDate);
        view.tag = viewHolder;
        return view;
    }

    companion object {
        private class ViewHolder {
            public var importanceView : View? = null;
            public var titleText : TextView? = null;
            public var dateText : TextView? = null;
        }
    }
}