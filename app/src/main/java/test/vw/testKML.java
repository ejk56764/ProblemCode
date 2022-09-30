package test.vw;

import android.graphics.PorterDuff.Mode;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import com.Vworld2DAPI.R;

import vw.Map;
import vw.source.Vector;

public class testKML extends test.vw.testButton {
	
	public testKML(final Map map) {
		super(map);
		
		icon = Integer.toString(R.drawable.test_kml);
		
		toolButton= new ImageView(rl.getContext());
		
		toolButton.setLayoutParams(rlLp);
		
		//버튼 이미지 설정
		toolButton.setImageResource(Integer.parseInt(icon));

		//버튼 이미지 크기
		toolButton.getLayoutParams().width = 90;
		toolButton.getLayoutParams().height = 90;
		
		//클릭시 효과 및 확대 이벤트
		toolButton.setOnTouchListener(new OnTouchListener() {
			boolean toolbarChk = true;
			int toolbarC;
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				ImageView image = (ImageView)v;
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					image.setColorFilter(0xaa111111, Mode.SRC_OVER);
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					image.setColorFilter(0x00000000, Mode.SRC_OVER);

					Vector kmlVector = new Vector("XML");
				    kmlVector.setUrl("http://openlayers.org/en/v3.11.2/examples/data/kml/2012_Earthquakes_Mag5.kml");
				    //kmlVector.setUrl("https://developers.google.com/maps/tutorials/kml/westcampus.kml");
				    vw.layer.Vector klVector = new vw.layer.Vector(map, kmlVector);
				}	
				return true;
			}
		});
		
		rl.addView(toolButton);
		
	}

}
