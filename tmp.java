package com.example.myapp;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Formatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity {

    private Button btnRequest;
    private static final String TAG = MainActivity.class.getName();
    private static final String portfolio = "portfolio";
    private static final String favorites = "favorites";
    private AtomicInteger totalRequests;

    private String[] nameArr;
    private String[] tickerArr;
    private double[] priceArr;
    private double[] priceChangeArr;
    private double[] priceChangePerArr;

    private int[] sharesNum;
    private String[] tickerArr_por;
    private double[] priceArr_por;
    private double[] priceChangeArr_por;
    private double[] priceChangePerArr_por;
    private double[] sharesCost;

    private SectionedRecyclerViewAdapter sectionAdapter = new SectionedRecyclerViewAdapter();
    private RecyclerView recyclerView;
    String[] fruits = {"Apple", "Banana", "Cherry", "Date", "Grape", "Kiwi", "Mango", "Pear"};
    String dataArr[] = {};
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private JsonObjectRequest mJsonObjectRequest;
    private String url = "https://nodejs-786565.wl.r.appspot.com/api/stockdetail/TSLA";
    private List<String> autoCompleteList = new ArrayList<>();
    private double moneyLeft;

    private favitem[] favArr;
    private int favsize;
    private favitem[] porArr;
    private int porsize = 3;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_MyApp);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        mRequestQueue = Volley.newRequestQueue(this);
        RecyclerView tmpRecycle = findViewById(R.id.recyclerview);
        tmpRecycle.setVisibility(View.GONE);
        TextView tmpText = findViewById(R.id.textView_today);
        tmpText.setVisibility(View.GONE);
        TextView tmpText2 = findViewById(R.id.myfooter);
        tmpText2.setVisibility(View.GONE);
        tmpText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://finnhub.io"));
                startActivity(browserIntent);
            }
        });

        ///test
        String response = "[\"AAPL\",\"HPQ\",\"DELL\",\"1337.HK\",\"HPE\",\"NTAP\",\"WDC\",\"PSTG\",\"XRX\",\"SMCI\"]";


        ///

        TextView dateView = findViewById(R.id.textView_today);
        Formatter fmt1 = new Formatter();
        Calendar cal1 = Calendar.getInstance();
        fmt1 = new Formatter();
        fmt1.format("%tB",cal1);
        String todaymonth =fmt1.toString();
        Formatter fmt2 = new Formatter();
        Calendar cal2 = Calendar.getInstance();
        fmt2 = new Formatter();
        fmt2.format("%td",cal2);
        String todaydate =fmt2.toString();
        Formatter fmt3 = new Formatter();
        Calendar cal3 = Calendar.getInstance();
        fmt3 = new Formatter();
        fmt3.format("%tY",cal3);
        String todayyear =fmt3.toString();





        sharedpreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        //editor.clear();
        //editor.commit();




        Log.i(TAG,"is exist fav:" + (sharedpreferences.contains(favorites)));
        Log.i(TAG,"is exist por:" + (sharedpreferences.contains(portfolio)));
        Log.i(TAG,"is exist money:" + (sharedpreferences.contains("money")));
        Log.i(TAG,"is exist AAPL  :" + (sharedpreferences.contains("AAPL")));
        Log.i(TAG,"is exist AAPL_ :" + (sharedpreferences.contains("AAPL_")));
        //Log.i(TAG,"is exist AAPL :" + (sharedpreferences.contains("AAPL_")));
        if(!sharedpreferences.contains(favorites)){
            Set<String> newfavset = new HashSet<String>();
            editor.putStringSet(favorites,newfavset);
            editor.commit();
        }
        if(!sharedpreferences.contains(portfolio)){
            Set<String> newporset = new HashSet<String>();
            editor.putStringSet(portfolio,newporset);
            editor.commit();
        }
        if(!sharedpreferences.contains("money")){
            editor.putString("money","25000.00");
            editor.commit();
        }

        Set<String> favset = sharedpreferences.getStringSet(favorites,null);
        Set<String> porset = sharedpreferences.getStringSet(portfolio,null);
        favsize = favset.size();
        porsize = porset.size();
        Log.i(TAG,"is exist favsize_ :" + favsize);
        Log.i(TAG,"is exist porsize_ :" +  porsize);
        if(favsize==0 && porsize==0){
            List<favitem> favList = new ArrayList<>();
            favitem[] tmppor = new favitem[1];
            moneyLeft = Double.parseDouble(sharedpreferences.getString("money",null));
            tmppor[0] = new favitem("Net Worth","money",moneyLeft,25000.0,1.0,1,1.0);
            List<favitem> porList = Arrays.asList(tmppor);
            porsize++;
            sectionAdapter.addSection(portfolio,new PorSection(porList));
            sectionAdapter.addSection(favorites,new FavSection(favList));
            recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(sectionAdapter);
            dateView.setText(todaydate+" "+ todaymonth+" "+todayyear);
            //tmpRecycle = findViewById(R.id.recyclerview);
            tmpRecycle.setVisibility(View.VISIBLE);
            //TextView tmpText = findViewById(R.id.textView_today);
            tmpText.setVisibility(View.VISIBLE);
            ProgressBar p2 = findViewById(R.id.progressBar2);
            p2.setVisibility(View.GONE);
            return;
        }
        nameArr = new String[favsize];
        tickerArr = new String[favsize];
        priceArr = new double[favsize];
        priceChangeArr = new double[favsize];
        priceChangePerArr = new double[favsize];

        sharesNum = new int[porsize];
        tickerArr_por = new String[porsize];
        priceArr_por = new double[porsize];
        priceChangeArr_por = new double[porsize];
        priceChangePerArr_por = new double[porsize];
        sharesCost = new double[porsize];

        Log.i(TAG,"is exist fav :" + favset.toString());
        Log.i(TAG,"is exist por :" + porset.toString());
        for(String ob:porset){
            Log.i(TAG,"is prodetail "+ob+" amt :" + sharedpreferences.getInt(ob,-1));
            Log.i(TAG,"is prodetail "+ob+" cost :" + sharedpreferences.getString(ob+"_cost",null));
        }

        //Log.i(TAG,"is exist AAPL amt :" + sharedpreferences.getInt("AAPL",-1));
        Log.i(TAG,"is exist money amt :" + sharedpreferences.getString("money",null));
        moneyLeft = Double.parseDouble(sharedpreferences.getString("money",null));
        dateView.setText(todaydate+" "+ todaymonth+" "+todayyear);

        //Toast.makeText(getApplicationContext(),sharedpreferences.getStringSet(favorites,null).toString(), Toast.LENGTH_LONG).show();
        Object[] favArray = favset.toArray();
        Object[] porArray = porset.toArray();
        totalRequests = new AtomicInteger(2*favsize+porsize);
        for (int i = 0; i < favArray.length; i++) {
            Log.i(TAG,"tickername :" + favArray[i]);
            tickerArr[i] = favArray[i].toString();
            getStockDetail(favArray[i].toString(),i);
            getLatestPrice(favArray[i].toString(),i,0);
        }
        for (int i = 0; i < porArray.length; i++) {
            Log.i(TAG,"por tickername :" + porArray[i]);
            Log.i(TAG,"por tickername shares:" + sharedpreferences.getInt(porArray[i].toString(),-1));
            sharesNum[i] = sharedpreferences.getInt(porArray[i].toString(),-1);
            sharesCost[i] = Double.parseDouble(sharedpreferences.getString(porArray[i].toString()+"_cost",null));
            tickerArr_por[i] = porArray[i].toString();
            //getStockDetail(favArray[i].toString(),i);
            getLatestPrice(porArray[i].toString(),i,1);
        }








        mRequestQueue.addRequestEventListener((request, event) -> {
            if(event == RequestQueue.RequestEvent.REQUEST_FINISHED ){
                //nnnn -= 1;
                //Log.d("tag", "now: n = "+nnnn);
                if(totalRequests.decrementAndGet() == 0){
                    favArr = new favitem[favsize];
                    for(int i=0;i<favsize;i++){
                        Log.i(TAG,"tickername :" + tickerArr[i]);
                        Log.i(TAG,"tickername :" + nameArr[i]);
                        Log.i(TAG,"tickername :" + priceArr[i]);
                        Log.i(TAG,"tickername :" + priceChangeArr[i]);
                        Log.i(TAG,"tickername :" + priceChangePerArr[i]);
                        favArr[i] = new favitem(tickerArr[i],nameArr[i],priceArr[i],priceChangeArr[i],priceChangePerArr[i],0,0.0);
                    }
                    List<favitem> favList = Arrays.asList(favArr);

                    porArr = new favitem[porsize+1];
                    porsize++;
                    double networth = 0.0;
                    for(int i=1;i<porsize;i++){
                        Log.i(TAG,"tickername :" + tickerArr_por[i-1]);
                        Log.i(TAG,"tickername :" + "ll");
                        Log.i(TAG,"tickername :" + priceArr_por[i-1]);
                        Log.i(TAG,"tickername :" + priceChangeArr_por[i-1]);
                        Log.i(TAG,"tickername :" + priceChangePerArr_por[i-1]);
                        Log.i(TAG,"tickername :" + sharesCost[i-1]);
                        networth += sharesNum[i-1]*priceArr_por[i-1];
                        porArr[i] = new favitem(tickerArr_por[i-1],sharesNum[i-1]+" shares",priceArr_por[i-1],priceChangeArr_por[i-1],priceChangePerArr_por[i-1],sharesNum[i-1],sharesCost[i-1]);
                    }
                    networth +=moneyLeft;


                    porArr[0] = new favitem("Net Worth","money",moneyLeft,networth,1.0,1,1.0);
                    List<favitem> porList = Arrays.asList(porArr);


// Add your Sections
                    sectionAdapter.addSection(portfolio,new PorSection(porList));
                    sectionAdapter.addSection(favorites,new FavSection(favList));


// Set up your RecyclerView with the SectionedRecyclerViewAdapter

                    recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    recyclerView.setAdapter(sectionAdapter);

                    //tmpRecycle = findViewById(R.id.recyclerview);
                    tmpRecycle.setVisibility(View.VISIBLE);
                    //TextView tmpText = findViewById(R.id.textView_today);
                    tmpText.setVisibility(View.VISIBLE);
                    tmpText2.setVisibility(View.VISIBLE);
                    ProgressBar p2 = findViewById(R.id.progressBar2);
                    p2.setVisibility(View.GONE);
                    //sectionAdapter.


                    ItemTouchHelper.SimpleCallback callback_swipe = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, ItemTouchHelper.LEFT) {
                        @Override
                        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                            Log.i("MainActivity", "drag yes  onMove");
                            Log.i("MainActivity", "drag yes  onMove postion: "+viewHolder.getAdapterPosition());
                            Log.i("MainActivity", "drag yes  onMove postion: "+target.getAdapterPosition());
                            int fromPos = viewHolder.getAdapterPosition();
                            int toPos = target.getAdapterPosition();
                            if(fromPos>=porsize+2){
                                if(toPos>=porsize+2){
                                    int from = fromPos - (porsize+2);
                                    int to = toPos - (porsize+2);
                                    sectionAdapter.notifyItemMovedInSection(favorites,from,to);
                                }
                            }
                            else if(fromPos>=2 && fromPos<=porsize){
                                if(toPos>=2 && toPos<=porsize){
                                    int from = fromPos - 1;
                                    int to = toPos - 1;
                                    sectionAdapter.notifyItemMovedInSection(portfolio,from,to);
                                }
                            }

                            return true;
                        }

                        @Override
                        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                            try {
                                final int position = viewHolder.getAdapterPosition();

                                Log.i("MainActivity", "swipe yes  pos: "+position);
                                if(position>=porsize+2){
                                    //fav
                                    TextView xx = viewHolder.itemView.findViewById(R.id.tvItem);
                                    String txtt = xx.getText().toString();
                                    Log.i("MainActivity", "swipe yes  txtt: "+txtt);
                                    //String targetticker ="TSLA";
                                    //txtt = "TSLA";
                                    String txt = "TSLA";
                                    txtt = txtt.substring(0,txtt.length()-1);
                                    Log.i("MainActivity", "swipe yes  txt:  "+txt);
                                    Log.i("MainActivity", "swipe yes  "+txt.equals(txtt));
                                    Log.i("MainActivity", "swipe yes  length txtt:  "+txtt.length()+"length txt:  "+txt.length());
                                    //String targetticker ="TSLA";

                                    String targetticker =txtt;
                                    sectionAdapter.removeSection(favorites);
                                    favitem[] newfavarr = new favitem[favsize-1];
                                    int index=0;
                                    while (!favArr[index].getTicker().equals(targetticker)) {
                                        newfavarr[index] = favArr[index];
                                        index++;
                                    }
                                    for(int i=index;i<favsize-1;i++){
                                        newfavarr[i] = favArr[i+1];
                                    }
                                    favArr = new favitem[favsize-1];
                                    favsize--;
                                    favArr = newfavarr;

                                    editor = sharedpreferences.edit();
                                    Set<String> favset = sharedpreferences.getStringSet(favorites,null);
                                    favset.remove(targetticker);
                                    editor.putStringSet(favorites,favset);
                                    editor.remove(targetticker+"_");
                                    editor.commit();


                                    //Log.i("this tst", "tst+"+myFavSection;
                                    List<favitem> newfavList = Arrays.asList(newfavarr);
                                    sectionAdapter.addSection(favorites,new FavSection(newfavList));
                                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                                    recyclerView.setAdapter(sectionAdapter);
                                    //final String item = mAdapter.removeItem(position);
                                }
                                else if(position>=1 && position<=porsize){
                                    TextView xx = viewHolder.itemView.findViewById(R.id.tvItem);
                                    String txtt = xx.getText().toString();
                                    Log.i("MainActivity", "swipe yes  txtt: "+txtt);
                                    //String targetticker ="TSLA";
                                    //txtt = "TSLA";
                                    String txt = "TSLA";
                                    txtt = txtt.substring(0,txtt.length()-1);
                                    Log.i("MainActivity", "swipe yes  txt:  "+txt);
                                    Log.i("MainActivity", "swipe yes  "+txt.equals(txtt));
                                    Log.i("MainActivity", "swipe yes  length txtt:  "+txtt.length()+"length txt:  "+txt.length());
                                    //String targetticker ="TSLA";
                                    String targetticker =txtt;
                                    //sectionAdapter.removeSection(portfolio);
                                    favitem[] newporarr = new favitem[porsize-1];
                                    int index=0;
                                    while (!porArr[index].getTicker().equals(targetticker)) {
                                        newporarr[index] = porArr[index];
                                        index++;
                                    }
                                    for(int i=index;i<porsize-1;i++){
                                        newporarr[i] = porArr[i+1];
                                    }
                                    porArr = new favitem[porsize-1];
                                    porsize--;
                                    porArr = newporarr;

                            /*editor = sharedpreferences.edit();
                            Set<String> porset = sharedpreferences.getStringSet(portfolio,null);
                            porset.remove(targetticker);
                            editor.putStringSet(portfolio,porset);
                            editor.remove(targetticker+"_");
                            editor.commit();*/


                                    //Log.i("this tst", "tst+"+myFavSection;
                                    List<favitem> newporList = Arrays.asList(newporarr);
                                    sectionAdapter.addSection(portfolio,new PorSection(newporList));
                                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                                    recyclerView.setAdapter(sectionAdapter);


                                }



                            } catch(Exception e) {
                                Log.e("MainActivity", e.getMessage());
                            }
                        }

                        // You must use @RecyclerViewSwipeDecorator inside the onChildDraw method
                        @Override
                        public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
                            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.red_down))
                                    .addSwipeLeftActionIcon(R.drawable.ic_delete)
                                    .addSwipeRightBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.red_down))
                                    .addSwipeRightActionIcon(R.drawable.ic_delete)
                                    //.addSwipeRightLabel("delete")
                                    //.setSwipeRightLabelColor(Color.WHITE)
                                    .addSwipeLeftLabel("")
                                    .setSwipeLeftLabelColor(Color.WHITE)
                                    //.addCornerRadius(TypedValue.COMPLEX_UNIT_DIP, 16)
                                    //.addPadding(TypedValue.COMPLEX_UNIT_DIP, 8, 16, 8)
                                    .create()
                                    .decorate();
                            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                            Log.i("MainActivity", "swipe yes");

                        }





                        @Override
                        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder,
                                                      int actionState) {


                            /*if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                                if (viewHolder instanceof RecyclerViewAdapter.MyViewHolder) {
                                    RecyclerViewAdapter.MyViewHolder myViewHolder=
                                            (RecyclerViewAdapter.MyViewHolder) viewHolder;
                                    mAdapter.onRowSelected(myViewHolder);
                                }

                            }*/

                            super.onSelectedChanged(viewHolder, actionState);
                        }

                    };
                    ItemTouchHelper itemTouchHelper_swipe = new ItemTouchHelper(callback_swipe);
                    itemTouchHelper_swipe.attachToRecyclerView(recyclerView);

                    /*ItemTouchHelper.SimpleCallback callback_drag = new ItemTouchHelper.SimpleCallback(
                            ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
                        @Override
                        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                            return false;
                        }
                        @Override
                        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        }
                    } ;
                    ItemTouchHelper itemTouchHelper_drag = new ItemTouchHelper(callback_drag);
                    itemTouchHelper_drag.attachToRecyclerView(recyclerView);*/

                };
            }
        });


//  Recycle View




        /*btnRequest = (Button) findViewById(R.id.button);
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                sendAndRequestResponse();
                Log.i(TAG,"HHHHHHHHH"+autoCompleteList.toString());
            }
        });*/

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, fruits);
        //Getting the instance of AutoCompleteTextView
        /*AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        actv.setTextColor(Color.RED);*/
    }


    @Override
    protected void onResume() {
        super.onResume();

        mRequestQueue = Volley.newRequestQueue(this);
        Log.i(TAG,"back back");



    }


    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the search menu action bar.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_actions, menu);
        // Get the search menu.
        MenuItem searchItem = menu.findItem(R.id.action_search);

        // Get SearchView object.
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setIconifiedByDefault(true);
        searchView.setSubmitButtonEnabled(false);

        // Get SearchView autocomplete object.
        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        // Create a new ArrayAdapter and add data to search auto complete object.
        //String dataArr[] = {};

        ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dataArr);
        //searchAutoComplete.setThreshold(2);
        searchAutoComplete.setAdapter(newsAdapter);

        // Listen to search view item on click event.
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                String queryString=(String)adapterView.getItemAtPosition(itemIndex);
                String[] arrOfQuery = queryString.split(" ");
                searchAutoComplete.setText("" + arrOfQuery[0]);
                //Toast.makeText(MainActivity.this, "you clicked " + queryString, Toast.LENGTH_LONG).show();
                searchView.setQuery(arrOfQuery[0], true);
            }
        });
        // Below event is triggered when submit search query.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                //Autocomplete
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // yourMethod();
                    }
                }, 1000);   //
                String usrInput = searchView.getQuery().toString();
                if(!usrInput.equals("")) {
                    getAutoComplete(usrInput,searchAutoComplete);
                }
                else{
                    dataArr = new String[]{};
                    ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, dataArr);
                    searchAutoComplete.setAdapter(newsAdapter);
                }


                //Toast.makeText(MainActivity.this, "you clicked "+usrInput, Toast.LENGTH_LONG).show();

                return false;
            }
        });
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchableActivity.class)));





        return true;
    }

    private void sendAndRequestResponse() {

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);
        //Toast.makeText(getApplicationContext(),"Response :" , Toast.LENGTH_LONG).show();
        //String Request initialized
        url ="https://finnhub.io/api/v1/stock/candle?symbol="+"TSLA"+"&resolution=5&from="+"1631022248"+"&to="+"1631627048"+"&token="+"c94ij8qad3if4j50t1tg";

        mJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                    Log.d("MYAPP", "STring of JSONNNN"+response.toString());

                //Toast.makeText(getApplicationContext(),"Response :" + response.getString("country"), Toast.LENGTH_LONG).show();//display the response on screen

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext()," ERROROR", Toast.LENGTH_LONG).show();
                Log.i(TAG,"Error :" + error.toString());
            }
        });
        mRequestQueue.add(mJsonObjectRequest);
    }

    private void getAutoComplete(String input,SearchView.SearchAutoComplete searchAutoComplete) {
//RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);
        //Log.i(TAG,"Error :");
        //Toast.makeText(getApplicationContext(),"Response :" , Toast.LENGTH_LONG).show();
        //String Request initialized
        String autoCompleteUrl = "https://nodejs-786565.wl.r.appspot.com/api/autocomplete/"+ input;
        mJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, autoCompleteUrl, null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int numberOfAuto = response.getInt("count");
                    autoCompleteList.clear();
                    List<String> desList = new ArrayList<>();
                    //dataArr = new String[numberOfAuto];
                    JSONArray aa = response.getJSONArray("result");
                    for(int i=0;i<numberOfAuto;i++){
                        JSONObject itemAA = aa.getJSONObject(i);
                        String sys = itemAA.getString("symbol");
                        String des = itemAA.getString("description");
                        //Toast.makeText(getApplicationContext(),"Response :" + sys, Toast.LENGTH_LONG).show();
                        //dataArr[i] = sys;
                        //Log.i("MYAPP", "autolist"+i+sys+" "+sys.contains("."));
                        if(!sys.contains(".")) {
                            autoCompleteList.add(sys);
                            desList.add(des);
                            Log.i("MYAPP", "autolist"+i+sys+" "+sys.contains("."));
                        }
                    }
                    dataArr = new String[autoCompleteList.size()];
                    for(int i=0;i<autoCompleteList.size();i++){
                        dataArr[i] = autoCompleteList.get(i) + " | "+desList.get(i);
                    }
                    ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, dataArr);
                    searchAutoComplete.setAdapter(newsAdapter);

                } catch (JSONException e) {
                    Log.e("MYAPP", "unexpected JSON exception", e);
                    // Do something to recover ... or kill the app.
                }
                //Toast.makeText(getApplicationContext(),"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext()," ERROROR", Toast.LENGTH_LONG).show();
                Log.i(TAG,"Error 11:" + error.toString());
            }
        });

        mRequestQueue.add(mJsonObjectRequest);

    }

    private void getStockDetail(String ticker,int index){
        String detailUrl = "https://nodejs-786565.wl.r.appspot.com/api/stockdetail/"+ticker;
        mJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, detailUrl, null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String comName = response.getString("name");
                    nameArr[index] = comName;

                } catch (JSONException e) {
                    Log.e("MYAPP", "unexpected JSON exception", e);
                    // Do something to recover ... or kill the app.
                }
                //Toast.makeText(getApplicationContext(),"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext()," ERROROR_DETAIL", Toast.LENGTH_LONG).show();
                Log.i(TAG,"Error 11:" + error.toString());
            }
        });

        mRequestQueue.add(mJsonObjectRequest);
    }
    private void getLatestPrice(String ticker,int index,int mode){
        String priceUrl = "https://nodejs-786565.wl.r.appspot.com/api/latestprice/"+ticker;
        mJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, priceUrl, null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    double currentPrice = response.getDouble("c");
                    double changeprice = response.getDouble("d");
                    //double changeprice = 3.0;
                    double changepriceper = response.getDouble("dp");
                    DecimalFormat df=new DecimalFormat("0.00");
                    if(mode==0){
                        priceArr[index] = round(currentPrice,2);
                        priceChangeArr[index]= round(changeprice,2);
                        priceChangePerArr[index]= round(changepriceper,2);
                    }
                    else{
                        priceArr_por[index] = round(currentPrice,2);
                        priceChangeArr_por[index]= round(changeprice,2);
                        priceChangePerArr_por[index]= round(changepriceper,2);
                    }


                } catch (JSONException e) {
                    Log.e("MYAPP", "unexpected JSON exception", e);
                    // Do something to recover ... or kill the app.
                }
                //Toast.makeText(getApplicationContext(),"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext()," ERROROR", Toast.LENGTH_LONG).show();
                Log.i(TAG,"Error 11:" + error.toString());
            }
        });

        mRequestQueue.add(mJsonObjectRequest);
    }
    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}