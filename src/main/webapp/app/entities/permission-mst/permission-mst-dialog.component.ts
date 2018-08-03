import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { PermissionMst } from './permission-mst.model';
import { PermissionMstPopupService } from './permission-mst-popup.service';
import { PermissionMstService } from './permission-mst.service';

@Component({
    selector: 'jhi-permission-mst-dialog',
    templateUrl: './permission-mst-dialog.component.html'
})
export class PermissionMstDialogComponent implements OnInit {

    permissionMst: PermissionMst;
    isSaving: boolean;
    createdOnDp: any;
    updatedOnDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private permissionMstService: PermissionMstService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.permissionMst.id !== undefined) {
            this.subscribeToSaveResponse(
                this.permissionMstService.update(this.permissionMst));
        } else {
            this.subscribeToSaveResponse(
                this.permissionMstService.create(this.permissionMst));
        }
    }

    private subscribeToSaveResponse(result: Observable<PermissionMst>) {
        result.subscribe((res: PermissionMst) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: PermissionMst) {
        this.eventManager.broadcast({ name: 'permissionMstListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-permission-mst-popup',
    template: ''
})
export class PermissionMstPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private permissionMstPopupService: PermissionMstPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.permissionMstPopupService
                    .open(PermissionMstDialogComponent as Component, params['id']);
            } else {
                this.permissionMstPopupService
                    .open(PermissionMstDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
