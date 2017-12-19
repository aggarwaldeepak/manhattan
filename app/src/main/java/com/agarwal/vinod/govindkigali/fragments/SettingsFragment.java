package com.agarwal.vinod.govindkigali.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.agarwal.vinod.govindkigali.MainActivity;
import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.activities.AboutAppDetails;
import com.agarwal.vinod.govindkigali.utils.PrefManager;

import java.util.Locale;

public class SettingsFragment extends Fragment {

    public SettingsFragment() {
        // Required empty public constructor
    }

    Switch night_mode;
    TextView rate_app, share_app, about_app , feedback, language;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View settingsFragment = inflater.inflate(R.layout.fragment_settings, container, false);

        night_mode = settingsFragment.findViewById(R.id.id_NightMode);
        language = settingsFragment.findViewById(R.id.id_Language);
        feedback = settingsFragment.findViewById(R.id.id_Feedback);
        rate_app = settingsFragment.findViewById(R.id.id_RateApp);
        share_app = settingsFragment.findViewById(R.id.id_ShareApp);
        about_app = settingsFragment.findViewById(R.id.id_AboutApp);


        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("text/html");
//                intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ getString(R.string.mail_feedback_email) });
//                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.mail_feedback_subject));
//                intent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.mail_feedback_message));
//                intent.setPackage("com.google.android.gm");
//                startActivity(Intent.createChooser(intent, getString(R.string.title_send_feedback)));

                AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                b.setTitle("Send Us Some Feedback!");
                final EditText input = new EditText(getContext());
                b.setView(input);
                b.setPositiveButton("Send Feedback", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String result = input.getText().toString();
                    }
                });
                b.setNegativeButton("CANCEL", null);
                b.show();
            }
        });

        rate_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("market://details?id=" +
                        "bhaktisagar.vinodagarwal.app"/*settingsFragment.getContext().getPackageName()*/);
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                } else {
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

                }
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" +
                                    "bhaktisagar.vinodagarwal.app"/*settingsFragment.getContext().getPackageName())*/)));
                }
            }
        });


        share_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    String str = "\nLet me recommend you this application\n\n";
                    str = str + "https://play.google.com/store/apps/details?id=bhaktisagar.vinodagarwal.app\n\n";
                    i.putExtra(Intent.EXTRA_TEXT, str);
                    startActivity(Intent.createChooser(i, getString(R.string.app_name)));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });

        about_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AboutAppDetails.class));
            }
        });

        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLanguageWithDialog(new PrefManager(getContext()));
            }
        });

        return settingsFragment;

    }
    public void setLanguageWithDialog(final PrefManager prefManager) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle(R.string.languages);
        // Add the buttons
        builder.setPositiveButton(R.string.english, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String languageToLoad = "en"; // your language
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getActivity().getBaseContext().getResources().updateConfiguration(config,
                        getActivity().getBaseContext().getResources().getDisplayMetrics());
                prefManager.setLanguage("en");
                dialog.dismiss();
                ((BottomNavigationView)getActivity().findViewById(R.id.navigation)).setSelectedItemId(R.id.navigation_play);
                getActivity().recreate();
                /*Intent refresh = new Intent(MainActivity.this, MainActivity.class);
                startActivity(refresh);
                finish();*/

            }
        });
        builder.setNegativeButton(R.string.hindi, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog

                String languageToLoad = "hi"; // your language
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getActivity().getBaseContext().getResources().updateConfiguration(config,
                        getActivity().getBaseContext().getResources().getDisplayMetrics());
                prefManager.setLanguage("hi");
                dialog.dismiss();
                ((BottomNavigationView)getActivity().findViewById(R.id.navigation)).setSelectedItemId(R.id.navigation_play);
               /* rEditor.putString("language", languageToLoad);
                rEditor.commit();*/


                getActivity().recreate();
               /* Intent refresh = new Intent(MainActivity.this, MainActivity.class);
                startActivity(refresh);
                finish();*/

            }
        });

        builder.create().show();
    }
}

