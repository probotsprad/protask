import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { UserRole } from './user-role.model';
import { UserRolePopupService } from './user-role-popup.service';
import { UserRoleService } from './user-role.service';

@Component({
    selector: 'jhi-user-role-delete-dialog',
    templateUrl: './user-role-delete-dialog.component.html'
})
export class UserRoleDeleteDialogComponent {

    userRole: UserRole;

    constructor(
        private userRoleService: UserRoleService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.userRoleService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'userRoleListModification',
                content: 'Deleted an userRole'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-user-role-delete-popup',
    template: ''
})
export class UserRoleDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private userRolePopupService: UserRolePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.userRolePopupService
                .open(UserRoleDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
