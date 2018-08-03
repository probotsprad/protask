import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { PermissionMst } from './permission-mst.model';
import { PermissionMstPopupService } from './permission-mst-popup.service';
import { PermissionMstService } from './permission-mst.service';

@Component({
    selector: 'jhi-permission-mst-delete-dialog',
    templateUrl: './permission-mst-delete-dialog.component.html'
})
export class PermissionMstDeleteDialogComponent {

    permissionMst: PermissionMst;

    constructor(
        private permissionMstService: PermissionMstService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.permissionMstService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'permissionMstListModification',
                content: 'Deleted an permissionMst'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-permission-mst-delete-popup',
    template: ''
})
export class PermissionMstDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private permissionMstPopupService: PermissionMstPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.permissionMstPopupService
                .open(PermissionMstDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
