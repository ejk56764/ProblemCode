package test.vw;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.Vworld2DAPI.R;

import org.osmdroid.ResourceProxy;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.tileprovider.modules.WMSMapTileProviderBasic;
import org.osmdroid.tileprovider.modules.WMSTileSource;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.TilesOverlay;

import java.util.ArrayList;
import java.util.List;

import vw.BasemapType;
import vw.Camera;
import vw.CameraPosition;
import vw.Collection;
import vw.Color;
import vw.Coord;
import vw.DensityType;
import vw.Extent;
import vw.Map;
import vw.MapEvent;
import vw.MapOptions;
import vw.SiteAlignType;
import vw.control.ToolButton;
import vw.control.Toolbar;
import vw.control.Zoom;
import vw.geom.LineString;
import vw.geom.MultiLineString;
import vw.geom.MultiPoint;
import vw.geom.MultiPolygon;
import vw.geom.RegularShape;
import vw.geom.SpecialShape;
import vw.interaction.DoubleTapZoom;
import vw.interaction.PinchZoom;
import vw.layer.Basemap;
import vw.layer.Group;
import vw.layer.Image;
import vw.layer.Tile;
import vw.style.Circle;
import vw.style.Fill;
import vw.style.Icon;
import vw.style.Marker;
import vw.style.Stroke;
import vw.style.StyleGroup;

public class test_Main extends Activity {
    Map map;
    private MapController mapController;
    private ExpandableListView lvNavList;
    private DrawerLayout dlDrawer;
    private static boolean mIsValidateSingle = true;

    private Button btn;
    private ArrayList<String> mGroupList = null;
    private ArrayList<ArrayList<String>> mChildList = null;
    private ArrayList<String> mapContent = null;
    private ArrayList<String> controlContent = null;
    private ArrayList<String> imageContent = null;
    private ArrayList<String> imagelayerContent = null;
    private ArrayList<String> cameraContent = null;
    private ArrayList<String> drawContent = null;
    private Tile tile = null;
    private CameraPosition cameraPostion = new CameraPosition();

    //사이드메뉴 열기/닫기
    @Override
    public void onBackPressed() {
        if (dlDrawer.isDrawerOpen(lvNavList)) {
            dlDrawer.closeDrawer(lvNavList);
        } else {
            super.onBackPressed();
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            map.getMapView().invalidate();
        }
    };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_main);
        //이전 화면으로 가기
        Button button = (Button) findViewById(R.id.back);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RelativeLayout mapContainer = (RelativeLayout) findViewById(R.id.mapview);

        btn = (Button) findViewById(R.id.menu);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dlDrawer.openDrawer(lvNavList);

                if (map != null) {
                    List<Overlay> overlay = map.getMapView().getOverlays();
                    Log.i("", "Overlay Count : " + overlay.size());

                    //이전작업 Close
                    Image.urlPointClose();
                    LineString.lineStringClose();
                    RegularShape.regularClose();
                    SpecialShape.specialClose();
                    MultiPoint.multiPointClose();
                    MultiLineString.multiLineClose();
                    MultiPolygon.multiPolygonClose();
                }
            }
        });

        dlDrawer = (DrawerLayout) findViewById(R.id.dl_activity_main_drawer);

        setLayout();

        mGroupList = new ArrayList<String>();
        mChildList = new ArrayList<ArrayList<String>>();

        mapContent = new ArrayList<String>();
        controlContent = new ArrayList<String>();
        cameraContent = new ArrayList<String>();
        imageContent = new ArrayList<String>();
        imagelayerContent = new ArrayList<String>();
        drawContent = new ArrayList<String>();


        mGroupList.add("Map");
        mGroupList.add("Control");
        mGroupList.add("TileLayer");
        mGroupList.add("ImageLayer");
        mGroupList.add("Camera");
        mGroupList.add("Geometry");

        //Map
        mapContent.add("지도 생성");
        mapContent.add("배경지도 설정(기본)");
        mapContent.add("배경지도 설정(그레이맵)");
        mapContent.add("배경지도 설정(미드나잇)");
        mapContent.add("배경지도 설정(위성)");
        mapContent.add("배경지도 설정(하이브리드)");

        //Control
        controlContent.add("화면 감추기(테스트용)");
        controlContent.add("데이터 초기화");

        //Image
        imageContent.add("타일이미지-광역시도");
        imageContent.add("타일이미지-제한고도");
        imageContent.add("타일이미지-해안선");
        imageContent.add("타일이미지-항공로");
        imageContent.add("그룹 ON(타일이미지)");
        imageContent.add("그룹 OFF(타일이미지)");

        //ImageLayer
        imagelayerContent.add("이미지-멀티포인트");
        imagelayerContent.add("이미지-마지막 멀티포인트");

        //camera
        cameraContent.add("원점이동");
        cameraContent.add("카메라이동");

        //draw
        drawContent.add("라인스트링");
        drawContent.add("정다각형");
        drawContent.add("별");
        drawContent.add("멀티포인트");
        drawContent.add("멀티라인스트링(RED)");
        drawContent.add("멀티라인스트링(BLUE)");
        drawContent.add("멀티폴리곤(RED)");
        drawContent.add("멀티폴리곤(BLUE)");
        drawContent.add("정다각형 불러오기");
        drawContent.add("별 불러오기");

        mChildList.add(mapContent);
        mChildList.add(controlContent);
        mChildList.add(imageContent);
        mChildList.add(imagelayerContent);
        mChildList.add(cameraContent);
        mChildList.add(drawContent);

        lvNavList.setAdapter(new BaseExpandableAdapter(this, mGroupList, mChildList));

        //그룹메뉴 클릭
        lvNavList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

        //하위메뉴 클릭
        lvNavList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            Extent extent = new Extent(0, 0, 0, 0);

            GestureDetector mGestureDetector;

            DrawerLayout dl = (DrawerLayout) findViewById(R.id.dl_activity_main_drawer);

            Zoom zoom = null;

            Collection<ToolButton> collection = new Collection<ToolButton>();
            Toolbar toolBar;

            //@TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                Log.v("번호", "그룹번호 : " + groupPosition + "자식번호" + childPosition);

                MapEvent mapEvent = null;

                switch (groupPosition) {
                    case 0:        //Map
                        switch (childPosition) {
                            case 0:        //지도생성
                                if (!mIsValidateSingle) {
                                    dl.closeDrawers();
                                    return true;
                                }
                                MapOptions opt = new MapOptions();

                                //초기 배경지도
                                opt.setBasemapType(BasemapType.GRAPHIC);

                                //컨트롤 밀도
                                opt.setControlsDensity(DensityType.EMPTY);
                                //인터액션 밀도
                                opt.setInteractionsDensity(DensityType.EMPTY);
                                //홈 카메라 위치

                                //초기 카메라 위치
                                cameraPostion.setCenter(new Coord(127.1898, 38.3049));
                                cameraPostion.setZoom(7);
                                cameraPostion.setRotation(0);
                                opt.setInitPostion(cameraPostion);

                                ViewGroup container = (ViewGroup) findViewById(R.id.mapview);

                                //-------------------------------------------
                                //지도를 불러오기 위해서 인증키가 필요합니다.
                                //-------------------------------------------
                                map = new Map(container, opt);
                                map.setActivity(test_Main.this);
                                map.setServiceKey("API인증키 입력란");

                                PinchZoom pinchZoom = new PinchZoom(map);
                                //zoom = new Zoom(map, SiteAlignType.BOTTOM_RIGHT);
                                /*DoubleTapZoom doubleTapZoom = new DoubleTapZoom(map);
                                doubleTapZoom.setDoubleTapZoom(true);*/

                                //지도생성 -> false
                                mIsValidateSingle = false;
                                dl.closeDrawers();
                                return false;

                            case 1:        //배경지도변경(기본)
                                Basemap basemap = new Basemap(map, BasemapType.GRAPHIC);
                                String name = "GRAPHIC";
                                basemap.setName(name);
                                name = basemap.getName();

                                Group group = new Group();
                                Group.getLayer(name);
                                Log.d("basemap", "GRAPHIC:" + name);
                                Toast.makeText(getApplicationContext(), name,
                                        Toast.LENGTH_SHORT).show();
                                dl.closeDrawers();
                                return true;
                            case 2:        //배경지도변경(그레이)
                                basemap = new Basemap(map, BasemapType.GRAPHIC_GRAY);
                                name = "GRAPHIC_GRAY";
                                basemap.setName(name);
                                name = basemap.getName();

                                group = new Group();
                                Group.getLayer(name);
                                Log.d("basemap", "GRAPHIC_GRAY:" + name);
                                Toast.makeText(getApplicationContext(), name,
                                        Toast.LENGTH_SHORT).show();
                                dl.closeDrawers();
                                return true;
                            case 3:        //배경지도변경(나이트)
                                basemap = new Basemap(map, BasemapType.GRAPHIC_NIGHT);
                                name = "GRAPHIC_NIGHT";
                                basemap.setName(name);
                                name = basemap.getName();

                                group = new Group();
                                Group.getLayer(name);
                                Log.d("basemap", "GRAPHIC_NIGHT:" + name);
                                Toast.makeText(getApplicationContext(), name,
                                        Toast.LENGTH_SHORT).show();
                                dl.closeDrawers();
                                return true;
                            case 4:        //배경지도변경(항공)
                                basemap = new Basemap(map, BasemapType.PHOTO);
                                name = "PHOTO";
                                basemap.setName(name);
                                name = basemap.getName();

                                group = new Group();
                                Group.getLayer(name);
                                Log.d("basemap", "PHOTO:" + name);
                                Toast.makeText(getApplicationContext(), name,
                                        Toast.LENGTH_SHORT).show();
                                dl.closeDrawers();
                                return true;
                            case 5:        //배경지도변경(하이브리드)
                                basemap = new Basemap(map, BasemapType.PHOTO_HYBRID);
                                name = "PHOTO_HYBRID";
                                basemap.setName(name);
                                name = basemap.getName();

                                group = new Group();
                                Group.getLayer(name);
                                Toast.makeText(getApplicationContext(), name,
                                        Toast.LENGTH_SHORT).show();
                                map.getMapView().invalidate();
                                dl.closeDrawers();
                                return true;
                        }
                    case 1:     //Control
                        switch (childPosition) {
                            case 0:
                                Toast.makeText(getApplicationContext(), "화면 감추기",
                                        Toast.LENGTH_SHORT).show();
                                map.clear();
                                map.getMapView().setMapOrientation(0);
                                Camera camera = map.getCamera();
                                camera.reset();

                                mapController = (MapController) map.getMapView().getController();
                                mapController.setZoom(6);

                                dl.closeDrawers();
                                return true;
                            case 1:
                                Toast.makeText(getApplicationContext(), "데이터 초기화",
                                        Toast.LENGTH_SHORT).show();
                                map.clear();
                                map.getMapView().setMapOrientation(0);

                                //각 클래스 배열 날리기
                                Image.initData();
                                Tile.initData();
                                MultiLineString.initData();
                                MultiPolygon.initData();
                                SpecialShape.initData();
                                RegularShape.initData();

                                dl.closeDrawers();
                                return true;
                        }

                    case 2:     //TileLayer
                        switch (childPosition) {
                            //타일WMS 불러오기1
                            case 0:
                                String name = "LT_C_ADSIDO";
                                tile = new Tile(map, name);
                                dl.closeDrawers();
                                return true;

                            //타일WMS 불러오기2
                            case 1:
                                name = "LT_L_AISROUTEU";
                                tile = new Tile(map, name);
                                dl.closeDrawers();
                                return true;

                            //타일WMS 불러오기3
                            case 2:
                                name = "LT_L_TOISDEPCNTAH";
                                tile = new Tile(map, name);
                                dl.closeDrawers();
                                return true;

                            //타일WMS 불러오기4
                            case 3:
                                name = "LT_L_AISPATH";
                                tile = new Tile(map, name);
                                dl.closeDrawers();
                                return true;

                            //그룹ON 호출(타일이미지)
                            case 4:
                                Toast.makeText(getApplicationContext(), "그룹ON 호출(타일이미지)",
                                        Toast.LENGTH_SHORT).show();
                                tile = new Tile(map);
                                dl.closeDrawers();
                                return true;

                            //그룹OFF 호출(타일이미지)
                            case 5:
                                Toast.makeText(getApplicationContext(), "그룹OFF 호출(타일이미지)",
                                        Toast.LENGTH_SHORT).show();
                                map.clear();
                                dl.closeDrawers();
                                return true;
                        }

                    case 3:     //ImageLayer
                        switch (childPosition) {
                            case 0:
                                Toast.makeText(getApplicationContext(), "이미지-멀티포인트",
                                        Toast.LENGTH_SHORT).show();
                                Marker marker = new Icon();
                                Fill fill = null;
                                StyleGroup st1 = new StyleGroup(marker, fill);

                                Image layerImage = new Image(map);
                                layerImage.getMarker(st1);
                                dl.closeDrawers();
                                return true;
                            case 1:
                                Toast.makeText(getApplicationContext(), "이미지-마지막 이미지 호출",
                                        Toast.LENGTH_SHORT).show();
                                map.clear();
                                marker = new Icon();
                                fill = null;
                                st1 = new StyleGroup(marker, fill);
                                layerImage = new Image(map);
                                layerImage.selectPoint(st1);
                                dl.closeDrawers();
                                return true;
                        }

                    case 4:     //Camera
                        switch (childPosition) {
                            case 0://Camera
                                Toast.makeText(getApplicationContext(), "원점이동",
                                        Toast.LENGTH_SHORT).show();
                                Camera camera = map.getCamera();
                                camera.reset();

                                mapController = (MapController) map.getMapView().getController();
                                mapController.setZoom(6);

                                dl.closeDrawers();
                                return true;

                            case 1:
                                Toast.makeText(getApplicationContext(), "카메라이동",
                                        Toast.LENGTH_SHORT).show();
                                camera = map.getCamera();
                                CameraPosition cameraPosition = new CameraPosition();
                                cameraPosition.setCenter(new Coord(126.565927, 33.363924));
                                camera.moveTo(cameraPosition);
                                mapController = (MapController) map.getMapView().getController();
                                mapController.setZoom(7);

                                dl.closeDrawers();
                                return true;
/*                            case 2:
                                Toast.makeText(getApplicationContext(), "지도회전",
                                        Toast.LENGTH_SHORT).show();
                                camera = map.getCamera();
                                Camera camera2 = map.getCamera();
                                camera2.rotate(-15, map);
                                dl.closeDrawers();
                                return true;*/
                        }
                    case 5:     //Geometry
                        switch (childPosition) {
                            case 0:     //LineString
                                Toast.makeText(getApplicationContext(), "라인스트링",
                                        Toast.LENGTH_SHORT).show();
                                Color color = new Color(0, 0, 255);
                                Stroke stroke = new Stroke(color);
                                stroke.setWidth(7);
                                StyleGroup distanceSg = new StyleGroup(stroke);

                                LineString lineString = new LineString(map);
                                lineString.getPointByDistance(distanceSg);
                                dl.closeDrawers();
                                return true;
                            case 1:     //RegularShape
                                Toast.makeText(getApplicationContext(), "정다각형",
                                        Toast.LENGTH_SHORT).show();

                                Color sColor = new Color(255, 0, 0);
                                Stroke sStroke = new Stroke(sColor);
                                Color fColor = new Color(0, 0, 0, 0);
                                Fill fill = new Fill(fColor);
                                StyleGroup areaSg = new StyleGroup(sStroke, fill);

                                RegularShape regularShape = new RegularShape(map);
                                regularShape.getArea(areaSg);

                                map.getMapView().invalidate();
                                dl.closeDrawers();
                                return true;
                            case 2:    //SpecialShape
                                Toast.makeText(getApplicationContext(), "별",
                                        Toast.LENGTH_SHORT).show();

                                Color colorSt = new Color(255, 0, 0);
                                Stroke strokeSt = new Stroke(colorSt);
                                Color fcolorSt = new Color(0, 0, 0, 0);
                                Fill fillSt = new Fill(fcolorSt);
                                areaSg = new StyleGroup(strokeSt, fillSt);

                                SpecialShape specialShape = new SpecialShape(map);
                                specialShape.getArea(areaSg);

                                map.getMapView().invalidate();
                                dl.closeDrawers();
                                return true;
                            case 3:     //MultiPoint
                                Toast.makeText(getApplicationContext(), "멀티포인트",
                                        Toast.LENGTH_SHORT).show();
                                Marker marker1 = new Icon();
                                StyleGroup st1 = new StyleGroup(marker1);

                                MultiPoint multiPoint = new MultiPoint(map);
                                multiPoint.getMarker(st1);

                                dl.closeDrawers();
                                return true;
                            case 4:     //MultiLineString
                                Toast.makeText(getApplicationContext(), "멀티라인스트링(RED)",
                                        Toast.LENGTH_SHORT).show();
                                color = new Color(255, 0, 0);
                                stroke = new Stroke(color);
                                distanceSg = new StyleGroup(stroke);

                                MultiLineString multiLineString = new MultiLineString(map);
                                multiLineString.getPointByDistance(distanceSg);

                                dl.closeDrawers();
                                return true;

                            case 5:     //MultiLineString
                                Toast.makeText(getApplicationContext(), "멀티라인스트링(BLUE)",
                                        Toast.LENGTH_SHORT).show();
                                color = new Color(0, 0, 255);
                                stroke = new Stroke(color);
                                distanceSg = new StyleGroup(stroke);

                                multiLineString = new MultiLineString(map);
                                multiLineString.getPointByDistance(distanceSg);

                                dl.closeDrawers();
                                return true;

                            case 6:     //MultiPolygon
                                Toast.makeText(getApplicationContext(), "멀티폴리곤(RED)",
                                        Toast.LENGTH_SHORT).show();
                                sColor = new Color(255, 0, 0);
                                sStroke = new Stroke(sColor);
                                fColor = new Color(0, 0, 0, 30);
                                fill = new Fill(fColor);
                                areaSg = new StyleGroup(sStroke, fill);

                                MultiPolygon multiPolygon = new MultiPolygon(map);
                                multiPolygon.getArea(areaSg);
                                dl.closeDrawers();
                                return true;

                            case 7:     //MultiPolygon
                                Toast.makeText(getApplicationContext(), "멀티폴리곤(BLUE)",
                                        Toast.LENGTH_SHORT).show();
                                sColor = new Color(0, 0, 255);
                                sStroke = new Stroke(sColor);
                                fColor = new Color(0, 0, 0, 30);
                                fill = new Fill(fColor);
                                areaSg = new StyleGroup(sStroke, fill);

                                multiPolygon = new MultiPolygon(map);
                                multiPolygon.getArea(areaSg);

                                dl.closeDrawers();
                                return true;

                            case 8: //정다각형 불러오기
                                map.clear();
                                sColor = new Color(255, 0, 0);
                                sStroke = new Stroke(sColor);
                                fColor = new Color(0, 0, 0, 0);
                                fill = new Fill(fColor);
                                areaSg = new StyleGroup(sStroke, fill);

                                regularShape = new RegularShape(map);
                                regularShape.getAllRegular(areaSg);

                                dl.closeDrawers();
                                return true;

                            case 9: //별 불러오기
                                map.clear();
                                sColor = new Color(255, 0, 0);
                                sStroke = new Stroke(sColor);
                                fColor = new Color(0, 0, 0, 0);
                                fill = new Fill(fColor);
                                areaSg = new StyleGroup(sStroke, fill);

                                specialShape = new SpecialShape(map);
                                specialShape.getAllSpecial(areaSg);
                                dl.closeDrawers();
                                return true;
                        }
                }
                return false;
            }
        });

        lvNavList.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });
        lvNavList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });
    }

    private void setLayout() {
        lvNavList = (ExpandableListView) findViewById(R.id.lv_activity_main_nav_list);
    }
    //지도생성 1번가능
    public static void mapCreateCont(){
        mIsValidateSingle = true;
    }
}
