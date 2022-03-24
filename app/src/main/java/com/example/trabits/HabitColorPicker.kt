package com.example.trabits


import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.color_picker_fragment.*
import kotlinx.android.synthetic.main.color_picker_fragment.view.*

class ColorPickerDialogFragment : DialogFragment() {

    private val listOfRgb: Array<String> by lazy {
        requireContext().resources.getStringArray(R.array.rgb)
    }
    private val listOfHsv: Array<String> by lazy {
        requireContext().resources.getStringArray(R.array.hsv)
    }
    private val iChangeColor: IChangeColor by lazy {
        activity as IChangeColor
    }

    var color: Int = 0
    private var colors = Util.intColors
    private var curColorForCard = colors[DEFAULT_COLOR]!!
    private var curColorNumber = DEFAULT_COLOR

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.color_picker_fragment, container, false)

    override fun onStart() {

        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.window?.setLayout(width, height)
        }
        super.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val curColorNumber = requireArguments().getInt(COLOR_NUMBER_BUNDLE_ARG)

        if (curColorNumber != DEFAULT_COLOR) {
            choseColor(curColorNumber)
        } else {
            choseColor(DEFAULT_COLOR)
        }

        defaultColorButton.setOnClickListener {
            choseColor(DEFAULT_COLOR)
        }

        view.apply {
            val colorCards = mutableListOf<View>(
                colorCard1, colorCard2, colorCard3, colorCard4, colorCard5, colorCard6,
                colorCard7, colorCard8, colorCard9, colorCard10, colorCard11, colorCard12,
                colorCard13, colorCard14, colorCard15, colorCard16
            )

            for (i in 0 until colorCards.size) {
                colorCards[i].setOnClickListener {
                    choseColor(i)
                }
            }

            applyColorButton.setOnClickListener {
                iChangeColor.onChangeColor(
                    curColorForCard,
                    this@ColorPickerDialogFragment.curColorNumber
                )
                dismiss()
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    interface IChangeColor {
        fun onChangeColor(newColor: Int, newColorNumber: Int)
    }


    private fun choseColor(colorNumber: Int) {

        curColorForCard = colors[colorNumber]!!
        curColorNumber = colorNumber

        chosenColorCard.setCardForegroundColor(ColorStateList.valueOf(curColorForCard))

        rgbColorTextView.text =
            requireContext().getString(R.string.RGB, listOfRgb[colorNumber])

        hsvColorTextView.text =
            requireContext().getString(R.string.HSV, listOfHsv[colorNumber])

        val checkedColors = arrayOf(
            checkedColor1, checkedColor2, checkedColor3,
            checkedColor4, checkedColor5, checkedColor6, checkedColor7,
            checkedColor8, checkedColor9, checkedColor10, checkedColor11,
            checkedColor12, checkedColor13, checkedColor14, checkedColor15,
            checkedColor16,
        )

        checkedColors.forEach { it.visibility = INVISIBLE }
        if (colorNumber != 16) {
            checkedColors[colorNumber].visibility = VISIBLE
        }
    }

    companion object {
        const val TAG = "ColorPicker"
        const val COLOR_NUMBER_BUNDLE_ARG = "curColorNumber"
        const val DEFAULT_COLOR = 16
        const val VISIBLE = View.VISIBLE
        const val INVISIBLE = View.INVISIBLE


        fun newInstance(curColorNumber: Int): ColorPickerDialogFragment {
            val args = Bundle().apply {
                putInt(COLOR_NUMBER_BUNDLE_ARG, curColorNumber)
            }
            val fragment = ColorPickerDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}