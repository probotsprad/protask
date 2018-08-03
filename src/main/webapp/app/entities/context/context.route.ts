import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ContextComponent } from './context.component';
import { ContextDetailComponent } from './context-detail.component';
import { ContextPopupComponent } from './context-dialog.component';
import { ContextDeletePopupComponent } from './context-delete-dialog.component';

export const contextRoute: Routes = [
    {
        path: 'context',
        component: ContextComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.context.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'context/:id',
        component: ContextDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.context.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const contextPopupRoute: Routes = [
    {
        path: 'context-new',
        component: ContextPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.context.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'context/:id/edit',
        component: ContextPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.context.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'context/:id/delete',
        component: ContextDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.context.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
