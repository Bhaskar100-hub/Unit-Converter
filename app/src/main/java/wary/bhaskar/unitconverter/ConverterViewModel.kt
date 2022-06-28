package wary.bhaskar.unitconverter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.math.RoundingMode

class ConverterViewModel : ViewModel() {

    //state of conversion
    private var _millilitresToOuncesState = MutableLiveData(true)
    val millilitresToOuncesState: LiveData<Boolean> get() = _millilitresToOuncesState

    //when there is an invalid amount
    private var _isAmountInvalid = MutableLiveData<Boolean>()
    val isAmountInvalid: LiveData<Boolean> get() = _isAmountInvalid

    //final result
    private val _finalResult = MutableLiveData<String>()
    val finalResult: LiveData<String> get() = _finalResult

    fun changeState() {
        _millilitresToOuncesState.value = !_millilitresToOuncesState.value!!
    }

    private fun formatLongDecimal(
        decimalNumber: Double,
        decimalPlaces: Int
    ): String = decimalNumber
        .toBigDecimal()
        .setScale(decimalPlaces, RoundingMode.HALF_EVEN)
        .toString()

    fun calculate(amount: Double?, isRounded: Boolean) {
        /**
         *Checking input amount validity
         */
        if (amount == null || amount == 0.0) {
            _isAmountInvalid.value = true
        } else {
            _isAmountInvalid.value = false

            /**
             * Checking the conversion state
             */
            if (_millilitresToOuncesState.value == true) {
                val result: Double = amount / 29.574
                _finalResult.value = if (isRounded) {
                    formatLongDecimal(result, 1)
                } else formatLongDecimal(result, 3)
            } else {
                val result: Double = amount * 29.5735
                _finalResult.value = if (isRounded) {
                    formatLongDecimal(result, 1)
                } else formatLongDecimal(result, 3)

            }
        }
    }
}