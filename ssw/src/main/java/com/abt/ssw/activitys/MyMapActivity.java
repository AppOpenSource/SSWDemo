package com.abt.ssw.activitys;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;

import com.amap.mapapi.map.MapActivity;
import com.amap.mapapi.map.MapController;
import com.amap.mapapi.map.MapView;
/*****************************************************************
 * 地图activity
 * @author xuena.ni
 *
 */
public class MyMapActivity extends MapActivity implements OnClickListener{
	 private MapView mMapView;
	 private MapController controller;
	 private ImageButton back;//返回按钮
	 
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       requestWindowFeature(Window.FEATURE_NO_TITLE);
       setContentView(R.layout.map);
       back = (ImageButton)findViewById(R.id.back_btn);
       back.setOnClickListener(this);
	   mMapView=(MapView)findViewById(R.id.myMapView);
	   mMapView.setBuiltInZoomControls(true);
	   controller=mMapView.getController();
	   Drawable marker=getResources().getDrawable(R.drawable.da_marker_red1);
	   marker.setBounds(0,0,marker.getIntrinsicWidth(),marker.getIntrinsicHeight());
	   mMapView.getOverlays().add(new MyItemizedOverlay(marker, this));
   }

@Override
public void onClick(View v) {
	switch (v.getId()) {
	case R.id.back_btn:
		this.finish();
		break;

	default:
		break;
	}
	
}
}
