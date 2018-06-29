package com.hkm.api.Login;

/**
 * Created by hesk on 5/27/2014.
 */
public class AccountGeneral {

    /**
     * Account type id
     */
    public static final String ACCOUNT_TYPE = "com.hkm.onecall_auth";

    /**
     * Account name
     */
    public static final String ACCOUNT_NAME = "Onecall";

    /**
     * Auth token types
     */
    public static final String AUTHTOKEN_TYPE_CP_CLIENT = "cp";
    public static final String AUTHTOKEN_TYPE_CP_CLIENT_LABEL = "CP account to for Onecall.";
    public static final String AUTHTOKEN_TYPE_CR_CLIENT = "cr";
    public static final String AUTHTOKEN_TYPE_CR_CLIENT_LABEL = "Client representative account to for Onecall.";
    public static final String AUTHTOKEN_TYPE_STAFF_CLIENT = "staff";
    public static final String AUTHTOKEN_TYPE_STAFF_CLIENT_LABEL = "Staff account to for Onecall.";
    public static final String AUTHTOKEN_TYPE_READ_ONLY = "Read only";
    public static final String AUTHTOKEN_TYPE_READ_ONLY_LABEL = "Read only access to an Onecall account";
    public static final String AUTHTOKEN_TYPE_FULL_ACCESS = "Full access";
    public static final String AUTHTOKEN_TYPE_FULL_ACCESS_LABEL = "Full access to an Onecall account";


    /**
     * auth ID - application ID and REST KEY
     */
    public static final String X_APPLICATION_ID = "XUafJTkPikD5XN5HxciweVuSe12gDgk2tzMltOhr";
    public static final String X_REST_API_KEY = "8L9yTQ3M86O4iiucwWb4JS7HkxoSKo7ssJqGChWx";

    /**
     * path to account
     */

    public static final String sign_up_url = "https://api.parse.com/1/users";
    public static final String login_url  = "https://api.parse.com/1/login";


    public static final ServerAuthenticate sServerAuthenticate = new OCParseComServerAuthenticate();
}
