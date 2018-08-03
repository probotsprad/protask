import { BaseEntity } from './../../shared';

export class Task implements BaseEntity {
    constructor(
        public id?: number,
        public taskName?: string,
        public taskDescription?: string,
        public startDate?: any,
        public endDate?: any,
        public remainder?: any,
        public duration?: string,
        public context?: BaseEntity,
        public policies?: BaseEntity,
        public department?: BaseEntity,
        public category?: BaseEntity,
        public tags?: BaseEntity,
    ) {
    }
}
