package test.vw;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapboxMap mapboxMap;
    private MapView mapview;

    public enum TILEMAPTYPE {Base, gray, midnight, Satellite, Hybrid,}

    private final String wmtsurl = "https://api.vworld.kr/reg/wmts/1.0.0/";
    private final String apikey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstancestate);

        Mapbox.getInstance(this);
        setContentView(R.layout.activity_main);

        mapvien = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstancestate);

        mapView.getMapAsync(callback MainActivity.this);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        MainActivity .this.mapboxMap =mapboxMap;

        mapboxhap.setstyle(new Style.Builder().fromuri("asset: //mystyle. json"),style -> {
            AddWMNTSLayer(style, TILEMAPTYPE.Satellite);
            AddWMTSLayer(style, TILEMAPTYPE.Hybrid);
            mapboxMap.setCameraPosition(new CameraPosition.Builder()
                    .target(new LatLng(latitude:37.498, longitude:127.028))
                    .zoom(16.0)
                    .build());
        });
    }
    private void AddWHTSLayer(Style style, TILEMAPTYPE mapType) {
        RasterLayer layer = null;
        String layerId = mapType.name();
        String url = wmtsUrl + apikey + "/" + layerId + "/"
                + ((mapType == TILEMAPTYPE.Satellite)"{z}/{y}/{x}.jpeg : "{z}/{y}/{x}.png");

        Rastersource source = new RasterSource(id:layerId + "-src", new TileSet(tilejson :"2.1.0", url));
        style.addSource(source);
        layer = new Rasterlayer(layerId, sourceld layerId + "-src");
        style.addLayer(layer);
    }
}