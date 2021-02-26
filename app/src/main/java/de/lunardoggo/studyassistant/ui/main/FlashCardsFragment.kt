package de.lunardoggo.studyassistant.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import de.lunardoggo.studyassistant.R
import de.lunardoggo.studyassistant.learning.data.FlashCardDataSource
import de.lunardoggo.studyassistant.learning.io.AppFileManager
import de.lunardoggo.studyassistant.learning.models.FlashCard
import de.lunardoggo.studyassistant.learning.models.FlashCardGroup
import de.lunardoggo.studyassistant.learning.models.Guid
import de.lunardoggo.studyassistant.ui.adapters.FlashCardListViewAdapter

class FlashCardsFragment : Fragment() {

    private lateinit var dataSource : FlashCardDataSource;
    private lateinit var flashCardList : ListView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.dataSource = FlashCardDataSource(AppFileManager(this.requireContext()));
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_flash_cards, container, false);
        this.flashCardList = view.findViewById(R.id.flashCardsList) as ListView;

        this.dataSource.addFlashCardGroup(FlashCardGroup().apply {
            this.colorHex = "#AABBDD";
            this.subjectName = "Testsubject";
            this.name = "Testgroup";
            this.id = Guid.new();
            this.flashCards = ArrayList<FlashCard>();
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
        })
        this.flashCardList.adapter = FlashCardListViewAdapter(this.requireContext(), this.dataSource.getFlashCardGroups()[0]);
        return view;
    }

    companion object {
        @JvmStatic
        fun newInstance() = FlashCardsFragment().apply { arguments = Bundle().apply { } }
    }
}