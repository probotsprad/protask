import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Context } from './context.model';
import { ContextService } from './context.service';

@Component({
    selector: 'jhi-context-detail',
    templateUrl: './context-detail.component.html'
})
export class ContextDetailComponent implements OnInit, OnDestroy {

    context: Context;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private contextService: ContextService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInContexts();
    }

    load(id) {
        this.contextService.find(id).subscribe((context) => {
            this.context = context;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInContexts() {
        this.eventSubscriber = this.eventManager.subscribe(
            'contextListModification',
            (response) => this.load(this.context.id)
        );
    }
}
