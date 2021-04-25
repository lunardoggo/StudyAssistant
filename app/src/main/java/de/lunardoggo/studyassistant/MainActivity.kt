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
        this.setContentView(R.layout.activity_main);
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
        if(resultCode == ResultCodes.RESULT_OK) {
            when(requestCode) {
                RequestCodes.REQUEST_ADD_STUDY_REMINDER -> this.saveStudyReminder(data!!);
                RequestCodes.REQUEST_EDIT_STUDY_REMINDER -> this.saveStudyReminder(data!!);
            }
        } else if(resultCode == ResultCodes.RESULT_DELETE_STUDY_REMINDER) {
            this.deleteStudyReminder(data!!);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private fun saveStudyReminder(data : Intent) {
        val reminder = StudyReminderIntentConverter.instance.convertFrom(data);
        if(reminder.id == StudyReminder.DefaultId) {
            this.dataSource.insertStudyReminder(reminder);
        } else {
            this.dataSource.updateStudyReminder(reminder);
        }
        this.studyRemindersChanged.invoke(reminder);
    }

    private fun deleteStudyReminder(data : Intent) {
        val reminder = StudyReminderIntentConverter.instance.convertFrom(data);
        if(reminder.id != StudyReminder.DefaultId) {
            this.dataSource.deleteStudyReminder(reminder);
        }
        this.studyRemindersChanged.invoke(reminder);
    }

    companion object {
        public lateinit var instance : MainActivity
            get;
    }
}