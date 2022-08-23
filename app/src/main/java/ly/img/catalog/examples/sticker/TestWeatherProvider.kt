package ly.img.catalog.examples.sticker

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ly.img.android.pesdk.backend.smart.WeatherProvider

// <code-region>
class TestWeatherProvider : WeatherProvider() {

    private lateinit var job: Job
    private var temperature: Temperature? = null

    // Unit in which temperature should be shown in the UI
    override fun getShownUnit() = Unit.Celsius

    // Called when the editor starts
    override fun onStart() {
        // Launch coroutine to fetch weather
        job = GlobalScope.launch(Dispatchers.IO) {
            // Delay to mock fetching weather
            delay(10000)
            temperature = Temperature(27.3f, Unit.Celsius)
            // Updates the temperature for the stickers on the canvas
            update()
        }
    }

    // Called when the editor is closed
    override fun onStop() {
        // cancel coroutine
        job.cancel()
    }

    // Called by the SDK to fetch the temperature
    // If the method returns null, the default text (R.string.imgly_smart_sticker_temperature_default_text) is shown
    override fun provideTemperature() = temperature
}
// <code-region>