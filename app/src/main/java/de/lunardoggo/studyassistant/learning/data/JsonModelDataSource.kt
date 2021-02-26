package de.lunardoggo.studyassistant.learning.data

import android.content.Context
import de.lunardoggo.studyassistant.learning.io.AppFileManager
import de.lunardoggo.studyassistant.learning.models.PlannedSession
import org.json.JSONArray
import org.json.JSONObject
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList

class JsonModelDataSource(context : Context) : ModelDataSource {

    private val fileManager = AppFileManager(context);

    override fun getPlannedSession(date: Long): PlannedSession {
        return this.getPlannedSessions().stream().filter{_session -> _session.date == date}.findFirst().get();
    }

    override fun getPlannedSessions(): List<PlannedSession> {
        return this.getPlannedSessions(this.getSessionsJsonArray());
    }

    override fun savePlannedSession(session: PlannedSession) {

    }

    private fun getPlannedSessions(array : JSONArray) : List<PlannedSession> {
        val output = ArrayList<PlannedSession>();
        for(i in 0..array.length()) {
            output.add(this.getPlannedSession(array.getJSONObject(i)));
        }
        return output;
    }

    private fun getPlannedSession(obj : JSONObject) : PlannedSession {
        val session = PlannedSession();
        session.description = obj.getString("description");
        session.reminder = obj.getLong("reminder");
        session.date = obj.getLong("date");
        return session;
    }

    private fun getSessionsJsonArray() : JSONArray {
        val content = this.fileManager.readFileContent("data_session.json");
        return JSONArray(content);
    }
}