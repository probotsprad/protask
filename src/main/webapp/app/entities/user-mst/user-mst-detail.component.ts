import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { UserMst } from './user-mst.model';
import { UserMstService } from './user-mst.service';

@Component({
    selector: 'jhi-user-mst-detail',
    templateUrl: './user-mst-detail.component.html'
})
export class UserMstDetailComponent implements OnInit, OnDestroy {

    userMst: UserMst;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private userMstService: UserMstService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInUserMsts();
    }

    load(id) {
        this.userMstService.find(id).subscribe((userMst) => {
            this.userMst = userMst;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInUserMsts() {
        this.eventSubscriber = this.eventManager.subscribe(
            'userMstListModification',
            (response) => this.load(this.userMst.id)
        );
    }
}
