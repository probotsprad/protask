import { BaseEntity } from './../../shared';

export class Policies implements BaseEntity {
    constructor(
        public id?: number,
        public policyName?: string,
        public policyDescription?: string,
    ) {
    }
}
