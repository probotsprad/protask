import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { UserMst } from './user-mst.model';
import { UserMstPopupService } from './user-mst-popup.service';
import { UserMstService } from './user-mst.service';

@Component({
    selector: 'jhi-user-mst-delete-dialog',
    templateUrl: './user-mst-delete-dialog.component.html'
})
export class UserMstDeleteDialogComponent {

    userMst: UserMst;

    constructor(
        private userMstService: UserMstService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.userMstService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'userMstListModification',
                content: 'Deleted an userMst'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-user-mst-delete-popup',
    template: ''
})
export class UserMstDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private userMstPopupService: UserMstPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.userMstPopupService
                .open(UserMstDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
