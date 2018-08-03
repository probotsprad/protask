import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SlackbotsSharedModule } from '../../shared';
import {
    RolePermissionService,
    RolePermissionPopupService,
    RolePermissionComponent,
    RolePermissionDetailComponent,
    RolePermissionDialogComponent,
    RolePermissionPopupComponent,
    RolePermissionDeletePopupComponent,
    RolePermissionDeleteDialogComponent,
    rolePermissionRoute,
    rolePermissionPopupRoute,
    RolePermissionResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...rolePermissionRoute,
    ...rolePermissionPopupRoute,
];

@NgModule({
    imports: [
        SlackbotsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        RolePermissionComponent,
        RolePermissionDetailComponent,
        RolePermissionDialogComponent,
        RolePermissionDeleteDialogComponent,
        RolePermissionPopupComponent,
        RolePermissionDeletePopupComponent,
    ],
    entryComponents: [
        RolePermissionComponent,
        RolePermissionDialogComponent,
        RolePermissionPopupComponent,
        RolePermissionDeleteDialogComponent,
        RolePermissionDeletePopupComponent,
    ],
    providers: [
        RolePermissionService,
        RolePermissionPopupService,
        RolePermissionResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SlackbotsRolePermissionModule {}
