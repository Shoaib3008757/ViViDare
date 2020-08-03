package com.vividare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.ybq.android.spinkit.SpinKitView;
import com.vividare.HelperClasses.AppSingleton;
import com.vividare.HelperClasses.ConstantFunctions;
import com.vividare.HelperClasses.SharedPrefs;
import com.vividare.HelperClasses.URLS;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    RelativeLayout ll_sign_up;

    SharedPreferences sharedPreferencesData;
    String mUserKey;
    ImageView iv_back_arrow;
    EditText et_fullname, et_userName, et_email, et_password;

    SpinKitView spin_kit;
    RelativeLayout rl_bg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        init();
        registerButtonClick();
        backArrowClick();
    }

    private void init()
    {
        ll_sign_up = (RelativeLayout) findViewById(R.id.ll_sign_up);
        sharedPreferencesData = getSharedPreferences(SharedPrefs.PREF_NAME,0);
        et_fullname = (EditText) findViewById(R.id.et_fullname);
        et_userName = (EditText) findViewById(R.id.et_userName);
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);


        iv_back_arrow = (ImageView) findViewById(R.id.iv_back_arrow);


        spin_kit = (SpinKitView) findViewById(R.id.spin_kit);
        rl_bg = (RelativeLayout) findViewById(R.id.rl_bg);


        String firstName = sharedPreferencesData.getString("firstName", "");

        String myId = SharedPrefs.getStringPref(sharedPreferencesData, SharedPrefs.key);
        Log.e("TAg", "My id is here " + myId);
    }


    private void registerButtonClick()
    {
        ll_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = et_fullname.getText().toString();
                String userName = et_userName.getText().toString();
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                String tag = "Should Not Be Empty";


                if (fullName.isEmpty())
                {
                    et_fullname.setError(tag);

                } else if (userName.isEmpty()) {

                    et_userName.setError(tag);
                }
                else if (email.isEmpty()) {

                    et_email.setError(tag);
                }
                else if (password.isEmpty()) {

                    et_password.setError(tag);
                }

                else if (password.length()<4)
                {
                    et_password.setError("Atleast 5 charectors long");
                }
                else
                {
                    String randomNumber = ConstantFunctions.getratingRandom2Digit();

                    String userNameRe = userName.replaceAll("\\s+","")+randomNumber;

                    spin_kit.setVisibility(View.VISIBLE);
                    rl_bg.setVisibility(View.VISIBLE);

                    /*registerUser(userName, fullName, email,
                            password, "00",
                            "user", "212", "Active");*/
                }
            }
        });
    }

    private void registerUser(final String userName, final String fullName, final String email,
                              final String password, final String uic, final String userType,
                              final String imageUrl, final String userStatus)
    {

        // Tag used to cancel the request
        String cancel_req_tag = "register";

        StringRequest strReq = new StringRequest(Request.Method.POST, URLS.LOGIN_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Server Response: " + response);

                spin_kit.setVisibility(View.GONE);
                rl_bg.setVisibility(View.GONE);

                Toast.makeText(RegistrationActivity.this, response.toString(), Toast.LENGTH_SHORT).show();

                if (response.contains("Successfully")) {

                    successDialog(fullName);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Server Connection Fail", Toast.LENGTH_LONG).show();
                //hid pregress here
                spin_kit.setVisibility(View.GONE);
                rl_bg.setVisibility(View.GONE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                params.put("username", userName);
                params.put("fullname", fullName);
                params.put("email", email);
                params.put("password", password);
                params.put("imageUrl", imageUrl);
                params.put("usertype", userType);
                params.put("imageUrl", imageUrl);
                params.put("userStatus", userStatus);

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    private void successDialog(final String name)
    {
        final Dialog dialog = new Dialog(RegistrationActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.general_message_dialog);
        final TextView tv_dialog_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);
        final TextView tv_description = (TextView) dialog.findViewById(R.id.tv_description);
        final RelativeLayout rl_later_button = (RelativeLayout) dialog.findViewById(R.id.rl_later_button);
        final RelativeLayout rl_now_button = (RelativeLayout) dialog.findViewById(R.id.rl_now_button);
        final TextView tv_later_text = (TextView) dialog.findViewById(R.id.tv_later_text);
        final TextView tv_now_text = (TextView) dialog.findViewById(R.id.tv_now_text);

        tv_dialog_title.setText("Employee Added");
        tv_description.setText("Thank You! your employee has been added successfully");
        rl_later_button.setVisibility(View.GONE);
        tv_now_text.setText("Ok");
        rl_now_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
        dialog.show();

    }

    private void backArrowClick()
    {
        iv_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
