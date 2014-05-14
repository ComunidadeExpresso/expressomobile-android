package com.udinic.accounts_authenticator_example.authentication;

/**
 * Created with IntelliJ IDEA.
 * User: Udini
 * Date: 20/03/13
 * Time: 18:11
 */
public class AccountGeneral {

    /**
     * Account type id
     */
    public static final String ACCOUNT_TYPE = "com.celepar.expresso.account";

    /**
     * Account name
     */
    public static final String ACCOUNT_NAME = "Expresso";

    /**
     * Auth token types
     */
    public static final String AUTHTOKEN_TYPE_READ_ONLY = "Somente Leitura";
    public static final String AUTHTOKEN_TYPE_READ_ONLY_LABEL = "Acesso de somente leitura para uma conta do Expresso.";

    public static final String AUTHTOKEN_TYPE_FULL_ACCESS = "Acesso total";
    public static final String AUTHTOKEN_TYPE_FULL_ACCESS_LABEL = "Acesso total para uma conta do Expresso.";

    public static final ServerAuthenticate sServerAuthenticate = new ParseComServerAuthenticate();
}
