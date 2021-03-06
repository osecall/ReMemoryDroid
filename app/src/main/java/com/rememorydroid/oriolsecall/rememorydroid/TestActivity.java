package com.rememorydroid.oriolsecall.rememorydroid;

import android.app.NotificationManager;
import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.File;

public class TestActivity extends AppCompatActivity {

    public static SharedPreferences prefs;
    public static TestAnswers respostes_recuperades = new TestAnswers();
    public static PacientUsuari pacient = new PacientUsuari();
    public static boolean SegonTest;
    public static boolean Curta;
    public static String episodi = new String();
    public static Gson gson = new Gson();
    public static SharedPreferences.Editor editor;
    public static StorageReference myRef;
    public static DatabaseReference DBRef;
    public static ProgressDialog mProgressDialog;
    public static TextView tv;
    public static View textEntryView;
    private static ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        SegonTest=false;
        Curta = false;

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

        //Inicialització
        myRef = FirebaseStorage.getInstance().getReference();
        DBRef = FirebaseDatabase.getInstance().getReference("pacients");
        prefs = getSharedPreferences("pacient", Context.MODE_PRIVATE);
        editor= prefs.edit();

        //Recuperació valors

        String pacient_json = prefs.getString("pacient",null);
        pacient = gson.fromJson(pacient_json,PacientUsuari.class);
        episodi = prefs.getString("episodi",null);

        if(prefs.getString("Versio",null).matches("Short")){
            Curta=true;
        }
        if(getIntent().hasExtra("SegonTest")){
            SegonTest=true;
            respostes_recuperades = gson.fromJson(prefs.getString("respostes",null),TestAnswers.class);
        }
        mostrarAlertaPelicula();

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
            AlertDialog.Builder dialegPassword = new AlertDialog.Builder(TestActivity.this);
            LayoutInflater factory = LayoutInflater.from(TestActivity.this);
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
                                        Toast.makeText(TestActivity.this, getString(R.string.signed_out),
                                                Toast.LENGTH_LONG).show();
                                        Intent areaAvaluador = new Intent(TestActivity.this, SignInActivity.class);
                                        startActivity(areaAvaluador);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(TestActivity.this, getString(R.string.IncorrecPassword),
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
            AlertDialog.Builder dialegPassword = new AlertDialog.Builder(TestActivity.this);
            LayoutInflater factory = LayoutInflater.from(TestActivity.this);
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
                                        Toast.makeText(TestActivity.this, R.string.MenuChangePacient,
                                                Toast.LENGTH_LONG).show();
                                        Intent areaAvaluador = new Intent(TestActivity.this, AreaAvaluadorActivity.class);
                                        startActivity(areaAvaluador);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(TestActivity.this, getString(R.string.IncorrecPassword),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void mostrarAlertaPelicula() {
        AlertDialog.Builder DialegFormControl = new AlertDialog.Builder(TestActivity.this);
        LayoutInflater factory = LayoutInflater.from(this);
        textEntryView = factory.inflate(R.layout.dialegs, null);
        tv = (TextView) textEntryView.findViewById(R.id.tvMissatgeDialeg);
        tv.setText(R.string.AlertDialaogTest);
        Button bt = (Button) textEntryView.findViewById(R.id.btDiaelgOK);
        DialegFormControl
                .setCancelable(false)
                .setView(textEntryView)
                .setTitle(getString(R.string.Attention));

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
            final View rootView = inflater.inflate(R.layout.activity_test_1, container, false);

            ColorGenerator generator = ColorGenerator.DEFAULT;
            TextDrawable FromPage = TextDrawable.builder().beginConfig().width(40).height(40).endConfig().buildRound("1",generator.getRandomColor());
            TextDrawable ToPage = TextDrawable.builder().beginConfig().width(40).height(40).endConfig().buildRound("6",generator.getRandomColor());

            ImageView ivFromPage = (ImageView) rootView.findViewById(R.id.ivFromPage1);
            ImageView ivToPage = (ImageView) rootView.findViewById(R.id.ivToPage1);
            final ImageView ivNumSeleccionat= (ImageView) rootView.findViewById(R.id.ivNumSeleccionat);

            ivFromPage.setImageDrawable(FromPage);
            ivToPage.setImageDrawable(ToPage);

            final Button btBack = (Button) rootView.findViewById(R.id.btBackPel1);
            final Button btNext = (Button) rootView.findViewById(R.id.btNextPel1);

            btNext.setEnabled(false);
            btNext.setVisibility(View.INVISIBLE);

            if(SegonTest){
                btBack.setVisibility(View.INVISIBLE);
                btBack.setEnabled(false);
            }

            final RadioGroup rbGroup = (RadioGroup) rootView.findViewById(R.id.rbGroup1Pel1);

            rbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                    //Busquem quin radioButton s'ha seleccionat
                    int radioButtonID = rbGroup.getCheckedRadioButtonId();
                    //Recuperem l'element ara que tenim el identificador per a la classe R (id)
                    RadioButton rb = (RadioButton) rootView.findViewById(radioButtonID);

                    btNext.setVisibility(View.VISIBLE);
                    btNext.setEnabled(true);

                    TextDrawable NumeroSeleccionat = TextDrawable.builder().beginConfig().width(110).height(110).endConfig().buildRound(rb.getText().toString(),ColorGenerator.DEFAULT.getRandomColor());
                    ivNumSeleccionat.setImageDrawable(NumeroSeleccionat);
                    ivNumSeleccionat.setVisibility(View.VISIBLE);

                    if(SegonTest){
                        respostes_recuperades.setTest2Pregunta1(rb.getText().toString());
                    }
                    else{
                        respostes_recuperades.setTest1Pregunta1(rb.getText().toString());

                    }
                }
            });


            //Enrere
            btBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!SegonTest){
                        startActivity(new Intent (getContext(), TractamentsActivity.class));
                    }
                }
            });

            //Endevant
            btNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(1);
                }
            });

            return rootView;
        }
    }

    public static class Fragment2 extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public Fragment2() {
        }

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
            final View rootView = inflater.inflate(R.layout.activity_test_2, container, false);

            ColorGenerator generator = ColorGenerator.DEFAULT;
            TextDrawable FromPage = TextDrawable.builder().beginConfig().width(40).height(40).endConfig().buildRound("2",generator.getRandomColor());
            TextDrawable ToPage = TextDrawable.builder().beginConfig().width(40).height(40).endConfig().buildRound("6",generator.getRandomColor());

            ImageView ivFromPage = (ImageView) rootView.findViewById(R.id.ivFromPage1);
            ImageView ivToPage = (ImageView) rootView.findViewById(R.id.ivToPage1);
            final ImageView ivNumSeleccionat= (ImageView) rootView.findViewById(R.id.ivNumSeleccionat);

            ivFromPage.setImageDrawable(FromPage);
            ivToPage.setImageDrawable(ToPage);

            final Button btBack = (Button) rootView.findViewById(R.id.btBackPel2);
            final Button btNext = (Button) rootView.findViewById(R.id.btNextPel2);

            btNext.setEnabled(false);
            btNext.setVisibility(View.INVISIBLE);

            final RadioGroup rbGroup = (RadioGroup) rootView.findViewById(R.id.rbGroup1Pel2);

            rbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                    //Busquem quin radioButton s'ha seleccionat
                    int radioButtonID = rbGroup.getCheckedRadioButtonId();
                    //Recuperem l'element ara que tenim el identificador per a la classe R (id)
                    RadioButton rb = (RadioButton) rootView.findViewById(radioButtonID);

                    btNext.setVisibility(View.VISIBLE);
                    btNext.setEnabled(true);

                    TextDrawable NumeroSeleccionat = TextDrawable.builder().beginConfig().width(110).height(110).endConfig().buildRound(rb.getText().toString(),ColorGenerator.DEFAULT.getRandomColor());
                    ivNumSeleccionat.setImageDrawable(NumeroSeleccionat);
                    ivNumSeleccionat.setVisibility(View.VISIBLE);

                    if(SegonTest){
                        respostes_recuperades.setTest2Pregunta2(rb.getText().toString());
                    }
                    else{
                        respostes_recuperades.setTest1Pregunta2(rb.getText().toString());

                    }
                }
            });


            //Enrere
            btBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(0);
                }
            });

            //Endevant
            btNext.setOnClickListener(new View.OnClickListener() {
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
            final View rootView = inflater.inflate(R.layout.activity_test_3, container, false);

            ColorGenerator generator = ColorGenerator.DEFAULT;
            TextDrawable FromPage = TextDrawable.builder().beginConfig().width(40).height(40).endConfig().buildRound("3",generator.getRandomColor());
            TextDrawable ToPage = TextDrawable.builder().beginConfig().width(40).height(40).endConfig().buildRound("6",generator.getRandomColor());

            ImageView ivFromPage = (ImageView) rootView.findViewById(R.id.ivFromPage1);
            ImageView ivToPage = (ImageView) rootView.findViewById(R.id.ivToPage1);
            final ImageView ivNumSeleccionat= (ImageView) rootView.findViewById(R.id.ivNumSeleccionat);

            ivFromPage.setImageDrawable(FromPage);
            ivToPage.setImageDrawable(ToPage);

            final Button btBack = (Button) rootView.findViewById(R.id.btBackPel3);
            final Button btNext = (Button) rootView.findViewById(R.id.btNextPel3);

            btNext.setEnabled(false);
            btNext.setVisibility(View.INVISIBLE);

            final RadioGroup rbGroup = (RadioGroup) rootView.findViewById(R.id.rbGroup1Pel3);

            rbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                    //Busquem quin radioButton s'ha seleccionat
                    int radioButtonID = rbGroup.getCheckedRadioButtonId();
                    //Recuperem l'element ara que tenim el identificador per a la classe R (id)
                    RadioButton rb = (RadioButton) rootView.findViewById(radioButtonID);

                    btNext.setVisibility(View.VISIBLE);
                    btNext.setEnabled(true);

                    TextDrawable NumeroSeleccionat = TextDrawable.builder().beginConfig().width(110).height(110).endConfig().buildRound(rb.getText().toString(),ColorGenerator.DEFAULT.getRandomColor());
                    ivNumSeleccionat.setImageDrawable(NumeroSeleccionat);
                    ivNumSeleccionat.setVisibility(View.VISIBLE);

                    if(SegonTest){
                        respostes_recuperades.setTest2Pregunta3(rb.getText().toString());
                    }
                    else{
                        respostes_recuperades.setTest1Pregunta3(rb.getText().toString());
                    }
                }
            });


            //Enrere
            btBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(1);
                }
            });

            //Endevant
            btNext.setOnClickListener(new View.OnClickListener() {
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
            final View rootView = inflater.inflate(R.layout.activity_test_4, container, false);

            ColorGenerator generator = ColorGenerator.DEFAULT;
            TextDrawable FromPage = TextDrawable.builder().beginConfig().width(40).height(40).endConfig().buildRound("4",generator.getRandomColor());
            TextDrawable ToPage = TextDrawable.builder().beginConfig().width(40).height(40).endConfig().buildRound("6",generator.getRandomColor());

            ImageView ivFromPage = (ImageView) rootView.findViewById(R.id.ivFromPage1);
            ImageView ivToPage = (ImageView) rootView.findViewById(R.id.ivToPage1);
            final ImageView ivNumSeleccionat= (ImageView) rootView.findViewById(R.id.ivNumSeleccionat);

            ivFromPage.setImageDrawable(FromPage);
            ivToPage.setImageDrawable(ToPage);

            final Button btBack = (Button) rootView.findViewById(R.id.btBackPel4);
            final Button btNext = (Button) rootView.findViewById(R.id.btNextPel4);

            btNext.setEnabled(false);
            btNext.setVisibility(View.INVISIBLE);

            final RadioGroup rbGroup = (RadioGroup) rootView.findViewById(R.id.rbGroup1Pel4);

            rbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                    //Busquem quin radioButton s'ha seleccionat
                    int radioButtonID = rbGroup.getCheckedRadioButtonId();
                    //Recuperem l'element ara que tenim el identificador per a la classe R (id)
                    RadioButton rb = (RadioButton) rootView.findViewById(radioButtonID);

                    btNext.setVisibility(View.VISIBLE);
                    btNext.setEnabled(true);

                    TextDrawable NumeroSeleccionat = TextDrawable.builder().beginConfig().width(110).height(110).endConfig().buildRound(rb.getText().toString(),ColorGenerator.DEFAULT.getRandomColor());
                    ivNumSeleccionat.setImageDrawable(NumeroSeleccionat);
                    ivNumSeleccionat.setVisibility(View.VISIBLE);

                    if(SegonTest){
                        respostes_recuperades.setTest2Pregunta4(rb.getText().toString());
                    }
                    else{
                        respostes_recuperades.setTest1Pregunta4(rb.getText().toString());
                    }
                }
            });


            //Enrere
            btBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(2);
                }
            });

            //Endevant
            btNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(4);
                }
            });

            return rootView;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class Fragment5 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public Fragment5() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Fragment5 newInstance(int sectionNumber) {
            Fragment5 fragment = new Fragment5();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.activity_test_5, container, false);

            ColorGenerator generator = ColorGenerator.DEFAULT;
            TextDrawable FromPage = TextDrawable.builder().beginConfig().width(40).height(40).endConfig().buildRound("5",generator.getRandomColor());
            TextDrawable ToPage = TextDrawable.builder().beginConfig().width(40).height(40).endConfig().buildRound("6",generator.getRandomColor());

            ImageView ivFromPage = (ImageView) rootView.findViewById(R.id.ivFromPage1);
            ImageView ivToPage = (ImageView) rootView.findViewById(R.id.ivToPage1);
            final ImageView ivNumSeleccionat= (ImageView) rootView.findViewById(R.id.ivNumSeleccionat);

            ivFromPage.setImageDrawable(FromPage);
            ivToPage.setImageDrawable(ToPage);

            final Button btBack = (Button) rootView.findViewById(R.id.btBackPel5);
            final Button btNext = (Button) rootView.findViewById(R.id.btNextPel5);

            btNext.setEnabled(false);
            btNext.setVisibility(View.INVISIBLE);


            final RadioGroup rbGroup = (RadioGroup) rootView.findViewById(R.id.rbGroup1Pel5);

            rbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                    //Busquem quin radioButton s'ha seleccionat
                    int radioButtonID = rbGroup.getCheckedRadioButtonId();
                    //Recuperem l'element ara que tenim el identificador per a la classe R (id)
                    RadioButton rb = (RadioButton) rootView.findViewById(radioButtonID);

                    btNext.setVisibility(View.VISIBLE);
                    btNext.setEnabled(true);

                    TextDrawable NumeroSeleccionat = TextDrawable.builder().beginConfig().width(110).height(110).endConfig().buildRound(rb.getText().toString(),ColorGenerator.DEFAULT.getRandomColor());
                    ivNumSeleccionat.setImageDrawable(NumeroSeleccionat);
                    ivNumSeleccionat.setVisibility(View.VISIBLE);

                    if(SegonTest){
                        respostes_recuperades.setTest2Pregunta5(rb.getText().toString());
                    }
                    else{
                        respostes_recuperades.setTest1Pregunta5(rb.getText().toString());
                    }
                }
            });


            //Enrere
            btBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(3);
                }
            });

            //Endevant
            btNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(5);
                }
            });

            return rootView;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class Fragment6 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public Fragment6() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Fragment6 newInstance(int sectionNumber) {
            Fragment6 fragment = new Fragment6();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.activity_test_6, container, false);

            ColorGenerator generator = ColorGenerator.DEFAULT;
            TextDrawable FromPage = TextDrawable.builder().beginConfig().width(40).height(40).endConfig().buildRound("6",generator.getRandomColor());
            TextDrawable ToPage = TextDrawable.builder().beginConfig().width(40).height(40).endConfig().buildRound("6",generator.getRandomColor());

            ImageView ivFromPage = (ImageView) rootView.findViewById(R.id.ivFromPage1);
            ImageView ivToPage = (ImageView) rootView.findViewById(R.id.ivToPage1);
            final ImageView ivNumSeleccionat= (ImageView) rootView.findViewById(R.id.ivNumSeleccionat);

            ivFromPage.setImageDrawable(FromPage);
            ivToPage.setImageDrawable(ToPage);

            final Button btBack = (Button) rootView.findViewById(R.id.btBackPel6);
            final Button btNext = (Button) rootView.findViewById(R.id.btNextPel6);

            btNext.setEnabled(false);
            btNext.setVisibility(View.INVISIBLE);

            final RadioGroup rbGroup = (RadioGroup) rootView.findViewById(R.id.rbGroup1Pel6);

            rbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                    //Busquem quin radioButton s'ha seleccionat
                    int radioButtonID = rbGroup.getCheckedRadioButtonId();
                    //Recuperem l'element ara que tenim el identificador per a la classe R (id)
                    RadioButton rb = (RadioButton) rootView.findViewById(radioButtonID);

                    if(!SegonTest){
                        if(!respostes_recuperades.getTest1Pregunta1().matches("") && !respostes_recuperades.getTest1Pregunta2().matches("") && !respostes_recuperades.getTest1Pregunta3().matches("") &&
                                !respostes_recuperades.getTest1Pregunta4().matches("") && !respostes_recuperades.getTest1Pregunta5().matches("")){
                            respostes_recuperades.setTest1Pregunta6(rb.getText().toString());
                            respostes_recuperades.setTest1Sumatori();
                            btNext.setVisibility(View.VISIBLE);
                            btNext.setEnabled(true);
                        }
                        else{
                            Toast.makeText(getContext(), R.string.MissAnswers,
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                    if(SegonTest){
                        if(!respostes_recuperades.getTest2Pregunta1().matches("") && !respostes_recuperades.getTest2Pregunta2().matches("") && !respostes_recuperades.getTest2Pregunta3().matches("") &&
                                !respostes_recuperades.getTest2Pregunta4().matches("") && !respostes_recuperades.getTest2Pregunta5().matches("")){
                            btNext.setVisibility(View.VISIBLE);
                            btNext.setEnabled(true);
                            respostes_recuperades.setTest2Pregunta6(rb.getText().toString());
                            respostes_recuperades.setTest2Sumatori();
                        }
                        else{
                            Toast.makeText(getContext(), R.string.MissAnswers,
                                    Toast.LENGTH_LONG).show();
                        }

                    }

                    TextDrawable NumeroSeleccionat = TextDrawable.builder().beginConfig().width(110).height(110).endConfig().buildRound(rb.getText().toString(),ColorGenerator.DEFAULT.getRandomColor());
                    ivNumSeleccionat.setImageDrawable(NumeroSeleccionat);
                    ivNumSeleccionat.setVisibility(View.VISIBLE);

                }
            });


            //Enrere
            btBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(4);
                }
            });


            //Endevant

            btNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(Curta && SegonTest){
                        //Aquest cas és la versió curta i el segon test, s'ha d'enviar resultat a la DB i acabar
                        //Aqui enviem el fitxer CSV i JSON a FireBase i retornem a 'Tractaments'

                        final String ruta = respostes_recuperades.ConvertToCVS(true);
                        //Ara tenim la ruta del fitxer CSV[0] a la memoria de la tauleta
                        StorageReference PacientRef = myRef.child(pacient.getID()).child(episodi).child("respostes").child("ResultatVersioCurta_"+pacient.getID()+".csv");
                        Uri file = Uri.fromFile(new File(ruta));

                        // Create file metadata including the content type (CSV)
                        StorageMetadata metadata = new StorageMetadata.Builder()
                               .setContentType("text/csv")
                               .build();

                        mProgressDialog = new ProgressDialog(getContext());
                        mProgressDialog.setMessage(getString(R.string.loading));
                        mProgressDialog.setIndeterminate(true);
                        mProgressDialog.setCancelable(false);
                        mProgressDialog.show();

                        PacientRef.putFile(file,metadata).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                mProgressDialog.dismiss();
                                Toast.makeText(getContext(), R.string.UploadCSVSuccessful,
                                        Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mProgressDialog.dismiss();
                                Toast.makeText(getContext(), "Error!",
                                        Toast.LENGTH_LONG).show();
                            }
                        }) ;


                        //Notificació
                        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.iconrem);

                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(getContext())
                                        .setLargeIcon(bitmap)
                                        .setSmallIcon(R.drawable.attachment)
                                        .setContentTitle(getString(R.string.NotificationAlert))
                                        .setContentText(getString(R.string.NotificationTest2, pacient.getName()))
                                        .setTicker(getString(R.string.Test2Ticker,pacient.getName()));

                        NotificationManager m = (NotificationManager) getActivity().getSystemService(getContext().NOTIFICATION_SERVICE);
                        m.notify(1,mBuilder.build());


                        AlertDialog.Builder DialegDespedida = new AlertDialog.Builder(getContext());
                        LayoutInflater factory = LayoutInflater.from(getContext());
                        textEntryView = factory.inflate(R.layout.dialegs, null);
                        tv = (TextView) textEntryView.findViewById(R.id.tvMissatgeDialeg);
                        tv.setText(getString(R.string.Colaboration,pacient.getName()));
                        Button bt = (Button) textEntryView.findViewById(R.id.btDiaelgOK);


                        DialegDespedida
                                    .setCancelable(false)
                                    .setView(textEntryView);

                        final AlertDialog alerta = DialegDespedida.create();

                        alerta.show();

                        bt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alerta.dismiss();
                                startActivity(new Intent(getContext(), TractamentsActivity.class).putExtra("final","final").putExtra("file",ruta));
                            }
                        });
                    }

                    if(Curta && !SegonTest){
                        //Guardem respostes ja que és la primera vegada i és la versió curta
                        editor.putString("respostes",gson.toJson(respostes_recuperades,TestAnswers.class));
                        editor.commit();

                        //Notificació
                        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.iconrem);

                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(getContext())
                                        .setLargeIcon(bitmap)
                                        .setSmallIcon(R.drawable.attachment)
                                        .setContentTitle(getString(R.string.NotificationAlert))
                                        .setContentText(getString(R.string.NotificationTest1, pacient.getName()))
                                        .setTicker(getString(R.string.Test1Ticker,pacient.getName()));

                        NotificationManager m = (NotificationManager) getActivity().getSystemService(getContext().NOTIFICATION_SERVICE);
                        m.notify(2,mBuilder.build());

                        Intent intent = new Intent (getContext(), EvocarActivity.class);
                        intent.putExtra("EvocarC","EvocarC");
                        startActivity(intent);
                    }

                    if(!Curta && SegonTest){
                        //Aqui enviem el fitxer CSV i JSON a FireBase i retornem a 'Tractaments'
                        final String ruta = respostes_recuperades.ConvertToCVS(false);
                        Uri file = Uri.fromFile(new File(ruta));

                        //Ara tenim la ruta del fitxer CSV[0] a la memoria de la tauleta
                        StorageReference PacientRef = myRef.child(pacient.getID()).child(episodi).child("respostes").child("ResultatVersioLlarga_"+pacient.getID()+".csv");

                        // Create file metadata including the content type (CSV)
                        StorageMetadata metadata = new StorageMetadata.Builder()
                                .setContentType("text/csv")
                                .build();

                        mProgressDialog = new ProgressDialog(getContext());
                        mProgressDialog.setMessage(getString(R.string.loading));
                        mProgressDialog.setIndeterminate(true);
                        mProgressDialog.setCancelable(false);
                        mProgressDialog.show();

                        PacientRef.putFile(file,metadata).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                mProgressDialog.dismiss();
                                Toast.makeText(getContext(), R.string.UploadCSVSuccessful,
                                        Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Error!",
                                        Toast.LENGTH_LONG).show();
                            }
                        });

                        //Notificació

                        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.iconrem);

                        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(getContext())
                                        .setLargeIcon(bitmap)
                                        .setSmallIcon(R.drawable.attachment)
                                        .setContentTitle(getString(R.string.NotificationAlert))
                                        .setContentText(getString(R.string.NotificationTest2, pacient.getName()))
                                        .setSound(defaultSound)
                                        .setTicker(getString(R.string.Test2Ticker,pacient.getName()));

                        NotificationManager m = (NotificationManager) getActivity().getSystemService(getContext().NOTIFICATION_SERVICE);
                        m.notify(3,mBuilder.build());

                        AlertDialog.Builder DialegDespedida = new AlertDialog.Builder(getContext());
                        LayoutInflater factory = LayoutInflater.from(getContext());
                        textEntryView = factory.inflate(R.layout.dialegs, null);
                        tv = (TextView) textEntryView.findViewById(R.id.tvMissatgeDialeg);
                        tv.setText(getString(R.string.Colaboration,pacient.getName()));
                        Button bt = (Button) textEntryView.findViewById(R.id.btDiaelgOK);

                        DialegDespedida
                                .setCancelable(false)
                                .setView(textEntryView);


                        final AlertDialog alerta = DialegDespedida.create();

                        alerta.show();

                        bt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alerta.dismiss();
                                startActivity(new Intent(getContext(), TractamentsActivity.class).putExtra("final","final").putExtra("file",ruta));
                            }
                        });

                    }

                    if(!Curta && !SegonTest){
                        //Guardem respostes ja que és la primera vegada i és la versió curta
                        editor.putString("respostes",gson.toJson(respostes_recuperades,TestAnswers.class));
                        editor.commit();
                        Intent intent = new Intent (getContext(), RespirarActivity.class);

                        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.iconrem);

                        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        //Notificació
                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(getContext())
                                        .setLargeIcon(bitmap)
                                        .setSmallIcon(R.drawable.attachment)
                                        .setContentTitle(getString(R.string.NotificationAlert))
                                        .setContentText(getString(R.string.NotificationTest1, pacient.getName()))
                                        .setSound(defaultSound)
                                        .setTicker(getString(R.string.Test1Ticker,pacient.getName()));

                        NotificationManager m = (NotificationManager) getActivity().getSystemService(getContext().NOTIFICATION_SERVICE);
                        m.notify(4,mBuilder.build());

                        startActivity(intent);
                    }

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
                return TestActivity.PlaceholderFragment.newInstance(position + 1);
            }
            if(position==1){
                return TestActivity.Fragment2.newInstance(position + 1);
            }
            if(position==2){
                return TestActivity.Fragment3.newInstance(position + 1);
            }
            if(position==3){
                return TestActivity.Fragment4.newInstance(position + 1);
            }
            if(position==4){
                return TestActivity.Fragment5.newInstance(position + 1);
            }
            if(position==5){
                return TestActivity.Fragment6.newInstance(position + 1);
            }

            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 6 total pages.
            return 6;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.Question1);
                case 1:
                    return getString(R.string.Question2);
                case 2:
                    return getString(R.string.Question3);
                case 3:
                    return getString(R.string.Question4);
                case 4:
                    return getString(R.string.Question5);
                case 5:
                    return getString(R.string.Question6);
            }
            return null;
        }
    }

}