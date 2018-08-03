import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SlackbotsSharedModule } from '../../shared';
import {
    DepartmentService,
    DepartmentPopupService,
    DepartmentComponent,
    DepartmentDetailComponent,
    DepartmentDialogComponent,
    DepartmentPopupComponent,
    DepartmentDeletePopupComponent,
    DepartmentDeleteDialogComponent,
    departmentRoute,
    departmentPopupRoute,
} from './';

const ENTITY_STATES = [
    ...departmentRoute,
    ...departmentPopupRoute,
];

@NgModule({
    imports: [
        SlackbotsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        DepartmentComponent,
        DepartmentDetailComponent,
        DepartmentDialogComponent,
        DepartmentDeleteDialogComponent,
        DepartmentPopupComponent,
        DepartmentDeletePopupComponent,
    ],
    entryComponents: [
        DepartmentComponent,
        DepartmentDialogComponent,
        DepartmentPopupComponent,
        DepartmentDeleteDialogComponent,
        DepartmentDeletePopupComponent,
    ],
    providers: [
        DepartmentService,
        DepartmentPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SlackbotsDepartmentModule {}
