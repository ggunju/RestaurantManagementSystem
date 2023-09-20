import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Admin } from 'src/app/class/admin';
import { ResturantmanagemntService } from 'src/app/service/resturantmanagemnt.service';

@Component({
  selector: 'app-cash-payment',
  templateUrl: './cash-payment.component.html',
  styleUrls: ['./cash-payment.component.css']
})
export class CashPaymentComponent {
order : any;
  p: number = 1;
  count: number = 5;
  admin: Admin;
    constructor(private resturantManageService:ResturantmanagemntService,public router:Router, private activeRoute:ActivatedRoute) { }
    ngOnInit(): void 
    {
      this.activeRoute.paramMap.subscribe(()=>this.getSOrders());
      this.activeRoute.paramMap.subscribe(()=>this.admin=JSON.parse(sessionStorage.getItem("admin")))
      this.checkSessionAndNavigate();
    }

    getSOrders(){
      this.resturantManageService.getOrderStatus().subscribe(data=>{
        console.log(data);
        this.order=data;
      });
    }
    deleteOrder(id:number):void{
      console.log(id);
      if(confirm("Do you want to delete ?")){
        this.resturantManageService.deleteOrder(id).subscribe(data=>{
          console.log(data);
          this.getSOrders();
        })
      };
    }
    CashPay(id:number):void{
      console.log(id);
      if(confirm("Cash Payment Done ?")){
        this.resturantManageService.CashPay(id).subscribe(data=>{
          console.log(data);
          this.getSOrders();
        })
      };
      location.reload();
    }
    logout() {
      if (sessionStorage.getItem("admin")) {
        sessionStorage.clear()
        localStorage.clear()
        alert("Logout Successfully")
        this.router.navigateByUrl("/admin/login")
      }
      else {
        alert("No user loged in")
      }
    }
    checkSessionAndNavigate() {
      if (!this.admin) {
        this.router.navigateByUrl("/admin/login");
      }
    }

}
