import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SlackbotsSharedModule } from '../../shared';
import {
    PermissionMstService,
    PermissionMstPopupService,
    PermissionMstComponent,
    PermissionMstDetailComponent,
    PermissionMstDialogComponent,
    PermissionMstPopupComponent,
    PermissionMstDeletePopupComponent,
    PermissionMstDeleteDialogComponent,
    permissionMstRoute,
    permissionMstPopupRoute,
} from './';

const ENTITY_STATES = [
    ...permissionMstRoute,
    ...permissionMstPopupRoute,
];

@NgModule({
    imports: [
        SlackbotsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        PermissionMstComponent,
        PermissionMstDetailComponent,
        PermissionMstDialogComponent,
        PermissionMstDeleteDialogComponent,
        PermissionMstPopupComponent,
        PermissionMstDeletePopupComponent,
    ],
    entryComponents: [
        PermissionMstComponent,
        PermissionMstDialogComponent,
        PermissionMstPopupComponent,
        PermissionMstDeleteDialogComponent,
        PermissionMstDeletePopupComponent,
    ],
    providers: [
        PermissionMstService,
        PermissionMstPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SlackbotsPermissionMstModule {}
