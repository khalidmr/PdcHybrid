package com.example.testar.services;


public final class Constants {

    private Constants() {
            // restrict instantiation
    }
    
    public static final String URL_BASE = "http://linkedtree.herokuapp.com";
    public static final String URL_TREE = "/tree";
    public static final String URL_MESSAGE = "/message";
    public static final String SERVICE_PROXIMITY = "/proximity";
    public static final String SERVICE_DETAIL = "/id";
    public static final String SERVICE_GET = "/treeId";
    public static final String SERVICE_NEW = "/new";
    
    public static final String PARAMETER_LONGITUDE = "long";
    public static final String PARAMETER_LATITUDE = "lat";
    public static final String PARAMETER_RADIUS = "radius";
    public static final String PARAMETER_ID = "id";
    public static final String PARAMETER_TREE_ID = "treeId";
    
    public static final String JSON_CONTENT = "content";
    public static final String JSON_COORDINATES = "coordinates";
    public static final String JSON_KIND = "kind";
    public static final String JSON_SPECY = "specy";
    public static final String JSON_TYPE = "type";
    public static final String JSON_TRUNK = "trunk";
    public static final String JSON_CROWN = "crown";
    public static final String JSON_HEIGHT = "height";
    public static final String JSON_NAME = "name";
    public static final String JSON_DISTANCE = "distance";
    public static final String JSON_SENDER = "sender";
    
    public static final String MEASURE_UNIT = "m";
    public static final String MEASURE_UNIT_TRUNK = "cm";
	
}
