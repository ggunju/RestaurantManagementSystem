import { Customer } from "./customer";

export class Payment {
    paymentId: number;
    totalPrice: number;
    orderId: number;
    razorpayId :String;
    paidDate: Date;
    paidAmount: number;
    customer: Customer;

    constructor(
        paymentId: number,totalPrice: number,orderId: number,paidDate: Date,paidAmount: number,customer: Customer,razorpayId :String) {};
}
