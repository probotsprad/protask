import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { SubTasks } from './sub-tasks.model';
import { SubTasksService } from './sub-tasks.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-sub-tasks',
    templateUrl: './sub-tasks.component.html'
})
export class SubTasksComponent implements OnInit, OnDestroy {
subTasks: SubTasks[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private subTasksService: SubTasksService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.subTasksService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.subTasks = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.subTasksService.query().subscribe(
            (res: ResponseWrapper) => {
                this.subTasks = res.json;
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
        this.registerChangeInSubTasks();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: SubTasks) {
        return item.id;
    }
    registerChangeInSubTasks() {
        this.eventSubscriber = this.eventManager.subscribe('subTasksListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
