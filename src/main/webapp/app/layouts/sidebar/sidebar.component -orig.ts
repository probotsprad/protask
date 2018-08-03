import { Component, OnInit } from '@angular/core';

import {  LoginService, Account, AuthServerProvider, MenuItems, MenuItemsContainer } from '../../shared';

@Component({
    selector: 'app-sidebar',
    templateUrl: './sidebar.component.html',
    styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {

    isActive = false;
    showMenu = '';

    testLink = '/user-mst';
    expandClassForPeople = 'peopleManagement';
    testIconForUser =  'fa fa-user';

    mItems:  MenuItemsContainer;

     m = {
	"menuItems": [{
			"title": "Home",
			"permissionName": "Home",
			"iconClass": "fa fa-home",
			"expandClassName":"homepageManagement",
			"linkUrl": "/"
		}, {
			"title": "People Management",
			"permissionName": "People Management",
			"iconClass": "fa fa-user",
			"linkUrl": "",
		  	"expandClassName": "peopleManagement",
			"subMenus": [{
					"title": "User",
					"permissionName": "User",
					"linkUrl": "/user-mst"
				},
				{
					"title": "Role",
					"permissionName": "Role",
					"linkUrl": "/role-mst"
				},
				{
					"title": "Permission",
					"permissionName": "Permission",
					"linkUrl": "/permission-mst"
				}
			]
		},
		{
			"title": "Announcements",
			"permissionName": "Announcements",
			"iconClass": "fa fa-bullhorn",
			"linkUrl": "",
		  	"expandClassName": "announcementManagement",
			"subMenus": [{
					"title": "Announcement Type",
					"permissionName": "Announcement Type",
					"linkUrl": "/announcement-type"
				},
				{
					"title": "Announcement",
					"permissionName": "Announcement",
					"linkUrl": "/announcement"
				}
			]
		},
		{
			"title": "School Management",
			"permissionName": "School Management",
			"iconClass": "fa fa-university",
			"linkUrl": "",
		  	"expandClassName": "schoolManagement",
			"subMenus": [{
					"title": "Schools",
					"permissionName": "Schools",
					"linkUrl": "/school"
				},
				{
					"title": "School Class",
					"permissionName": "School Class",
					"linkUrl": "/school-class"
				}
			]
		},
    {
			"title": "Proof Management",
			"permissionName": "Proof Management",
			"iconClass": "fa fa-archive",
			"linkUrl": "",
		  	"expandClassName": "proofManagement",
			"subMenus": [{
					"title": "Proof Request",
					"permissionName": "Proof Request",
					"linkUrl": "/proof-request"
				},
				{
					"title": "Paper Vendor",
					"permissionName": "Paper Vendor",
					"linkUrl": "/paper-vendor"
				},
				{
					"title": "Paper",
					"permissionName": "Paper",
					"linkUrl": "/paper"
				},
				{
					"title": "Person",
					"permissionName": "Person",
					"linkUrl": "/person"
				},
				{
					"title": "Bindery",
					"permissionName": "Bindery",
					"linkUrl": "/bindery"
				},
				{
					"title": "Buyout",
					"permissionName": "Buyout",
					"linkUrl": "/buyout"
				},
				{
					"title": "Fraction",
					"permissionName": "Fraction",
					"linkUrl": "/fraction"
				},

			]
		}
	]
}

    ngOnInit() {
			this.mItems = this.m;
			//TODO
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

}
