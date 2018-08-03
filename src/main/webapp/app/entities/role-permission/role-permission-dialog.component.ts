import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { RolePermission } from './role-permission.model';
import { RolePermissionPopupService } from './role-permission-popup.service';
import { RolePermissionService } from './role-permission.service';
import { RoleMst, RoleMstService } from '../role-mst';
import { PermissionMst, PermissionMstService } from '../permission-mst';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-role-permission-dialog',
    templateUrl: './role-permission-dialog.component.html'
})
export class RolePermissionDialogComponent implements OnInit {

    rolePermission: RolePermission;
    isSaving: boolean;

    rolemsts: RoleMst[];

    permissionmsts: PermissionMst[];
    createdOnDp: any;
    updatedOnDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private rolePermissionService: RolePermissionService,
        private roleMstService: RoleMstService,
        private permissionMstService: PermissionMstService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.roleMstService.query()
            .subscribe((res: ResponseWrapper) => { this.rolemsts = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.permissionMstService.query()
            .subscribe((res: ResponseWrapper) => { this.permissionmsts = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.rolePermission.id !== undefined) {
            this.subscribeToSaveResponse(
                this.rolePermissionService.update(this.rolePermission));
        } else {
            this.subscribeToSaveResponse(
                this.rolePermissionService.create(this.rolePermission));
        }
    }

    private subscribeToSaveResponse(result: Observable<RolePermission>) {
        result.subscribe((res: RolePermission) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: RolePermission) {
        this.eventManager.broadcast({ name: 'rolePermissionListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackRoleMstById(index: number, item: RoleMst) {
        return item.id;
    }

    trackPermissionMstById(index: number, item: PermissionMst) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-role-permission-popup',
    template: ''
})
export class RolePermissionPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private rolePermissionPopupService: RolePermissionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.rolePermissionPopupService
                    .open(RolePermissionDialogComponent as Component, params['id']);
            } else {
                this.rolePermissionPopupService
                    .open(RolePermissionDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
