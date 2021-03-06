package com.koreatech.naeilro.ui.facility;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.koreatech.core.recyclerview.RecyclerViewClickListener;
import com.koreatech.core.toast.ToastUtil;
import com.koreatech.naeilro.NaeilroApplication;
import com.koreatech.naeilro.R;
import com.koreatech.naeilro.network.entity.facility.Facility;
import com.koreatech.naeilro.network.interactor.FacilityRestInteractor;
import com.koreatech.naeilro.ui.facility.adapter.FacilityDetailInfoRecyclerViewAdapter;
import com.koreatech.naeilro.ui.facility.adapter.FacilityImageRecyclerViewAdapter;
import com.koreatech.naeilro.ui.facility.presenter.FacilityDetailFragmentPresenter;
import com.koreatech.naeilro.ui.main.MainActivity;
import com.koreatech.naeilro.ui.myplan.MyPlanBottomSheetActivity;
import com.koreatech.naeilro.util.SearchKeyWordUtil;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import kr.co.prnd.readmore.ReadMoreTextView;

import static com.koreatech.naeilro.ui.myplan.MyPlanBottomSheetActivity.CONTENT_AREA_CODE;
import static com.koreatech.naeilro.ui.myplan.MyPlanBottomSheetActivity.CONTENT_ID;
import static com.koreatech.naeilro.ui.myplan.MyPlanBottomSheetActivity.CONTENT_MAP_X;
import static com.koreatech.naeilro.ui.myplan.MyPlanBottomSheetActivity.CONTENT_MAP_Y;
import static com.koreatech.naeilro.ui.myplan.MyPlanBottomSheetActivity.CONTENT_THUMBNAIL;
import static com.koreatech.naeilro.ui.myplan.MyPlanBottomSheetActivity.CONTENT_TITLE;
import static com.koreatech.naeilro.ui.myplan.MyPlanBottomSheetActivity.CONTENT_TYPE;

public class FacilityDetailFragment extends Fragment implements FacilityDetailFragmentContract.View {
    private static final double centerLon = 127.48318433761597;
    private static final double centerLat = 36.41592967015607;
    private static final int ZOOM_LEVEL = 15;
    private View view;
    private Unbinder unbinder;
    /* View component */
    private ImageView facilityDetailImage;
    private TextView facilityDetailTitle;
    private ReadMoreTextView facilityDetailOverview;
    private TextView facilityDetailInfoTextView;
    private LinearLayout facilityImageLinearLayout;
    private ImageView facilityExtraImageView;
    private RecyclerView facilityExtraImageRecyclerView;
    private RecyclerView facilityInfoRecyclerView;
    private LinearLayout facilityDetailMapLinearLayout;
    private LinearLayout facilityDetailTMapLinearLayout;
    private TextView facilityAddressTextView;
    private CheckBox restaurantCheckBox;
    private CheckBox convenienceStoreCheckBox;
    private TextView resetMapTextView;
    private TMapMarkerItem selectedTMapMarkerItem;
    private ArrayList<TMapPOIItem> restaurantIDArrayList;
    private ArrayList<TMapPOIItem> convenienceStoreIDArrayList;

    private TMapView tMapView;
    private FacilityDetailInfoRecyclerViewAdapter facilityDetailInfoRecyclerViewAdapter;
    private FacilityDetailFragmentPresenter facilityDetailPresenter;
    private LinearLayoutManager linearLayoutManager;
    private List<Facility> imagefacilityInfoList;
    private FacilityImageRecyclerViewAdapter facilityDetailImageRecyclerViewAdapter;
    private int contentId;
    private String contentTypeID;
    private String contentTitle;
    private String contentThumbnail;
    private String detailTitle;
    private String mapX;
    private String mapY;
    private String areaCode;


    public static Spanned fromHtml(String source) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(source);
        }
        return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_facility_detail, container, false);
        this.unbinder = ButterKnife.bind(this, view);
        contentId = getArguments().getInt("contentId");
        detailTitle = getArguments().getString("title");
        contentTitle = detailTitle;
        init(view);
        return view;
    }

    @OnClick(R.id.add_my_plan_facility)
    public void clickFacilityMyPlanButton() {
        Intent intent = new Intent(getActivity(), MyPlanBottomSheetActivity.class);
        intent.putExtra(CONTENT_ID, String.valueOf(contentId));
        intent.putExtra(CONTENT_TYPE, contentTypeID);
        intent.putExtra(CONTENT_TITLE, contentTitle);
        intent.putExtra(CONTENT_THUMBNAIL, contentThumbnail);
        intent.putExtra(CONTENT_MAP_X, mapX);
        intent.putExtra(CONTENT_MAP_Y, mapY);
        intent.putExtra(CONTENT_AREA_CODE, areaCode);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null) unbinder.unbind();
    }

    public void init(View view) {
        restaurantIDArrayList = new ArrayList<>();
        convenienceStoreIDArrayList = new ArrayList<>();
        imagefacilityInfoList = new ArrayList<>();
        initView(view);
        initTMap(view);
        initFacilityExtraImageRecyclerView();
        facilityDetailPresenter = new FacilityDetailFragmentPresenter(new FacilityRestInteractor(), this);
        facilityDetailPresenter.getCommonInfo(contentId);
        facilityDetailPresenter.getDetailInfo(contentId);
        facilityDetailPresenter.getImageInfo(contentId);


    }

    private void initView(View view) {
        facilityDetailImage = view.findViewById(R.id.facility_detail_image);
        facilityDetailTitle = view.findViewById(R.id.facility_detail_title);
        facilityDetailOverview = view.findViewById(R.id.facility_detail_overview);
        facilityDetailInfoTextView = view.findViewById(R.id.facility_detail_info_text_view);
        facilityImageLinearLayout = view.findViewById(R.id.facility_image_linear_layout);
        facilityExtraImageView = view.findViewById(R.id.facility_extra_image_view);
        facilityExtraImageRecyclerView = view.findViewById(R.id.facility_extra_image_recycler_view);
        facilityDetailMapLinearLayout = view.findViewById(R.id.facility_detail_map_linear_layout);
        facilityDetailTMapLinearLayout = view.findViewById(R.id.facility_detail_t_map_linear_layout);
        facilityAddressTextView = view.findViewById(R.id.facility_address_text_view);
        facilityDetailMapLinearLayout.setVisibility(View.GONE);
        facilityImageLinearLayout.setVisibility(View.GONE);
        facilityInfoRecyclerView = view.findViewById(R.id.facility_info_recycler_view);
        restaurantCheckBox = view.findViewById(R.id.restaurant_check_box);
        convenienceStoreCheckBox = view.findViewById(R.id.convenience_store_check_box);
        resetMapTextView = view.findViewById(R.id.reset_text_view);
        restaurantCheckBox.setOnCheckedChangeListener(this::setRestaurantCheckBox);
        convenienceStoreCheckBox.setOnCheckedChangeListener(this::setConvenienceStoreCheckBox);
        resetMapTextView.setOnClickListener(v -> resetPosition());
    }


    private void initTMap(View view) {
        tMapView = new TMapView(Objects.requireNonNull(getActivity()));
        tMapView.setSKTMapApiKey(NaeilroApplication.getTMapApiKey());
        tMapView.setCenterPoint(centerLon, centerLat);
        tMapView.setOnCalloutRightButtonClickListener(this::goToDetailPageByMarker);
        facilityDetailTMapLinearLayout.addView(tMapView);
    }

    private void goToDetailPageByMarker(TMapMarkerItem tMapMarkerItem) {
        String[] s = tMapMarkerItem.getCalloutSubTitle().split(" ");
        String searchName = s[s.length - 1] + " " + tMapMarkerItem.getCalloutTitle();
        SearchKeyWordUtil.searchByNaver(searchName, getContext());
    }


    public void setRestaurantCheckBox(View view, boolean isChecked) {
        String id = "음식점";
        if (selectedTMapMarkerItem == null) return;
        if (isChecked) {
            findAroundByName(id, R.drawable.ic_restaurant_color);
        } else {
            removeMapMarkerByID(id);
        }
    }

    public void setConvenienceStoreCheckBox(View view, boolean isChecked) {
        String id = "편의점";
        if (selectedTMapMarkerItem == null) return;
        if (isChecked) {
            findAroundByName(id, R.drawable.ic_facility_color);
        } else {
            removeMapMarkerByID(id);
        }
    }

    public void setAddressInfo(double x, double y, String title, String address) {
        showMapPoint(x, y, title, address);
        facilityAddressTextView.setText(address);
    }

    private void findAroundByName(String id, @DrawableRes int drawable) {
        if (selectedTMapMarkerItem == null) return;
        TMapPoint tMapPoint = new TMapPoint(selectedTMapMarkerItem.latitude, selectedTMapMarkerItem.longitude);
        new TMapData().findAroundKeywordPOI(tMapPoint, id, 3, 50, arrayList -> {
            if (id.equals("편의점")) {
                removeMapMarkerByID(id);
                convenienceStoreIDArrayList.addAll(arrayList);
            } else {
                removeMapMarkerByID(id);
                restaurantIDArrayList.addAll(arrayList);
            }
            for (TMapPOIItem point : arrayList) {
                addPin(point.getPOIName(), point.getPOIAddress().replace("null", ""), point.getPOIPoint().getLongitude(), point.getPOIPoint().getLatitude(), drawable);
            }
            resetPosition();
        });
    }

    private void removeMapMarkerByID(String id) {
        if (id.equals("편의점")) {
            for (TMapPOIItem mapPOIItem : convenienceStoreIDArrayList) {
                tMapView.removeMarkerItem(mapPOIItem.getPOIName());
            }
            convenienceStoreIDArrayList.clear();
        } else {
            for (TMapPOIItem mapPOIItem : restaurantIDArrayList) {
                tMapView.removeMarkerItem(mapPOIItem.getPOIName());
            }
            restaurantIDArrayList.clear();
        }
    }

    private void resetPosition() {
        if (selectedTMapMarkerItem == null) return;
        tMapView.setCenterPoint(selectedTMapMarkerItem.longitude, selectedTMapMarkerItem.latitude, true);
        tMapView.setZoomLevel(ZOOM_LEVEL);
    }

    private void addPin(String name, String subTitle, Double longitude, Double latitude, @DrawableRes int drawable) {
        TMapMarkerItem markerItem1 = new TMapMarkerItem();
        TMapPoint tMapPoint1 = new TMapPoint(latitude, longitude); // SKT타워
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), drawable);
        Bitmap markerBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
        Bitmap selectBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_arrow_forward_white_36dp);
        Bitmap callOutSelectBitmap = Bitmap.createScaledBitmap(selectBitmap, 50, 50, false);
        markerItem1.setIcon(markerBitmap); // 마커 아이콘 지정
        markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
        markerItem1.setTMapPoint(tMapPoint1); // 마커의 좌표 지정
        markerItem1.setName(name); // 마커의 타이틀 지정
        markerItem1.setCanShowCallout(true);
        markerItem1.setEnableClustering(false);
        markerItem1.setCalloutTitle(name);
        markerItem1.setCalloutSubTitle(subTitle);
        markerItem1.setCalloutRightButtonImage(callOutSelectBitmap);
        tMapView.addMarkerItem(name, markerItem1); // 지도에 마커 추가
        tMapView.setCenterPoint(longitude, latitude);
    }

    private void showMapPoint(double x, double y, String title, String address) {
        facilityDetailMapLinearLayout.setVisibility(View.VISIBLE);
        TMapMarkerItem markerItem = new TMapMarkerItem();
        TMapPoint tMapPoint1 = new TMapPoint(y, x);
        selectedTMapMarkerItem = markerItem;
        markerItem.setVisible(TMapMarkerItem.VISIBLE);
        markerItem.setPosition(0f, 0f);
        markerItem.setTMapPoint(tMapPoint1);
        markerItem.setName(title);
        markerItem.setCanShowCallout(true);
        markerItem.setCalloutTitle(title);
        markerItem.setCalloutSubTitle(address);
        tMapView.addMarkerItem(title, markerItem);
        tMapView.setCenterPoint(x, y, true);
        tMapView.setZoomLevel(ZOOM_LEVEL);
        tMapView.initView();
    }

    @Override
    public void showDetailInfoList(List<Facility> facilityList) {
        facilityInfoRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        facilityDetailInfoRecyclerViewAdapter = new FacilityDetailInfoRecyclerViewAdapter(facilityList);
        facilityDetailInfoRecyclerViewAdapter.setRecyclerViewClickListener(new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                facilityDetailOverview.toggle();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
        facilityInfoRecyclerView.setAdapter(facilityDetailInfoRecyclerViewAdapter);
        //setDetailInfo(facilityList);
        //facilityDetailFragmentPresenter.getImageInfo(contentId);
    }

    @Override
    public void showImageInfoList(List<Facility> facilityList) {
        /*
        facilityImageRecyclerView = view.findViewById(R.id.facility_Image_recyclerview);
        facilityImageRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        facilityImageRecyclerViewAdapter = new FacilityImageRecyclerViewAdapter(facilityList);
        facilityImageRecyclerView.setAdapter(facilityImageRecyclerViewAdapter);

         */
        setImageExtra(facilityList);
    }

    @Override
    public void showCommonInfo(Facility facility) {
        contentTypeID = facility.getContenttypeid();
        contentThumbnail = facility.getFirstimage();
        mapX = facility.getMapx();
        mapY = facility.getMapy();
        areaCode = facility.getAreacode();
        setAddressInfo(Double.parseDouble(facility.getMapx()), Double.parseDouble(facility.getMapy()), detailTitle, facility.getAddr1());
        setFirstImageView(facility.getFirstimage());
        setTitle(detailTitle);
        setSummary(facility.getOverview());
    }

    private void setImageExtra(List<Facility> facilityItems) {
        if (facilityItems == null || facilityItems.size() == 0) return;
        facilityImageLinearLayout.setVisibility(View.VISIBLE);
        imagefacilityInfoList.addAll(facilityItems);
        facilityDetailImageRecyclerViewAdapter.notifyDataSetChanged();
        setFacilityExtraImageView(0);
    }

    private void initFacilityExtraImageRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        facilityDetailImageRecyclerViewAdapter = new FacilityImageRecyclerViewAdapter(imagefacilityInfoList, getContext());
        facilityDetailImageRecyclerViewAdapter.setRecyclerViewClickListener(new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Glide.with(getContext())
                        .load(imagefacilityInfoList.get(position).getOriginimgurl())
                        .error(R.drawable.ic_no_image)
                        .into(facilityExtraImageView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
        facilityExtraImageRecyclerView.setLayoutManager(linearLayoutManager);
        facilityExtraImageRecyclerView.setAdapter(facilityDetailImageRecyclerViewAdapter);
    }

    private void setDetailInfo(List<Facility> facilityItems) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Facility facilityInfo : facilityItems) {
            stringBuilder.append("<b>").append(" · ").append(facilityInfo.getInfoname()).append("</b>").append(" : ").append(facilityInfo.getInfotext()).append("<br>").append("<br>");
        }
        facilityDetailInfoTextView.setText(fromHtml(stringBuilder.toString()));
    }

    private void setFacilityExtraImageView(int position) {
        if (imagefacilityInfoList.isEmpty() || imagefacilityInfoList.size() < position) return;
        Glide.with(getContext())
                .load(imagefacilityInfoList.get(position).getOriginimgurl())
                .error(R.drawable.ic_no_image)
                .into(facilityExtraImageView);
    }

    private void setFirstImageView(String url) {
        Glide.with(getContext()).load(url)
                .apply(new RequestOptions().bitmapTransform(new RoundedCorners(24)))
                .error(R.drawable.ic_no_image)
                .into(facilityDetailImage);
    }

    private void setSummary(String text) {
        if (text == null) return;
        facilityDetailOverview.setText(fromHtml(text));
        facilityDetailOverview.setChangeListener(this::toggle);
        toggle(facilityDetailOverview.getState());
    }

    private void toggle(ReadMoreTextView.State state) {
        if (state == ReadMoreTextView.State.COLLAPSED) {
            facilityDetailInfoTextView.setVisibility(View.GONE);
            facilityInfoRecyclerView.setVisibility(View.GONE);
        } else {
            facilityDetailInfoTextView.setVisibility(View.VISIBLE);
            facilityInfoRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void setTitle(String text) {
        if (text == null) return;
        facilityDetailTitle.setText(text);
    }

    @Override
    public void showLoading() {
        ((MainActivity) getActivity()).showProgressDialog(R.string.loading_facility_info);
    }

    @Override
    public void hideLoading() {
        ((MainActivity) getActivity()).hideProgressDialog();
    }

    @Override
    public void setPresenter(FacilityDetailFragmentPresenter presenter) {
        this.facilityDetailPresenter = presenter;
    }
}
