import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { RolePermissionComponent } from './role-permission.component';
import { RolePermissionDetailComponent } from './role-permission-detail.component';
import { RolePermissionPopupComponent } from './role-permission-dialog.component';
import { RolePermissionDeletePopupComponent } from './role-permission-delete-dialog.component';

@Injectable()
export class RolePermissionResolvePagingParams implements Resolve<any> {

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

export const rolePermissionRoute: Routes = [
    {
        path: 'role-permission',
        component: RolePermissionComponent,
        resolve: {
            'pagingParams': RolePermissionResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.rolePermission.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'role-permission/:id',
        component: RolePermissionDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.rolePermission.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const rolePermissionPopupRoute: Routes = [
    {
        path: 'role-permission-new',
        component: RolePermissionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.rolePermission.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'role-permission/:id/edit',
        component: RolePermissionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.rolePermission.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'role-permission/:id/delete',
        component: RolePermissionDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.rolePermission.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
