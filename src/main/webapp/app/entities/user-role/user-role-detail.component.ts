import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { UserRole } from './user-role.model';
import { UserRoleService } from './user-role.service';

@Component({
    selector: 'jhi-user-role-detail',
    templateUrl: './user-role-detail.component.html'
})
export class UserRoleDetailComponent implements OnInit, OnDestroy {

    userRole: UserRole;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private userRoleService: UserRoleService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInUserRoles();
    }

    load(id) {
        this.userRoleService.find(id).subscribe((userRole) => {
            this.userRole = userRole;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInUserRoles() {
        this.eventSubscriber = this.eventManager.subscribe(
            'userRoleListModification',
            (response) => this.load(this.userRole.id)
        );
    }
}
