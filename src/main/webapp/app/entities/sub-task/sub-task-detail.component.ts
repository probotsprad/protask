import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { SubTask } from './sub-task.model';
import { SubTaskService } from './sub-task.service';

@Component({
    selector: 'jhi-sub-task-detail',
    templateUrl: './sub-task-detail.component.html'
})
export class SubTaskDetailComponent implements OnInit, OnDestroy {

    subTask: SubTask;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private subTaskService: SubTaskService,
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
        this.subTaskService.find(id).subscribe((subTask) => {
            this.subTask = subTask;
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
            'subTaskListModification',
            (response) => this.load(this.subTask.id)
        );
    }
}
