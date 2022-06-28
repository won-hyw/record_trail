package com.example.record_trail;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private View view;
    private String title, time, length;
    private Double latitude, longitude;

    public interface OnListItemLongSelectedInterface {
        void onItemLongSelected(View v, int position);
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    private OnListItemLongSelectedInterface mLongListener;
    private OnListItemSelectedInterface mListener;

    ArrayList<TrailItem2> itemArrayList;

    public CardAdapter(ArrayList<TrailItem2> itemArrayList, OnListItemSelectedInterface listener, OnListItemLongSelectedInterface longListener) {
        this.itemArrayList = itemArrayList;
        this.mListener = listener;
        this.mLongListener = longListener;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 지도 화면이 들어가 있는 카드뷰 레이아웃으로 설정
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        final Geocoder geocoder = new Geocoder(holder.itemView.getContext());

        title = itemArrayList.get(position).getTitle();
        time = itemArrayList.get(position).getTime();
        length = itemArrayList.get(position).getLength() + "KM";

        holder.title.setText(title);
        holder.time.setText(time);
        holder.length.setText(length);

        holder.mapView.onCreate(null);
        holder.mapView.onResume();
        holder.mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                if (itemArrayList.get(holder.getAdapterPosition()).getStartingPointSupportAddr().isEmpty() == false) {
                    List<Address> list = null;
                    try {
                        list = geocoder.getFromLocationName(itemArrayList.get(holder.getAdapterPosition()).getStartingPointAddress(), 10);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (list != null) {
                        if (list.size() == 0) {
                            Log.i("[error]", "올바른 주소를 입력해주세요");
                        }
                        else {
                            Address address = list.get(0);
                            latitude = address.getLatitude();
                            longitude = address.getLongitude();
                        }
                    }
                }

                if (holder.mMap == null) {
                    holder.mMap = googleMap;
                    MapsInitializer.initialize(holder.itemView.getContext());
                    LatLng location = new LatLng(latitude, longitude);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(location);
                    // 마커 설정
                    holder.mMap.addMarker(markerOptions);
                    // 지도 줌 레벨 설정
                    holder.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
                    // 지도 확대, 축소 등의 기능 제어
                    holder.mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    holder.mMap.getUiSettings().setZoomControlsEnabled(false);
                    holder.mMap.getUiSettings().setZoomGesturesEnabled(false);
                    holder.mMap.getUiSettings().setScrollGesturesEnabled(false);
                    holder.mMap.getUiSettings().setTiltGesturesEnabled(false);
                    holder.mMap.getUiSettings().setRotateGesturesEnabled(false);
                }
            }
        });
    }

    public int getItemCount() {
        return itemArrayList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        GoogleMap mMap;
        MapView mapView;
        TextView title;
        TextView time;
        TextView length;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);

            this.title = itemView.findViewById(R.id.tv_title);
            this.time = itemView.findViewById(R.id.tv_time);
            this.length = itemView.findViewById(R.id.tv_length);
            this.mapView = itemView.findViewById(R.id.mapView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    mListener.onItemSelected(v, getAdapterPosition());
                    if (position != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(v.getContext(), DetailActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("title", CardAdapter.this.itemArrayList.get(position).getTitle());
                        intent.putExtra("time", CardAdapter.this.itemArrayList.get(position).getTime());
                        intent.putExtra("length", CardAdapter.this.itemArrayList.get(position).getLength());
                        intent.putExtra("latitude", CardAdapter.this.latitude);
                        intent.putExtra("longitude", CardAdapter.this.longitude);
                        intent.putExtra("endingPointAddress", CardAdapter.this.itemArrayList.get(position).getEndingPointAddress());
                        intent.putExtra("endingPointSupportAddr", CardAdapter.this.itemArrayList.get(position).getEndingPointSupportAddr());
                        v.getContext().startActivity(intent);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongListener.onItemLongSelected(v, getAdapterPosition());
                    return false;
                }
            });
        }
    }


}
