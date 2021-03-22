package de.lunardoggo.studyassistant.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import de.lunardoggo.studyassistant.R
import de.lunardoggo.studyassistant.learning.models.FlashCardGroup

class FlashCardGroupSpinnerAdapter : ArrayAdapter<FlashCardGroup> {

    companion object {
        private const val layoutResource = R.layout.template_spinner_item_flashcardgroup;
    }

    private val groups : List<FlashCardGroup>;

    constructor(context : Context, groups : List<FlashCardGroup>) : super(context, layoutResource, groups) {
        this.groups = groups;
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return this.getItemView(position, parent);
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return this.getItemView(position, parent);
    }

    private fun getItemView(position: Int, parent: ViewGroup) : View {
        val view = LayoutInflater.from(this.context).inflate(layoutResource, parent, false);
        val group = this.groups[position];
        val title = view.findViewById<TextView>(R.id.flashcardGroupSpinnerItemText);
        title.text = group.subjectName + " - " + group.name;
        title.setBackgroundColor(group.color);
        return view;
    }
}