package in.pixli.android;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.pixli.android.retrofit.ApiClient;
import in.pixli.android.retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sidra on 02-11-2016.
 *
 * This Activity is responsible for showing the list of events related to the user account (as admin and as guest).
 * When an event code is clicked, EVENT_ID is set to that code and photos for it are displayed in BucketDisplay.java
 */

public class Event_List extends Activity{

    private RecyclerView mRecyclerView;
    private EventViewHolderMain mAdapter;

    public SimpleCursorAdapter listAdapter;

    List<CustomViewHolder> hostedEvent;
    List<CustomViewEventList> attendedEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_event_list);




        //Make a get call to get the list of events from database

        ApiInterface apiService1 = ApiClient.createService(ApiInterface.class);
        Call<CustomViewEventListResponse> call1 = apiService1.getEventList(LoginActivity.EMAIL_ID);
        //Call<CustomViewEventList> call1 = apiService1.getEventList(getIntent().getStringExtra("data"));
        //Call<CustomViewEventList> call1 = apiService1.getEventList("sidraeffendi@gmail.com");
        call1.enqueue(new Callback<CustomViewEventListResponse>() {
            @Override
            public void onResponse(Call<CustomViewEventListResponse> call1, Response<CustomViewEventListResponse> response) {
                int statuscode = response.code();

                Log.e("Getting no.of Photos", "Response: "+statuscode);

                if (response.body() != null) {

                    hostedEvent =response.body().getEvents();
                    attendedEvent = response.body().getGuestList();


                    /* To get the no.of events hosted and attended as guest */
                    int sizeHevents = hostedEvent.size();
                    int sizeGevents = attendedEvent.size();




                    if(sizeHevents != 0 ) {
                        System.out.println(hostedEvent.get(0).getCode_id());

                        List<String> eventListStringData = new ArrayList<String>();
                        for (int i= 0; i<sizeHevents; i++){
                            eventListStringData.add(hostedEvent.get(i).getCode_id());
                        }
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        mRecyclerView.setHasFixedSize(true);
                        mAdapter = new EventViewHolderMain(eventListStringData);
                        mRecyclerView.setAdapter(mAdapter);

                    }

                    /*if(sizeGevents != 0 ) {
                       // System.out.println(attendedEvent.get(0).getAttended_event_id());

                        List<String> eventListStringData = new ArrayList<String>();
                        for (int i= 0; i<sizeGevents; i++){
                            eventListStringData.add(attendedEvent.get(i).getAttended_event_id());
                        }
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        mRecyclerView.setHasFixedSize(true);
                        mAdapter = new EventViewHolderMain(eventListStringData);
                        mRecyclerView.setAdapter(mAdapter);

                    }*/


                    Log.e("Hosted_events size  :  ",""+ sizeHevents);
                    System.out.println(""+sizeHevents);


                    System.out.println(hostedEvent.get(0).getCode_id());
                   // Toast.makeText(Event_List.this,hostedEvent.get(0).getCode_id(), Toast.LENGTH_LONG).show();
                    //Log.e("Guest_events  :  ", attendedEvent.get(0).getAttended_event_id());

                    MainActivity.PHOTO_COUNT =1;     /* static variable declared in MainActiviyt.java*/

                    /* Set the event id, the photos of whihc will be displayed in Bucket */
                    MainActivity.EVENT_ID= hostedEvent.get(0).getCode_id();
                    System.out.println(MainActivity.EVENT_ID);




                    //Display the list of events
                    displayEventList();
                }
                else{
                    Log.e("Error",""+statuscode+ "......"+ "....null body");
                    Toast.makeText(getApplicationContext(), "No events", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<CustomViewEventListResponse> call1,Throwable t) {
                t.printStackTrace();
            }
        });

        // Set the EVENT_ID= the one user clicked and start the BucketDispay file
        //MainActivity.EVENT_ID = "ASP1";

//        MainActivity.FOLDER_NAME ="img"+MainActivity.EVENT_ID;
//        Intent myIntent = new Intent(Event_List.this, BucketDisplay.class);
//        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        Event_List.this.startActivity(myIntent);
        //finish();
    }

    public void displayHostEvents(){

    }

    public void displayEventList(){

        /*Log.i("Event list", "updating");

        listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.event_item,
                cursor,
                new String[] { TableData.Columns.VENUE, TableData.Columns.CODE},
                new int[] { R.id.admin_code_id,R.id.guest_code_id},
                0
        );
        this.setListAdapter(listAdapter);*/
    }



    public class EventViewHolderMain extends RecyclerView.Adapter<EventViewHolder> {
        private Context mContext;
        List<String> eventListItem;

        public EventViewHolderMain(List<String> eventListItem) {
            this.eventListItem=eventListItem;


        }

        @Override
        public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.event_list_items, parent, false);

            return new EventViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(EventViewHolder holder, final int position) {

            String eventString = eventListItem.get(position);
            holder.eventText.setText(eventString);



            holder.eventText.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){



                }
            });

        }

        @Override
        public int getItemCount() {
            return eventListItem.size();
        }
    }

    class EventViewHolder extends RecyclerView.ViewHolder{

        final public TextView eventText;

        public EventViewHolder(View itemView) {
            super(itemView);
            eventText = (TextView) itemView.findViewById(R.id.titleEvent);
        }
    }
}
