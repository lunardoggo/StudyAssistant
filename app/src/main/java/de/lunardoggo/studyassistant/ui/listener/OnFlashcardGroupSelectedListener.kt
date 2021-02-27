package de.lunardoggo.studyassistant.ui.listener

import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.*
import de.lunardoggo.studyassistant.events.Event

class OnFlashcardGroupSelectedListener :  OnItemSelectedListener {

    public val itemSelectionChanged = Event<Int>();

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        this.itemSelectionChanged.invoke(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}