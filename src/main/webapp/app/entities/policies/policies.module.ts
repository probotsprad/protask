import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SlackbotsSharedModule } from '../../shared';
import {
    PoliciesService,
    PoliciesPopupService,
    PoliciesComponent,
    PoliciesDetailComponent,
    PoliciesDialogComponent,
    PoliciesPopupComponent,
    PoliciesDeletePopupComponent,
    PoliciesDeleteDialogComponent,
    policiesRoute,
    policiesPopupRoute,
} from './';

const ENTITY_STATES = [
    ...policiesRoute,
    ...policiesPopupRoute,
];

@NgModule({
    imports: [
        SlackbotsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        PoliciesComponent,
        PoliciesDetailComponent,
        PoliciesDialogComponent,
        PoliciesDeleteDialogComponent,
        PoliciesPopupComponent,
        PoliciesDeletePopupComponent,
    ],
    entryComponents: [
        PoliciesComponent,
        PoliciesDialogComponent,
        PoliciesPopupComponent,
        PoliciesDeleteDialogComponent,
        PoliciesDeletePopupComponent,
    ],
    providers: [
        PoliciesService,
        PoliciesPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SlackbotsPoliciesModule {}
