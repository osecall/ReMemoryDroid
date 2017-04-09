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

import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

public class QuestionariActivity2 extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private static ViewPager mViewPager;

    public static SharedPreferences prefs;

    public static TestAnswers respostes_recuperades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionari2);

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
            Toast.makeText(QuestionariActivity2.this, R.string.signed_out,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(QuestionariActivity2.this, IniciActivity.class);
            startActivity(areaAvaluador);

        }

        if (id == R.id.btSignOutPacient) {

            //Retorna a la pantalla 'Area Avaluador'

            Toast.makeText(QuestionariActivity2.this, R.string.MenuChangePacient,
                    Toast.LENGTH_LONG).show();
            Intent areaAvaluador = new Intent(QuestionariActivity2.this, AreaAvaluadorActivity.class);
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
            final View rootView = inflater.inflate(R.layout.fragment_questionari_activity2, container, false);
            Button btNextPeople = (Button) rootView.findViewById(R.id.btNextPeople);
            Button btBackPeople = (Button) rootView.findViewById(R.id.btBackPeople);

            final RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.rgPeople);

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
                    respostes_recuperades.setPreguntesPersones_Accions(rb.getText().toString());



                }

            });

            btBackPeople.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(),VisualitzarActivity1.class);
                    intent.putExtra("Tercer","Tercer");
                    startActivity(intent);
                }
            });
            btNextPeople.setOnClickListener(new View.OnClickListener() {
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
            final View rootView = inflater.inflate(R.layout.fragment2_questionari_activity2, container, false);
            Button btNextPeople = (Button) rootView.findViewById(R.id.btNextPeople2);
            Button btBackPeople = (Button) rootView.findViewById(R.id.btBackPeople2);

            final RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.rgPeople2);

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
                    respostes_recuperades.setPreguntesPersones_Grups(rb.getText().toString());



                }

            });

            btBackPeople.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(0);
                }
            });
            btNextPeople.setOnClickListener(new View.OnClickListener() {
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
            final View rootView = inflater.inflate(R.layout.fragment3_questionari_activity2, container, false);
            Button btNextPeople = (Button) rootView.findViewById(R.id.btNextEmotions);
            Button btBackPeople = (Button) rootView.findViewById(R.id.btBackPeople3);

            final RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.rgPeople3);

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
                    respostes_recuperades.setPreguntesPersones_Relacio(rb.getText().toString());

                }

            });

            btBackPeople.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(2);
                }
            });
            btNextPeople.setOnClickListener(new View.OnClickListener() {
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
            final View rootView = inflater.inflate(R.layout.fragment4_questionari_activity2, container, false);
            Button btNextEmotions = (Button) rootView.findViewById(R.id.btNextEmotions);
            Button btBackEmotions = (Button) rootView.findViewById(R.id.btBackEmotions);






            //Guardem resposta obteniguda
            respostes_recuperades.setPreguntesEmocions_Observades(null);
            respostes_recuperades.setPreguntesEmocions_Propies(null);



            btBackEmotions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(2);
                }
            });
            btNextEmotions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(4);
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
                return QuestionariActivity2.PlaceholderFragment.newInstance(position + 1);
            }
            if(position==1){
                return QuestionariActivity2.Fragment2.newInstance(position + 1);
            }
            if(position==2){
                return QuestionariActivity2.Fragment3.newInstance(position + 1);
            }
            if(position==3){
                return QuestionariActivity2.Fragment4.newInstance(position + 1);
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
