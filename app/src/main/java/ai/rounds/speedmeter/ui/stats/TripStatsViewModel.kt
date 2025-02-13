package ai.rounds.speedmeter.ui.stats

import ai.rounds.speedmeter.db.access.SessionAccess
import ai.rounds.speedmeter.utils.Formatter
import ai.rounds.speedmeter.utils.SingleLiveEvent
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TripStatsViewModel(application: Application) : AndroidViewModel(application) {

    private val _sessionStartLd = MutableLiveData<String>()
    val sessionStartLd: LiveData<String> = _sessionStartLd
    private val _sessionEndLd = MutableLiveData<String>()
    val sessionEndLd: LiveData<String> = _sessionEndLd
    private val _sessionDurationLd = MutableLiveData<String>()
    val sessionDurationLd: LiveData<String> = _sessionDurationLd
    private val _sessionDistanceLd = MutableLiveData<String>()
    val sessionDistanceLd: LiveData<String> = _sessionDistanceLd
    private val _sessionSpeedLd = MutableLiveData<String>()
    val sessionSpeedLd: LiveData<String> = _sessionSpeedLd
    private val _sessionStartLocationLd = MutableLiveData<String>()
    val sessionStartLocationLd: LiveData<String> = _sessionStartLocationLd
    private val _sessionEndLocationLd = MutableLiveData<String>()
    val sessionEndLocationLd: LiveData<String> = _sessionEndLocationLd

    private val _closeEvent = SingleLiveEvent<Unit>()
    val closeEvent: LiveData<Unit> = _closeEvent


    fun loadSession(sessionId: String?) {
        val access = SessionAccess(getApplication())
        access.openToRead()
        val session = if (!sessionId.isNullOrEmpty()) {
            access.getTrackingSessionById(sessionId)
        } else {
            access.getLastTrackingSession()
        }
        access.close()

        if (session != null) {
            val df = SimpleDateFormat("kk:mm:ss", Locale.FRANCE)
            _sessionStartLd.postValue(df.format(Date(session.startTime)))
            _sessionEndLd.postValue(df.format(Date(session.endTime)))
            _sessionDurationLd.postValue(
                Formatter.getFormattedTime(session.endTime - session.startTime)
            )
            _sessionDistanceLd.postValue(Formatter.getFormattedDistance(session.distance))
            _sessionSpeedLd.postValue(
                Formatter.getKilometersPerHour(
                    session.averageSpeed
                )
            )
            _sessionStartLocationLd.postValue(
                Formatter.getFormattedCoordinates(
                    session.startLatitude,
                    session.startLongitude
                )
            )
            _sessionEndLocationLd.postValue(
                Formatter.getFormattedCoordinates(
                    session.endLatitude,
                    session.endLongitude
                )
            )
        } else {
            _closeEvent.call()
        }
    }

}