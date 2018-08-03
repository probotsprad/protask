import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SlackbotsSharedModule } from '../../shared';
import {
    SubTaskService,
    SubTaskPopupService,
    SubTaskComponent,
    SubTaskDetailComponent,
    SubTaskDialogComponent,
    SubTaskPopupComponent,
    SubTaskDeletePopupComponent,
    SubTaskDeleteDialogComponent,
    subTaskRoute,
    subTaskPopupRoute,
} from './';

const ENTITY_STATES = [
    ...subTaskRoute,
    ...subTaskPopupRoute,
];

@NgModule({
    imports: [
        SlackbotsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        SubTaskComponent,
        SubTaskDetailComponent,
        SubTaskDialogComponent,
        SubTaskDeleteDialogComponent,
        SubTaskPopupComponent,
        SubTaskDeletePopupComponent,
    ],
    entryComponents: [
        SubTaskComponent,
        SubTaskDialogComponent,
        SubTaskPopupComponent,
        SubTaskDeleteDialogComponent,
        SubTaskDeletePopupComponent,
    ],
    providers: [
        SubTaskService,
        SubTaskPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SlackbotsSubTaskModule {}
