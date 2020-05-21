package com.autogarcon.android.API;

import com.autogarcon.android.ActiveSession;

/**
 * A class representation of the NewOrder JSON object request sent to initiate a new order
 * @author Mitchell Nelson
 */
public class NewOrderRequest {
    private int customerID;

    public NewOrderRequest(){
        customerID = Integer.parseInt(ActiveSession.getInstance().getUserId());
    }

    public int getCustomerID() {
        return customerID;
    }

}
