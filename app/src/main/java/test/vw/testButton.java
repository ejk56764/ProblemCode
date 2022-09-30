package test.vw;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import vw.Map;
import vw.control.Control;

public abstract class testButton extends Control{

	private Map map;
	protected String icon;
	protected String caption;
	protected boolean captionVisible = false;
	
	protected RelativeLayout.LayoutParams rlLp;
	protected RelativeLayout rl;
	protected GestureDetector mGestureDetector;	
	protected ImageView toolButton;
	
	protected testButton(Map map) {
		super(map);
		ViewGroup container = map.getContainer();
		
		// RelativeLayout 생성
		rl = new RelativeLayout(container.getContext());
				
		//레이아웃 비우기
		rl.removeAllViews();
				
		// RelativeLayout width, height 설정
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		
		// RelativeLayout에 width, height 설정 적용
		rl.setLayoutParams(params);
				
		//확대버튼 생성, 설정
		rlLp = new RelativeLayout.LayoutParams(30, 30);
	

		
	}

	protected class DoubleClickListener implements GestureDetector.OnDoubleTapListener {
		@Override
		public boolean onDoubleTap(final MotionEvent e) {
			if (map.getMapView().getOverlayManager().onDoubleTap(e, map.getMapView())) {
				return true;
			}

			// final IGeoPoint center = getProjection().fromPixels((int) e.getX(), (int) e.getY(),
			// null);
			if(map.getMapView().mDoubleTapZoom){
				map.getMapView().getProjection().rotateAndScalePoint((int) e.getX(), (int) e.getY(), map.getMapView().mRotateScalePoint);
				return map.getMapView().zoomInFixing(map.getMapView().mRotateScalePoint.x, map.getMapView().mRotateScalePoint.y);
			}else{
				//getProjection().rotateAndScalePoint((int) e.getX(), (int) e.getY(), mRotateScalePoint);
				return false;
			}

		}

		@Override
		public boolean onDoubleTapEvent(final MotionEvent e) {
			return map.getMapView().getOverlayManager().onDoubleTapEvent(e, map.getMapView());

		}

		@Override
		public boolean onSingleTapConfirmed(final MotionEvent e) {
			return map.getMapView().getOverlayManager().onSingleTapConfirmed(e, map.getMapView());

		}
	}

}
