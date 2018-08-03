import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { PermissionMst } from './permission-mst.model';
import { PermissionMstService } from './permission-mst.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-permission-mst',
    templateUrl: './permission-mst.component.html'
})
export class PermissionMstComponent implements OnInit, OnDestroy {
permissionMsts: PermissionMst[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private permissionMstService: PermissionMstService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.permissionMstService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.permissionMsts = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.permissionMstService.query().subscribe(
            (res: ResponseWrapper) => {
                this.permissionMsts = res.json;
                this.currentSearch = '';
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInPermissionMsts();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: PermissionMst) {
        return item.id;
    }
    registerChangeInPermissionMsts() {
        this.eventSubscriber = this.eventManager.subscribe('permissionMstListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
