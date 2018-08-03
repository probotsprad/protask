import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ConfigDataComponent } from './config-data.component';
import { ConfigDataDetailComponent } from './config-data-detail.component';
import { ConfigDataPopupComponent } from './config-data-dialog.component';
import { ConfigDataDeletePopupComponent } from './config-data-delete-dialog.component';

@Injectable()
export class ConfigDataResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const configDataRoute: Routes = [
    {
        path: 'config-data',
        component: ConfigDataComponent,
        resolve: {
            'pagingParams': ConfigDataResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.configData.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'config-data/:id',
        component: ConfigDataDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.configData.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const configDataPopupRoute: Routes = [
    {
        path: 'config-data-new',
        component: ConfigDataPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.configData.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'config-data/:id/edit',
        component: ConfigDataPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.configData.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'config-data/:id/delete',
        component: ConfigDataDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.configData.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
