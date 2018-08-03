import { BaseEntity } from './../../shared';

export class PermissionMst implements BaseEntity {
    constructor(
        public id?: number,
        public prmName?: string,
        public prmDesc?: string,
        public createdOn?: any,
        public createdBy?: string,
        public updatedOn?: any,
        public updatedBy?: string,
    ) {
    }
}
