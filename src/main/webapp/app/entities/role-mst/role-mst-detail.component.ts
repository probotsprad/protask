import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { RoleMst } from './role-mst.model';
import { RoleMstService } from './role-mst.service';

@Component({
    selector: 'jhi-role-mst-detail',
    templateUrl: './role-mst-detail.component.html'
})
export class RoleMstDetailComponent implements OnInit, OnDestroy {

    roleMst: RoleMst;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private roleMstService: RoleMstService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInRoleMsts();
    }

    load(id) {
        this.roleMstService.find(id).subscribe((roleMst) => {
            this.roleMst = roleMst;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInRoleMsts() {
        this.eventSubscriber = this.eventManager.subscribe(
            'roleMstListModification',
            (response) => this.load(this.roleMst.id)
        );
    }
}
