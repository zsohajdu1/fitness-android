package aut.bme.hu.fitness.presentation.steps

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.core.content.edit
import java.time.LocalDate

class StepCounterManager(
    private val context: Context,
    private val onStepCountChanged: (Int) -> Unit
) : SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var stepCounterSensor: Sensor? = null
    private var initialStepCount: Int? = null

    fun startListening() {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepCounterSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        stepCounterSensor?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    fun stopListening() {
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        val totalSteps = event.values[0].toInt()
        if (initialStepCount == null) {
            initialStepCount = loadBaseline() ?: totalSteps
            saveBaseline(initialStepCount!!)
        }

        val dailySteps = totalSteps - (initialStepCount ?: 0)
        onStepCountChanged(dailySteps)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun saveBaseline(value: Int) {
        context.getSharedPreferences("steps", Context.MODE_PRIVATE)
            .edit {
                putInt("baseline_${LocalDate.now()}", value)
            }
    }

    private fun loadBaseline(): Int? {
        val prefs = context.getSharedPreferences("steps", Context.MODE_PRIVATE)
        return if (prefs.contains("baseline_${LocalDate.now()}"))
            prefs.getInt("baseline_${LocalDate.now()}", 0)
        else null
    }
}
