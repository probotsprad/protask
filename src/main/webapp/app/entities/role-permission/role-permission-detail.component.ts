import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { RolePermission } from './role-permission.model';
import { RolePermissionService } from './role-permission.service';

@Component({
    selector: 'jhi-role-permission-detail',
    templateUrl: './role-permission-detail.component.html'
})
export class RolePermissionDetailComponent implements OnInit, OnDestroy {

    rolePermission: RolePermission;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private rolePermissionService: RolePermissionService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInRolePermissions();
    }

    load(id) {
        this.rolePermissionService.find(id).subscribe((rolePermission) => {
            this.rolePermission = rolePermission;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInRolePermissions() {
        this.eventSubscriber = this.eventManager.subscribe(
            'rolePermissionListModification',
            (response) => this.load(this.rolePermission.id)
        );
    }
}
