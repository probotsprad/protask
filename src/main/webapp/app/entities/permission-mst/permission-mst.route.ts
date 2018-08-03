import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { PermissionMstComponent } from './permission-mst.component';
import { PermissionMstDetailComponent } from './permission-mst-detail.component';
import { PermissionMstPopupComponent } from './permission-mst-dialog.component';
import { PermissionMstDeletePopupComponent } from './permission-mst-delete-dialog.component';

export const permissionMstRoute: Routes = [
    {
        path: 'permission-mst',
        component: PermissionMstComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.permissionMst.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'permission-mst/:id',
        component: PermissionMstDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.permissionMst.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const permissionMstPopupRoute: Routes = [
    {
        path: 'permission-mst-new',
        component: PermissionMstPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.permissionMst.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'permission-mst/:id/edit',
        component: PermissionMstPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.permissionMst.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'permission-mst/:id/delete',
        component: PermissionMstDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.permissionMst.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
