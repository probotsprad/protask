import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { RoleMstComponent } from './role-mst.component';
import { RoleMstDetailComponent } from './role-mst-detail.component';
import { RoleMstPopupComponent } from './role-mst-dialog.component';
import { RoleMstDeletePopupComponent } from './role-mst-delete-dialog.component';

@Injectable()
export class RoleMstResolvePagingParams implements Resolve<any> {

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

export const roleMstRoute: Routes = [
    {
        path: 'role-mst',
        component: RoleMstComponent,
        resolve: {
            'pagingParams': RoleMstResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.roleMst.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'role-mst/:id',
        component: RoleMstDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.roleMst.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const roleMstPopupRoute: Routes = [
    {
        path: 'role-mst-new',
        component: RoleMstPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.roleMst.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'role-mst/:id/edit',
        component: RoleMstPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.roleMst.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'role-mst/:id/delete',
        component: RoleMstDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.roleMst.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
