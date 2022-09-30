package test.vw;

import android.graphics.PorterDuff.Mode;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.Vworld2DAPI.R;

import vw.Feature;
import vw.Features;
import vw.Map;
import vw.Pixel;
import vw.source.Vector;
import vw.style.Icon;
import vw.style.Marker;
import vw.style.StyleGroup;

public class testMarker extends test.vw.testButton {
	
	private boolean isChecked = true;
	
	public testMarker(final Map map) {
		super(map);
		
		mGestureDetector = map.getMapView().mGestureDetector;
		
		icon = Integer.toString(R.drawable.test_marker);
		
		toolButton= new ImageView(rl.getContext());
		
		toolButton.setLayoutParams(rlLp);
		
		//버튼 이미지 설정
		toolButton.setImageResource(Integer.parseInt(icon));

		//버튼 이미지 크기
		toolButton.getLayoutParams().width = 90;
		toolButton.getLayoutParams().height = 90;
		
		//클릭시 효과 및 확대 이벤트
		toolButton.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				ImageView img;
				for(int i=0;i<rl.getChildCount();i++){
					img = (ImageView) rl.getChildAt(i);
					img.setColorFilter(0x00000000, Mode.SRC_OVER);
				}
				
				final ImageView image = (ImageView)v;
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					image.setColorFilter(0xaa111111, Mode.SRC_OVER);
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					image.setColorFilter(0x00000000, Mode.SRC_OVER);
					mGestureDetector.setOnDoubleTapListener(new DoubleClickListener(){
						
						@Override
						public boolean onSingleTapConfirmed(MotionEvent event) {
							
							Pixel pixel = new Pixel();
							pixel.setX((int)event.getX());
							pixel.setY((int)event.getY());
							Marker marker = new Icon();
						     StyleGroup st = new StyleGroup(marker);
						     
						     Feature f = new Feature(map, map.pixelToCoord(pixel), st);
						         
						     Features fs = new Features();
						     fs.add(f);
						     Vector ss = new Vector(fs);
						     
						     vw.layer.Vector v2 = new vw.layer.Vector(map, ss);
						     
						     map.getMapView().invalidate();
							return true;
						}
						
			    		@Override
			    		public boolean onDoubleTap(MotionEvent e) {
			    			image.setColorFilter(0x00000000, Mode.SRC_OVER);
							isChecked = true;

							mGestureDetector.setOnDoubleTapListener(new DoubleClickListener());
							
							Toast.makeText(map.getMapView().getContext(), "마커생성 종료. ", Toast.LENGTH_LONG).show();
			    			return true;//super.onDoubleTap(e);
			    		}
			    	
			    		/*@Override
			    		public boolean onDoubleTapEvent(MotionEvent e) {
			    		Log.v("하이하이", "온더블탭이벤트");
				
			    		return true;//super.onDoubleTapEvent(e);
			    		}*/
					});
				     
				}	
				return true;
			}
		});
		
		rl.addView(toolButton);
		
	}

}
