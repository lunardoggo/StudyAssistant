package de.lunardoggo.studyassistant

import android.content.Intent
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button
import de.lunardoggo.studyassistant.events.Event
import de.lunardoggo.studyassistant.learning.data.StudyAssistantDataSource
import de.lunardoggo.studyassistant.learning.models.StudyReminder
import de.lunardoggo.studyassistant.learning.utility.StudyReminderIntentConverter
import de.lunardoggo.studyassistant.ui.activities.SettingsActivity
import de.lunardoggo.studyassistant.ui.adapters.SectionsPagerAdapter
import de.lunardoggo.studyassistant.ui.main.RequestCodes
import de.lunardoggo.studyassistant.ui.main.ResultCodes

class MainActivity : AppCompatActivity() {

    public val studyRemindersChanged = Event<StudyReminder>();

    public lateinit var dataSource : StudyAssistantDataSource
        get;

    private lateinit var tabLayout : TabLayout;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;

        this.dataSource = StudyAssistantDataSource(this.applicationContext);

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager);
        val viewPager: ViewPager = this.findViewById(R.id.view_pager);
        viewPager.adapter = sectionsPagerAdapter;
        this.tabLayout = this.findViewById(R.id.tabs);
        this.tabLayout.setupWithViewPager(viewPager);

        val settingsButton : Button = this.findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(::onSettingsButtonClicked);
    }

    private fun onSettingsButtonClicked(view : View) {
        if(this.tabLayout.selectedTabPosition < 2) {
            this.showSettingsActivity();
        }
    }

    private fun showSettingsActivity() {
        val intent = Intent(this, SettingsActivity::class.java);
        this.startActivity(intent);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == RequestCodes.REQUEST_STUDY_REMINDER && resultCode == ResultCodes.RESULT_OK) {
            val reminder = StudyReminderIntentConverter.instance.convertFrom(data!!);
            if(reminder.id == StudyReminder.DefaultId) {
                this.dataSource.insertStudyReminder(reminder);
            } else {
                this.dataSource.updateStudyReminder(reminder);
            }
            this.studyRemindersChanged.invoke(reminder);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    companion object {
        public lateinit var instance : MainActivity
            get;
    }
}