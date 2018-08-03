import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { RolePermission } from './role-permission.model';
import { RolePermissionPopupService } from './role-permission-popup.service';
import { RolePermissionService } from './role-permission.service';

@Component({
    selector: 'jhi-role-permission-delete-dialog',
    templateUrl: './role-permission-delete-dialog.component.html'
})
export class RolePermissionDeleteDialogComponent {

    rolePermission: RolePermission;

    constructor(
        private rolePermissionService: RolePermissionService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.rolePermissionService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'rolePermissionListModification',
                content: 'Deleted an rolePermission'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-role-permission-delete-popup',
    template: ''
})
export class RolePermissionDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private rolePermissionPopupService: RolePermissionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.rolePermissionPopupService
                .open(RolePermissionDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
