package wary.bhaskar.unitconverter

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import androidx.fragment.app.viewModels
import wary.bhaskar.unitconverter.databinding.FragmentConverterBinding

class ConverterFragment : Fragment() {
    private val converterViewModel: ConverterViewModel by viewModels()
    private var _binding: FragmentConverterBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConverterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.iconReplaceUnitTypes.setOnClickListener {
            animateView(requireContext(), binding.iconReplaceUnitTypes, R.anim.icon_rotation_anim)
            converterViewModel.changeState()
            animateView(requireContext(), binding.cardOne, R.anim.fade_in)
            animateView(requireContext(), binding.cardTwo, R.anim.fade_in)
        }

        /**
         * Swapping unit type textViews of two cards
         */
        converterViewModel.millilitresToOuncesState.observe(viewLifecycleOwner) {
            if (converterViewModel.millilitresToOuncesState.value == true) {
                binding.unitTypeTextviewCardOne.text = getString(R.string.millilitres)
                binding.unitTypeTextviewCardTwo.text = getString(R.string.ounces)
            } else {
                binding.unitTypeTextviewCardOne.text = getString(R.string.ounces)
                binding.unitTypeTextviewCardTwo.text = getString(R.string.millilitres)
            }
            binding.finalResultTextView.text = null
        }

        /**
         * Displaying the final converted result
         */
        converterViewModel.finalResult.observe(viewLifecycleOwner) { newFinalResult ->
            binding.finalResultTextView.text = getString(
                R.string.final_result,
                newFinalResult
            )
        }

        /**
         * In case, invalid amount input occurs
         */
        converterViewModel.isAmountInvalid.observe(viewLifecycleOwner) {
            if (converterViewModel.isAmountInvalid.value == true) {
                binding.inputAmountTextField.isErrorEnabled = true
                binding.inputAmountEditText.error = "Invalid amount"
            } else binding.inputAmountTextField.isCounterEnabled = false
        }

        /**
         * converting the input amount when tapped
         */
        binding.calculateButton.setOnClickListener { convertInputAmount() }
    }

    private fun animateView(context: Context, view: View, @AnimRes animationId: Int) {
        val animation = AnimationUtils.loadAnimation(context, animationId)
        view.startAnimation(animation)
    }

    private fun convertInputAmount() {
        val inputAmount = binding.inputAmountEditText.text.toString()
        if (binding.roundUpSwitch.isChecked) {
            converterViewModel.calculate(inputAmount.toDoubleOrNull(), true)
        } else converterViewModel.calculate(inputAmount.toDoubleOrNull(), false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}