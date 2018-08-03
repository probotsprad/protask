import { BaseEntity } from './../../shared';

export class UserRole implements BaseEntity {
    constructor(
        public id?: number,
        public userId?: string,
        public createdOn?: any,
        public createdBy?: string,
        public updatedOn?: any,
        public updatedBy?: string,
        public roles?: BaseEntity[],
    ) {
    }
}
