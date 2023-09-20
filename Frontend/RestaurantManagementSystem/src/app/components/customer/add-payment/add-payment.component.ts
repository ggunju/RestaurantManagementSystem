import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Customer } from 'src/app/class/customer';
import { Order } from 'src/app/class/order';
import { ResturantmanagemntService } from 'src/app/service/resturantmanagemnt.service';
declare var Razorpay:any;
@Component({
  selector: 'app-add-payment',
  templateUrl: './add-payment.component.html',
  styleUrls: ['./add-payment.component.css']
})
export class AddPaymentComponent implements OnInit {
  customer: Customer
  order: Order;
  generatedOrderId: number;
  cust: number;
  isPaymentProcessing = false;
  payment: any = {
    razorpayId:"",
    paidAmount: 0
  };
  constructor(private activeRoute: ActivatedRoute, private router: Router, private resturantmanagemntservice: ResturantmanagemntService) { }
  ngOnInit(): void {
    this.generatedOrderId = Number(localStorage.getItem("generatedOrderId"));
    this.getOrderByOrderId();
    this.activeRoute.paramMap.subscribe(() => this.customer = JSON.parse(sessionStorage.getItem("customer")));
    this.cust = this.customer.customerId
    this.checkSessionAndNavigate();
  }

 

  getOrderByOrderId() {
    this.resturantmanagemntservice.getorderbyid(this.generatedOrderId).subscribe(
      (data) => {
        console.log(data);
        this.order = data;
        this.payment.paidAmount = this.order.totalPrice;
      },
      (error) => {
        console.error('Error fetching order', error);

      }
    );
  }
  checkSessionAndNavigate() {
    if (!this.customer) {
      this.router.navigateByUrl("/customer/login");
    }
  }


  async createTransaction(paymentForm: NgForm) {
    if (!this.isPaymentProcessing && paymentForm.valid) {
      try {
        this.isPaymentProcessing = true;
        const response = await this.resturantmanagemntservice.createTransaction(this.payment.paidAmount).toPromise();
        this.openTransactionModal(response);
      } catch (error) {
        console.log(error);
      }
    }
  }
    openTransactionModal(response:any){
      var options = {
        order_id :response.orderId,
        key : response.key,
        amount : response.amount,
        currency : response.currency,
        name : 'Resturant Managment',
        description :'Payment of your order',
        image : '',
        handler : (response : any)=> {
          
          this.processResponse(response) ;
        },
        prefill:{
        name:this.customer.customerName,
        email:this.customer.email,
        contact:this.customer.customerPhone,
        },
        notes:{
          address:'Food Order'
        },
        theme:{
          color:'#F37254'
        }
      }
      var razorPayObject = new  Razorpay (options);
      razorPayObject.open();
      console.log(response)
    }
    processResponse(resp:any){
      const razorpayPaymentId = resp.razorpay_payment_id; 
      this.payment.razorpayId = razorpayPaymentId; 
      console.log("the result is:", resp); 
      
      
    this.resturantmanagemntservice.addPayment(this.payment, this.generatedOrderId, this.cust).subscribe(
      () => {
        console.log(this.payment);
        console.log('Payment added successfully');
        alert("Amount Payed successfully")
        localStorage.clear()
        this.router.navigateByUrl("/customer/myorders")
      },
      (error) => {
        console.error('Error adding payment', error);
      }
    );
      
  }
}


