package ru.ashirobokov.spotlight.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.ashirobokov.spotlight.R
import ru.ashirobokov.spotlight.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private val TAG: String? = SettingsFragment::class.simpleName
    private val viewModel: DictionaryViewModel by activityViewModels()

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout XML file and return a binding object instance
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        Log.d(TAG, "SettingsFragment created/re-created!")
//        Log.d(
//            "GameFragment", "Word: ${viewModel.currentScrambledWord} " +
//                    "Score: ${viewModel.score} WordCount: ${viewModel.currentWordCount}"
//        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dictionaryViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.floatingActionButton.setOnClickListener {
            Toast.makeText(context,"НАЧИНАЕМ ИГРУ!", Toast.LENGTH_LONG).show()
            /**
             * Should be re-done cause it does not work properly
              */
            viewModel.reinitializeData()
            findNavController().navigate(R.id.action_settingsFragment_to_dictionaryFragment)

        }

    }

}