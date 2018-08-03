import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { ConfigData } from './config-data.model';
import { ConfigDataService } from './config-data.service';

@Component({
    selector: 'jhi-config-data-detail',
    templateUrl: './config-data-detail.component.html'
})
export class ConfigDataDetailComponent implements OnInit, OnDestroy {

    configData: ConfigData;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private configDataService: ConfigDataService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInConfigData();
    }

    load(id) {
        this.configDataService.find(id).subscribe((configData) => {
            this.configData = configData;
        });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInConfigData() {
        this.eventSubscriber = this.eventManager.subscribe(
            'configDataListModification',
            (response) => this.load(this.configData.id)
        );
    }
}
