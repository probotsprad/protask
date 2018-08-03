import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { PermissionMst } from './permission-mst.model';
import { PermissionMstService } from './permission-mst.service';

@Injectable()
export class PermissionMstPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private permissionMstService: PermissionMstService

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
                this.permissionMstService.find(id).subscribe((permissionMst) => {
                    if (permissionMst.createdOn) {
                        permissionMst.createdOn = {
                            year: permissionMst.createdOn.getFullYear(),
                            month: permissionMst.createdOn.getMonth() + 1,
                            day: permissionMst.createdOn.getDate()
                        };
                    }
                    if (permissionMst.updatedOn) {
                        permissionMst.updatedOn = {
                            year: permissionMst.updatedOn.getFullYear(),
                            month: permissionMst.updatedOn.getMonth() + 1,
                            day: permissionMst.updatedOn.getDate()
                        };
                    }
                    this.ngbModalRef = this.permissionMstModalRef(component, permissionMst);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.permissionMstModalRef(component, new PermissionMst());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    permissionMstModalRef(component: Component, permissionMst: PermissionMst): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.permissionMst = permissionMst;
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
