import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { SubTasks } from './sub-tasks.model';
import { SubTasksService } from './sub-tasks.service';

@Component({
    selector: 'jhi-sub-tasks-detail',
    templateUrl: './sub-tasks-detail.component.html'
})
export class SubTasksDetailComponent implements OnInit, OnDestroy {

    subTasks: SubTasks;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private subTasksService: SubTasksService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSubTasks();
    }

    load(id) {
        this.subTasksService.find(id).subscribe((subTasks) => {
            this.subTasks = subTasks;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSubTasks() {
        this.eventSubscriber = this.eventManager.subscribe(
            'subTasksListModification',
            (response) => this.load(this.subTasks.id)
        );
    }
}
