package tudresden.mobile.reserverest;

import java.util.List;

import tudresden.mobile.reserverest.R;
import tudresden.mobile.reserverest.backend.Dish;
import tudresden.mobile.reserverest.backend.RestaurantManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class BasketActivity extends Activity {
	
	TextView 	textDishesSummary;
	ListView 	lvBasket;
	Button		btnToReserveForm;
	
	List<Dish> basketDishes;
	
	private void updateScreen() {
        basketDishes = RestaurantManager.getBasketDishes();
		DishArrayAdapter adapter = new DishArrayAdapter(
				BasketActivity.this, R.layout.listitem_dishes, basketDishes);
		lvBasket.setAdapter(adapter);
        
		double sum = 0;
		for (Dish dish: basketDishes) {
			sum += dish.getPrice();
		}
		
		textDishesSummary.setText("Amount of dishes: " + basketDishes.size() + "    Total sum: " + sum +  " \u20ac");		
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basket_layout);
        
        textDishesSummary = (TextView) findViewById(R.id.textDishesSummary1);
        lvBasket = (ListView) findViewById(R.id.lvBasket1);
             
        btnToReserveForm = (Button) findViewById(R.id.btnToReserveForm1);
        
        btnToReserveForm.setOnClickListener(new View.OnClickListener() {						
    	   	@Override
			public void onClick(View v) {
    	   		// go to Reservation Form
				Intent intent = new Intent(BasketActivity.this, Reservation_Form.class);
	        	startActivity(intent);
			}
		});
      
        updateScreen();
        
		lvBasket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, final int position, long id) {
		    	AlertDialog.Builder builder = new AlertDialog.Builder(BasketActivity.this);
		    	builder
//		    	.setTitle("Remote the dish?")
		    	.setMessage("Remove the dish?")
		    	.setIcon(android.R.drawable.ic_dialog_alert)
		    	.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		    	    public void onClick(DialogInterface dialog, int which) {			      	
		    	    	//Yes button clicked
						basketDishes.remove(lvBasket.getItemAtPosition(position));
						updateScreen();
		    	    	Toast.makeText(BasketActivity.this, "Removed", 
		                               Toast.LENGTH_SHORT).show();
		    	    }
		    	})
		    	.setNegativeButton("No", null)						//Do nothing on no
		    	.show();
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
	   		Toast.makeText(getBaseContext(), "Updated", Toast.LENGTH_LONG).show();	
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
