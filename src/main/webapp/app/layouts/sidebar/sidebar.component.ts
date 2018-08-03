import { Component, OnInit, HostListener } from '@angular/core';


import { Http, Response } from '@angular/http';
import {  LoginService, Account, AuthServerProvider, MenuItems, MenuItemsContainer } from '../../shared';
import {Subscription} from 'rxjs/Subscription';
import { Observable } from 'rxjs/Rx';

@Component({
    selector: 'app-sidebar',
    templateUrl: './sidebar.component.html',
    styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {

    isActive = false;
    showMenu = '';
    mItems:  MenuItemsContainer;
    subscription:Subscription;
    text: string;
    account: any;
    showMenuSubMenu = '';
    showNestedSubMenu = false;
  
    m = {
	"menuItems": [{
		"title": "Home",
		"permissionName": "Home",
		"iconClass": "fa fa-home",
		"expandClassName": "homepageManagement",
		"linkUrl": "/"
	}
	]
   }

 	constructor(
             private loginService: LoginService,
       		 private authServerProvider: AuthServerProvider
    		){}

	
    ngOnInit() {
    		
			this.mItems = this.m;
			
			 this.subscription = this.loginService.navItem$.subscribe(text =>{
			 				 	
			 	if(text != '') {
			 		 
			 		this.mItems = this.loginService.menuItemContainer;
			 		
			 		
			    } else if( text == '' && this.loginService.getPageRefresh() ){
			    	var token = this.authServerProvider.getToken();
			    	
			    	if(token != null ){
			    	
			    		this.subscribeToSaveResponse( this.loginService.getMenuJson( 'DeafultUserLogin' ) );
			    	 }
			    	
			 	} else {
			 		
			 		this.mItems = this.m;
			 	}
			  	
			 });
			
    }

	private subscribeToSaveResponse(result: Observable<MenuItemsContainer>) {
        result.subscribe( (res: MenuItemsContainer) =>{
        		
            	this.loginService.changeMenuJson(res);
            });
    }

    eventCalled() {
    
        this.isActive = !this.isActive;
    }

    addExpandClass(element: any) {
   
        if (element === this.showMenu) {
            this.showMenu = '0';
        } else {
            this.showMenu = element;
        }
    }
    
    addExpandClass2(element: any) {
        if (element === this.showMenuSubMenu) {
        	this.showNestedSubMenu = false;
            this.showMenuSubMenu = '0';
        } else {
        	this.showNestedSubMenu = true;
            this.showMenuSubMenu = element;
        }
    }
    
   

}
