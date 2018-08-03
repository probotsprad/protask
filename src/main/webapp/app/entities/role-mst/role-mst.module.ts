import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SlackbotsSharedModule } from '../../shared';
import {
    RoleMstService,
    RoleMstPopupService,
    RoleMstComponent,
    RoleMstDetailComponent,
    RoleMstDialogComponent,
    RoleMstPopupComponent,
    RoleMstDeletePopupComponent,
    RoleMstDeleteDialogComponent,
    roleMstRoute,
    roleMstPopupRoute,
    RoleMstResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...roleMstRoute,
    ...roleMstPopupRoute,
];

@NgModule({
    imports: [
        SlackbotsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        RoleMstComponent,
        RoleMstDetailComponent,
        RoleMstDialogComponent,
        RoleMstDeleteDialogComponent,
        RoleMstPopupComponent,
        RoleMstDeletePopupComponent,
    ],
    entryComponents: [
        RoleMstComponent,
        RoleMstDialogComponent,
        RoleMstPopupComponent,
        RoleMstDeleteDialogComponent,
        RoleMstDeletePopupComponent,
    ],
    providers: [
        RoleMstService,
        RoleMstPopupService,
        RoleMstResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SlackbotsRoleMstModule {}
