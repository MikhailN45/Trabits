package com.example.trabits.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.trabits.R
import com.example.trabits.databinding.ColorPickerFragmentBinding
import com.example.trabits.models.Util
import com.example.trabits.viewmodels.HabitCustomizeViewModel

class ColorPickerDialogFragment : DialogFragment() {
    private var _binding: ColorPickerFragmentBinding? = null
    private val binding get() = _binding!!

    private val listOfRgb: Array<String> by lazy {
        requireContext().resources.getStringArray(R.array.rgb)
    }

    private val listOfHsv: Array<String> by lazy {
        requireContext().resources.getStringArray(R.array.hsv)
    }

    private val customizeViewModel: HabitCustomizeViewModel by activityViewModels()

    var color: Int = 0
    private var colors = Util.intColors
    private var currentColorForCard = colors[DEFAULT_COLOR]!!
    private var currentColorNumber = DEFAULT_COLOR

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ColorPickerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.window?.setLayout(width, height)
        }
        super.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        customizeViewModel.colorPair.observe(viewLifecycleOwner, { pairColor ->
            if (pairColor != null)
                choseColor(pairColor.second)
            else
                choseColor(DEFAULT_COLOR)
        })

        binding.setDefaultColorButton.setOnClickListener {
            choseColor(DEFAULT_COLOR)
        }

        view.apply {
            val colorCards = mutableListOf<View>(
                binding.colorCard1,
                binding.colorCard2,
                binding.colorCard3,
                binding.colorCard4,
                binding.colorCard5,
                binding.colorCard6,
                binding.colorCard7,
                binding.colorCard8,
                binding.colorCard9,
                binding.colorCard10,
                binding.colorCard11,
                binding.colorCard12,
                binding.colorCard13,
                binding.colorCard14,
                binding.colorCard15,
                binding.colorCard16
            )

            for (i in 0 until colorCards.size) {
                colorCards[i].setOnClickListener {
                    choseColor(i)
                }
            }

            binding.saveColorButton.setOnClickListener {
                customizeViewModel.setColorPair(currentColorForCard, currentColorNumber)
                dismiss()
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun choseColor(colorNumber: Int) {

        currentColorForCard = colors[colorNumber]!!
        currentColorNumber = colorNumber
        binding.chosenColorCard.setCardForegroundColor(ColorStateList.valueOf(currentColorForCard))

        binding.rgbColorTextView.text =
            requireContext().getString(R.string.RGB, listOfRgb[colorNumber])

        binding.hsvColorTextView.text =
            requireContext().getString(R.string.HSV, listOfHsv[colorNumber])

        val checkedColors = arrayOf(
            binding.checkedColor1,
            binding.checkedColor2,
            binding.checkedColor3,
            binding.checkedColor4,
            binding.checkedColor5,
            binding.checkedColor6,
            binding.checkedColor7,
            binding.checkedColor8,
            binding.checkedColor9,
            binding.checkedColor10,
            binding.checkedColor11,
            binding.checkedColor12,
            binding.checkedColor13,
            binding.checkedColor14,
            binding.checkedColor15,
            binding.checkedColor16
        )

        checkedColors.forEach { it.visibility = INVISIBLE }
        if (colorNumber != 16) {
            checkedColors[colorNumber].visibility = VISIBLE
        }
    }

    companion object {
        const val TAG = "ColorPicker"
        const val DEFAULT_COLOR = 16
        const val VISIBLE = View.VISIBLE
        const val INVISIBLE = View.INVISIBLE

        fun newInstance(): ColorPickerDialogFragment = ColorPickerDialogFragment()
    }
}