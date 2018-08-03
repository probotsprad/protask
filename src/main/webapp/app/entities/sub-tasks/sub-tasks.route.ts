import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { SubTasksComponent } from './sub-tasks.component';
import { SubTasksDetailComponent } from './sub-tasks-detail.component';
import { SubTasksPopupComponent } from './sub-tasks-dialog.component';
import { SubTasksDeletePopupComponent } from './sub-tasks-delete-dialog.component';

export const subTasksRoute: Routes = [
    {
        path: 'sub-tasks',
        component: SubTasksComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.subTasks.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'sub-tasks/:id',
        component: SubTasksDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.subTasks.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const subTasksPopupRoute: Routes = [
    {
        path: 'sub-tasks-new',
        component: SubTasksPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.subTasks.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sub-tasks/:id/edit',
        component: SubTasksPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.subTasks.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sub-tasks/:id/delete',
        component: SubTasksDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.subTasks.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
