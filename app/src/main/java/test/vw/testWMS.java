package test.vw;

import android.graphics.PorterDuff.Mode;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.Vworld2DAPI.R;

import vw.CRS;
import vw.Map;
import vw.source.TileWMS;

public class testWMS extends test.vw.testButton {
    private vw.source.Tile sourceTile;

    public testWMS(final Map map) {
        super(map);

        icon = Integer.toString(R.drawable.test_wms);

        toolButton = new ImageView(rl.getContext());

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
                ImageView image = (ImageView) v;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    image.setColorFilter(0xaa111111, Mode.SRC_OVER);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    image.setColorFilter(0x00000000, Mode.SRC_OVER);

                    TileWMS tileWMS = new TileWMS();
                    tileWMS.setUrl("http://2d.vworld.kr:8895/2DCache/gis/map/WMS");
                    tileWMS.setVersion("1.3.0");
                    CRS crs = new CRS();
                    crs.code = "EPSG:900913";
                    tileWMS.setCrs(crs);
                    tileWMS.setLayers("LT_C_ADSIDO");
                    tileWMS.setStyles("");
                    tileWMS.setTransparent(true);

                    vw.source.Tile sTile = tileWMS;
                    sourceTile = new vw.source.Tile(map, sTile);
                    Toast.makeText(map.getMapView().getContext(), "타일이미지-광역시도",
                            Toast.LENGTH_LONG).show();

                    //map.getMapView().setTileSource(map.getMapView().getTileProvider().getTileSource());


                }
                return true;
            }
        });

        rl.addView(toolButton);

    }

}
