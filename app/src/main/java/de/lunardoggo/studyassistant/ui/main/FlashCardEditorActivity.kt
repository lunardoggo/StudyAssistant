package de.lunardoggo.studyassistant.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.larswerkman.holocolorpicker.ColorPicker
import com.larswerkman.holocolorpicker.SVBar
import com.larswerkman.holocolorpicker.SaturationBar
import com.larswerkman.holocolorpicker.ValueBar
import de.lunardoggo.studyassistant.R

class FlashCardEditorActivity : AppCompatActivity() {

    private lateinit var saveButton : Button;
    private lateinit var groupPicker : Spinner;
    private lateinit var titleInput : EditText;
    private lateinit var contentInput : EditText;
    private lateinit var colorPicker : ColorPicker;
    private lateinit var saturationBar : SaturationBar;
    private lateinit var colorValueBar : ValueBar;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flash_card_editor)

        this.groupPicker = this.findViewById(R.id.groupSelectionSpinner);
        this.saturationBar = this.findViewById(R.id.colorSaturationBar);
        this.contentInput = this.findViewById(R.id.flashcardContent);
        this.colorValueBar = this.findViewById(R.id.colorValueBar);
        this.titleInput = this.findViewById(R.id.flashcardTitle);
        this.colorPicker = this.findViewById(R.id.colorPicker);

        this.linkColorPickerControls(this.colorPicker, this.saturationBar, this.colorValueBar);
    }

    private fun linkColorPickerControls(colorPicker: ColorPicker, saturationBar: SaturationBar, valueBar: ValueBar) {
        colorPicker.addSaturationBar(saturationBar);
        colorPicker.addValueBar(valueBar);
    }
}