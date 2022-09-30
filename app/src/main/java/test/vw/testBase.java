package test.vw;

import android.graphics.PorterDuff.Mode;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import com.Vworld2DAPI.R;

import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;

import vw.Map;

public class testBase extends test.vw.testButton {
	
	public testBase(final Map map) {
		super(map);
		
		icon = Integer.toString(R.drawable.test_base);
		
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
				ImageView image = (ImageView)v;
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					image.setColorFilter(0xaa111111, Mode.SRC_OVER);
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					image.setColorFilter(0x00000000, Mode.SRC_OVER);
					ITileSource tileSource;
					
					
					if(map.getMapView().getTileProvider().getTileSource() == TileSourceFactory.GRAPHIC){
						tileSource = TileSourceFactory.PHOTO;
					}else{
						tileSource = TileSourceFactory.GRAPHIC;
					}

					map.getMapView().setTileSource(tileSource);
				}	
				return true; 
			}
		});
		
		rl.addView(toolButton);
		
	}

}
