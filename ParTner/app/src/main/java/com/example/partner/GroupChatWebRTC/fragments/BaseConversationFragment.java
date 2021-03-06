package com.example.partner.GroupChatWebRTC.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.partner.CallData;
import com.example.partner.GroupChatWebRTC.activities.CallActivity;
import com.example.partner.GroupChatWebRTC.db.QbUsersDbManager;
import com.example.partner.GroupChatWebRTC.utils.CollectionsUtils;
import com.example.partner.GroupChatWebRTC.utils.Consts;
import com.example.partner.GroupChatWebRTC.utils.UsersUtils;
import com.example.partner.GroupChatWebRTC.utils.WebRtcSessionManager;
import com.example.partner.PopupTrainFinishedActivity;
import com.example.partner.PopupTrainerInfoActivity;
import com.example.partner.PopupTrainerTrainFinishedActivity;
import com.example.partner.R;
import com.example.partner.SharedPreferenceData;
import com.quickblox.chat.QBChatService;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;

import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.Nullable;

public abstract class BaseConversationFragment extends BaseToolBarFragment implements CallActivity.CurrentCallStateCallback {

    private static final String TAG = BaseConversationFragment.class.getSimpleName();
    protected QbUsersDbManager dbManager;
    protected WebRtcSessionManager sessionManager;
    private boolean isIncomingCall;
    protected QBRTCSession currentSession;
    protected ArrayList<QBUser> opponents;
    private QBRTCTypes.QBConferenceType qbConferenceType;

    private ToggleButton micToggleVideoCall;
    private ImageButton handUpVideoCall;
    protected ConversationFragmentCallbackListener conversationFragmentCallbackListener;
    protected Chronometer timerChronometer;
    private boolean isMessageProcessed;
    protected boolean isStarted;
    protected View outgoingOpponentsRelativeLayout;
    protected TextView allOpponentsTextView;
    protected TextView ringingTextView;
    protected QBUser currentUser;


    public static BaseConversationFragment newInstance(BaseConversationFragment baseConversationFragment, boolean isIncomingCall) {
        Log.d(TAG, "isIncomingCall =  " + isIncomingCall);
        Bundle args = new Bundle();
        args.putBoolean(Consts.EXTRA_IS_INCOMING_CALL, isIncomingCall);

        baseConversationFragment.setArguments(args);

        return baseConversationFragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            conversationFragmentCallbackListener = (ConversationFragmentCallbackListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ConversationFragmentCallbackListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conversationFragmentCallbackListener.addCurrentCallStateCallback(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        sessionManager = WebRtcSessionManager.getInstance(getActivity());
        currentSession = sessionManager.getCurrentSession();
        if (currentSession == null) {
            Log.d(TAG, "currentSession = null onCreateView");
            return view;
        }
        initFields();
        initViews(view);
        initActionBar();
        initButtonsListener();
        prepareAndShowOutgoingScreen();

        return view;
    }

    private void prepareAndShowOutgoingScreen() {
        configureOutgoingScreen();
        allOpponentsTextView.setText(CollectionsUtils.makeStringFromUsersFullNames(opponents));
    }

    protected abstract void configureOutgoingScreen();

    private void initActionBar() {
        configureToolbar();
        configureActionBar();
    }

    protected abstract void configureActionBar();

    protected abstract void configureToolbar();

    protected void initFields() {
        currentUser = QBChatService.getInstance().getUser();
        dbManager = QbUsersDbManager.getInstance(getActivity().getApplicationContext());
        sessionManager = WebRtcSessionManager.getInstance(getActivity());
        currentSession = sessionManager.getCurrentSession();

        if (getArguments() != null) {
            isIncomingCall = getArguments().getBoolean(Consts.EXTRA_IS_INCOMING_CALL);
        }

        initOpponentsList();

        qbConferenceType = currentSession.getConferenceType();

        Log.d(TAG, "opponents: " + opponents.toString());
        Log.d(TAG, "currentSession " + currentSession.toString());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (currentSession == null) {
            Log.d(TAG, "currentSession = null onStart");
            return;

        }

        if (currentSession.getState() != QBRTCSession.QBRTCSessionState.QB_RTC_SESSION_CONNECTED) {
            if (isIncomingCall) {
                currentSession.acceptCall(null);
            } else {
                currentSession.startCall(null);
            }
            isMessageProcessed = true;
        }
    }

    @Override
    public void onDestroy() {
        conversationFragmentCallbackListener.removeCurrentCallStateCallback(this);
        super.onDestroy();
       // Toast.makeText(getActivity(), "통화종료료료 - ondestroy", Toast.LENGTH_SHORT).show();

        if(SharedPreferenceData.getType(getContext()).equals("trainer")) {
            CallData.getInstance().setCallReceiverName(CollectionsUtils.makeStringFromUsersFullNames(opponents));
            Intent intent = new Intent(getContext(), PopupTrainerTrainFinishedActivity.class);
            startActivity(intent);
        }
        else {
            if(CallData.getInstance().isCalled()) {
                Intent intent = new Intent(getContext(), PopupTrainFinishedActivity.class);
                startActivity(intent);
            }
        }

    }

    protected void initViews(View view) {
        micToggleVideoCall = (ToggleButton) view.findViewById(R.id.toggle_mic);
        handUpVideoCall = (ImageButton) view.findViewById(R.id.button_hangup_call);
        outgoingOpponentsRelativeLayout = view.findViewById(R.id.layout_background_outgoing_screen);
        allOpponentsTextView = (TextView) view.findViewById(R.id.text_outgoing_opponents_names);
        ringingTextView = (TextView) view.findViewById(R.id.text_ringing);

        if (isIncomingCall) {
            hideOutgoingScreen();
        }
    }

    protected void initButtonsListener() {

        micToggleVideoCall
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                conversationFragmentCallbackListener.onSetAudioEnabled(isChecked);
            }
        });

        handUpVideoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionButtonsEnabled(false);
                handUpVideoCall.setEnabled(false);
                handUpVideoCall.setActivated(false);

                conversationFragmentCallbackListener.onHangUpCurrentSession();
                Log.d(TAG, "Call is stopped");
            }
        });
    }

    protected void actionButtonsEnabled(boolean inability) {

        micToggleVideoCall.setEnabled(inability);

        // inactivate toggle buttons
        micToggleVideoCall.setActivated(inability);
    }

    private void startTimer() {
        if (!isStarted) {
            timerChronometer.setVisibility(View.VISIBLE);
            timerChronometer.setBase(SystemClock.elapsedRealtime());
            timerChronometer.start();
            CallData.getInstance().setCalled(true);

            long now = System.currentTimeMillis();
            Date date = new Date(now);
            CallData.getInstance().setStart_time(date);
            isStarted = true;
        }
    }

    private void stopTimer() {
        if (timerChronometer != null) {
            timerChronometer.stop();
            CallData.getInstance().setCallTime((SystemClock.elapsedRealtime()-timerChronometer.getBase())/1000);

            long now = System.currentTimeMillis();
            Date date = new Date(now);
            CallData.getInstance().setEnd_time(date);
            isStarted = false;
        }
    }

    private void hideOutgoingScreen() {
        outgoingOpponentsRelativeLayout.setVisibility(View.GONE);
    }

    @Override
    public void onCallStarted() {
        hideOutgoingScreen();
        startTimer();
        actionButtonsEnabled(true);
    }

    @Override
    public void onCallStopped() {
        if (currentSession == null) {
            Log.d(TAG, "currentSession = null onCallStopped");
            return;
        }
        stopTimer();
        actionButtonsEnabled(false);
       // Toast.makeText(getActivity(), "통화종료료료 - oncallstopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOpponentsListUpdated(ArrayList<QBUser> newUsers) {
        initOpponentsList();
    }

    private void initOpponentsList() {
        Log.v("UPDATE_USERS", "super initOpponentsList()");
        ArrayList<QBUser> usersFromDb = dbManager.getUsersByIds(currentSession.getOpponents());
        opponents = UsersUtils.getListAllUsersFromIds(usersFromDb, currentSession.getOpponents());

        QBUser caller = dbManager.getUserById(currentSession.getCallerID());
        if (caller == null) {
            caller = new QBUser(currentSession.getCallerID());
            caller.setFullName(String.valueOf(currentSession.getCallerID()));
        }

        if (isIncomingCall) {
            opponents.add(caller);
            opponents.remove(QBChatService.getInstance().getUser());
        }
    }
}
