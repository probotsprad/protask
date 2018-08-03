import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Policies } from './policies.model';
import { PoliciesService } from './policies.service';

@Component({
    selector: 'jhi-policies-detail',
    templateUrl: './policies-detail.component.html'
})
export class PoliciesDetailComponent implements OnInit, OnDestroy {

    policies: Policies;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private policiesService: PoliciesService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPolicies();
    }

    load(id) {
        this.policiesService.find(id).subscribe((policies) => {
            this.policies = policies;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPolicies() {
        this.eventSubscriber = this.eventManager.subscribe(
            'policiesListModification',
            (response) => this.load(this.policies.id)
        );
    }
}
