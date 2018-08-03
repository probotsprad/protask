import { BaseEntity } from './../../shared';

export class RolePermission implements BaseEntity {
    constructor(
        public id?: number,
        public createdOn?: any,
        public createdBy?: string,
        public updatedOn?: any,
        public updatedBy?: string,
        public role?: BaseEntity,
        public prm?: BaseEntity,
    ) {
    }
}
