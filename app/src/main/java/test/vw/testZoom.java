package test.vw;

import android.graphics.PorterDuff.Mode;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import com.Vworld2DAPI.R;

import vw.Map;
import vw.SiteAlignType;
import vw.control.Zoom;

public class testZoom extends test.vw.testButton {
	
	public testZoom(final Map map) {
		super(map);
		
		icon = Integer.toString(R.drawable.test_zoom);
		
		toolButton= new ImageView(rl.getContext());
		
		toolButton.setLayoutParams(rlLp);
		
		//버튼 이미지 설정
		toolButton.setImageResource(Integer.parseInt(icon));

		//버튼 이미지 크기
		toolButton.getLayoutParams().width = 90;
		toolButton.getLayoutParams().height = 90;
		
		//클릭시 효과 및 확대 이벤트
		toolButton.setOnTouchListener(new OnTouchListener() {
			boolean zoomChk;
			int zoomC;
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				ImageView image = (ImageView)v;
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					image.setColorFilter(0xaa111111, Mode.SRC_OVER);
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					image.setColorFilter(0x00000000, Mode.SRC_OVER);

					for(int i=0;i<map.getContainer().getChildCount();i++){
						
						if(map.getContainer().getChildAt(i).getId() == R.drawable.test_zoom){
							zoomChk = true;
							zoomC = i;
						}else{
							zoomChk = false;
						}
					}
					
					if(zoomChk){
						map.getContainer().removeViewAt(zoomC);
					}else{
						Zoom zoom = new Zoom(map, SiteAlignType.BOTTOM_RIGHT);
					}
					
				}	
				return true;
			}
		});
		
		rl.addView(toolButton);
		
	}

}
