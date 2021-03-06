package tudresden.mobile.reserverest;

import java.util.ArrayList;
import java.util.List;

import tudresden.mobile.reserverest.R;
import tudresden.mobile.reserverest.backend.Restaurant;
import tudresden.mobile.reserverest.backend.RestaurantManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RestaurantListActivity extends Activity {

	List<Restaurant> restaurantsList = new ArrayList<Restaurant>();
	String city;
	
	ListView lvRestaurants;
	
	
	/* dimakuv: 
	 *   Background Task for fetching list of restaurants from our backend server into Spinner
	 */
	public class GetRestaurantsTask extends AsyncTask<String, Void, RestaurantArrayAdapter> {
		
		@Override
		protected RestaurantArrayAdapter doInBackground(String... cities) {
			RestaurantArrayAdapter aa = null;

			restaurantsList = RestaurantManager.getRestaurants(cities[0]);		

			if (restaurantsList != null) {
				aa = new RestaurantArrayAdapter(
						getApplicationContext(), R.layout.listitem_restaurants, restaurantsList);
			}	        
	        
			return aa;
		}
		
		@Override
		protected void onPostExecute(RestaurantArrayAdapter aa) {			
			// NOTE: this method is executed in main, UI thread
			if (aa != null) {
				lvRestaurants.setAdapter(aa);			
			} else {
        	   	Toast.makeText(RestaurantListActivity.this, "WARNING: Didn't retrieve restaurants!", Toast.LENGTH_SHORT).show();		        	   
           }
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Grab the city parameter from MainActivity
		Bundle b = getIntent().getExtras();
		this.city = b.getString("city");
       		
		// Set the View layer
		setContentView(R.layout.list_restaurants);
		setTitle("Restaurants in " + this.city);

		// Get reference to ListView holder
		lvRestaurants = (ListView) this.findViewById(R.id.listview_restaurants);
		
        new GetRestaurantsTask().execute(city);

		// Create a customized ArrayAdapter
		// empty in the beginning
		RestaurantArrayAdapter adapter1 = new RestaurantArrayAdapter(
			getApplicationContext(), R.layout.listitem_restaurants, restaurantsList);

		// Set the ListView adapter
		lvRestaurants.setAdapter(adapter1);
		lvRestaurants.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				Intent intent = new Intent(RestaurantListActivity.this, ReservationMenuOfChoosenRestaurant.class);
				
				Restaurant r = restaurantsList.get(position);
				RestaurantManager.setRestaurant(r);
				
				Bundle b = new Bundle();
				b.putString("name", r.getName());
				b.putString("desc", r.getDesc());
				b.putString("address", r.getAddress());
				b.putInt("rating", r.getRating());
				intent.putExtras(b);
				
				startActivity(intent);
				System.out.println("OK");
			}
		});
	}
	
	   @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.activity_menu, menu);
	        return true;
	    }

	   @Override
	   public boolean onOptionsItemSelected(MenuItem item) {
	       switch (item.getItemId()) {
		   	case R.id.update:
		   		Toast.makeText(getBaseContext(), "List of restaurants updated", Toast.LENGTH_LONG).show();	
		        return true;
		   	case R.id.about:
		   		LayoutInflater inflater = getLayoutInflater();
		   		View layout = inflater.inflate(R.layout.toast_center,
		   		                               (ViewGroup) findViewById(R.id.toast_layout_root));	   		
		   		Toast t = new Toast(getApplicationContext());
		   		TextView tv = (TextView) layout.findViewById(R.id.tvToastCenter);
		   		tv.setText("Authors:\nUliana Andriieshyna & Dmitry Kuvayskiy\n\u00A9 2013");
		   		t.setView(layout);
		   		t.setDuration(Toast.LENGTH_LONG);
		   		t.show();
		        return true;
	        case R.id.exit:  
	        	Intent intent = new Intent(Intent.ACTION_MAIN);
	        	intent.addCategory(Intent.CATEGORY_HOME);
	        	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        	startActivity(intent);
	        	return true;
	       }
		return false;
	   };
}
