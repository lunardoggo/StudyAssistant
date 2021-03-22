package de.lunardoggo.studyassistant

import android.app.Notification
import android.content.Intent
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Toast
import de.lunardoggo.studyassistant.android.NotificationHelper
import de.lunardoggo.studyassistant.ui.activities.SettingsActivity
import de.lunardoggo.studyassistant.ui.adapters.SectionsPagerAdapter
import de.lunardoggo.studyassistant.ui.main.FlashCardEditorActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout : TabLayout;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            this.showSettingsPage();
        } else {
            this.showEditFlashcardsView();
        }
    }

    private fun showSettingsPage() {
        val intent = Intent(this, SettingsActivity::class.java);
        this.startActivity(intent);
    }

    private fun showEditFlashcardsView() {
        val intent = Intent(this, FlashCardEditorActivity::class.java);
        this.startActivity(intent);
        NotificationHelper.showToastShortDuration(this.applicationContext, "Edit page coming soon!", Toast.LENGTH_SHORT);
    }
}