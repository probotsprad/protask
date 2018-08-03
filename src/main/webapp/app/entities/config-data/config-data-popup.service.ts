import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { ConfigData } from './config-data.model';
import { ConfigDataService } from './config-data.service';

@Injectable()
export class ConfigDataPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private configDataService: ConfigDataService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.configDataService.find(id).subscribe((configData) => {
                    configData.createdOn = this.datePipe
                        .transform(configData.createdOn, 'yyyy-MM-ddTHH:mm:ss');
                    configData.updatedOn = this.datePipe
                        .transform(configData.updatedOn, 'yyyy-MM-ddTHH:mm:ss');
                    this.ngbModalRef = this.configDataModalRef(component, configData);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.configDataModalRef(component, new ConfigData());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    configDataModalRef(component: Component, configData: ConfigData): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.configData = configData;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
