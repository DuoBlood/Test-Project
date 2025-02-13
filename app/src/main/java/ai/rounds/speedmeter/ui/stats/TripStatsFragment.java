package ai.rounds.speedmeter.ui.stats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ai.rounds.speedmeter.R;
import ai.rounds.speedmeter.db.access.SessionAccess;
import ai.rounds.speedmeter.ui.speed.SpeedViewViewModel;
import ai.rounds.speedmeter.utils.Formatter;
import ai.rounds.speedmeter.models.Session;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Tracking session summary fragment
 */
public class TripStatsFragment extends Fragment {

    private View mRootView;

    private TripStatsViewModel viewModel;

    public TripStatsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_summary, container, false);

        viewModel = new ViewModelProvider
                .AndroidViewModelFactory(getActivity().getApplication())
                .create(TripStatsViewModel.class);

        init();
        observeData();
        return mRootView;
    }

    private void observeData() {
        viewModel.getCloseEvent().observe(this, unit -> {
            getActivity().finish();
        });
        viewModel.getSessionStartLd().observe(this, txt -> {
            ((TextView) mRootView.findViewById(R.id.txtv_session_start)).setText(txt);
        });
        viewModel.getSessionEndLd().observe(this, txt -> {
            ((TextView) mRootView.findViewById(R.id.txtv_session_end)).setText(txt);
        });
        viewModel.getSessionDurationLd().observe(this, txt -> {
            ((TextView) mRootView.findViewById(R.id.txtv_session_duration)).setText(txt);
        });
        viewModel.getSessionDistanceLd().observe(this, txt -> {
            ((TextView) mRootView.findViewById(R.id.txtv_session_distance)).setText(txt);
        });
        viewModel.getSessionSpeedLd().observe(this, txt -> {
            ((TextView) mRootView.findViewById(R.id.txtv_session_speed)).setText(txt);
        });
        viewModel.getSessionStartLocationLd().observe(this, txt -> {
            ((TextView) mRootView.findViewById(R.id.txtv_session_start_location)).setText(txt);
        });
        viewModel.getSessionEndLocationLd().observe(this, txt -> {
            ((TextView) mRootView.findViewById(R.id.txtv_session_end_location)).setText(txt);
        });
    }


    private void init() {
        viewModel.loadSession(getActivity().getIntent().getStringExtra(TripStatsActivity.EXTRA_SESSION_ID));
    }
}
