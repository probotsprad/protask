import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SlackbotsSharedModule } from '../../shared';
import {
    ContextService,
    ContextPopupService,
    ContextComponent,
    ContextDetailComponent,
    ContextDialogComponent,
    ContextPopupComponent,
    ContextDeletePopupComponent,
    ContextDeleteDialogComponent,
    contextRoute,
    contextPopupRoute,
} from './';

const ENTITY_STATES = [
    ...contextRoute,
    ...contextPopupRoute,
];

@NgModule({
    imports: [
        SlackbotsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ContextComponent,
        ContextDetailComponent,
        ContextDialogComponent,
        ContextDeleteDialogComponent,
        ContextPopupComponent,
        ContextDeletePopupComponent,
    ],
    entryComponents: [
        ContextComponent,
        ContextDialogComponent,
        ContextPopupComponent,
        ContextDeleteDialogComponent,
        ContextDeletePopupComponent,
    ],
    providers: [
        ContextService,
        ContextPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SlackbotsContextModule {}
