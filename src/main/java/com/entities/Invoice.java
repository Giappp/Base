package com.entities;

import java.sql.Date;

public class Invoice {
    private int id;
    private int customerId;
    private int userId;
    private int orderId;
    private int paymentType;
    private double totalPrice;
    private double totalPaid;
    private Date dateRecorded;
}
