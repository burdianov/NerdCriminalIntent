package com.crackncrunch.nerdcriminalintent.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.crackncrunch.nerdcriminalintent.R;
import com.crackncrunch.nerdcriminalintent.dto.Crime;
import com.crackncrunch.nerdcriminalintent.fragments.CrimeFragment;
import com.crackncrunch.nerdcriminalintent.db.CrimeLab;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity
        implements View.OnClickListener, CrimeFragment.Callbacks {

    public static final String EXTRA_CRIME_ID =
            "com.crackncrunch.nerdcriminalintent.crime_id";

    private ViewPager mViewPager;
    private Button mFirstButton;
    private Button mLastButton;
    private List<Crime> mCrimes;

    public static Intent newIntent(Context packageContext, UUID crimeID) {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeID);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID crimeID = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager = (ViewPager) findViewById(R.id.crime_view_pager);
        mFirstButton = (Button) findViewById(R.id.first_button);
        mFirstButton.setOnClickListener(this);
        mLastButton = (Button) findViewById(R.id.last_button);
        mLastButton.setOnClickListener(this);

        mCrimes = CrimeLab.get(this).getCrimes();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        setViewPagerCurrentItem(crimeID);
    }

    private void setViewPagerCurrentItem(UUID crimeID) {
        for (int i = 0; i < mCrimes.size(); i++) {

            if (mCrimes.get(i).getId().equals(crimeID)) {
                mViewPager.setCurrentItem(i);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.first_button:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.last_button:
                mViewPager.setCurrentItem(mCrimes.size() - 1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {

    }
}
