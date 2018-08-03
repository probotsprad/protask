import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SlackbotsSharedModule } from '../../shared';
import {
    UserMstService,
    UserMstPopupService,
    UserMstComponent,
    UserMstDetailComponent,
    UserMstDialogComponent,
    UserMstPopupComponent,
    UserMstDeletePopupComponent,
    UserMstDeleteDialogComponent,
    userMstRoute,
    userMstPopupRoute,
    UserMstResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...userMstRoute,
    ...userMstPopupRoute,
];

@NgModule({
    imports: [
        SlackbotsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        UserMstComponent,
        UserMstDetailComponent,
        UserMstDialogComponent,
        UserMstDeleteDialogComponent,
        UserMstPopupComponent,
        UserMstDeletePopupComponent,
    ],
    entryComponents: [
        UserMstComponent,
        UserMstDialogComponent,
        UserMstPopupComponent,
        UserMstDeleteDialogComponent,
        UserMstDeletePopupComponent,
    ],
    providers: [
        UserMstService,
        UserMstPopupService,
        UserMstResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SlackbotsUserMstModule {}
