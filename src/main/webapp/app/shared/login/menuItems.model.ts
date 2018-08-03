import { SubMenus } from './submenus.model'; 
export class MenuItems {

    constructor(
       
        public title?: string,
        public permissionName?: string,
        public iconClass?: string,
        public linkUrl?: any,
        public expandClassName?: string,
        public subMenus?: SubMenus[]
       
    ) {
    }
}
