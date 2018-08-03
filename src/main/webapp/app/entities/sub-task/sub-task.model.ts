import { BaseEntity } from './../../shared';

export class SubTask implements BaseEntity {
    constructor(
        public id?: number,
        public taskName?: string,
        public taskDescription?: string,
    ) {
    }
}
