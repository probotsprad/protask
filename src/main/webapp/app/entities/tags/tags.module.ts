import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SlackbotsSharedModule } from '../../shared';
import {
    TagsService,
    TagsPopupService,
    TagsComponent,
    TagsDetailComponent,
    TagsDialogComponent,
    TagsPopupComponent,
    TagsDeletePopupComponent,
    TagsDeleteDialogComponent,
    tagsRoute,
    tagsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...tagsRoute,
    ...tagsPopupRoute,
];

@NgModule({
    imports: [
        SlackbotsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        TagsComponent,
        TagsDetailComponent,
        TagsDialogComponent,
        TagsDeleteDialogComponent,
        TagsPopupComponent,
        TagsDeletePopupComponent,
    ],
    entryComponents: [
        TagsComponent,
        TagsDialogComponent,
        TagsPopupComponent,
        TagsDeleteDialogComponent,
        TagsDeletePopupComponent,
    ],
    providers: [
        TagsService,
        TagsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SlackbotsTagsModule {}
