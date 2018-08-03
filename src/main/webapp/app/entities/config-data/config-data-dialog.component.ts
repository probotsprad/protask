import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { ConfigData } from './config-data.model';
import { ConfigDataPopupService } from './config-data-popup.service';
import { ConfigDataService } from './config-data.service';

@Component({
    selector: 'jhi-config-data-dialog',
    templateUrl: './config-data-dialog.component.html'
})
export class ConfigDataDialogComponent implements OnInit {

    configData: ConfigData;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private configDataService: ConfigDataService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.configData.id !== undefined) {
            this.subscribeToSaveResponse(
                this.configDataService.update(this.configData));
        } else {
            this.subscribeToSaveResponse(
                this.configDataService.create(this.configData));
        }
    }

    private subscribeToSaveResponse(result: Observable<ConfigData>) {
        result.subscribe((res: ConfigData) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: ConfigData) {
        this.eventManager.broadcast({ name: 'configDataListModification', content: 'OK'});
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
    selector: 'jhi-config-data-popup',
    template: ''
})
export class ConfigDataPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private configDataPopupService: ConfigDataPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.configDataPopupService
                    .open(ConfigDataDialogComponent as Component, params['id']);
            } else {
                this.configDataPopupService
                    .open(ConfigDataDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
