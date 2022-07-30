package com.example.lntapplication;

public class ApiUrl {
    public static String baseUrl = "http://164.52.223.163:4512/api/";

//    public static String baseUrl =  "http://mudvprfidiis:82/api/";

    public static final String SearchApi = baseUrl + "GetProductDetail?serialno=";
    public static final String IdentifyApi = baseUrl + "SearchByRfid?rfidno=";
    public static final String AssetIDDAta = baseUrl + "GetProductDetail?serialno=";
    public static final String GetAssetID = baseUrl + "GetProductSerials";
    public static final String MapRfidID = baseUrl + "MapProductsWithRFID";
    public static final String GetMappedITem = baseUrl + "GetMapedItems";
    public static final String GetAllData = baseUrl + "GetProducts";
    public static final String GetUpdateData = baseUrl + "SyncUpdateProducts";



//    public static final String ScannedItemList = baseUrl + "ReadRfidByDate?Date=";

//    public static final String WriteRfidDetails = baseUrl + "WriteRfidTagDetails";
}

