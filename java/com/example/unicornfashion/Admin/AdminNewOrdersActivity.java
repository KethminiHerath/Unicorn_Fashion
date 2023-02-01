package com.example.unicornfashion.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unicornfashion.Model.AdminOrders;
import com.example.unicornfashion.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrdersActivity extends AppCompatActivity {


    private RecyclerView orderList;
    private DatabaseReference orderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        orderList = findViewById(R.id.order_list);
        orderList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(orderRef, AdminOrders.class)
                .build();
       FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter =
               new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
                   @Override
                   protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, final int position, @NonNull final AdminOrders model) {
                       holder.userName.setText("Name : " + model.getName());
                       holder.userPhoneNo.setText("Phone : " + model.getPhone());
                       holder.userShippingAddress.setText("Address : " + model.getAddress() + " " +model.getCity());
                       holder.userDateTime.setText("Order at : " + model.getDate() + " " + model.getTime());
                       holder.userTotalPrice.setText("Total Amount : $" + model.getTotalAmount());

                       holder.showOrderDetails.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {

                               String uID = getRef(position).getKey();

                               Intent intent = new Intent(AdminNewOrdersActivity.this, AdminUserProductActivity.class);
                               intent.putExtra("uid", uID);
                               startActivity(intent);
                           }
                       });

                       holder.itemView.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               CharSequence options[] = new CharSequence[]{
                                       "Yes",
                                       "No"
                               };
                               AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                               builder.setTitle("Have You Shipped Order Product ? ");

                               builder.setItems(options, new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialogInterface, int i) {
                                       if (i == 0){
                                           String uID = getRef(position).getKey();
                                           RemoveOrder(uID);
                                       }
                                       else {

                                       }
                                   }
                               });
                               builder.show();
                           }
                       });

                   }

                   @NonNull
                   @Override
                   public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                       return new AdminOrdersViewHolder(view);
                   }
               };

       orderList.setAdapter(adapter);
       adapter.startListening();
    }

    private void RemoveOrder(String uID) {
        orderRef.child(uID).removeValue();
    }


    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder{

        public TextView userName, userPhoneNo, userTotalPrice, userDateTime, userShippingAddress;
        public Button showOrderDetails;

        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.order_user_name);
            userPhoneNo = itemView.findViewById(R.id.order_phone_number);
            userTotalPrice = itemView.findViewById(R.id.order_Total_price);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            userShippingAddress = itemView.findViewById(R.id.order_address_city);
            showOrderDetails = itemView.findViewById(R.id.show_all_products);

        }
    }

}