package de.lunardoggo.studyassistant.learning.io

import android.content.Context
import java.lang.Exception
import java.lang.StringBuilder
import java.util.*

class AppFileManager(context : Context) {

    private val context : Context = context;

    public fun readFileContent(filename : String) : String {
        val builder = StringBuilder();
        try {
            this.context.assets.open(filename).use { stream ->
                Scanner(stream).use { scanner ->
                    while (scanner.hasNextLine()) {
                        builder.appendLine(scanner.nextLine());
                    }
                }
            }
        } catch (ex : Exception) {}
        return builder.toString();
    }
}