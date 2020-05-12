package com.autogarcon.android.API;

import com.autogarcon.android.ActiveSession;

public class NewOrderRequest {
    private int customerID;

    public NewOrderRequest(){
        customerID = Integer.parseInt(ActiveSession.getInstance().getUserId());
    }

    public int getCustomerID() {
        return customerID;
    }

}
