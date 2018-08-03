import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { UserMst } from './user-mst.model';
import { UserMstService } from './user-mst.service';

@Injectable()
export class UserMstPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private userMstService: UserMstService

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
                this.userMstService.find(id).subscribe((userMst) => {
                    if (userMst.createdOn) {
                        userMst.createdOn = {
                            year: userMst.createdOn.getFullYear(),
                            month: userMst.createdOn.getMonth() + 1,
                            day: userMst.createdOn.getDate()
                        };
                    }
                    if (userMst.updatedOn) {
                        userMst.updatedOn = {
                            year: userMst.updatedOn.getFullYear(),
                            month: userMst.updatedOn.getMonth() + 1,
                            day: userMst.updatedOn.getDate()
                        };
                    }
                    this.ngbModalRef = this.userMstModalRef(component, userMst);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.userMstModalRef(component, new UserMst());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    userMstModalRef(component: Component, userMst: UserMst): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.userMst = userMst;
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
