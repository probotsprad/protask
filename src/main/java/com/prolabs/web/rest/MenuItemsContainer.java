package com.prolabs.web.rest;

import java.util.List;

public class MenuItemsContainer {
	
	private  List<MenuItems> menuItems;

	public List<MenuItems> getMenuItems() {
		return menuItems;
	}

	public void setMenuItems(List<MenuItems> menuItems) {
		this.menuItems = menuItems;
	}
	
	public class MenuItems {
		
		 private String title;
		 private String permissionName;
		 private String iconClass;
	     private String linkUrl;
	     private String expandClassName;
	     private List<SubMenus> subMenus;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getPermissionName() {
			return permissionName;
		}
		public void setPermissionName(String permissionName) {
			this.permissionName = permissionName;
		}
		public String getIconClass() {
			return iconClass;
		}
		public void setIconClass(String iconClass) {
			this.iconClass = iconClass;
		}
		public String getLinkUrl() {
			return linkUrl;
		}
		public void setLinkUrl(String linkUrl) {
			this.linkUrl = linkUrl;
		}
		public String getExpandClassName() {
			return expandClassName;
		}
		public void setExpandClassName(String expandClassName) {
			this.expandClassName = expandClassName;
		}
		public List<SubMenus> getSubMenus() {
			return subMenus;
		}
		public void setSubMenus(List<SubMenus> subMenus) {
			this.subMenus = subMenus;
		}
	     
		public class SubMenus {

			private  String title;
			private String permissionName;
			private  String linkUrl;
			private boolean submenus2;
			private List<SubMenu2Items> subMenu2Items;
			
			public String getTitle() {
				return title;
			}
			public void setTitle(String title) {
				this.title = title;
			}
			public String getPermissionName() {
				return permissionName;
			}
			public void setPermissionName(String permissionName) {
				this.permissionName = permissionName;
			}
			public String getLinkUrl() {
				return linkUrl;
			}
			public void setLinkUrl(String linkUrl) {
				this.linkUrl = linkUrl;
			}
			public boolean isSubmenus2() {
				return submenus2;
			}
			public void setSubmenus2(boolean submenus2) {
				this.submenus2 = submenus2;
			}
			
			public List<SubMenu2Items> getSubMenu2Items() {
				return subMenu2Items;
			}
			public void setSubMenu2Items(List<SubMenu2Items> subMenu2Items) {
				this.subMenu2Items = subMenu2Items;
			}



			public class SubMenu2Items {
				private  String title;
				private String permissionName;
				private  String linkUrl;
				public String getTitle() {
					return title;
				}
				public void setTitle(String title) {
					this.title = title;
				}
				public String getPermissionName() {
					return permissionName;
				}
				public void setPermissionName(String permissionName) {
					this.permissionName = permissionName;
				}
				public String getLinkUrl() {
					return linkUrl;
				}
				public void setLinkUrl(String linkUrl) {
					this.linkUrl = linkUrl;
				}
				
			}

		}
	}

}
