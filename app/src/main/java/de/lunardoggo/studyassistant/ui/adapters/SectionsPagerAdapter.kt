package de.lunardoggo.studyassistant.ui.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import de.lunardoggo.studyassistant.R
import de.lunardoggo.studyassistant.ui.main.FlashCardsFragment
import de.lunardoggo.studyassistant.ui.main.PlannerFragment
import de.lunardoggo.studyassistant.ui.main.TimerFragment

private val TAB_TITLES = arrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2,
        R.string.tab_text_3
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val flashCards : FlashCardsFragment = FlashCardsFragment.newInstance();
    private val planner : PlannerFragment = PlannerFragment.newInstance();
    private val timer : TimerFragment = TimerFragment.newInstance();

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> this.timer;
            1 -> this.planner;
            2 -> this.flashCards;
            else -> throw NotImplementedError();
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 3
    }
}