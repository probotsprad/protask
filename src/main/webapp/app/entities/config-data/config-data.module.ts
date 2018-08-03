import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SlackbotsSharedModule } from '../../shared';
import {
    ConfigDataService,
    ConfigDataPopupService,
    ConfigDataComponent,
    ConfigDataDetailComponent,
    ConfigDataDialogComponent,
    ConfigDataPopupComponent,
    ConfigDataDeletePopupComponent,
    ConfigDataDeleteDialogComponent,
    configDataRoute,
    configDataPopupRoute,
    ConfigDataResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...configDataRoute,
    ...configDataPopupRoute,
];

@NgModule({
    imports: [
        SlackbotsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ConfigDataComponent,
        ConfigDataDetailComponent,
        ConfigDataDialogComponent,
        ConfigDataDeleteDialogComponent,
        ConfigDataPopupComponent,
        ConfigDataDeletePopupComponent,
    ],
    entryComponents: [
        ConfigDataComponent,
        ConfigDataDialogComponent,
        ConfigDataPopupComponent,
        ConfigDataDeleteDialogComponent,
        ConfigDataDeletePopupComponent,
    ],
    providers: [
        ConfigDataService,
        ConfigDataPopupService,
        ConfigDataResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SlackbotsConfigDataModule {}
