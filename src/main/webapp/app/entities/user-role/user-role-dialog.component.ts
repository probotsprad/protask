import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { UserRole } from './user-role.model';
import { UserRolePopupService } from './user-role-popup.service';
import { UserRoleService } from './user-role.service';
import { RoleMst, RoleMstService } from '../role-mst';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-user-role-dialog',
    templateUrl: './user-role-dialog.component.html'
})
export class UserRoleDialogComponent implements OnInit {

    userRole: UserRole;
    isSaving: boolean;

    rolemsts: RoleMst[];
    createdOnDp: any;
    updatedOnDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private userRoleService: UserRoleService,
        private roleMstService: RoleMstService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.roleMstService.query()
            .subscribe((res: ResponseWrapper) => { this.rolemsts = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.userRole.id !== undefined) {
            this.subscribeToSaveResponse(
                this.userRoleService.update(this.userRole));
        } else {
            this.subscribeToSaveResponse(
                this.userRoleService.create(this.userRole));
        }
    }

    private subscribeToSaveResponse(result: Observable<UserRole>) {
        result.subscribe((res: UserRole) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: UserRole) {
        this.eventManager.broadcast({ name: 'userRoleListModification', content: 'OK'});
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
    selector: 'jhi-user-role-popup',
    template: ''
})
export class UserRolePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private userRolePopupService: UserRolePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.userRolePopupService
                    .open(UserRoleDialogComponent as Component, params['id']);
            } else {
                this.userRolePopupService
                    .open(UserRoleDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
