package polyteam.myownstetho.Activities;

import java.util.List;
import java.util.Vector;

import polyteam.myownstetho.Activities.ActivityDialogs.EnterManuallyPulseDialog;
import polyteam.myownstetho.Activities.ActivityDialogs.FloatButtonsDialog;
import polyteam.myownstetho.Activities.ActivityDialogs.WaitForPulseMeasured;
import polyteam.myownstetho.Adapters.MyFragmentPagerAdapter;
import polyteam.myownstetho.Fragments.Dos;
import polyteam.myownstetho.Fragments.Droit;
import polyteam.myownstetho.Fragments.Gauche;
import polyteam.myownstetho.Fragments.Torse;
import polyteam.myownstetho.HandlerTCP.AsyncResponseString;

import polyteam.myownstetho.MyApp;
import polyteam.myownstetho.R;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.Toast;

public class Simulation extends AppCompatActivity implements
		OnTabChangeListener, OnPageChangeListener, AsyncResponseString, EnterManuallyPulseDialog.EnterManuallyPulseListener {

	private RadioGroup floatButtons;
	private TabHost tabHost;
    private boolean choixHomme;
	private ViewPager viewPager;
	private MyFragmentPagerAdapter myViewPagerAdapter;
	private short currentCheckedPatho =0;
	private DialogFragment waitForPulse;
	private DialogFragment setPulseManually;

	private boolean pulseMeasured = false;
	private int manualPulse;
	private boolean pulseSet = false;



	// fake content for tabhost
	class FakeContent implements TabContentFactory {
		private final Context mContext;

		public FakeContent(Context context) {
			mContext = context;
		}

		@Override
		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumHeight(0);
			v.setMinimumWidth(0);
			return v;
		}
	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simu_fragments_handler);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		/*
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            choixHomme = extras.getBoolean("choixHomme");
        }*/
        choixHomme=((MyApp) getApplicationContext()).getChoixHomme();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		floatButtons = (RadioGroup) findViewById(R.id.floatButtons);
		// init tabhost
		this.initializeTabHost(savedInstanceState);
		// init ViewPager
		this.initializeViewPager(choixHomme);


	}

	protected void onResume(){
		super.onResume();
		System.out.println("Mode onResume de Simulation");
		/*
		if(!pulseMeasured){
			// Affiche d'un dialogue en attendant que le stethoscope soit pret
			waitForPulse = WaitForPulseMeasured.newInstance();
			waitForPulse.show(getFragmentManager(), "Stetho Ready");
			waitForPulse.setCancelable(false);
			((MyApp) getApplicationContext()).getResponse(this); // Demande si le stetho est prete
		}*/

		if(!pulseSet){
			// Affiche d'un dialogue en attendant que le stethoscope soit pret
			setPulseManually = EnterManuallyPulseDialog.newInstance();
			setPulseManually.show(getFragmentManager(), "Manual Pulse");
			setPulseManually.setCancelable(false);
		}

	}

	public void onUserPulseDetermined(DialogFragment dialog, int value){
		System.out.println("Transmission du pouls: " + value);
		((MyApp) getApplicationContext()).sendMessage("modeSetPulse", this);
		manualPulse= value;
	}

	public void onBackPressed(){
		((MyApp) getApplicationContext()).sendMessage("StopSimu", this);
		return;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.simu, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home: // Appui sur bouton retour
				((MyApp) getApplicationContext()).sendMessage("StopSimu", this);
                return true;

            case R.id.buttons: // Affichage description bouttons
				DialogFragment newFragment = FloatButtonsDialog.newInstance();
				newFragment.show(getFragmentManager(), "Buttons Description");
                return true;

			case R.id.clearCheck:
                // Clear de float buttons et fixed buttons
				clearButtons();
				((MyApp) getApplicationContext()).sendMessage("dis_pathoPoint_0", this);
		}
		return super.onOptionsItemSelected(item);
	}

	public void clearButtons(){
		if(floatButtons.getCheckedRadioButtonId() != -1) {
			// Un boutton est activé
			floatButtons.clearCheck();
		}
		clearFixButtons();
	}

    public void clearFixButtons(){
        // Il faut déterminer sur quelle onglet nous sommes
        String currentTab = tabHost.getCurrentTabTag();
        switch(currentTab){
            case "torse":
                RadioGroup torseRadio = (RadioGroup) findViewById(R.id.groupe_torse);
				if(torseRadio.getCheckedRadioButtonId() != -1) {
					// Un boutton est activé
					torseRadio.clearCheck();
				}
                break;

            case "gauche":
                RadioGroup gaucheRadio = (RadioGroup) findViewById(R.id.groupe_cote_gauche);
				if(gaucheRadio.getCheckedRadioButtonId() != -1) {
					// Un boutton est activé
					gaucheRadio.clearCheck();
				}
                break;

            case "dos":
                RadioGroup dosRadio = (RadioGroup) findViewById(R.id.groupe_dos);
				if(dosRadio.getCheckedRadioButtonId() != -1) {
					// Un boutton est activé
					dosRadio.clearCheck();
				}
                break;

            case "droit":
                RadioGroup droiteRadio = (RadioGroup) findViewById(R.id.groupe_cote_droit);
				if(droiteRadio.getCheckedRadioButtonId() != -1) {
					// Un boutton est activé
					droiteRadio.clearCheck();
				}
                break;
        }
    }

	public void setListener(){
		floatButtons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// Enable patho
				// Check the corresponding button
				short idPatho = -1;
				switch (checkedId) {
					case R.id.float1:
						idPatho = 33;
						break;

					case R.id.float2:
						idPatho = 34;
						break;

					case R.id.float3:
						idPatho = 35;
						break;

					case R.id.float4:
						idPatho = 36;
						break;
				}
				if (!(idPatho == currentCheckedPatho)) { // Toujours le cas normalement, sauf si provient d'un clearCheck()
					clearFixButtons(); // Clear seulement si non vide
					currentCheckedPatho = idPatho;
					((MyApp) getApplicationContext()).sendMessage("en_pathoPoint_" + idPatho, ((MyApp) getApplicationContext()).getCurrentActivity());
				} else {
					currentCheckedPatho = -1;
				}
			}
		});
	}






	public void processFinishSendString(String output){
        if(output != null) {
			System.out.println("Reponse: " +output);
			//GetResponse();
            switch (output) {
				
				case "Ok_StopSimu":
					finish();
					break;

				case "Ok_modeSetPulse":
					((MyApp) getApplicationContext()).sendMessage(String.valueOf(manualPulse), this);
					break;

				case "Ok_pulseChoice":
					Toast.makeText(this, "Choix du pouls validé", Toast.LENGTH_LONG).show();
					pulseSet = true;
					setPulseManually.dismiss();
					break;


                case "not_PulseMeasured":
                    Toast.makeText(this, "Pouls non mesuré, veuillez recommencer", Toast.LENGTH_LONG).show();
                    break;

                case "ok_PulseMeasured":
					Toast.makeText(this, "Pouls mesuré, vous pouvez continuer", Toast.LENGTH_LONG).show();
					waitForPulse.dismiss();
					setListener();
					pulseMeasured = true;
                    break;

				case "dis_Patho":
					clearButtons();
					break;

            }
        }
	}



	private void initializeViewPager(boolean choixHomme) {
		List<Fragment> fragments = new Vector<Fragment>();

		fragments.add(Torse.newInstance(choixHomme));
		fragments.add(Gauche.newInstance(choixHomme));
		fragments.add(Dos.newInstance(choixHomme));
        fragments.add(Droit.newInstance(choixHomme));

		this.myViewPagerAdapter = new MyFragmentPagerAdapter(
				getSupportFragmentManager(), fragments);
		this.viewPager = (ViewPager) super.findViewById(R.id.viewPager);
		this.viewPager.setAdapter(this.myViewPagerAdapter);
		this.viewPager.setOnPageChangeListener(this);
		onRestart();
	}

	private void initializeTabHost(Bundle args) {

		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();


		TabHost.TabSpec tabSpec;
		tabSpec = tabHost.newTabSpec("torse");
		tabSpec.setIndicator("Torse");
		tabSpec.setContent(new FakeContent(this));
		tabHost.addTab(tabSpec);
		tabSpec = tabHost.newTabSpec("gauche");
		tabSpec.setIndicator("Côté gauche");
		tabSpec.setContent(new FakeContent(this));
		tabHost.addTab(tabSpec);
		tabSpec = tabHost.newTabSpec("dos");
		tabSpec.setIndicator("Dos");
		tabSpec.setContent(new FakeContent(this));
		tabHost.addTab(tabSpec);
		tabSpec = tabHost.newTabSpec("droit");
		tabSpec.setIndicator("Côté droit");
		tabSpec.setContent(new FakeContent(this));
		tabHost.addTab(tabSpec);
		tabHost.setOnTabChangedListener(this);
	}

	@Override
	public void onTabChanged(String tabId) {
		int pos = this.tabHost.getCurrentTab(); // Récupération position
		this.viewPager.setCurrentItem(pos);
		clearFixButtons(); // Clear des bouttons à chaque changement

		HorizontalScrollView hScrollView = (HorizontalScrollView) findViewById(R.id.hScrollView);
		View tabView = tabHost.getCurrentTabView();
		int scrollPos = tabView.getLeft()
				- (hScrollView.getWidth() - tabView.getWidth()) / 2;
		hScrollView.smoothScrollTo(scrollPos, 0);
	}


	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		clearFixButtons(); // Clear des bouttons à chaque changement
	}

	@Override
	public void onPageSelected(int position) {
		this.tabHost.setCurrentTab(position);
	}

}
