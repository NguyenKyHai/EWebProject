package com.ute.common.constants;

public class Constants {
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_SALESPERSON = "ROLE_SALESPERSON";
    public static final String ROLE_ASSISTANT = "ROLE_ASSISTANT";
    public static final String ROLE_SHIPPER = "ROLE_SHIPPER";
    public static final String ROLE_EDITOR = "ROLE_EDITOR";

    public static final String STATUS_ACTIVE = "STATUS_ACTIVE";
    public static final String STATUS_LOGOUT = "STATUS_LOGOUT";
    public static final String STATUS_BLOCKED = "STATUS_BLOCKED";
    public static final String STATUS_VERIFY = "STATUS_VERIFY";

    public static final String STATUS_INITIAL = "STATUS_INITIAL";

    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    public static final int cookieExpireSeconds = 180;

    public static final String PHOTO_IMAGE_DEFAULT =
            "https://res.cloudinary.com/disyupqea/image/upload/v1680441268/default/avatar-default_l4u1ft.png";
    public static final String USER_PUBLIC_ID_DEFAULT = "default/avatar-default_l4u1ft";
    public static final String PRODUCT_IMAGE_DEFAULT =
            "https://res.cloudinary.com/disyupqea/image/upload/v1680441271/default/product-default_caexf1.jpg";
    public static final String PRODUCT_PUBLIC_ID_DEFAULT = "default/product-default_caexf1";

    public static final String VNPay = "VNPay";
    public static final String COD = "COD";
    public static final String ORDER_STATUS_NEW = "NEW";
    public static final String ORDER_STATUS_PAID = "PAID";
    public static final String ORDER_STATUS_DELIVERED = "DELIVERED";
    public static final String ORDER_STATUS_PROCESSING = "PROCESSING";
    public static final String ORDER_STATUS_PACKAGED = "PACKAGED";
    public static final String ORDER_STATUS_SHIPPING = "SHIPPING";
    public static final String ORDER_STATUS_RETURNED = "RETURNED";
    public static final String DISABLED = "DISABLED";
    public static final String ENABLED = "ENABLED";

    public static final String TYPE_REPORT_WEEK = "WEEK";
    public static final String TYPE_REPORT_MONTH = "MONTH";
    public static final String TYPE_REPORT_QUARTER = "QUARTER";
    public static final String TYPE_REPORT_YEAR = "YEAR";
}
