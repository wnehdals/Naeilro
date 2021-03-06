package com.koreatech.naeilro.network.service;

import com.koreatech.core.network.Xml;
import com.koreatech.naeilro.network.entity.house.HouseInfo;
import com.koreatech.naeilro.network.entity.house.HouseInfoList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HouseService {
    String HOUSE_LIST_INFO = "rest/KorService/searchStay";
    String HOUSE_COMMON_INFO = "rest/KorService/detailCommon";
    String HOUSE_INTRO_INFO = "rest/KorService/detailIntro";
    String HOUSE_DETAILIMAGE_INFO = "rest/KorService/detailImage";

    @GET(HOUSE_LIST_INFO)
    @Xml
    Observable<HouseInfoList> getHouseList(@Query("serviceKey") String serviceKey, @Query("numOfRows") int numOfRows,
                                           @Query("pageNo") int pageNo, @Query("MobileOS") String mobileOS, @Query("MobileApp") String mobileApp,
                                           @Query("arrange") String arrange, @Query("listYN") String listYN);
    @GET(HOUSE_LIST_INFO)
    @Xml
    Observable<HouseInfoList> getHouseCategoryList(@Query("serviceKey") String serviceKey, @Query("numOfRows") int numOfRows,
                                           @Query("pageNo") int pageNo, @Query("MobileOS") String mobileOS, @Query("MobileApp") String mobileApp,
                                           @Query("arrange") String arrange, @Query("listYN") String listYN, @Query("areaCode") int areaCode, @Query("sigunguCode") int sigunguCode);

    @GET(HOUSE_COMMON_INFO)
    @Xml
    Observable<HouseInfoList> getHouseCommonInfo(@Query("serviceKey") String serviceKey, @Query("contentTypeId") int contentTypeId,
                                                   @Query("contentId") int contentId, @Query("MobileOS") String mobileOS, @Query("MobileApp") String mobileApp,
                                                   @Query("firstImageYN") String firstImageYN, @Query("areaCodeYN") String areaCodeYN, @Query("addrinfoYN") String addrinfoYN,
                                                 @Query("mapinfoYN") String mapinfoYN,@Query("overviewYN") String overviewYN);

    @GET(HOUSE_INTRO_INFO)
    @Xml
    Observable<HouseInfoList> getHouseIntroInfo(@Query("serviceKey") String serviceKey, @Query("contentTypeId") int contentTypeId,
                                                 @Query("contentId") int contentId, @Query("MobileOS") String mobileOS, @Query("MobileApp") String mobileApp);

    @GET(HOUSE_DETAILIMAGE_INFO)
    @Xml
    Observable<HouseInfoList> getHouseImageInfo(@Query("serviceKey") String serviceKey, @Query("contentTypeId") int contentTypeId, @Query("contentId") int contentId
            , @Query("MobileOS") String mobileOS, @Query("MobileApp") String mobileApp);

}
