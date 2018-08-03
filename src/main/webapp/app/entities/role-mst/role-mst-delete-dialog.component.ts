import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { RoleMst } from './role-mst.model';
import { RoleMstPopupService } from './role-mst-popup.service';
import { RoleMstService } from './role-mst.service';

@Component({
    selector: 'jhi-role-mst-delete-dialog',
    templateUrl: './role-mst-delete-dialog.component.html'
})
export class RoleMstDeleteDialogComponent {

    roleMst: RoleMst;

    constructor(
        private roleMstService: RoleMstService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.roleMstService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'roleMstListModification',
                content: 'Deleted an roleMst'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-role-mst-delete-popup',
    template: ''
})
export class RoleMstDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private roleMstPopupService: RoleMstPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.roleMstPopupService
                .open(RoleMstDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
