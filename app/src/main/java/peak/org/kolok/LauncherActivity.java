package peak.org.kolok;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LauncherActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        // Note here that we DO NOT use setContentView();

        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.

        if (KolokCloud.cloudBoost == null) {
            KolokCloud.cloudBoost.init(KolokCloud.CLOUDBOOST_APP_ID, KolokCloud.CLOUDBOOST_API_KEY);
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submitButton) {
            final TextInputLayout emailLoginWrapper = (TextInputLayout) findViewById(R.id.emailLoginWrapper);
            final TextInputLayout passLoginWrapper = (TextInputLayout) findViewById(R.id.passLoginWrapper);
            final TextInputLayout passVerLoginWrapper = (TextInputLayout) findViewById(R.id.passLoginVerWrapper);
            final EditText et = (EditText) findViewById(R.id.emailLogin);
            final EditText etPass = (EditText) findViewById(R.id.passLogin);
            final EditText etPassVer = (EditText) findViewById(R.id.passLoginVer);
            final Button submit = (Button) findViewById(R.id.submitButton);
            final Button signUp = (Button) findViewById(R.id.signUpSubmit);
            final Button login  = (Button) findViewById(R.id.loginSubmit);
            final String userEmail = et.getText().toString();
            KolokCloud.getUser(userEmail, new KolokCallback() {
                @Override
                public void methodToCall() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            passLoginWrapper.setVisibility(View.VISIBLE);
                            passVerLoginWrapper.setVisibility(View.VISIBLE);
                            submit.setVisibility(View.INVISIBLE);
                            signUp.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }, new KolokCallback() {
                @Override
                public void methodToCall() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            passLoginWrapper.setVisibility(View.VISIBLE);
                            login.setVisibility(View.VISIBLE);
                        }
                    });
                }
            });
        }
    }

    public void onClickSubmit(View v) {
        Log.d("KOLOK:", "WE in this bitch");
        final EditText et = (EditText) findViewById(R.id.emailLogin);
        final EditText etPass = (EditText) findViewById(R.id.passLogin);
        final EditText etPassVer = (EditText) findViewById(R.id.passLoginVer);
        final String userEmail = et.getText().toString();
        final String userPass = etPass.getText().toString();
        KolokCloud.createUser(userEmail, userPass, new KolokCallback() {
            @Override
            public void methodToCall() {
                KolokCloud.loginUser(userEmail, userPass, new KolokCallback() {
                    @Override
                    public void methodToCall() {
                        Intent myIntent = new Intent(getApplicationContext(), IssueActivity.class);
                        startActivity(myIntent);
                    }
                }, new KolokCallback() {
                    @Override
                    public void methodToCall() {

                    }
                });
            }
        });
    }

    public void onClickLogin(View v) {
        Log.d("KOLOK:", "WE in this bitch");
        final EditText et = (EditText) findViewById(R.id.emailLogin);
        final EditText etPass = (EditText) findViewById(R.id.passLogin);
        final EditText etPassVer = (EditText) findViewById(R.id.passLoginVer);
        final TextInputLayout passLoginWrapper = (TextInputLayout) findViewById(R.id.passLoginWrapper);
        final String userEmail = et.getText().toString();
        final String userPass = etPass.getText().toString();
        KolokCloud.loginUser(userEmail, userPass, new KolokCallback() {
            @Override
            public void methodToCall() {
                Intent myIntent = new Intent(getApplicationContext(), IssueActivity.class);
                startActivity(myIntent);
            }
        }, new KolokCallback() {
            @Override
            public void methodToCall() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        passLoginWrapper.setError("Not a valid password!");
                    }
                });
            }
        });
    }
}