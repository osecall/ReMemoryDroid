package com.rememorydroid.oriolsecall.rememorydroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.IdRes;
import android.support.design.widget.TabLayout;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

public class QuestionariActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private static ViewPager mViewPager;

    public static SharedPreferences prefs;

    public static TestAnswers respostes_recuperades;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionari);

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

        SharedPreferences.Editor editor = prefs.edit();

        String respostes_json = prefs.getString("respostes", null);
        //Guardem a TestAnswers
        Gson gson = new Gson();
        respostes_recuperades= new TestAnswers();
        respostes_recuperades = gson.fromJson(respostes_json, TestAnswers.class);


    }


    //Part del menú 'action bar'

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menuavaluadors, menu);
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
            Toast.makeText(QuestionariActivity.this, R.string.signed_out,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(QuestionariActivity.this, IniciActivity.class);
            startActivity(areaAvaluador);

        }

        if (id == R.id.btSignOutPacient) {

            //Retorna a la pantalla 'Area Avaluador'

            Toast.makeText(QuestionariActivity.this, R.string.MenuChangePacient,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(QuestionariActivity.this, AreaAvaluadorActivity.class);
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
        private static final String ARG_SECTION_NUMBER = "section_number"; //Es podrà borrar

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
            final View rootView = inflater.inflate(R.layout.activity_when_it_happened, container, false);

            Button btNextTime = (Button) rootView.findViewById(R.id.btNextTime);

            final RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.rgWhenTime);




            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                    //Busquem quin radioButton s'ha seleccionat
                    int radioButtonID = radioGroup.getCheckedRadioButtonId();
                    //Recuperem l'element ara que tenim el identificador per a la classe R (id)
                    RadioButton rb = (RadioButton) rootView.findViewById(radioButtonID);

                    Toast.makeText(getContext(), rb.getText().toString(),
                            Toast.LENGTH_LONG).show();

                    //Guardem resposta obteniguda
                    respostes_recuperades.setPreguntesQuan_Temps(rb.getText().toString());



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
            final View rootView = inflater.inflate(R.layout.when_it_happened2, container, false);

            Button btNextWeather = (Button) rootView.findViewById(R.id.btNextWeather);

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
                            Toast.LENGTH_LONG).show();

                    respostes_recuperades.setPreguntesQuan_EpocaAny(rb.getText().toString());


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
            final View rootView = inflater.inflate(R.layout.when_it_happened3, container, false);

            final RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.rgWhen1Long);

            Button btNextLong = (Button) rootView.findViewById(R.id.btNextLong);

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
                            Toast.LENGTH_LONG).show();

                    respostes_recuperades.setPreguntesQuan_EpocaAny(rb.getText().toString());


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
            final View rootView = inflater.inflate(R.layout.when_it_happened4, container, false);

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

            Button btNextMonth = (Button) rootView.findViewById(R.id.btNextMonth);

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
                            Toast.LENGTH_LONG).show();
                    respostes_recuperades.setPreguntesQuan_Mes(seleccionat);
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
            final View rootView = inflater.inflate(R.layout.when_it_happened5, container, false);

            final RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.rgWhen1DayTime);

            Button btNextDayTime = (Button) rootView.findViewById(R.id.btNextDayTime);

            btNextDayTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(4);
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
                            Toast.LENGTH_LONG).show();

                    respostes_recuperades.setPreguntesQuan_FranjaDia(rb.getText().toString());


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

            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 5;
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
            }
            return null;
        }
    }

}
