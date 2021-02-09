package de.lunardoggo.studyassistant.config

import android.content.Context
import android.content.SharedPreferences
import de.lunardoggo.studyassistant.R

class ConfigurationLoader {

    companion object {
        public val instance = ConfigurationLoader();
    }

    public fun load(context : Context) : AppConfiguration {
        val preferences = this.getSharedPreferences(context);
        val config = AppConfiguration();

        config.studySessions.learningTimeMinutes = preferences.getInt(
                context.getString(R.string.config_studySessions_learningMinutes), Context.MODE_PRIVATE);
        config.studySessions.breakTimeMinutes = preferences.getInt(
                context.getString(R.string.config_studySessions_breakMinutes), Context.MODE_PRIVATE);

        return config;
    }

    public fun save(context : Context, config : AppConfiguration) {
        val preferences = this.getSharedPreferences(context);
        with(preferences.edit()) {
            putInt(context.getString(R.string.config_studySessions_learningMinutes), config.studySessions.learningTimeMinutes);
            putInt(context.getString(R.string.config_studySessions_breakMinutes), config.studySessions.breakTimeMinutes);
            apply();
        }
    }

    private fun getSharedPreferences(context : Context) : SharedPreferences {
        return context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
    }
}