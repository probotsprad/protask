import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SlackbotsCategoryModule } from './category/category.module';
import { SlackbotsPoliciesModule } from './policies/policies.module';
import { SlackbotsContextModule } from './context/context.module';
import { SlackbotsTagsModule } from './tags/tags.module';
import { SlackbotsEmployeeModule } from './employee/employee.module';
import { SlackbotsTaskModule } from './task/task.module';
import { SlackbotsDepartmentModule } from './department/department.module';
import { SlackbotsSubTaskModule } from './sub-task/sub-task.module';
import { SlackbotsSubTasksModule } from './sub-tasks/sub-tasks.module';
import { SlackbotsConfigDataModule } from './config-data/config-data.module';
import { SlackbotsUserMstModule } from './user-mst/user-mst.module';
import { SlackbotsRoleMstModule } from './role-mst/role-mst.module';
import { SlackbotsPermissionMstModule } from './permission-mst/permission-mst.module';
import { SlackbotsUserRoleModule } from './user-role/user-role.module';
import { SlackbotsRolePermissionModule } from './role-permission/role-permission.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        SlackbotsCategoryModule,
        SlackbotsPoliciesModule,
        SlackbotsContextModule,
        SlackbotsTagsModule,
        SlackbotsEmployeeModule,
        SlackbotsTaskModule,
        SlackbotsDepartmentModule,
        SlackbotsSubTaskModule,
        SlackbotsSubTasksModule,
        SlackbotsConfigDataModule,
        SlackbotsUserMstModule,
        SlackbotsRoleMstModule,
        SlackbotsPermissionMstModule,
        SlackbotsUserRoleModule,
        SlackbotsRolePermissionModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SlackbotsEntityModule {}
