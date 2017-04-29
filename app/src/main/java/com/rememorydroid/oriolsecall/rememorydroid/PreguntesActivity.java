package com.rememorydroid.oriolsecall.rememorydroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.File;

public class PreguntesActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static ViewPager mViewPager;
    public static SharedPreferences prefs;
    public static TestAnswers respostes_recuperades = new TestAnswers();
    private static PacientUsuari pacient;
    public static StorageReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntes);

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

        String respostes_json = prefs.getString("respostes", null);
        //Guardem a TestAnswers
        Gson gson = new Gson();
        respostes_recuperades = gson.fromJson(respostes_json, TestAnswers.class);

        String pacient_json = prefs.getString("pacient",null);
        pacient = gson.fromJson(pacient_json, PacientUsuari.class);

        AlertDialog.Builder test = new AlertDialog.Builder(PreguntesActivity.this);
        test.setMessage(respostes_recuperades.toString());

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

            //Retorna a la pantalla inicial
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(PreguntesActivity.this, R.string.signed_out,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(PreguntesActivity.this, SignInActivity.class);
            startActivity(areaAvaluador);

        }

        if (id == R.id.btSignOutPacient) {

            //Retorna a la pantalla 'Area Avaluador'

            Toast.makeText(PreguntesActivity.this, R.string.MenuChangePacient,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(PreguntesActivity.this, AreaAvaluadorActivity.class);
            startActivity(areaAvaluador);

        }

        return super.onOptionsItemSelected(item);
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
            final View rootView = inflater.inflate(R.layout.activity_preguntes1_1, container, false);

            final Button btNextTime = (Button) rootView.findViewById(R.id.btNextTime);
            final Button btBackTime = (Button) rootView.findViewById(R.id.btBackTime);

            btNextTime.setEnabled(false);
            btNextTime.setVisibility(View.INVISIBLE);

            final RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.rgWhenTime);

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
                    respostes_recuperades.setPreguntesQuanEpocaAny(rb.getText().toString());

                    btNextTime.setVisibility(View.VISIBLE);
                    btNextTime.setEnabled(true);



                }

            });

            btBackTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(),VisualitzarFragmentsActivity.class);
                    intent.putExtra("Segon","Segon");
                    startActivity(intent);
                }
            });
            btNextTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(1);
                }
            });
            return rootView;
        }
    }


    //---------------------------------------------------------------------------------------------
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
            final View rootView = inflater.inflate(R.layout.activity_preguntes1_2, container, false);

            final Button btNextWeather = (Button) rootView.findViewById(R.id.btNextWeather);
            final Button btBackWeather = (Button) rootView.findViewById(R.id.btBackWeather);

            btNextWeather.setEnabled(false);
            btNextWeather.setVisibility(View.INVISIBLE);


            btBackWeather.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(0);
                }
            });
            btNextWeather.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(2);
                }
            });


            final RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.rgWhen1Weather);

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                    //Busquem quin radioButton s'ha seleccionat
                    int radioButtonID = radioGroup.getCheckedRadioButtonId();
                    //Recuperem l'element ara que tenim el identificador per a la classe R (id)
                    RadioButton rb = (RadioButton) rootView.findViewById(radioButtonID);

                    Toast.makeText(getContext(), rb.getText().toString(),
                            Toast.LENGTH_SHORT).show();

                    respostes_recuperades.setPreguntesQuanTemps(rb.getText().toString());

                    btNextWeather.setEnabled(true);
                    btNextWeather.setVisibility(View.VISIBLE);

                }

            });


            return rootView;
        }

    }

    //---------------------------------------------------------------------------------------------
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
            final View rootView = inflater.inflate(R.layout.activity_preguntes1_3, container, false);

            final RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.rgWhen1Long);

            final Button btNextLong = (Button) rootView.findViewById(R.id.btNextLong);
            final Button btBackLong = (Button) rootView.findViewById(R.id.btBackLong);

            btNextLong.setEnabled(false);
            btNextLong.setVisibility(View.INVISIBLE);


            btBackLong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(1);
                }
            });
            btNextLong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(3);
                }
            });

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                    //Busquem quin radioButton s'ha seleccionat
                    int radioButtonID = radioGroup.getCheckedRadioButtonId();
                    //Recuperem l'element ara que tenim el identificador per a la classe R (id)
                    RadioButton rb = (RadioButton) rootView.findViewById(radioButtonID);

                    Toast.makeText(getContext(), rb.getText().toString(),
                            Toast.LENGTH_SHORT).show();

                    respostes_recuperades.setPreguntesQuanDuracio(rb.getText().toString());

                    btNextLong.setEnabled(true);
                    btNextLong.setVisibility(View.VISIBLE);


                }

            });


            return rootView;
        }

    }


    //---------------------------------------------------------------------------------------------
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
            final View rootView = inflater.inflate(R.layout.activity_preguntes1_4, container, false);

            final ListView listView = (ListView) rootView.findViewById(R.id.lvMonths);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item);

            adapter.add(getString(R.string.January));
            adapter.add(getString(R.string.February));
            adapter.add(getString(R.string.March));
            adapter.add(getString(R.string.April));
            adapter.add(getString(R.string.May));
            adapter.add(getString(R.string.June));
            adapter.add(getString(R.string.July));
            adapter.add(getString(R.string.August));
            adapter.add(getString(R.string.September));
            adapter.add(getString(R.string.October));
            adapter.add(getString(R.string.November));
            adapter.add(getString(R.string.Desember));

            listView.setAdapter(adapter);

            final Button btNextMonth = (Button) rootView.findViewById(R.id.btNextMonth);
            final Button btBackMonth = (Button) rootView.findViewById(R.id.btBackMonth);

            btNextMonth.setEnabled(false);
            btNextMonth.setVisibility(View.INVISIBLE);

            btBackMonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(2);
                }
            });
            btNextMonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(4);
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String seleccionat = listView.getItemAtPosition(i).toString();
                    listView.setItemChecked(i,true);
                    Toast.makeText(getContext(), seleccionat,
                            Toast.LENGTH_SHORT).show();
                    respostes_recuperades.setPreguntesQuanMes(seleccionat);

                    btNextMonth.setEnabled(true);
                    btNextMonth.setVisibility(View.VISIBLE);
                }
            });


            return rootView;
        }

    }

    //---------------------------------------------------------------------------------------------
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
            final View rootView = inflater.inflate(R.layout.activity_preguntes1_5, container, false);

            final RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.rgWhen1DayTime);

            final Button btNextDayTime = (Button) rootView.findViewById(R.id.btNextDayTime);
            final Button btBackDayTime = (Button) rootView.findViewById(R.id.btBackDayTime);

            btNextDayTime.setEnabled(false);
            btNextDayTime.setVisibility(View.INVISIBLE);


            btBackDayTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(3);
                }
            });
            btNextDayTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(5);
                }
            });

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                    //Busquem quin radioButton s'ha seleccionat
                    int radioButtonID = radioGroup.getCheckedRadioButtonId();
                    //Recuperem l'element ara que tenim el identificador per a la classe R (id)
                    RadioButton rb = (RadioButton) rootView.findViewById(radioButtonID);

                    Toast.makeText(getContext(), rb.getText().toString(),
                            Toast.LENGTH_SHORT).show();

                    respostes_recuperades.setPreguntesQuanFranjaDia(rb.getText().toString());

                    btNextDayTime.setEnabled(true);
                    btNextDayTime.setVisibility(View.VISIBLE);
                }

            });


            return rootView;
        }

    }

    //---------------------------------------------------------------------------------------------
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
            final View rootView = inflater.inflate(R.layout.activity_preguntes1_6, container, false);

            final RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.rgWhen1Location);

            final Button btNextGeo = (Button) rootView.findViewById(R.id.btNextGeo);
            final Button btBackGeo = (Button) rootView.findViewById(R.id.btBackGeo);

            btNextGeo.setEnabled(false);
            btNextGeo.setVisibility(View.INVISIBLE);


            btBackGeo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(4);

                }
            });
            btNextGeo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(6);
                }
            });

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                    //Busquem quin radioButton s'ha seleccionat
                    int radioButtonID = radioGroup.getCheckedRadioButtonId();
                    //Recuperem l'element ara que tenim el identificador per a la classe R (id)
                    RadioButton rb = (RadioButton) rootView.findViewById(radioButtonID);

                    Toast.makeText(getContext(), rb.getText().toString(),
                            Toast.LENGTH_SHORT).show();

                    respostes_recuperades.setPreguntesOnLocalitzacio(rb.getText().toString());

                    btNextGeo.setEnabled(true);
                    btNextGeo.setVisibility(View.VISIBLE);


                }

            });


            return rootView;
        }

    }


    //---------------------------------------------------------------------------------------------
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class Fragment7 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public Fragment7() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Fragment7 newInstance(int sectionNumber) {
            Fragment7 fragment = new Fragment7();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.activity_preguntes1_7, container, false);

            final RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.rgWhen1Enviroments);

            final Button btNextEnv = (Button) rootView.findViewById(R.id.btNextEnv);
            final Button btBackEnv = (Button) rootView.findViewById(R.id.btBackEnv);

            btNextEnv.setEnabled(false);
            btNextEnv.setVisibility(View.INVISIBLE);

            btBackEnv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(5);

                }
            });
            btNextEnv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(7);
                }
            });

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                    //Busquem quin radioButton s'ha seleccionat
                    int radioButtonID = radioGroup.getCheckedRadioButtonId();
                    //Recuperem l'element ara que tenim el identificador per a la classe R (id)
                    RadioButton rb = (RadioButton) rootView.findViewById(radioButtonID);

                    Toast.makeText(getContext(), rb.getText().toString(),
                            Toast.LENGTH_SHORT).show();

                    respostes_recuperades.setPreguntesOnEntorns(rb.getText().toString());

                    btNextEnv.setEnabled(true);
                    btNextEnv.setVisibility(View.VISIBLE);
                }

            });


            return rootView;
        }

    }

    //---------------------------------------------------------------------------------------------
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class Fragment8 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public Fragment8() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Fragment8 newInstance(int sectionNumber) {
            Fragment8 fragment = new Fragment8();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.activity_preguntes1_8, container, false);


            final RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.rgWhen1Loc);
            final Button btNextLoc = (Button) rootView.findViewById(R.id.btNextLoc);
            final Button btBackLoc = (Button) rootView.findViewById(R.id.btBackDetails);

            btNextLoc.setEnabled(false);
            btNextLoc.setVisibility(View.INVISIBLE);

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                    //Busquem quin radioButton s'ha seleccionat
                    int radioButtonID = radioGroup.getCheckedRadioButtonId();
                    //Recuperem l'element ara que tenim el identificador per a la classe R (id)
                    RadioButton rb = (RadioButton) rootView.findViewById(radioButtonID);

                    Toast.makeText(getContext(), rb.getText().toString(),
                            Toast.LENGTH_SHORT).show();

                    respostes_recuperades.setPreguntesOnUbicacio(rb.getText().toString());

                    btNextLoc.setEnabled(true);
                    btNextLoc.setVisibility(View.VISIBLE);


                }

            });



            btBackLoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(6);
                }
            });
            btNextLoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(8);
                }
            });

            return rootView;
        }

    }

//---------------------------------------------------------------------------------------------
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class Fragment9 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public Fragment9() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Fragment9 newInstance(int sectionNumber) {
            Fragment9 fragment = new Fragment9();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.activity_preguntes1_9, container, false);

            final Button btNextDetails = (Button) rootView.findViewById(R.id.btNextDetails);
            final Button btBackDetails = (Button) rootView.findViewById(R.id.btBackDetails);

            btNextDetails.setEnabled(false);
            btNextDetails.setVisibility(View.INVISIBLE);


            btBackDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(7);
                }
            });
            btNextDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(9);
                }
            });


            final RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.rgDetails1);

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                    //Busquem quin radioButton s'ha seleccionat
                    int radioButtonID = radioGroup.getCheckedRadioButtonId();
                    //Recuperem l'element ara que tenim el identificador per a la classe R (id)
                    RadioButton rb = (RadioButton) rootView.findViewById(radioButtonID);

                    Toast.makeText(getContext(), rb.getText().toString(),
                            Toast.LENGTH_SHORT).show();

                    respostes_recuperades.setPreguntesPerceptiusSons(rb.getText().toString());

                    btNextDetails.setEnabled(true);
                    btNextDetails.setVisibility(View.VISIBLE);
                }

            });


            return rootView;
        }

    }


    //---------------------------------------------------------------------------------------------
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class Fragment10 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public Fragment10() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Fragment10 newInstance(int sectionNumber) {
            Fragment10 fragment = new Fragment10();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.activity_preguntes1_10, container, false);

            final Button btNextDetails2 = (Button) rootView.findViewById(R.id.btNextDetails2);
            final Button btBackDetails2 = (Button) rootView.findViewById(R.id.btBackDetails2);

            btNextDetails2.setEnabled(false);
            btNextDetails2.setVisibility(View.INVISIBLE);


            btBackDetails2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(8);
                }
            });
            btNextDetails2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(10);
                }
            });


            final RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.rgDetails2);

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                    //Busquem quin radioButton s'ha seleccionat
                    int radioButtonID = radioGroup.getCheckedRadioButtonId();
                    //Recuperem l'element ara que tenim el identificador per a la classe R (id)
                    RadioButton rb = (RadioButton) rootView.findViewById(radioButtonID);

                    Toast.makeText(getContext(), rb.getText().toString(),
                            Toast.LENGTH_SHORT).show();

                    respostes_recuperades.setPreguntesPerceptiusTemperatura(rb.getText().toString());

                    btNextDetails2.setEnabled(true);
                    btNextDetails2.setVisibility(View.VISIBLE);

                }

            });
            return rootView;
        }

    }


    //---------------------------------------------------------------------------------------------
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class Fragment11 extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public Fragment11() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Fragment11 newInstance(int sectionNumber) {
            Fragment11 fragment = new Fragment11();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.activity_preguntes1_11, container, false);

            final Button btNextDetails3 = (Button) rootView.findViewById(R.id.btNextDetails3);
            final Button btBackDetails3 = (Button) rootView.findViewById(R.id.btBackDetails3);

            btNextDetails3.setEnabled(false);
            btNextDetails3.setVisibility(View.INVISIBLE);


            btBackDetails3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(9);
                }
            });
            btNextDetails3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Ho guardem a SharedPreferences
                    Gson gson= new Gson();
                    String respostes_json = gson.toJson(respostes_recuperades,TestAnswers.class);
                    prefs.edit().putString("respostes",respostes_json);
                    prefs.edit().commit();
                    prefs.edit().apply();

                    Intent intent = new Intent(getContext(),RespirarActivity.class);
                    intent.putExtra("Tercer","Tercer");
                    startActivity(intent);



                }
            });

            final RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.rgDetails3);

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                    //Busquem quin radioButton s'ha seleccionat
                    int radioButtonID = radioGroup.getCheckedRadioButtonId();
                    //Recuperem l'element ara que tenim el identificador per a la classe R (id)
                    RadioButton rb = (RadioButton) rootView.findViewById(radioButtonID);

                    Toast.makeText(getContext(), rb.getText().toString(),
                            Toast.LENGTH_SHORT).show();

                    respostes_recuperades.setPreguntesPerceptiusOlors(rb.getText().toString());

                    if(respostes_recuperades.getPreguntesQuanTemps()!=null && respostes_recuperades.getPreguntesQuanDuracio()!=null &&
                            respostes_recuperades.getPreguntesQuanMes()!=null && respostes_recuperades.getPreguntesQuanFranjaDia()!=null &&
                            respostes_recuperades.getPreguntesOnLocalitzacio()!=null && respostes_recuperades.getPreguntesOnEntorns()!=null &&
                            respostes_recuperades.getPreguntesOnUbicacio()!=null && respostes_recuperades.getPreguntesPerceptiusSons()!=null &&
                            respostes_recuperades.getPreguntesPerceptiusTemperatura()!=null){
                        btNextDetails3.setEnabled(true);
                        btNextDetails3.setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(getContext(), R.string.MissAnswers,
                                Toast.LENGTH_LONG).show();
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
                return PlaceholderFragment.newInstance(position + 1);
            }
            if(position==1){
                return Fragment2.newInstance(position + 1);
            }
            if(position==2){
                return Fragment3.newInstance(position + 1);
            }
            if(position==3){
                return Fragment4.newInstance(position + 1);
            }
            if(position==4){
                return Fragment5.newInstance(position + 1);
            }
            if(position==5){
                return Fragment6.newInstance(position + 1);
            }
            if(position==6){
                return Fragment7.newInstance(position + 1);
            }
            if(position==7){
                return Fragment8.newInstance(position + 1);
            }
            if(position==8){
                return Fragment9.newInstance(position + 1);
            }
            if(position==9){
                return Fragment10.newInstance(position + 1);
            }
            if(position==10){
                return Fragment11.newInstance(position + 1);
            }


            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
             return 11;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.EpisodeSeasonTime);
                case 1:
                    return getString(R.string.WhatWasTheWeather);
                case 2:
                    return getString(R.string.HowLongItLast);
                case 3:
                    return getString(R.string.Month);
                case 4:
                    return getString(R.string.DayTime);
                case 5:
                    return getString(R.string.GeographicLocalization);
                case 6:
                    return getString(R.string.HowManyEnviroments);
                case 7:
                    return getString(R.string.Location);
                case 8:
                    return getString(R.string.HowManySounds);
                case 9:
                    return getString(R.string.Temperature);
                case 10:
                    return getString(R.string.WhatOdors);
            }
            return null;
        }
    }

    private void DialegQuestionari(){
        AlertDialog.Builder DialegFormControl = new AlertDialog.Builder(PreguntesActivity.this);

        LayoutInflater factory = LayoutInflater.from(PreguntesActivity.this);
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

}
