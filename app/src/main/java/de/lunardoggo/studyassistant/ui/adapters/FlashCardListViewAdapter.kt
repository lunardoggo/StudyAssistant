package de.lunardoggo.studyassistant.ui.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.TextView
import de.lunardoggo.studyassistant.R
import de.lunardoggo.studyassistant.learning.models.FlashCard
import de.lunardoggo.studyassistant.learning.models.FlashCardGroup
import kotlin.math.min
import kotlin.math.roundToInt


class FlashCardListViewAdapter : ArrayAdapter<FlashCard> {

    public var flashcardGroup : FlashCardGroup = FlashCardGroup()
        get() = field
        set(value) {
            field = value;
            this.clear();
            this.addAll(this.flashcardGroup.flashCards);
            this.notifyDataSetChanged();
        };

    constructor(context: Context, flashcardGroup: FlashCardGroup) : super(
        context,
        R.layout.template_plaintext_flashcard,
        flashcardGroup.flashCards
    ) {
        this.flashcardGroup = flashcardGroup;
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val flashCard = this.flashcardGroup[position];
        val viewHolder : ViewHolder;
        val view : View;
        if(convertView == null) {
            val inflater = LayoutInflater.from(this.context);
            view = inflater.inflate(R.layout.template_plaintext_flashcard, parent, false);
            viewHolder = this.getViewHolder(view).also { view.tag = it };
        } else {
            viewHolder = convertView.tag as ViewHolder;
            view = convertView;
        }

        val background = this.flashcardGroup.color;
        with(viewHolder) {
            this.background!!.setBackgroundColor(background)
            this.titleText!!.setBackgroundColor(alterColorBrightness(background, 0.8));
            this.contentText!!.text = flashCard.content;
            this.titleText!!.text = flashCard.title;
        };
        return view;
    }

    private fun alterColorBrightness(color: Int, factor: Double) : Int {
        val a = Color.alpha(color)
        val r = (Color.red(color) * factor).roundToInt();
        val g = (Color.green(color) * factor).roundToInt();
        val b = (Color.blue(color) * factor).roundToInt();
        return Color.argb(a, min(r, 255), min(g, 255), min(b, 255));
    }

    private fun getViewHolder(view: View) : ViewHolder {
        val output = ViewHolder();
        output.background = view.findViewById(R.id.flashcardBackground) as RelativeLayout;
        output.contentText = view.findViewById(R.id.flashcardContent) as TextView;
        output.titleText = view.findViewById(R.id.flashcardTitle) as TextView;
        return output;
    }

    private class ViewHolder {
        public var titleText : TextView? = null
            get set;
        public var contentText : TextView? = null
            get set;
        public var background : RelativeLayout? = null
            get set;
    }
}