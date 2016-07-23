package xyz.aungpyaephyo.padc.myanmarattractions.fragments;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import xyz.aungpyaephyo.padc.myanmarattractions.R;
import xyz.aungpyaephyo.padc.myanmarattractions.controllers.ControllerAccountControl;
import xyz.aungpyaephyo.padc.myanmarattractions.events.DataEvent;
import xyz.aungpyaephyo.padc.myanmarattractions.utils.MyanmarAttractionsConstants;
import xyz.aungpyaephyo.padc.myanmarattractions.views.PasswordVisibilityListener;

/**
 * Created by aung on 7/15/16.
 */
public class LoginFragment extends Fragment {

    @BindView(R.id.lbl_login_title)
    TextView lblLoginTitle;

    @BindView(R.id.et_email_login)
    EditText edEmailLogin;

    @BindView(R.id.et_password_login)
    EditText edPasswordLogin;

    @BindView(R.id.tv_forget_password)
    TextView txtForgetPassword;

    @BindView(R.id.tv_do_register_now)
    TextView txtDoRegister;

    private ControllerAccountControl mControllerAccountControl;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mControllerAccountControl = (ControllerAccountControl)context;
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus eventBus = EventBus.getDefault();
        eventBus.unregister(this);  // EventBus is on UI thread that's why it is no need to be alerted when app is in background (onStop call back fun: is called)
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, rootView);

        txtForgetPassword.setPaintFlags(txtForgetPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtDoRegister.setPaintFlags(txtDoRegister.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        lblLoginTitle.setText(Html.fromHtml(getString(R.string.lbl_login_title)));
        edPasswordLogin.setOnTouchListener(new PasswordVisibilityListener());

        return rootView;
    }

    @OnClick(R.id.btn_login_detail)
    public void onTapLogin(Button btnRegister) {
        String email = edEmailLogin.getText().toString();
        String password = edPasswordLogin.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {

            if (TextUtils.isEmpty(email)) {
                edEmailLogin.setError(getString(R.string.error_missing_email));
            }

            if (TextUtils.isEmpty(password)) {
                edPasswordLogin.setError(getString(R.string.error_missing_password));
            }


        } else if (!isEmailValid(email)) {
            //Email address is not in the right format.
            edEmailLogin.setError(getString(R.string.error_email_is_not_valid));
        } else {
            //Checking on client side is done here.
            mControllerAccountControl.onLogin(email, password);
        }

    }

    public boolean isEmailValid(String email) {
        Pattern pattern = Pattern.compile(MyanmarAttractionsConstants.EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }
    public void onEventMainThread(DataEvent.DatePickedEvent event) { //another method you can use is ****onEventAnyThread*** when you wanna use background thread

    }

}
