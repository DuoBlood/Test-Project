package ai.rounds.speedmeter.ui.speed

import ai.rounds.speedmeter.R
import ai.rounds.speedmeter.SpeedMeterApp
import ai.rounds.speedmeter.repo.TrackerRepo
import ai.rounds.speedmeter.services.SpeedTrackingService
import ai.rounds.speedmeter.utils.Formatter
import ai.rounds.speedmeter.utils.PermissionHelper
import ai.rounds.speedmeter.utils.SingleLiveEvent
import android.app.Application
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SpeedViewViewModel(application: Application) : AndroidViewModel(application) {

    private val _buttonImageIdLd = MutableLiveData<Int>()
    val buttonImageIdLd: LiveData<Int> = _buttonImageIdLd
    private val _speedTextLd = MutableLiveData<CharSequence>()
    val speedTextLd: LiveData<CharSequence> = _speedTextLd
    private val _speedValueLd = MutableLiveData<Float>()
    val speedValueLd: LiveData<Float> = _speedValueLd

    private val _navigateToStatusScreenEvent = SingleLiveEvent<String?>()
    val navigateToStatusScreenEvent: LiveData<String?> = _navigateToStatusScreenEvent
    private val _showGPSMandatoryAlertEvent = SingleLiveEvent<Unit>()
    val showGPSMandatoryAlertEvent: LiveData<Unit> = _showGPSMandatoryAlertEvent
    private val _showNotificationPermissionExplanationEvent = SingleLiveEvent<Unit>()
    val showNotificationPermissionExplanationEvent: LiveData<Unit> =
        _showNotificationPermissionExplanationEvent
    private val _showLocationPermissionExplanationEvent = SingleLiveEvent<Unit>()
    val showLocationPermissionExplanationEvent: LiveData<Unit> =
        _showLocationPermissionExplanationEvent

    private var locationManager: LocationManager? = null


    init {
        _buttonImageIdLd.value = R.drawable.icon_play
        _speedTextLd.value = application.getText(R.string.stop)
        locationManager = application.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
    }

    fun toggleTrackingAction() {
        if (PermissionHelper.hasLocationPermission(getApplication())) {
            if (PermissionHelper.hasNotificationPermission(getApplication())) {
                if (isGPSEnabled()) {
                    if (!SpeedTrackingService.isRunning(getApplication())) {
                        startTracking();
                    } else {
                        stopTracking();
                    }
                } else {
                    _showGPSMandatoryAlertEvent.call()
                }
            } else {
                _showNotificationPermissionExplanationEvent.call()
            }
        } else {
            _showLocationPermissionExplanationEvent.call()
        }
    }

    private fun isGPSEnabled(): Boolean {
        return locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false
    }

    private fun startTracking() {
        TrackerRepo.apply {
            onSpeedUpdated = ::onSpeedUpdated
            onSessionEnded = ::onSessionEnded
        }
        val serviceIntent = Intent(
            getApplication(),
            SpeedTrackingService::class.java
        )
        ContextCompat.startForegroundService(getApplication<SpeedMeterApp>(), serviceIntent)
        _speedTextLd.postValue(getApplication<SpeedMeterApp>().getString(R.string.enabling))
        _buttonImageIdLd.postValue(R.drawable.icon_stop)
    }

    private fun stopTracking() {
        val serviceIntent = Intent(
            getApplication(),
            SpeedTrackingService::class.java
        )
        getApplication<SpeedMeterApp>().stopService(serviceIntent)
        _speedTextLd.postValue(getApplication<SpeedMeterApp>().getString(R.string.stop))
        _buttonImageIdLd.postValue(R.drawable.icon_play)
    }

    private fun onSpeedUpdated(speed: Float) {
        _speedTextLd.postValue(Formatter.getKilometersPerHour(speed))
        _speedValueLd.postValue(speed * 3600 / 1000)
    }

    private fun onSessionEnded(sessionId: String?) {
        _speedTextLd.postValue(getApplication<SpeedMeterApp>().getString(R.string.stop))
        _speedValueLd.postValue(0f)
        _buttonImageIdLd.postValue(R.drawable.icon_play)
        _navigateToStatusScreenEvent.value = sessionId
    }

}