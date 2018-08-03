import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { PermissionMst } from './permission-mst.model';
import { PermissionMstService } from './permission-mst.service';

@Component({
    selector: 'jhi-permission-mst-detail',
    templateUrl: './permission-mst-detail.component.html'
})
export class PermissionMstDetailComponent implements OnInit, OnDestroy {

    permissionMst: PermissionMst;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private permissionMstService: PermissionMstService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPermissionMsts();
    }

    load(id) {
        this.permissionMstService.find(id).subscribe((permissionMst) => {
            this.permissionMst = permissionMst;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPermissionMsts() {
        this.eventSubscriber = this.eventManager.subscribe(
            'permissionMstListModification',
            (response) => this.load(this.permissionMst.id)
        );
    }
}
