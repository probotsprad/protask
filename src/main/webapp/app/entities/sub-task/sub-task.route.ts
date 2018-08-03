import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { SubTaskComponent } from './sub-task.component';
import { SubTaskDetailComponent } from './sub-task-detail.component';
import { SubTaskPopupComponent } from './sub-task-dialog.component';
import { SubTaskDeletePopupComponent } from './sub-task-delete-dialog.component';

export const subTaskRoute: Routes = [
    {
        path: 'sub-task',
        component: SubTaskComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.subTask.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'sub-task/:id',
        component: SubTaskDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.subTask.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const subTaskPopupRoute: Routes = [
    {
        path: 'sub-task-new',
        component: SubTaskPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.subTask.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sub-task/:id/edit',
        component: SubTaskPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.subTask.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sub-task/:id/delete',
        component: SubTaskDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'slackbotsApp.subTask.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
