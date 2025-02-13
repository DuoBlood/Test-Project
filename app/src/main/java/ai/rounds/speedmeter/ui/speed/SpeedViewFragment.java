package ai.rounds.speedmeter.ui.speed;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ai.rounds.speedmeter.R;
import ai.rounds.speedmeter.utils.PermissionHelper;
import ai.rounds.speedmeter.ui.stats.TripStatsActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SpeedViewFragment extends Fragment {

    public static final int LOCATION_PERMISSION_REQUEST_ID = 0x56;
    public static final int NOTIFICATION_PERMISSION_REQUEST_ID = 0x57;
    private FloatingActionButton toggleTrackingBtn;
    private SpeedMeterView speedMeterView;

    private TextView speedTextView;

    private View rootView;

    private SpeedViewViewModel viewModel;

    public SpeedViewFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_monitoring, container, false);

        viewModel = new ViewModelProvider
                .AndroidViewModelFactory(getActivity().getApplication())
                .create(SpeedViewViewModel.class);

        init();
        observeData();
        return rootView;
    }

    private void init() {

        toggleTrackingBtn = rootView.findViewById(R.id.fba_toggle_tracking);

        toggleTrackingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.toggleTrackingAction();
            }
        });

        speedTextView = rootView.findViewById(R.id.txtv_speed);
        speedMeterView = rootView.findViewById(R.id.smv_speedmeterview);
    }

    private void observeData() {
        viewModel.getNavigateToStatusScreenEvent().observe(this, sessionId -> {
            Intent summmaryIntent = new Intent(getActivity(), TripStatsActivity.class);
            summmaryIntent.putExtra(TripStatsActivity.EXTRA_SESSION_ID, sessionId);
            startActivity(summmaryIntent);
        });
        viewModel.getShowGPSMandatoryAlertEvent().observe(this, unit -> {
            showGPSMandatoryAlert();
        });
        viewModel.getShowLocationPermissionExplanationEvent().observe(this, unit -> {
            showLocationPermissionExplanation();
        });
        viewModel.getShowNotificationPermissionExplanationEvent().observe(this, unit -> {
            showNotificationPermissionExplanation();
        });
        viewModel.getSpeedTextLd().observe(this, txt -> {
            speedTextView.setText(txt);
        });
        viewModel.getButtonImageIdLd().observe(this, imageId -> {
            toggleTrackingBtn.setImageResource(imageId);
        });
        viewModel.getSpeedValueLd().observe(this, speed -> {
            speedMeterView.updateSpeed(speed);
        });
    }

    private void showLocationPermissionExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.location_explanation_title)
                .setMessage(R.string.location_explanation_msg)
                .setNeutralButton(R.string.understood, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PermissionHelper.requestLocationPermission(getActivity(), LOCATION_PERMISSION_REQUEST_ID);
                    }
                })
                .show();
    }

    private void showNotificationPermissionExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.notification_explanation_title)
                .setMessage(R.string.notification_explanation_msg)
                .setNeutralButton(R.string.understood, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PermissionHelper.requestNotificationPermission(getActivity(), NOTIFICATION_PERMISSION_REQUEST_ID);
                    }
                })
                .show();
    }

    private void showGPSMandatoryAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.gps_mandatory_title)
                .setMessage(R.string.gps_mandatory_msg)
                .setNeutralButton(R.string.understood, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if ((requestCode == LOCATION_PERMISSION_REQUEST_ID || requestCode == NOTIFICATION_PERMISSION_REQUEST_ID)
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            viewModel.toggleTrackingAction();
        }
    }
}
