package test.vw;

import android.graphics.PorterDuff.Mode;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import com.Vworld2DAPI.R;

import vw.Collection;
import vw.Map;
import vw.SiteAlignType;
import vw.control.Area;
import vw.control.Distance;
import vw.control.Reset;
import vw.control.ToolButton;
import vw.control.Toolbar;
import vw.control.ZoomIn;
import vw.control.ZoomOut;

public class testToolbar extends test.vw.testButton {
	
	public testToolbar(final Map map) {
		super(map);
		
		icon = Integer.toString(R.drawable.test_bar);
		
		toolButton= new ImageView(rl.getContext());
		
		toolButton.setLayoutParams(rlLp);
		
		//버튼 이미지 설정
		toolButton.setImageResource(Integer.parseInt(icon));

		//버튼 이미지 크기
		toolButton.getLayoutParams().width = 90;
		toolButton.getLayoutParams().height = 90;
		
		//클릭시 효과 및 확대 이벤트
		toolButton.setOnTouchListener(new OnTouchListener() {
			boolean toolbarChk;
			int toolbarC;
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				ImageView image = (ImageView)v;
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					image.setColorFilter(0xaa111111, Mode.SRC_OVER);
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					image.setColorFilter(0x00000000, Mode.SRC_OVER);
					
					for(int i=0;i<map.getContainer().getChildCount();i++){
						if(map.getContainer().getChildAt(i).getId() == R.drawable.test_bar){
							toolbarChk = true;
							toolbarC = i;
						}else{
							toolbarChk = false;
						}
					}
					
					if(toolbarChk){
						map.getContainer().removeViewAt(toolbarC);
					}else{
						 ToolButton zoomIn = new ZoomIn(map);
					     ToolButton zoomOut = new ZoomOut(map);
					     ToolButton remove = new Reset(map);
					     ToolButton distance = new Distance(map);
					     ToolButton area = new Area(map);
					     //ToolButton hamburger = new HbButton(map);

					     Collection<ToolButton> collection = new Collection<ToolButton>();
					     
					     //collection.add(hamburger);
					     collection.add(zoomIn);
					     collection.add(zoomOut);
					     collection.add(remove);
					     collection.add(distance);
					     collection.add(area);
					     
					     Toolbar toolBar = new Toolbar(map, collection);
					     toolBar.setAlign(SiteAlignType.CENTER_RIGHT);
					     toolBar.setVertical();
					     toolBar.setCollapsible(true);
					     toolBar.setCollapsed(false);
					}
					
				}	
				return true;
			}
		});
		
		rl.addView(toolButton);
		
	}

}
