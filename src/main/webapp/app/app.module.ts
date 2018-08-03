import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Ng2Webstorage } from 'ng2-webstorage';

import { SlackbotsSharedModule, UserRouteAccessService } from './shared';
import { SlackbotsAppRoutingModule} from './app-routing.module';
import { SlackbotsHomeModule } from './home/home.module';
import { SlackbotsAdminModule } from './admin/admin.module';
import { SlackbotsAccountModule } from './account/account.module';
import { SlackbotsEntityModule } from './entities/entity.module';
import { customHttpProvider } from './blocks/interceptor/http.provider';
import { PaginationConfig } from './blocks/config/uib-pagination.config';

// jhipster-needle-angular-add-module-import JHipster will add new module here

import {
    JhiMainComponent,
    NavbarComponent,
    FooterComponent,
    ProfileService,
    PageRibbonComponent,
    ActiveMenuDirective,
    ErrorComponent,
    SidebarComponent
} from './layouts';

@NgModule({
    imports: [
        BrowserModule,
        SlackbotsAppRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-'}),
        SlackbotsSharedModule,
        SlackbotsHomeModule,
        SlackbotsAdminModule,
        SlackbotsAccountModule,
        SlackbotsEntityModule,
        // jhipster-needle-angular-add-module JHipster will add new module here
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        ActiveMenuDirective,
        FooterComponent,
        SidebarComponent
    ],
    providers: [
        ProfileService,
        customHttpProvider(),
        PaginationConfig,
        UserRouteAccessService
    ],
    bootstrap: [ JhiMainComponent ]
})
export class SlackbotsAppModule {}
