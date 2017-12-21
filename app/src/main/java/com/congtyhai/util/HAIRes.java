package com.congtyhai.util;

import com.congtyhai.model.api.AgencyInfo;
import com.congtyhai.model.api.CalendarDayCreate;
import com.congtyhai.model.api.CalendarStatus;
import com.congtyhai.model.api.EventProduct;
import com.congtyhai.model.api.GeneralInfo;
import com.congtyhai.model.api.ProductCodeInfo;
import com.congtyhai.model.api.ProductOrder;
import com.congtyhai.model.api.ResultEventInfo;
import com.congtyhai.model.api.order.C1OrderInfo;
import com.congtyhai.model.api.order.OrderProductResult;
import com.congtyhai.model.api.order.YourOrderInfo;
import com.congtyhai.model.app.C2Info;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by HAI on 8/14/2017.
 */

public class HAIRes {

    // announce
    public final String ANNOUNCEMENT_DISCONNECT_NETWORK = "Mất kết nối";

    public int inOder = 0;


    // broadcast receiver intent filters
    public final String REGISTRATION_COMPLETE = "registrationComplete";
    public final String PUSH_NOTIFICATION = "pushNotification";
    // global topic to receive app wide push notifications
    public final String TOPIC_GLOBAL = "global";
    // id to handle the notification in the notification tray
    public final int NOTIFICATION_ID = 100;
    public final String SHARED_PREF = "ah_firebase";

  //  public final String KEY_USER = "userlogin";
  ///  public final String KEY_TOKEN = "tokenlogin";
    public final String KEY_INTENT_USER = "username";
    public final String KEY_INTENT_PHONE = "phonenumber";
    public final String KEY_INTENT_CREATE_CALENDAR = "createcalendar";
    public final String KEY_INTENT_AGENCY_CODE = "agencycode";
    public final String KEY_INTENT_TEMP = "temp";
    public final String KEY_INTENT_TEMP2 = "temp2";
    public final String KEY_INTENT_ORDER = "inorder";
    public final String KEY_INTENT_DAY = "dayselect";
    public final String KEY_INTENT_MONTH = "monthselect";
    public final String KEY_INTENT_YEAR = "yearselect";
    public final String KEY_SCREEN_KEY_SCAN = "ScreenKey";
    public final String KEY_INTENT_ACTION = "actionrequest";
    public String CurrentAgency ;
    private CalendarDayCreate calendarDayCreate;
  //  private List<CalendarStatus> calendarStatuses;

   // public final String baseUrl = "http://192.168.2.170:802/api/";
   // public final String baseUrlUpload = "http://192.168.2.170:801/";
    public final String baseUrl = "http://dmsapi.nongduochai.vn/api/";
    public final String baseUrlUpload = "http://dms.nongduochai.vn/";

    private static HAIRes instance = null;

    public static HAIRes getInstance() {
        if (instance == null) {
            instance = new HAIRes();
        }
        return instance;
    }
    public final String PHONE_CALL_CENTER = "1800577768";
    // code menu check in
    //public final int CHECKIN_CHECK = 1;
  //  public final int CHECKIN_PICTURE = 2;
   // public final int CHECKIN_ORDER = 3;
    //public final int CHECKIN_PRODUCT = 4;

    // code menu bottom calendar
   // public final int CALENDAR_CREATE = 1;
    public final int CALENDAR_MODIFY = 2;

    // save file path
    public final String PATH_PRODUCT_GROUP_JSON = "productgroup.json";
    public final String PATH_AGENCY_JSON = "agency.json";
    public final String PATH_AGENCY_C1_JSON = "agencyc1.json";
    //public final String PATH_RECEIVE_JSON = "/receive.json";
    public final String PATH_PRODUCT_JSON = "product.json";

    // pref

    public final String PREF_KEY_USER = "HAIUSER";
    public final String PREF_KEY_TOKEN = "HAITOKEN";
    public final String PREF_KEY_TYPE= "HAITYPE";

    public final String PREF_KEY_UPDATE_DAILY = "updatedatadaily";
    public final String PREF_KEY_FIREBASE = "regFBId";
    public final String PREF_KEY_FUNCTION = "functionlogin";

    // data
    public C2Info c2Select;

    private List<CalendarStatus> calendarStatuses = new ArrayList<>();
    public void addListCalendarStatus(List<CalendarStatus> statuses) {
        calendarStatuses.clear();
        calendarStatuses.addAll(statuses);
    }

    public List<CalendarStatus> getCalendarStatuses() {
        return this.calendarStatuses;
    }

   // public CalendarCreateSend calendarCreateSend = new CalendarCreateSend();
    public AgencyInfo currentAgencySelect = new AgencyInfo();
    public final double LIMIT_DISTANCE = 1000;

    /*
    public String[] GetListStatusName() {
        String[] statusTemp = new String[calendarStatuses.size()];
        for(int i=0; i< calendarStatuses.size(); i++) {
            statusTemp[i] = calendarStatuses.get(i).name;
        }

        return statusTemp;
    }*/

    public int findPostitionStatus(String status) {
        for(int i=0; i< calendarStatuses.size(); i++) {
            if (status.equals(calendarStatuses.get(i).id)){
                return  i;
            }
        }

        return 0;
    }

    /*
    public String findStatusName(String status) {
        for(int i=0; i< calendarStatuses.size(); i++) {
            if (status.equals(calendarStatuses.get(i).id)){
                return calendarStatuses.get(i).name;
            }
        }

        return "";
    }
*/
    // status
    public final String CALENDAR_CSKH = "CSKH";
  //  public final String CALENDAR_OTHER = "OTHER";


    // product
    public String PRODUCT_IMPORT = "NK";
    public String PRODUCT_EXPORT = "XK";
    public String PRODUCT_TRANSPORT= "TRANSPORT";
    public String PRODUCT_HELP_SCAN = "HELPSCAN";
    public List<String> LIST_PRODUCT;
    public void resetListProduct() {
        LIST_PRODUCT = new ArrayList<>();
    }

    private HashMap<String, String> productCodeMap = new HashMap<>();
    public void clearProductCodeMap() {
        productCodeMap.clear();
    }

    public void addProductCodeMap(ProductCodeInfo info) {
        productCodeMap.put(info.getBarcode(), info.getName());
    }
    public void addListProduct(String item) {
        if (LIST_PRODUCT == null) {
            LIST_PRODUCT = new ArrayList<>();
        }

        LIST_PRODUCT.add(0, item);
    }

    public int countListProduct() {
        return getLIST_PRODUCT().size();
    }

    public List<String> getLIST_PRODUCT() {
        if (LIST_PRODUCT == null) {
            LIST_PRODUCT = new ArrayList<>();
        }

        return LIST_PRODUCT;
    }

    public String[] toProductArrays() {
        String[] arrays = new String[HAIRes.getInstance().countListProduct()];

        int i = 0;
        for (String item : LIST_PRODUCT) {
            arrays[i] = item;
            i++;
        }

        return arrays;
    }
    public String  findProductNameByCode(String code) {

        if (productCodeMap.containsKey(code)) {
            return productCodeMap.get(code);
        }

        return "KHÔNG PHẢI SẢN PHẨM CỦA HAI";
    }


    //
    private List<ProductOrder> productOrders;

    public void addProductOrder(ProductOrder product) {
        if (productOrders == null)
            productOrders = new ArrayList<>();

        if (product != null)
            productOrders.add(product);
    }


    public List<ProductOrder> getProductOrder() {
        if (productOrders == null)
            productOrders = new ArrayList<>();

        return productOrders;
    }

    public void clearProductOrder() {
        productOrders = new ArrayList<>();
    }

    /*
    public void removeProductOrder(ProductOrder productCodeInfo) {
        if (productOrders !=  null)
            productOrders.remove(productCodeInfo);
    }*/

    public void removeProductOrderAt(int i) {
        if (productOrders != null)
            productOrders.remove(i);
    }

    public boolean checkExistProductOrder(String code) {
        if (productOrders != null) {
            for(ProductOrder order: productOrders) {
                if (code.equals(order.getCode())) {
                    return true;
                }
            }
        }

        return  false;
    }

    public ProductOrder getProductOrder(String code) {
        if (productOrders != null) {
            for(ProductOrder order: productOrders) {
                if (code.equals(order.getCode())) {
                    return order;
                }
            }
        }

        return  null;
    }

    public int getProductOrderIndex(String code) {
        if (productOrders != null) {
            for(int i = 0; i< productOrders.size(); i++) {
                if (code.equals(productOrders.get(i).getCode())) {
                    return i;
                }
            }
        }
        return  0;
    }

    public String formatMoneyToText(double value) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String moneyString = formatter.format(value);
        if (moneyString.endsWith(".00")) {
            int centsIndex = moneyString.lastIndexOf(".00");
            if (centsIndex != -1) {
                moneyString = moneyString.substring(1, centsIndex);
            }
        }

        return  moneyString + " VND";
    }

    public CalendarDayCreate getCalendarDayCreate() {
        return calendarDayCreate;
    }

    public void setCalendarDayCreate(CalendarDayCreate calendarDayCreate) {
        this.calendarDayCreate = calendarDayCreate;
    }

    // event
    private List<ResultEventInfo> resultEventInfos;
    public List<ResultEventInfo> getResultEventInfos() {
        if (resultEventInfos == null) {
            resultEventInfos = new ArrayList<>();
        }
        return resultEventInfos;
    }

    public void addListEvent(ResultEventInfo info) {

        if (resultEventInfos == null) {
            resultEventInfos = new ArrayList<>();
        }
        resultEventInfos.add(info);

    }

    public void clearListEvent() {
        if (resultEventInfos != null) {
            resultEventInfos = new ArrayList<>();
        }
    }

    // procduct event
    private List<EventProduct> PRODUCTEVENT;
    public void addListProductEvent(EventProduct eventProduct) {
        if (PRODUCTEVENT == null) {
            PRODUCTEVENT = new ArrayList<>();
        }

        PRODUCTEVENT.add(eventProduct);
    }

    public List<EventProduct> getListProductEvent() {
        if (PRODUCTEVENT == null) {
            PRODUCTEVENT = new ArrayList<>();
        }

        return PRODUCTEVENT;
    }

    public void resetListProductEvent() {
        if (PRODUCTEVENT == null) {
            PRODUCTEVENT = new ArrayList<>();
        }
        PRODUCTEVENT.clear();
    }
    //
    private List<String> EVENTCODES = new ArrayList<>();
    public List<String> getEVENTCODES() {
        return this.EVENTCODES;
    }

    public boolean addToEventCode(String id) {

        if (EVENTCODES.contains(id))
            return false;

        EVENTCODES.add(id);

        return true;
    }

    public void resetEventCode() {
        EVENTCODES.clear();
    }

    //
    private List<GeneralInfo> eventCodeResult;
    public List<GeneralInfo> getEventCodeResult() {
        return eventCodeResult;
    }

    public void setEventCodeResult(List<GeneralInfo> eventCodeResult) {
        this.eventCodeResult = eventCodeResult;
    }


    // order
    public C1OrderInfo C1OrderInfo;

    public YourOrderInfo yourOrderInfo;

    public OrderProductResult orderProductResult;

    public int CREATE_ORDER_TYPE = 0;

    public String getOrderQuantityDetailText(int box, int quantity, String unit) {
        int countCan = quantity / box;
        int countBox = quantity - countCan*box;

        if (countCan == 0) {
            return countBox + " " + unit;
        }

        if (countBox == 0) {
            return countCan + " thùng";
        }

        return countCan + " thùng " + countBox + " " + unit;

    }

    public String getConvertMesterDistance (double value) {
        if (value < 1000) {
            return  Math.round(value) + " m";
        }

        return  Math.round(value / 1000) + " km";
    }
}
