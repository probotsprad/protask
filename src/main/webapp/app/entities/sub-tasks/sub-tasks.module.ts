import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SlackbotsSharedModule } from '../../shared';
import {
    SubTasksService,
    SubTasksPopupService,
    SubTasksComponent,
    SubTasksDetailComponent,
    SubTasksDialogComponent,
    SubTasksPopupComponent,
    SubTasksDeletePopupComponent,
    SubTasksDeleteDialogComponent,
    subTasksRoute,
    subTasksPopupRoute,
} from './';

const ENTITY_STATES = [
    ...subTasksRoute,
    ...subTasksPopupRoute,
];

@NgModule({
    imports: [
        SlackbotsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        SubTasksComponent,
        SubTasksDetailComponent,
        SubTasksDialogComponent,
        SubTasksDeleteDialogComponent,
        SubTasksPopupComponent,
        SubTasksDeletePopupComponent,
    ],
    entryComponents: [
        SubTasksComponent,
        SubTasksDialogComponent,
        SubTasksPopupComponent,
        SubTasksDeleteDialogComponent,
        SubTasksDeletePopupComponent,
    ],
    providers: [
        SubTasksService,
        SubTasksPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SlackbotsSubTasksModule {}
