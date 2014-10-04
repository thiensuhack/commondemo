package com.orange.studio.bobo.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.orange.studio.bobo.R;
import com.orange.studio.bobo.activities.HomeActivity;
import com.orange.studio.bobo.adapters.MenuDrawerAdapter;
import com.orange.studio.bobo.configs.OrangeConfig.MENU_NAME;
import com.orange.studio.bobo.configs.OrangeConfig.UrlRequest;
import com.orange.studio.bobo.models.CommonModel;
import com.orange.studio.bobo.objects.MenuItemDTO;
import com.orange.studio.bobo.utils.OrangeUtils;

public class NavigationDrawerFragment extends Fragment implements
		OnItemClickListener,OnClickListener {

	/**
	 * Remember the position of the selected item.
	 */
	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

	/**
	 * Per the design guidelines, you should show the drawer on launch until the
	 * user manually expands it. This shared preference tracks this.
	 */
	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

	/**
	 * A pointer to the current callbacks instance (the Activity).
	 */
	private NavigationDrawerCallbacks mCallbacks;

	/**
	 * Helper component that ties the action bar to the navigation drawer.
	 */
	private ActionBarDrawerToggle mDrawerToggle;

	private DrawerLayout mDrawerLayout;
	
	private View mView=null;	
	private ListView mDrawerListView;
	private View mFragmentContainerView;
	private View mSearchBtn=null;
	private EditText mSearchEditText=null;
	
	private int mCurrentSelectedPosition = 0;
	private boolean mFromSavedInstanceState;
	private boolean mUserLearnedDrawer;

	private MenuDrawerAdapter mMenuDrawerAdapter = null;
	private List<MenuItemDTO> mMenuList = null;
	
	private GetCategoryTask mGetCategoryTask=null;
	
	public NavigationDrawerFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Read in the flag indicating whether or not the user has demonstrated
		// awareness of the
		// drawer. See PREF_USER_LEARNED_DRAWER for details.
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, true);

		if (savedInstanceState != null) {
			mCurrentSelectedPosition = savedInstanceState
					.getInt(STATE_SELECTED_POSITION);
			mFromSavedInstanceState = true;
		}

		// Select either the default item (0) or the last selected item.
		selectItem(mCurrentSelectedPosition);
		mMenuDrawerAdapter = new MenuDrawerAdapter(getActivity());
		mMenuList = new ArrayList<MenuItemDTO>();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Indicate that this fragment would like to influence the set of
		// actions in the action bar.
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = (LinearLayout) inflater.inflate(
				R.layout.fragment_navigation_drawer, container, false);
		mDrawerListView=(ListView)mView.findViewById(R.id.menuDrawerListView);
		mDrawerListView.setOnItemClickListener(this);

		//createMenuDrawer();

		//mMenuDrawerAdapter.updateDataList(mMenuList);
		mDrawerListView.setAdapter(mMenuDrawerAdapter);
		mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
		
		mSearchBtn=(ImageView)mView.findViewById(R.id.searchBtn);
		mSearchBtn.setOnClickListener(this);
		mSearchEditText=(EditText)mView.findViewById(R.id.searchTextBox);
		return mView;
	}

	private String getMenuName(int resID) {
		return getActivity().getString(resID);
	}
	public String getSearchKey(){
		return mSearchEditText.getText().toString().trim();
	}
	public void setSearchKey(String searchKey){
		if(mSearchEditText!=null){
			mSearchEditText.setText(searchKey);
		}
	}
	private void createMenuDrawer() {
		int index = 1;
		MenuItemDTO item1 = new MenuItemDTO();
		item1.id = String.valueOf(index);
		item1.position=index;
		item1.resId = R.drawable.ic_menu_home;
		item1.name = getMenuName(R.string.menu_drawer_home);
		item1.total = 0;

		index++;
		MenuItemDTO item2 = new MenuItemDTO();
		item2.id = String.valueOf(index);
		item2.position=index;
		item2.resId = R.drawable.ic_menu_product;
		item2.name = getMenuName(R.string.menu_drawer_product);
		item2.total = 459;

		index++;
		MenuItemDTO item3 = new MenuItemDTO();
		item3.id = String.valueOf(index);
		item3.position=index;
		item3.resId = R.drawable.ic_menu_shop;
		item3.name = getMenuName(R.string.menu_drawer_shop);;
		item3.total = 4;

		index++;
		MenuItemDTO item4 = new MenuItemDTO();
		item4.id = String.valueOf(index);
		item4.position=index;
		item4.resId = R.drawable.ic_menu_new_arivals;
		item4.name = getMenuName(R.string.menu_drawer_new_arrivals);
		item4.total = 29;

		index++;
		MenuItemDTO item5 = new MenuItemDTO();
		item5.id = String.valueOf(index);
		item5.position=index;
		item5.resId = R.drawable.ic_menu_best_seller;
		item5.name = getMenuName(R.string.menu_drawer_best_seller);
		item5.total = 49;

		index++;
		MenuItemDTO item6 = new MenuItemDTO();
		item6.id = String.valueOf(index);
		item6.position=index;
		item6.resId = R.drawable.ic_menu_apparel;
		item6.name = getMenuName(R.string.menu_drawer_apparel);
		item6.total = 25;

		index++;
		MenuItemDTO item7 = new MenuItemDTO();
		item7.id = String.valueOf(index);
		item7.position=index;
		item7.resId = R.drawable.ic_menu_accessories;
		item7.name = getMenuName(R.string.menu_drawer_accessories);
		item7.total = 299;

		index++;
		MenuItemDTO item8 = new MenuItemDTO();
		item8.id = String.valueOf(index);
		item8.position=index;
		item8.resId = R.drawable.ic_menu_clearance;
		item8.name = getMenuName(R.string.menu_drawer_clearance);
		item8.total = 9;

		index++;
		MenuItemDTO item9 = new MenuItemDTO();
		item9.id = String.valueOf(index);
		item9.position=index;
		item9.resId = R.drawable.ic_menu_event_spin_to_win;
		item9.name = getMenuName(R.string.menu_drawer_event_spin_to_win);
		item9.total = 0;

		index++;
		MenuItemDTO item10 = new MenuItemDTO();
		item10.id = String.valueOf(index);
		item10.position=index;
		item10.resId = R.drawable.ic_menu_about_us;
		item10.name = getMenuName(R.string.menu_drawer_about_us);
		item10.total = 0;

		index++;
		MenuItemDTO item11 = new MenuItemDTO();
		item11.id = String.valueOf(index);
		item11.position=index;
		item11.resId = R.drawable.ic_menu_contact_us;
		item11.name = getMenuName(R.string.menu_drawer_contact_us);
		item11.total = 0;

		mMenuList.add(item1);
		mMenuList.add(item2);
		mMenuList.add(item3);
		mMenuList.add(item4);
		mMenuList.add(item5);
		mMenuList.add(item6);
		mMenuList.add(item7);
		mMenuList.add(item8);
		mMenuList.add(item9);
		mMenuList.add(item10);
		mMenuList.add(item11);

	}

	public boolean isDrawerOpen() {
		return mDrawerLayout != null
				&& mDrawerLayout.isDrawerOpen(mFragmentContainerView);
	}

	public void openNaviDrawer() {
		if (isDrawerOpen()) {
			mDrawerLayout.closeDrawer(mFragmentContainerView);
		} else {
			mDrawerLayout.openDrawer(mFragmentContainerView);
		}

	}

	/**
	 * Users of this fragment must call this method to set up the navigation
	 * drawer interactions.
	 * 
	 * @param fragmentId
	 *            The android:id of this fragment in its activity's layout.
	 * @param drawerLayout
	 *            The DrawerLayout containing this fragment's UI.
	 */
	public void setUp(int fragmentId, DrawerLayout drawerLayout) {
		mFragmentContainerView = getActivity().findViewById(fragmentId);
		mDrawerLayout = drawerLayout;

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the navigation drawer and the action bar app icon.
		mDrawerToggle = new ActionBarDrawerToggle(getActivity(), /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.navigation_drawer_open, /*
										 * "open drawer" description for
										 * accessibility
										 */
		R.string.navigation_drawer_close /*
										 * "close drawer" description for
										 * accessibility
										 */
		) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				if (!isAdded()) {
					return;
				}

				getActivity().supportInvalidateOptionsMenu(); // calls
																// onPrepareOptionsMenu()
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				if (!isAdded()) {
					return;
				}

				if (!mUserLearnedDrawer) {
					// The user manually opened the drawer; store this flag to
					// prevent auto-showing
					// the navigation drawer automatically in the future.
					mUserLearnedDrawer = true;
					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(getActivity());
					sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true)
							.apply();
				}

				getActivity().supportInvalidateOptionsMenu(); // calls
																// onPrepareOptionsMenu()
			}
		};

		// If the user hasn't 'learned' about the drawer, open it to introduce
		// them to the drawer,
		// per the navigation drawer design guidelines.
		if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
			mDrawerLayout.openDrawer(mFragmentContainerView);
		}

		// Defer code dependent on restoration of previous instance state.
		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	private void selectItem(int position) {
		mCurrentSelectedPosition = position;
		if (mDrawerListView != null) {
			mDrawerListView.setItemChecked(position, true);
		}
		if (mDrawerLayout != null) {
			mDrawerLayout.closeDrawer(mFragmentContainerView);
		}
		if (mCallbacks != null) {
			mCallbacks.onNavigationDrawerItemSelected(position);
		}
	}
	private void callFragmentReplace(int position){
		if (mDrawerLayout != null) {
			mDrawerLayout.closeDrawer(mFragmentContainerView);
		}
		if (mCallbacks != null) {
			mCallbacks.onNavigationDrawerItemSelected(position);
		}
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallbacks = (NavigationDrawerCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"Activity must implement NavigationDrawerCallbacks.");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Forward the new configuration the drawer toggle component.
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * Per the navigation drawer design guidelines, updates the action bar to
	 * show the global app 'context', rather than just what's in the current
	 * screen.
	 */
	private void showGlobalContextActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setTitle(R.string.app_name);
	}

	private ActionBar getActionBar() {
		return ((ActionBarActivity) getActivity()).getSupportActionBar();
	}

	/**
	 * Callbacks interface that all activities using this fragment must
	 * implement.
	 */
	public static interface NavigationDrawerCallbacks {
		/**
		 * Called when an item in the navigation drawer is selected.
		 */
		void onNavigationDrawerItemSelected(int position);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.searchBtn:
			getHomeActivity().mSearchKey=mSearchEditText.getText().toString().trim();
			callFragmentReplace(MENU_NAME.SEARCH_RESULT_FRAGMENT);
			break;
		default:
			break;
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {		
		MenuItemDTO item=mMenuDrawerAdapter.getItem(position);
		if(item.tag!=null && item.tag.equals("home")){
			callFragmentReplace(1);
			return;
		}
		if(item.tag!=null && item.tag.equals("product")){
			callFragmentReplace(2);
			return;
		}
		if(item.tag!=null && item.tag.equals("bestseller")){
			callFragmentReplace(3);
			return;
		}
		if(item.tag!=null && item.tag.equals("about")){
			callFragmentReplace(9);
			return;
		}
		if(item.tag!=null && item.tag.equals("contactus")){
			callFragmentReplace(10);
			return;
		}
		if(item.tag!=null && item.tag.equals("spintowin")){
			callFragmentReplace(MENU_NAME.SPIN_TO_WIN_FRAGMENT);
			return;
		}
		getHomeActivity().mCurCategory=item;
		callFragmentReplace(MENU_NAME.PRODUCT_CATEGORY_FRAGMENT);
	}
	private void getMenuCategory(){
		if(mGetCategoryTask==null || mGetCategoryTask.getStatus()==Status.FINISHED){
			mGetCategoryTask=new GetCategoryTask();
			mGetCategoryTask.execute();
		}
	}
	public class GetCategoryTask extends AsyncTask<Void, Void, List<MenuItemDTO>>{
		
		@Override
		protected List<MenuItemDTO> doInBackground(Void... params) {
			String fields="[id,name]";
			Bundle mParams = OrangeUtils
					.createRequestBundle(null,fields);
			return CommonModel.getInstance().getListMenuCategory(UrlRequest.CATEGORY_MENU, null, mParams);
		}
		@Override
		protected void onPostExecute(List<MenuItemDTO> result) {
			super.onPostExecute(result);
			if(result!=null && result.size()>0){
				int index=1;
				MenuItemDTO item1 = new MenuItemDTO();
				item1.id = String.valueOf(index);
				item1.resId = R.drawable.ic_menu_home;
				item1.name = getMenuName(R.string.menu_drawer_home);
				item1.tag="home";
				item1.isHaveResId=true;

				index++;
				MenuItemDTO item2 = new MenuItemDTO();
				item2.id = String.valueOf(index);
				item2.resId = R.drawable.ic_menu_product;
				item2.name = getMenuName(R.string.menu_drawer_product);
				item2.tag="product";
				item2.isHaveResId=true;
				
				index++;
				MenuItemDTO item3 = new MenuItemDTO();
				item3.id = String.valueOf(index);
				item3.resId = R.drawable.ic_menu_best_seller;
				item3.name = getMenuName(R.string.menu_drawer_best_seller);
				item3.tag="bestseller";
				item3.isHaveResId=true;
				
				index++;
				MenuItemDTO item9 = new MenuItemDTO();
				item9.id = String.valueOf(index);
				item9.position=index;
				item9.resId = R.drawable.ic_menu_event_spin_to_win;
				item9.name = getMenuName(R.string.menu_drawer_event_spin_to_win);
				item9.tag="spintowin";
				item9.total = 0;
				
				index++;
				MenuItemDTO item10 = new MenuItemDTO();
				item10.id = String.valueOf(index);
				item10.resId = R.drawable.ic_menu_about_us;
				item10.name = getMenuName(R.string.menu_drawer_about_us);
				item10.tag="about";
				item10.isHaveResId=true;
				
				index++;
				MenuItemDTO item11 = new MenuItemDTO();
				item11.id = String.valueOf(index);
				item11.resId = R.drawable.ic_menu_contact_us;
				item11.name = getMenuName(R.string.menu_drawer_contact_us);
				item11.tag="contactus";
				item11.isHaveResId=true;
				
				result.add(0,item3);
				result.add(0,item2);
				result.add(0,item1);
				result.add(item9);
				result.add(item10);
				result.add(item11);
				
				for (index = 0; index < result.size(); index++) {
					if(!result.get(index).isHaveResId){
						result.get(index).resId=R.drawable.ic_shopping;
					}					
					result.get(index).position=(index+1);
				}
				
				
				mMenuDrawerAdapter.updateDataList(result);
			}
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		getMenuCategory();
	}
	public HomeActivity getHomeActivity(){
		return (HomeActivity)getActivity();
	}
}
