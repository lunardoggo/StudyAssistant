package de.lunardoggo.studyassistant.events

class Event<T> {
    private val listeners = mutableSetOf<(T) -> Unit>();

    operator fun plusAssign(listener : (T) -> Unit) {
        this.listeners.add(listener);
    }

    operator fun minusAssign(listener : (T) -> Unit) {
        if(this.listeners.contains(listener)) {
            this.listeners.remove(listener);
        }
    }

    operator fun invoke(value : T) {
        for(listener in this.listeners) {
            listener(value);
        }
    }
}