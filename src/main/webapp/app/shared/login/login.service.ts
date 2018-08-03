import { Injectable } from '@angular/core';
import { JhiLanguageService } from 'ng-jhipster';

import { Principal } from '../auth/principal.service';
import { AuthServerProvider } from '../auth/auth-jwt.service';

import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import { LocalStorageService } from 'ng2-webstorage';
import { MenuItems } from './menuItems.model';
import { MenuItemsContainer } from './menuItems-container-model';
import { SERVER_API_URL } from '../../app.constants';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { Subject } from 'rxjs/Subject';

import { ResponseWrapper, createRequestOption, Account } from '../../shared';

@Injectable()
export class LoginService {
	
	 private subject = new Subject<string>();
	 private resourceUrl = SERVER_API_URL + 'api/menuJson';
	 menuItemContainer: MenuItemsContainer ; 
	 pageRefresh: boolean = true;
	 private _navItemSource = new BehaviorSubject<string>('');
	 navItem$ = this._navItemSource.asObservable();
	 changeMenuJson(menuItems) {
	 	 this.localStorage.store('menu.json', menuItems);
	 	 this.pageRefresh = false;
	 	 this.menuItemContainer = menuItems;
   		 this._navItemSource.next(menuItems);
  	 }
	
    constructor(
        private languageService: JhiLanguageService,
        private principal: Principal,
        private authServerProvider: AuthServerProvider,
        private localStorage: LocalStorageService,
        private http: Http
    ) {}

    login(credentials, callback?) {
        const cb = callback || function() {};

        return new Promise((resolve, reject) => {
            this.authServerProvider.login(credentials).subscribe((data) => {
                this.principal.identity(true).then((account) => {
                	this.setLoggedInUser(account.firstName);
                    // After the login the language will be changed to
                    // the language selected by the user during his registration
                    if (account !== null) {
                        this.languageService.changeLanguage(account.langKey);
                    }
                    resolve(data);
                });
                return cb();
            }, (err) => {
                this.logout();
                reject(err);
                return cb(err);
            });
        });
    }

    loginWithToken(jwt, rememberMe) {
        return this.authServerProvider.loginWithToken(jwt, rememberMe);
    }
	
	getPageRefresh() {
		return this.pageRefresh;
	}
	
    logout() {
    	this.localStorage.clear('menu.json');
    	this._navItemSource.next('');
        this.authServerProvider.logout().subscribe();
        this.principal.authenticate(null);
        
    }
    
     getMenuJson(userLoginId: string): Observable<MenuItemsContainer> {
        
        return this.http.post(this.resourceUrl, userLoginId).map((res: Response) => {
            const jsonResponse = res.json();
            return jsonResponse;
        });
    }    
    
    getLoggedInUser(): Observable<string> {
        return this.subject.asObservable();
    }
    
    setLoggedInUser(user: string) {
    	this.subject.next(user);
    }
    
}
