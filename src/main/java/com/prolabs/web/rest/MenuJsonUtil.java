package com.prolabs.web.rest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.prolabs.domain.PermissionMst;
import com.prolabs.domain.RoleMst;
import com.prolabs.domain.UserMst;
import com.prolabs.web.rest.MenuItemsContainer.MenuItems;

public class MenuJsonUtil {

	private static MenuItemsContainer menuItemsContainer = null;
	private static Gson gson = new Gson();
	/*static  {
		if( menuItemsContainer == null ) {
			try {

				BufferedReader br = new BufferedReader(new FileReader(AppConstants.MENU_JSON_FILE));
				menuItemsContainer = gson.fromJson(br, MenuItemsContainer.class);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}*/

	public static MenuItemsContainer buildMenuJsonForAdmin() {
		MenuItemsContainer menuItemContainerNew = new  MenuItemsContainer();
		List<MenuItems> menuItemListNew = new ArrayList<MenuItems>();
		List<MenuItems> menuItems = menuItemsContainer.getMenuItems();

		menuItems.forEach(menuItem->menuItemListNew.add(menuItem));
		
		menuItemContainerNew.setMenuItems(menuItemListNew);
		return menuItemContainerNew;

	}

	public static MenuItemsContainer buildMenuJson(UserMst userMst){
		MenuItemsContainer menuItemContainerNew = new  MenuItemsContainer();
		Set<MenuItems> menuItemsNew = new HashSet<MenuItems>();

		List<RoleMst> roles = userMst.getUserRoles();
		Set<PermissionMst> permissionset = new HashSet<PermissionMst>();
		roles.forEach(role->{
			List<PermissionMst> permissions = role.getUserAccessLevels();
			permissions.forEach(p->permissionset.add(p));
		});
		List<MenuItems> menuItemListNew = new ArrayList<MenuItems>(menuItemsNew);
		List<MenuItems> menuItems = menuItemsContainer.getMenuItems();
		menuItems.forEach(menuItem->{
			if("Home".equalsIgnoreCase(menuItem.getPermissionName()))
				menuItemListNew.add(menuItem);
		}); 
		
		menuItems.forEach(menuItem->{
			permissionset.forEach(p -> {
				if(p.getPrmName().equalsIgnoreCase(menuItem.getPermissionName()))
					menuItemListNew.add(menuItem);
			});

		});
		menuItemContainerNew.setMenuItems(menuItemListNew);
		return menuItemContainerNew;
	}

	public static MenuItemsContainer getMenuItemsContainer() {
		return menuItemsContainer;
	}

	public static void setMenuItemsContainer(MenuItemsContainer menuItemsContainer) {
		MenuJsonUtil.menuItemsContainer = menuItemsContainer;
	}


}
