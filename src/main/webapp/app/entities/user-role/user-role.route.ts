import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRoleComponent } from './user-role.component';
import { UserRoleDetailComponent } from './user-role-detail.component';
import { UserRolePopupComponent } from './user-role-dialog.component';
import { UserRoleDeletePopupComponent } from './user-role-delete-dialog.component';

@Injectable()
export class UserRoleResolvePagingParams implements Resolve<any> {

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

export const userRoleRoute: Routes = [
    {
        path: 'user-role',
        component: UserRoleComponent,
        resolve: {
            'pagingParams': UserRoleResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.userRole.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'user-role/:id',
        component: UserRoleDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.userRole.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const userRolePopupRoute: Routes = [
    {
        path: 'user-role-new',
        component: UserRolePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.userRole.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'user-role/:id/edit',
        component: UserRolePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.userRole.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'user-role/:id/delete',
        component: UserRoleDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.userRole.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
