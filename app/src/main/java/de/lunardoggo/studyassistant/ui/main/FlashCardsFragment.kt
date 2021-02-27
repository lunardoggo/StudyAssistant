package de.lunardoggo.studyassistant.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import de.lunardoggo.studyassistant.R
import de.lunardoggo.studyassistant.learning.data.FlashCardDataSource
import de.lunardoggo.studyassistant.learning.io.AppFileManager
import de.lunardoggo.studyassistant.learning.models.FlashCard
import de.lunardoggo.studyassistant.learning.models.FlashCardGroup
import de.lunardoggo.studyassistant.learning.models.Guid
import de.lunardoggo.studyassistant.ui.adapters.FlashCardGroupSpinnerAdapter
import de.lunardoggo.studyassistant.ui.adapters.FlashCardListViewAdapter
import de.lunardoggo.studyassistant.ui.listener.OnFlashcardGroupSelectedListener

class FlashCardsFragment : Fragment() {

    private lateinit var dataSource : FlashCardDataSource;
    private lateinit var groupSelectionSpinner : Spinner;
    private lateinit var flashCardList : ListView;

    private var spinnerAdapter : FlashCardGroupSpinnerAdapter? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.dataSource = FlashCardDataSource(AppFileManager(this.requireContext()));
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_flash_cards, container, false);
        this.groupSelectionSpinner = view.findViewById(R.id.groupSelectionSpinner);
        this.flashCardList = view.findViewById(R.id.flashCardsList);

        this.dataSource.addFlashCardGroup(FlashCardGroup().apply {
            this.colorHex = "#009933";
            this.subjectName = "Testsubject 1";
            this.name = "Testgroup 1";
            this.id = Guid.new();
            this.flashCards.add(FlashCard().apply {
                this.title = "Test 1";
                this.content = "Testcontent 1";
                this.type = FlashCard.FlashCardType.PLAINTEXT;
            });
            this.flashCards.add(FlashCard().apply {
                this.title = "Test 2";
                this.content = "Testcontent 2";
                this.type = FlashCard.FlashCardType.PLAINTEXT;
            });
            this.flashCards.add(FlashCard().apply {
                this.title = "Test 3";
                this.content = "Testcontent 3";
                this.type = FlashCard.FlashCardType.PLAINTEXT;
            });
        });
        this.dataSource.addFlashCardGroup(FlashCardGroup().apply {
            this.colorHex = "#0066FF";
            this.subjectName = "Testsubject 2";
            this.name = "Testgroup 2";
            this.id = Guid.new();
            this.flashCards.add(FlashCard().apply {
                this.title = "Test 1";
                this.content = "Testcontent 1 - second group";
                this.type = FlashCard.FlashCardType.PLAINTEXT;
            });
            this.flashCards.add(FlashCard().apply {
                this.title = "Test 2";
                this.content = "Testcontent 2 - second group";
                this.type = FlashCard.FlashCardType.PLAINTEXT;
            });
        });

        this.spinnerAdapter = FlashCardGroupSpinnerAdapter(this.requireContext(), this.dataSource.getFlashCardGroups());
        this.groupSelectionSpinner.adapter = this.spinnerAdapter;
        this.groupSelectionSpinner.onItemSelectedListener = OnFlashcardGroupSelectedListener().apply {
            this.itemSelectionChanged += ::onSelectedFlashCardGroupChanged;
        };

        this.flashCardList.adapter = FlashCardListViewAdapter(this.requireContext(), this.dataSource.getFlashCardGroups()[0]);

        return view;
    }

    private fun onSelectedFlashCardGroupChanged(index : Int) {
        (this.flashCardList.adapter as FlashCardListViewAdapter).apply {
            this.flashcardGroup = dataSource.getFlashCardGroups()[index];
            /*this.clear();
            this.addAll(dataSource.getFlashCardGroups()[index].flashCards);*/
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FlashCardsFragment().apply { arguments = Bundle().apply { } }
    }
}