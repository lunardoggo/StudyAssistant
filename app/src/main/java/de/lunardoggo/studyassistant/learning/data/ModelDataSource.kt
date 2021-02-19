package de.lunardoggo.studyassistant.learning.data

import de.lunardoggo.studyassistant.learning.models.PlannedSession

interface ModelDataSource {

    public fun getPlannedSession(date: Long): PlannedSession;
    public fun savePlannedSession(session: PlannedSession);
    public fun getPlannedSessions(): List<PlannedSession>;
}
