package de.lunardoggo.studyassistant.ui.main

class RequestCodes {
    companion object {
        public const val REQUEST_ADD_STUDY_REMINDER = 10;
        public const val REQUEST_EDIT_STUDY_REMINDER = 11;
        public const val REQUEST_MAIN_ACTIVITY = 12;
    }
}

class ResultCodes {
    companion object {
        public const val RESULT_DELETE_STUDY_REMINDER = 10;
        public const val RESULT_CANCEL = 0;
        public const val RESULT_OK = 1;
    }
}