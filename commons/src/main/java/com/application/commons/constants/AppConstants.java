package com.application.commons.constants;

public class AppConstants {

    public final static String CONTENT_TYPE_KEY = "Content-Type";
    public final static String REQUEST_HEADER_OAUTH_TOKEN_KEY = "x-user-token";
    public final static String REQUEST_HEADER_MID_KEY = "x-user-mid";
    public final static String REQUEST_CONTEXT_TOKEN_KEY = "sessionToken";
    public final static String MODEL_REQUEST_CONTEXT_KEY = "requestContext";
    public static final String REQUEST_HEADER_REQUEST_ID_KEY = "requestId";
    public static final String OAUTH_USER_INFO_SERIAL = "sessionUserSerial";
    public static final String OAUTH_USER_INFO = "sessionUser";
    public static final String REQUEST_ID = "requestId";

	public static final String X_AUTH_UMP = "x-auth-ump";
	public static final String X_USER_TOKEN = "x-user-token";
	public static final String X_USER_MID = "x-user-mid";
	public static final String X_SITE_ID = "x-site-id";
	public static final String X_SITE_NAME = "x-site-name";
	public static final String X_FILE_NAME = "x-file-name";
	
	public static final String X_CLIENT_ID = "x-client-id";
	public static final String X_CLIENT_TOKEN = "x-client-token";
	
	//miscellaneous
	public static final String BLANK = "";

	public static final String COMMA = ",";
	public static final String COMMA_WITH_WHITESPACE = "\\s*,\\s*";

	public static final String AR_SITE_ID = "-1";

    public enum ResultStatus{
		SUCCESS,
		ACCEPTED,
		FAILED;
	}
    
	public static final String INVOICE_NUM_SEPARATOR = "-";
	public static final String INVOICE_FILE_NAME_FORMAT = "Invoice no. #%s.pdf";
	public static final String RECEIPT_FILE_NAME_FORMAT = "Receipt no. #%s.pdf";
	public static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

	//properties
	public static final String KAFKA_CONSUMER_BOOTSTRAP_ADDRESS ="kafka.consumer.bootstrap.address";
	public static final String KAFKA_PRODUCER_BOOTSTRAP_ADDRESS ="kafka.producer.bootstrap.address";
	public static final String KAFKA_CONSUMER_CONCURRENCY="kafka.consumer.concurrency";
	public static final String KAFKA_AUTO_OFFSET_RESET="kafka.auto.offset.reset";
	public static final String KAFKA_DEFAULT_GROUP_ID="kafka." + "default.consumer.group.id";
	
}
