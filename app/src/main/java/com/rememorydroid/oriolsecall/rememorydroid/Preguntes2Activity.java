package com.rememorydroid.oriolsecall.rememorydroid;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import static com.rememorydroid.oriolsecall.rememorydroid.TestActivity.gson;

public class Preguntes2Activity extends AppCompatActivity {

    public static SharedPreferences prefs;
    public static SharedPreferences.Editor editor;
    public static TestAnswers respostes_recuperades;
    public static PacientUsuari pacient;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private static ViewPager mViewPager;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntes2);

        respostes_recuperades=new TestAnswers();

        DialegQuestionari();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
        editor= prefs.edit();

        String respostes_json = prefs.getString("respostes", null);
        //Guardem a TestAnswers
        Gson gson = new Gson();
        respostes_recuperades = gson.fromJson(respostes_json, TestAnswers.class);

        pacient = gson.fromJson(prefs.getString("pacient",null),PacientUsuari.class);
    }

    //Part del menú 'action bar'

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.getItem(0).setTitle(getString(R.string.sign_out, FirebaseAuth.getInstance().getCurrentUser().getEmail().toString()));
        menu.getItem(1).setTitle(getString(R.string.sign_out_Pacient)+" ("+pacient.getID()+")");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.btSignOutMenu) {

            //Confirmar eliminació per contrasenya
            //-------------------------------------------------------------------
            AlertDialog.Builder dialegPassword = new AlertDialog.Builder(Preguntes2Activity.this);
            LayoutInflater factory = LayoutInflater.from(Preguntes2Activity.this);
            View textEntryView = factory.inflate(R.layout.dialeg_delete_user, null);
            //Instanciem els elements del diàleg per poder obtenir el que ha escrit l'usuari
            final EditText input = (EditText) textEntryView.findViewById(R.id.etPasswordDelete);
            dialegPassword
                    .setView(textEntryView)
                    .setIcon(R.drawable.passwordicon)
                    .setTitle(R.string.PasswordDialog)
                    .setMessage(R.string.IntroducePassword)
                    .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            // Recuperem el email del avaluador i el reautentiquem
                            String email_user = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
                            String pass_user = input.getText().toString();
                            if (!pass_user.isEmpty()) {
                                //Reautentiquem al avaluador per seguretat
                                AuthCredential credential = EmailAuthProvider.getCredential(email_user, pass_user);
                                FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //Retorna a la pantalla inicial
                                        FirebaseAuth.getInstance().signOut();
                                        Toast.makeText(Preguntes2Activity.this, R.string.signed_out,
                                                Toast.LENGTH_LONG).show();
                                        Intent areaAvaluador = new Intent(Preguntes2Activity.this, SignInActivity.class);
                                        startActivity(areaAvaluador);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Preguntes2Activity.this, getString(R.string.IncorrecPassword),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).show();
        }

        if (id == R.id.btSignOutPacient) {

            //Confirmar eliminació per contrasenya
            //-------------------------------------------------------------------
            AlertDialog.Builder dialegPassword = new AlertDialog.Builder(Preguntes2Activity.this);
            LayoutInflater factory = LayoutInflater.from(Preguntes2Activity.this);
            View textEntryView = factory.inflate(R.layout.dialeg_delete_user, null);
            //Instanciem els elements del diàleg per poder obtenir el que ha escrit l'usuari
            final EditText input = (EditText) textEntryView.findViewById(R.id.etPasswordDelete);
            dialegPassword
                    .setView(textEntryView)
                    .setIcon(R.drawable.passwordicon)
                    .setTitle(R.string.PasswordDialog)
                    .setMessage(R.string.IntroducePassword)
                    .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            // Recuperem el email del avaluador i el reautentiquem
                            String email_user = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
                            String pass_user = input.getText().toString();
                            if (!pass_user.isEmpty()) {
                                //Reautentiquem al avaluador per seguretat
                                AuthCredential credential = EmailAuthProvider.getCredential(email_user, pass_user);
                                FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //Retorna a la pantalla 'Area Avaluador'
                                        editor.remove("pacient").commit();
                                        Toast.makeText(Preguntes2Activity.this, R.string.MenuChangePacient,
                                                Toast.LENGTH_LONG).show();
                                        Intent areaAvaluador = new Intent(Preguntes2Activity.this, AreaAvaluadorActivity.class);
                                        startActivity(areaAvaluador);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Preguntes2Activity.this, getString(R.string.IncorrecPassword),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void DialegQuestionari() {
        AlertDialog.Builder DialegFormControl = new AlertDialog.Builder(Preguntes2Activity.this);
        LayoutInflater factory = LayoutInflater.from(Preguntes2Activity.this);
        View textEntryView = factory.inflate(R.layout.dialegs, null);
        TextView tv = (TextView) textEntryView.findViewById(R.id.tvMissatgeDialeg);
        Button bt = (Button) textEntryView.findViewById(R.id.btDiaelgOK);
        tv.setText(R.string.AskingQuestions);

        DialegFormControl
                .setTitle(getString(R.string.Attention))
                .setView(textEntryView)
                .setCancelable(false);

        final AlertDialog alerta = DialegFormControl.create();

        alerta.show();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerta.dismiss();
            }
        });
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.activity_preguntes2_1, container, false);

            final Button btNextPeople1 = (Button) rootView.findViewById(R.id.btNextPeople1);
            final Button btBackPeople1 = (Button) rootView.findViewById(R.id.btBackPeople1);

            btNextPeople1.setEnabled(false);
            btNextPeople1.setVisibility(View.INVISIBLE);

            final RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.rgPeople);

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                    //Busquem quin radioButton s'ha seleccionat
                    int radioButtonID = radioGroup.getCheckedRadioButtonId();
                    //Recuperem l'element ara que tenim el identificador per a la classe R (id)
                    RadioButton rb = (RadioButton) rootView.findViewById(radioButtonID);

                    Toast.makeText(getContext(), rb.getText().toString(),
                            Toast.LENGTH_SHORT).show();

                    //Guardem resposta obteniguda
                    respostes_recuperades.setPreguntesPersonesAccions(rb.getText().toString());

                    btNextPeople1.setEnabled(true);
                    btNextPeople1.setVisibility(View.VISIBLE);

                }

            });

            btBackPeople1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(),VisualitzarFragmentsActivity.class);
                    intent.putExtra("Tercer","Tercer");
                    startActivity(intent);
                }
            });
            btNextPeople1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(1);
                }
            });

            return rootView;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class Fragment2 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public Fragment2() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Fragment2 newInstance(int sectionNumber) {
            Fragment2 fragment = new Fragment2();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.activity_preguntes2_2, container, false);
            final Button btNextPeople2 = (Button) rootView.findViewById(R.id.btNextPeople2);
            final Button btBackPeople2 = (Button) rootView.findViewById(R.id.btBackPeople2);

            btNextPeople2.setEnabled(false);
            btNextPeople2.setVisibility(View.INVISIBLE);

            final RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.rgPeople2);

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                    //Busquem quin radioButton s'ha seleccionat
                    int radioButtonID = radioGroup.getCheckedRadioButtonId();
                    //Recuperem l'element ara que tenim el identificador per a la classe R (id)
                    RadioButton rb = (RadioButton) rootView.findViewById(radioButtonID);

                    Toast.makeText(getContext(), rb.getText().toString(),
                            Toast.LENGTH_SHORT).show();

                    //Guardem resposta obteniguda
                    respostes_recuperades.setPreguntesPersonesGrups(rb.getText().toString());

                    btNextPeople2.setEnabled(true);
                    btNextPeople2.setVisibility(View.VISIBLE);
                }

            });

            btBackPeople2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(0);
                }
            });
            btNextPeople2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(2);
                }
            });

            return rootView;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class Fragment3 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public Fragment3() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Fragment3 newInstance(int sectionNumber) {
            Fragment3 fragment = new Fragment3();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.activity_preguntes2_3, container, false);
            final Button btNextPeople3 = (Button) rootView.findViewById(R.id.btNextPeople3);
            final Button btBackPeople3 = (Button) rootView.findViewById(R.id.btBackPeople3);

            btNextPeople3.setEnabled(false);
            btNextPeople3.setVisibility(View.INVISIBLE);

            final RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.rgPeople3);

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                    //Busquem quin radioButton s'ha seleccionat
                    int radioButtonID = radioGroup.getCheckedRadioButtonId();
                    //Recuperem l'element ara que tenim el identificador per a la classe R (id)
                    RadioButton rb = (RadioButton) rootView.findViewById(radioButtonID);

                    Toast.makeText(getContext(), rb.getText().toString(),
                            Toast.LENGTH_SHORT).show();

                    //Guardem resposta obteniguda
                    respostes_recuperades.setPreguntesPersonesRelacio(rb.getText().toString());

                    btNextPeople3.setEnabled(true);
                    btNextPeople3.setVisibility(View.VISIBLE);

                }

            });

            btBackPeople3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(1);
                }
            });


            btNextPeople3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(3);
                }
            });

            return rootView;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class Fragment4 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public Fragment4() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Fragment4 newInstance(int sectionNumber) {
            Fragment4 fragment = new Fragment4();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.activity_preguntes2_4, container, false);

            final Button btNextPeople4 = (Button) rootView.findViewById(R.id.btNextPeople4);
            final Button btBackPeople4 = (Button) rootView.findViewById(R.id.btBackPeople4);

            btNextPeople4.setEnabled(false);
            btNextPeople4.setVisibility(View.INVISIBLE);

            final RadioGroup rgFragment41Q2 = (RadioGroup) rootView.findViewById(R.id.rgFragment41Q2);
            final RadioGroup rgFragment42Q2 = (RadioGroup) rootView.findViewById(R.id.rgFragment42Q2);


            rgFragment41Q2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                    RadioButton rbSeleccionat = (RadioButton) rootView.findViewById(radioGroup.getCheckedRadioButtonId());
                    //Guardem resposta obteniguda
                    respostes_recuperades.setPreguntesEmocionsObservades(rbSeleccionat.getText().toString());
                    if(!respostes_recuperades.getPreguntesPersonesRelacio().isEmpty() && !respostes_recuperades.getPreguntesPersonesGrups().isEmpty()
                            && !respostes_recuperades.getPreguntesPersonesAccions().isEmpty()) {
                        if (rgFragment42Q2.getCheckedRadioButtonId() != -1) {
                            btNextPeople4.setEnabled(true);
                            btNextPeople4.setVisibility(View.VISIBLE);
                        }
                    }
                    else{
                        Toast.makeText(getContext(), R.string.MissAnswers,
                                Toast.LENGTH_LONG).show();
                    }
                }
            });

            rgFragment42Q2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                    RadioButton rbSeleccionat = (RadioButton) rootView.findViewById(radioGroup.getCheckedRadioButtonId());
                    //Guardem resposta obteniguda
                    respostes_recuperades.setPreguntesEmocionsPropies(rbSeleccionat.getText().toString());

                    if(!respostes_recuperades.getPreguntesPersonesRelacio().isEmpty() && !respostes_recuperades.getPreguntesPersonesGrups().isEmpty()
                            && !respostes_recuperades.getPreguntesPersonesAccions().isEmpty()){
                        if(rgFragment41Q2.getCheckedRadioButtonId()!=-1){
                            btNextPeople4.setEnabled(true);
                            btNextPeople4.setVisibility(View.VISIBLE);
                        }
                    }
                    else{
                        Toast.makeText(getContext(), R.string.MissAnswers,
                        Toast.LENGTH_LONG).show();
                    }
                }
            });



            btBackPeople4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(2);
                }
            });
            btNextPeople4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String respostes_json = gson.toJson(respostes_recuperades,TestAnswers.class);
                    editor.putString("respostes",respostes_json);
                    editor.commit();
                    editor.apply();


                    //Notificació

                    Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.iconrem);

                    Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(getContext())
                                    .setLargeIcon(bitmap)
                                    .setSmallIcon(R.drawable.attachment)
                                    .setContentTitle(getString(R.string.NotificationAlert))
                                    .setContentText(getString(R.string.Episode2Questions, pacient.getName()))
                                    .setSound(defaultSound)
                                    .setTicker(getString(R.string.Test2Ticker,pacient.getName()));

                    NotificationManager m = (NotificationManager) getActivity().getSystemService(getContext().NOTIFICATION_SERVICE);
                    m.notify(6,mBuilder.build());

                    Intent intent = new Intent(getActivity(),RespirarActivity.class);
                    intent.putExtra("Quarta","Quarta");
                    startActivity(intent);
                }
            });

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position==0){
                return Preguntes2Activity.PlaceholderFragment.newInstance(position + 1);
            }
            if(position==1){
                return Preguntes2Activity.Fragment2.newInstance(position + 1);
            }
            if(position==2){
                return Preguntes2Activity.Fragment3.newInstance(position + 1);
            }
            if(position==3){
                return Preguntes2Activity.Fragment4.newInstance(position + 1);
            }


            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.PeopleActions);
                case 1:
                    return getString(R.string.AgesPeople);
                case 2:
                    return getString(R.string.HowManyPeople);
                case 3:
                    return getString(R.string.EmotionsDuringEpisode);

            }
            return null;
        }
    }
}
