package test.vw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.widget.Button;
import com.Vworld2DAPI.R;
import vw.BasemapType;
import vw.CameraPosition;
import vw.Collection;
import vw.Coord;
import vw.DensityType;
import vw.Map;
import vw.MapOptions;
import vw.SiteAlignType;
import vw.control.Area;
import vw.control.Distance;
import vw.control.Reset;
import vw.control.ToolButton;
import vw.control.Toolbar;
import vw.control.ZoomIn;
import vw.control.ZoomOut;


public class Main extends Activity {
    Map map;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();

        setContentView(R.layout.main);


        //테스트 화면 버튼 이벤트
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main.this, test_Main.class);
                test_Main.mapCreateCont();
                startActivity(intent);

            }
        });

        MapOptions opt = new MapOptions();

        //초기 배경지도
        opt.setBasemapType(BasemapType.GRAPHIC);

        //레이어 목록

        //컨트롤 밀도
        opt.setControlsDensity(DensityType.BASIC);
        //인터액션 밀도
        opt.setInteractionsDensity(DensityType.EMPTY);
        //홈 카메라 위치

        //초기 카메라 위치
        CameraPosition cameraPosition = new CameraPosition();
        cameraPosition.setCenter(new Coord(127.1898, 38.3049));
        cameraPosition.setZoom(6);
        cameraPosition.setRotation(0);
        opt.setInitPostion(cameraPosition);

        ViewGroup container = (ViewGroup) findViewById(R.id.Layout);

        //-------------------------------------------
        //지도를 불러오기 위해서 인증키가 필요합니다.
        //-------------------------------------------
        map = new Map(container, opt);
        map.setActivity(Main.this);
        map.setServiceKey("1F8F6A64-CDC7-3BAC-BF57-EF49A09E6BF9");

        //테스트버튼 생성
        test.vw.testButton testBase = new testBase(map);
        test.vw.testButton testZoom = new testZoom(map);
        test.vw.testButton testToolbar = new testToolbar(map);
        test.vw.testButton testMarker = new testMarker(map);
        test.vw.testButton testPopup = new testPopup(map);
        //test.vw.testButton testKML = new testKML(map);
        test.vw.testButton testWMS = new testWMS(map);

        Collection<test.vw.testButton> testCollection = new Collection<test.vw.testButton>();
        testCollection.add(testBase);
        testCollection.add(testMarker);
        testCollection.add(testZoom);
        testCollection.add(testToolbar);
        testCollection.add(testPopup);
        //testCollection.add(testKML);
        testCollection.add(testWMS);
        test.vw.testBar test = new testBar(map, testCollection, true);
        test.setAlign(SiteAlignType.CENTER_LEFT);

        //최초 툴바가 접혀있는지 테스트

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
        //툴바접기 가능, 접힘 설정
        toolBar.setCollapsible(true);
        toolBar.setCollapsed(true);

        }
    }




