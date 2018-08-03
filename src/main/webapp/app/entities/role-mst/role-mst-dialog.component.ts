import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { RoleMst } from './role-mst.model';
import { RoleMstPopupService } from './role-mst-popup.service';
import { RoleMstService } from './role-mst.service';
import { UserRole, UserRoleService } from '../user-role';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-role-mst-dialog',
    templateUrl: './role-mst-dialog.component.html'
})
export class RoleMstDialogComponent implements OnInit {

    roleMst: RoleMst;
    isSaving: boolean;

    userroles: UserRole[];
    createdOnDp: any;
    updatedOnDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private roleMstService: RoleMstService,
        private userRoleService: UserRoleService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userRoleService.query()
            .subscribe((res: ResponseWrapper) => { this.userroles = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.roleMst.id !== undefined) {
            this.subscribeToSaveResponse(
                this.roleMstService.update(this.roleMst));
        } else {
            this.subscribeToSaveResponse(
                this.roleMstService.create(this.roleMst));
        }
    }

    private subscribeToSaveResponse(result: Observable<RoleMst>) {
        result.subscribe((res: RoleMst) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: RoleMst) {
        this.eventManager.broadcast({ name: 'roleMstListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackUserRoleById(index: number, item: UserRole) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-role-mst-popup',
    template: ''
})
export class RoleMstPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private roleMstPopupService: RoleMstPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.roleMstPopupService
                    .open(RoleMstDialogComponent as Component, params['id']);
            } else {
                this.roleMstPopupService
                    .open(RoleMstDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
