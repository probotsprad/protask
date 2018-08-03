import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserMstComponent } from './user-mst.component';
import { UserMstDetailComponent } from './user-mst-detail.component';
import { UserMstPopupComponent } from './user-mst-dialog.component';
import { UserMstDeletePopupComponent } from './user-mst-delete-dialog.component';

@Injectable()
export class UserMstResolvePagingParams implements Resolve<any> {

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

export const userMstRoute: Routes = [
    {
        path: 'user-mst',
        component: UserMstComponent,
        resolve: {
            'pagingParams': UserMstResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.userMst.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'user-mst/:id',
        component: UserMstDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.userMst.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const userMstPopupRoute: Routes = [
    {
        path: 'user-mst-new',
        component: UserMstPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.userMst.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'user-mst/:id/edit',
        component: UserMstPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.userMst.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'user-mst/:id/delete',
        component: UserMstDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.userMst.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
