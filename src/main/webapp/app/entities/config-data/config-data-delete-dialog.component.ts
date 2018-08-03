import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ConfigData } from './config-data.model';
import { ConfigDataPopupService } from './config-data-popup.service';
import { ConfigDataService } from './config-data.service';

@Component({
    selector: 'jhi-config-data-delete-dialog',
    templateUrl: './config-data-delete-dialog.component.html'
})
export class ConfigDataDeleteDialogComponent {

    configData: ConfigData;

    constructor(
        private configDataService: ConfigDataService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.configDataService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'configDataListModification',
                content: 'Deleted an configData'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-config-data-delete-popup',
    template: ''
})
export class ConfigDataDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private configDataPopupService: ConfigDataPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.configDataPopupService
                .open(ConfigDataDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
