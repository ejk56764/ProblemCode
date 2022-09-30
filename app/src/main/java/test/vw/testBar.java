package test.vw;

import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import vw.Collection;
import vw.Map;
import vw.SiteAlignType;
import vw.control.Collapsible;

public class testBar extends Collapsible {

	Collection<testButton> buttons;
	boolean vertical = false;
	protected RelativeLayout rl;

	public testBar(final Map map, Collection<testButton> testCollection, Boolean vertical) {
		super(map);
		this.vertical = vertical;
		// RelativeLayout 생성
		ViewGroup container = map.getContainer();

		rl = new RelativeLayout(container.getContext());

		//레이아웃 비우기
		rl.removeAllViews();

		// RelativeLayout width, height 설정
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

		// RelativeLayout에 width, height 설정 적용
		rl.setLayoutParams(params);


		// RelativeLayout 생성
		LinearLayout ll = new LinearLayout(container.getContext());

		//레이아웃 비우기
		ll.removeAllViews();


		//전개방향 설정
		if(vertical){
			ll.setOrientation(LinearLayout.VERTICAL);
		}

		//버튼 추가
		for(int i=0;i<testCollection.getArray().size();i++){
			testButton toolButton = (testButton)testCollection.getArray().get(i);

			RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.RIGHT_OF);
			toolButton.rl.setLayoutParams(params2);

			ll.addView(toolButton.rl);

		}

		//툴바 추가
		rl.addView(ll);
		container.addView(rl);

	}

	public void setAlign(SiteAlignType type){
		if(type!=null){
			site = type;
		}
		switch(site){
		/*case NONE :
			
				break;*/
			case TOP_LEFT :
				rl.setVerticalGravity(Gravity.TOP);
				rl.setHorizontalGravity(Gravity.LEFT);
				break;
			case TOP_CENTER :
				rl.setVerticalGravity(Gravity.TOP);
				rl.setHorizontalGravity(Gravity.CENTER);
				break;
			case TOP_RIGHT :
				rl.setVerticalGravity(Gravity.TOP);
				rl.setHorizontalGravity(Gravity.RIGHT);
				break;
			case CENTER_LEFT :
				rl.setVerticalGravity(Gravity.CENTER);
				rl.setHorizontalGravity(Gravity.LEFT);
				break;
			case CENTER_CENTER :
				rl.setVerticalGravity(Gravity.CENTER);
				rl.setHorizontalGravity(Gravity.CENTER);
				break;
			case CENTER_RIGHT :
				rl.setVerticalGravity(Gravity.CENTER);
				rl.setHorizontalGravity(Gravity.RIGHT);
				break;
			case BOTTOM_LEFT :
				rl.setVerticalGravity(Gravity.BOTTOM);
				rl.setHorizontalGravity(Gravity.LEFT);
				break;
			case BOTTOM_CENTER :
				rl.setVerticalGravity(Gravity.BOTTOM);
				rl.setHorizontalGravity(Gravity.CENTER);
				break;
			case BOTTOM_RIGHT :
				rl.setVerticalGravity(Gravity.BOTTOM);
				rl.setHorizontalGravity(Gravity.RIGHT);
				break;
			default :
				rl.setVerticalGravity(Gravity.TOP);
				rl.setHorizontalGravity(Gravity.LEFT);
				break;
		}
	}

}
