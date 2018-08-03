import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { PoliciesComponent } from './policies.component';
import { PoliciesDetailComponent } from './policies-detail.component';
import { PoliciesPopupComponent } from './policies-dialog.component';
import { PoliciesDeletePopupComponent } from './policies-delete-dialog.component';

export const policiesRoute: Routes = [
    {
        path: 'policies',
        component: PoliciesComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.policies.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'policies/:id',
        component: PoliciesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.policies.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const policiesPopupRoute: Routes = [
    {
        path: 'policies-new',
        component: PoliciesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.policies.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'policies/:id/edit',
        component: PoliciesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.policies.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'policies/:id/delete',
        component: PoliciesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.policies.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
