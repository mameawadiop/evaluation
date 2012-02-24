
    create table EVAL_ADHOC_EVALUATEES (
        GROUP_ID bigint not null,
        USER_ID varchar(255) not null,
        EVALUATEES_INDEX integer not null,
        primary key (GROUP_ID, EVALUATEES_INDEX)
    );

    create table EVAL_ADHOC_GROUP (
        ID bigint not null auto_increment,
        LAST_MODIFIED datetime not null,
        OWNER varchar(255) not null,
        TITLE varchar(255),
        primary key (ID)
    );

    create table EVAL_ADHOC_PARTICIPANTS (
        GROUP_ID bigint not null,
        USER_ID varchar(255) not null,
        PARTICIPANTS_INDEX integer not null,
        primary key (GROUP_ID, PARTICIPANTS_INDEX)
    );

    create table EVAL_ADHOC_USER (
        ID bigint not null auto_increment,
        LAST_MODIFIED datetime not null,
        OWNER varchar(255) not null,
        EMAIL varchar(255) not null unique,
        USER_TYPE varchar(255),
        USERNAME varchar(255) unique,
        DISPLAY_NAME varchar(255),
        primary key (ID)
    );

    create table EVAL_ADMIN (
        ID bigint not null auto_increment,
        USER_ID varchar(255) not null,
        ASSIGN_DATE datetime not null,
        ASSIGNOR_USER_ID varchar(255) not null,
        primary key (ID)
    );

    create table EVAL_ANSWER (
        ID bigint not null auto_increment,
        LAST_MODIFIED datetime not null,
        TEMPLATEITEM_FK bigint not null,
        ITEM_FK bigint,
        RESPONSE_FK bigint not null,
        TEXT_ANSWER text,
        NUM_ANSWER integer,
        MULTI_ANSWER_CODE varchar(255),
        ASSOCIATED_ID varchar(255),
        ASSOCIATED_TYPE varchar(255),
        COMMENT_ANSWER text,
        primary key (ID)
    );

    create table EVAL_ASSIGN_GROUP (
        ID bigint not null auto_increment,
        EID varchar(255),
        LAST_MODIFIED datetime not null,
        OWNER varchar(255) not null,
        group_id varchar(255) not null,
        group_type varchar(255) not null,
        INSTRUCTOR_APPROVAL bit not null,
        INSTRUCTORS_VIEW_RESULTS bit not null,
        INSTRUCTORS_VIEW_ALL_RESULTS bit,
        STUDENTS_VIEW_RESULTS bit not null,
        EVALUATION_FK bigint not null,
        NODE_ID varchar(255),
        SELECTION_SETTINGS text,
        primary key (ID)
    );

    create table EVAL_ASSIGN_HIERARCHY (
        ID bigint not null auto_increment,
        LAST_MODIFIED datetime not null,
        OWNER varchar(255) not null,
        NODE_ID varchar(255) not null,
        INSTRUCTOR_APPROVAL bit not null,
        INSTRUCTORS_VIEW_RESULTS bit not null,
        INSTRUCTORS_VIEW_ALL_RESULTS bit,
        STUDENTS_VIEW_RESULTS bit not null,
        EVALUATION_FK bigint not null,
        primary key (ID)
    );

    create table EVAL_ASSIGN_USER (
        ID bigint not null auto_increment,
        EID varchar(255),
        LAST_MODIFIED datetime not null,
        OWNER varchar(255) not null,
        USER_ID varchar(255) not null,
        GROUP_ID varchar(255) not null,
        ASSIGN_TYPE varchar(255) not null,
        ASSIGN_STATUS varchar(255) not null,
        LIST_ORDER integer not null,
        AVAILABLE_EMAIL_SENT datetime,
        REMINDER_EMAIL_SENT datetime,
        COMPLETED_DATE datetime,
        ASSIGN_GROUP_ID bigint,
        EVALUATION_FK bigint not null,
        primary key (ID),
        unique (USER_ID, GROUP_ID, ASSIGN_TYPE, EVALUATION_FK)
    );

    create table EVAL_CONFIG (
        ID bigint not null auto_increment,
        LAST_MODIFIED datetime not null,
        NAME varchar(255) not null unique,
        VALUE varchar(255) not null,
        primary key (ID)
    );

    create table EVAL_EMAIL_PROCESSING_QUEUE (
        ID bigint not null auto_increment,
        EAU_ID bigint,
        USER_ID varchar(255),
        GROUP_ID varchar(255) not null,
        EMAIL_TEMPLATE_ID bigint,
        EVALUATION_ID bigint,
        RESPONSE_ID bigint,
        EVAL_DUE_DATE datetime,
        PROCESSING_STATUS tinyint,
        primary key (ID)
    );

    create table EVAL_EMAIL_TEMPLATE (
        ID bigint not null auto_increment,
        LAST_MODIFIED datetime not null,
        OWNER varchar(255) not null,
        TEMPLATE_TYPE varchar(255) not null,
        SUBJECT text not null,
        MESSAGE text not null,
        defaultType varchar(255),
        EID varchar(255),
        primary key (ID)
    );

    create table EVAL_EVALUATION (
        ID bigint not null auto_increment,
        EID varchar(255),
        LAST_MODIFIED datetime not null,
        EVAL_TYPE varchar(255) default 'Evaluation' not null,
        OWNER varchar(255) not null,
        TITLE varchar(255) not null,
        INSTRUCTIONS text,
        START_DATE datetime not null,
        DUE_DATE datetime,
        STOP_DATE datetime,
        VIEW_DATE datetime,
        STUDENT_VIEW_RESULTS bit,
        INSTRUCTOR_VIEW_RESULTS bit,
        INSTRUCTOR_VIEW_ALL_RESULTS bit default null,
        STUDENTS_DATE datetime,
        INSTRUCTORS_DATE datetime,
        STATE varchar(255) not null,
        INSTRUCTOR_OPT varchar(255),
        REMINDER_DAYS integer not null,
        REMINDER_FROM_EMAIL varchar(255),
        TERM_ID varchar(255),
        AVAILABLE_EMAIL_SENT bit default 0,
        AVAILABLE_EMAIL_TEMPLATE_FK bigint,
        REMINDER_EMAIL_TEMPLATE_FK bigint,
        TEMPLATE_FK bigint not null,
        RESULTS_SHARING varchar(255) default 'visible' not null,
        BLANK_RESPONSES_ALLOWED bit,
        MODIFY_RESPONSES_ALLOWED bit,
        ALL_ROLES_PARTICIPATE bit,
        UNREGISTERED_ALLOWED bit,
        LOCKED bit,
        AUTH_CONTROL varchar(255),
        EVAL_CATEGORY varchar(255),
        AUTO_USE_TAG varchar(255),
        AUTO_USE_INSERTION varchar(255),
        SELECTION_SETTINGS text,
        EMAIL_OPEN_NOTIFICATION bit,
        REMINDER_STATUS varchar(255),
        LOCAL_SELECTOR varchar(255),
        primary key (ID)
    );

    create table EVAL_GROUPNODES (
        ID bigint not null auto_increment,
        LAST_MODIFIED datetime not null,
        NODE_ID varchar(255) not null,
        primary key (ID)
    );

    create table EVAL_GROUPNODES_GROUPS (
        ID bigint not null,
        GROUPS varchar(255) not null,
        GROUPS_INDEX integer not null,
        primary key (ID, GROUPS_INDEX)
    );

    create table EVAL_ITEM (
        ID bigint not null auto_increment,
        EID varchar(255),
        LAST_MODIFIED datetime not null,
        OWNER varchar(255) not null,
        ITEM_TEXT text not null,
        DESCRIPTION text,
        SHARING varchar(255) not null,
        CLASSIFICATION varchar(255) not null,
        EXPERT bit not null,
        EXPERT_DESCRIPTION text,
        SCALE_FK bigint,
        USES_NA bit,
        USES_COMMENT bit,
        DISPLAY_ROWS integer,
        SCALE_DISPLAY_SETTING varchar(255),
        COMPULSORY bit,
        CATEGORY varchar(255),
        LOCKED bit,
        COPY_OF bigint,
        HIDDEN bit,
        AUTO_USE_TAG varchar(255),
        IG_ITEM_ID bigint,
        primary key (ID)
    );

    create table EVAL_ITEMGROUP (
        ID bigint not null auto_increment,
        LAST_MODIFIED datetime,
        OWNER varchar(255) not null,
        type varchar(255) not null,
        title varchar(80) not null,
        description text,
        EXPERT bit,
        GROUP_PARENT_FK bigint,
        primary key (ID)
    );

    create table EVAL_LOCK (
        ID bigint not null auto_increment,
        LAST_MODIFIED datetime not null,
        NAME varchar(255) not null unique,
        HOLDER varchar(255) not null,
        primary key (ID)
    );

    create table EVAL_RESPONSE (
        ID bigint not null auto_increment,
        LAST_MODIFIED datetime not null,
        OWNER varchar(255) not null,
        GROUP_ID varchar(255) not null,
        START_TIME datetime not null,
        COMMENT_RESPONSE text,
        SELECTIONS_CODE text,
        END_TIME datetime,
        EVALUATION_FK bigint not null,
        primary key (ID),
        unique (OWNER, GROUP_ID, EVALUATION_FK)
    );

    create table EVAL_SCALE (
        ID bigint not null auto_increment,
        EID varchar(255),
        LAST_MODIFIED datetime not null,
        OWNER varchar(255) not null,
        TITLE varchar(255) not null,
        SCALE_MODE varchar(255) not null,
        SHARING varchar(255) not null,
        EXPERT bit not null,
        EXPERT_DESCRIPTION text,
        IDEAL varchar(255),
        LOCKED bit,
        COPY_OF bigint,
        HIDDEN bit,
        primary key (ID)
    );

    create table EVAL_SCALE_OPTIONS (
        ID bigint not null,
        SCALE_OPTION varchar(255) not null,
        SCALE_OPTION_INDEX integer not null,
        primary key (ID, SCALE_OPTION_INDEX)
    );

    create table EVAL_TAGS (
        ID bigint not null auto_increment,
        LAST_MODIFIED datetime,
        TAG varchar(255) not null,
        ENTITY_TYPE varchar(255) not null,
        ENTITY_ID varchar(255) not null,
        primary key (ID)
    );

    create table EVAL_TAGS_META (
        ID bigint not null auto_increment,
        LAST_MODIFIED datetime,
        OWNER varchar(255) not null,
        TAG varchar(255) not null,
        TITLE varchar(255),
        DESCRIPTION text,
        primary key (ID)
    );

    create table EVAL_TEMPLATE (
        ID bigint not null auto_increment,
        EID varchar(255),
        LAST_MODIFIED datetime not null,
        OWNER varchar(255) not null,
        TYPE varchar(255) not null,
        TITLE varchar(255) not null,
        DESCRIPTION text,
        SHARING varchar(255) not null,
        EXPERT bit not null,
        expertDescription text,
        LOCKED bit,
        COPY_OF bigint,
        HIDDEN bit,
        AUTO_USE_TAG varchar(255),
        primary key (ID)
    );

    create table EVAL_TEMPLATEITEM (
        ID bigint not null auto_increment,
        EID varchar(255),
        LAST_MODIFIED datetime not null,
        OWNER varchar(255) not null,
        template_id bigint not null,
        item_id bigint not null,
        DISPLAY_ORDER integer not null,
        ITEM_CATEGORY varchar(255) not null,
        HIERARCHY_LEVEL varchar(255) not null,
        HIERARCHY_NODE_ID varchar(255) not null,
        DISPLAY_ROWS integer,
        SCALE_DISPLAY_SETTING varchar(255),
        USES_NA bit,
        USES_COMMENT bit,
        COMPULSORY bit,
        BLOCK_PARENT bit,
        BLOCK_ID bigint,
        RESULTS_SHARING varchar(255),
        COPY_OF bigint,
        HIDDEN bit,
        AUTO_USE_TAG varchar(255),
        AUTO_USE_INSERT_TAG varchar(255),
        primary key (ID)
    );

    create table EVAL_TRANSLATION (
        ID bigint not null auto_increment,
        LAST_MODIFIED datetime,
        LANGUAGE_CODE varchar(255) not null,
        OBJECT_CLASS varchar(255) not null,
        OBJECT_ID varchar(255) not null,
        FIELD_NAME varchar(255) not null,
        TRANSLATION text not null,
        primary key (ID)
    );

    alter table EVAL_ADHOC_EVALUATEES 
        add index FK91C74B304BC805E4 (GROUP_ID), 
        add constraint FK91C74B304BC805E4 
        foreign key (GROUP_ID) 
        references EVAL_ADHOC_GROUP (ID);

    create index eval_ahgroup_owner on EVAL_ADHOC_GROUP (OWNER);

    alter table EVAL_ADHOC_PARTICIPANTS 
        add index FKAF16E1894BC805E4 (GROUP_ID), 
        add constraint FKAF16E1894BC805E4 
        foreign key (GROUP_ID) 
        references EVAL_ADHOC_GROUP (ID);

    create index eval_ahuser_type on EVAL_ADHOC_USER (USER_TYPE);

    create index eval_eval_admin_user_id on EVAL_ADMIN (USER_ID);

    create index eval_answer_num on EVAL_ANSWER (NUM_ANSWER);

    alter table EVAL_ANSWER 
        add index ANSWER_RESPONSE_FKC (RESPONSE_FK), 
        add constraint ANSWER_RESPONSE_FKC 
        foreign key (RESPONSE_FK) 
        references EVAL_RESPONSE (ID);

    alter table EVAL_ANSWER 
        add index ANSWER_ITEM_FKC (ITEM_FK), 
        add constraint ANSWER_ITEM_FKC 
        foreign key (ITEM_FK) 
        references EVAL_ITEM (ID);

    alter table EVAL_ANSWER 
        add index ANSWER_TEMPLATEITEM_FKC (TEMPLATEITEM_FK), 
        add constraint ANSWER_TEMPLATEITEM_FKC 
        foreign key (TEMPLATEITEM_FK) 
        references EVAL_TEMPLATEITEM (ID);

    create index eval_assigngroup_eid on EVAL_ASSIGN_GROUP (EID);

    create index eval_assign_group_nodeid on EVAL_ASSIGN_GROUP (NODE_ID);

    create index eval_assign_groupid on EVAL_ASSIGN_GROUP (group_id);

    alter table EVAL_ASSIGN_GROUP 
        add index ASSIGN_COURSE_EVALUATION_FKC (EVALUATION_FK), 
        add constraint ASSIGN_COURSE_EVALUATION_FKC 
        foreign key (EVALUATION_FK) 
        references EVAL_EVALUATION (ID);

    create index eval_assign_hier_nodeid on EVAL_ASSIGN_HIERARCHY (NODE_ID);

    alter table EVAL_ASSIGN_HIERARCHY 
        add index ASSIGN_HIER_EVALUATION_FKC (EVALUATION_FK), 
        add constraint ASSIGN_HIER_EVALUATION_FKC 
        foreign key (EVALUATION_FK) 
        references EVAL_EVALUATION (ID);

    create index eval_asgnuser_userid on EVAL_ASSIGN_USER (USER_ID);

    create index eval_asgnuser_eid on EVAL_ASSIGN_USER (EID);

    create index eval_asgnuser_reminderSent on EVAL_ASSIGN_USER (REMINDER_EMAIL_SENT);

    create index eval_asgnuser_status on EVAL_ASSIGN_USER (ASSIGN_STATUS);

    create index eval_asgnuser_groupid on EVAL_ASSIGN_USER (GROUP_ID);

    create index eval_asgnuser_type on EVAL_ASSIGN_USER (ASSIGN_TYPE);

    create index eval_asgnuser_completedDate on EVAL_ASSIGN_USER (COMPLETED_DATE);

    create index eval_asgnuser_availableSent on EVAL_ASSIGN_USER (AVAILABLE_EMAIL_SENT);

    alter table EVAL_ASSIGN_USER 
        add index ASSIGN_USER_EVALUATION_FKC (EVALUATION_FK), 
        add constraint ASSIGN_USER_EVALUATION_FKC 
        foreign key (EVALUATION_FK) 
        references EVAL_EVALUATION (ID);

    create index eval_config_name on EVAL_CONFIG (NAME);

    create index eval_user_temp_map on EVAL_EMAIL_PROCESSING_QUEUE (USER_ID, EMAIL_TEMPLATE_ID);

    create index eval_emailq_duedate on EVAL_EMAIL_PROCESSING_QUEUE (EVAL_DUE_DATE);

    create index eval_emailq_userid on EVAL_EMAIL_PROCESSING_QUEUE (USER_ID);

    create index eval_emailq_id on EVAL_EMAIL_PROCESSING_QUEUE (EAU_ID, EMAIL_TEMPLATE_ID);

    create index eval_emailq_evalid on EVAL_EMAIL_PROCESSING_QUEUE (EVALUATION_ID);

    create index eval_templ_owner on EVAL_EMAIL_TEMPLATE (OWNER);

    create index eval_templ_type on EVAL_EMAIL_TEMPLATE (TEMPLATE_TYPE);

    create index eval_templ_eid on EVAL_EMAIL_TEMPLATE (EID);

    create index eval_eval_state on EVAL_EVALUATION (STATE);

    create index eval_eval_viewdate on EVAL_EVALUATION (VIEW_DATE);

    create index eval_eval_category on EVAL_EVALUATION (EVAL_CATEGORY);

    create index eval_eval_term on EVAL_EVALUATION (TERM_ID);

    create index eval_eval_type on EVAL_EVALUATION (EVAL_TYPE);

    create index eval_eval_owner on EVAL_EVALUATION (OWNER);

    create index eval_eval_duedate on EVAL_EVALUATION (DUE_DATE);

    create index eval_eval_startdate on EVAL_EVALUATION (START_DATE);

    create index eval_evaluation_eid on EVAL_EVALUATION (EID);

    alter table EVAL_EVALUATION 
        add index EVALUATION_REMINDER_EMAIL_TEMP (REMINDER_EMAIL_TEMPLATE_FK), 
        add constraint EVALUATION_REMINDER_EMAIL_TEMP 
        foreign key (REMINDER_EMAIL_TEMPLATE_FK) 
        references EVAL_EMAIL_TEMPLATE (ID);

    alter table EVAL_EVALUATION 
        add index EVALUATION_AVAILABLE_EMAIL_TEM (AVAILABLE_EMAIL_TEMPLATE_FK), 
        add constraint EVALUATION_AVAILABLE_EMAIL_TEM 
        foreign key (AVAILABLE_EMAIL_TEMPLATE_FK) 
        references EVAL_EMAIL_TEMPLATE (ID);

    alter table EVAL_EVALUATION 
        add index EVALUATION_TEMPLATE_FKC (TEMPLATE_FK), 
        add constraint EVALUATION_TEMPLATE_FKC 
        foreign key (TEMPLATE_FK) 
        references EVAL_TEMPLATE (ID);

    create index eval_group_nodeid on EVAL_GROUPNODES (NODE_ID);

    alter table EVAL_GROUPNODES_GROUPS 
        add index FK2248663E2E2E0ED0 (ID), 
        add constraint FK2248663E2E2E0ED0 
        foreign key (ID) 
        references EVAL_GROUPNODES (ID);

    create index eval_item_owner on EVAL_ITEM (OWNER);

    create index eval_item_sharing on EVAL_ITEM (SHARING);

    create index eval_item_eid on EVAL_ITEM (EID);

    create index eval_item_expert on EVAL_ITEM (EXPERT);

    alter table EVAL_ITEM 
        add index ITEM_SCALE_FKC (SCALE_FK), 
        add constraint ITEM_SCALE_FKC 
        foreign key (SCALE_FK) 
        references EVAL_SCALE (ID);

    alter table EVAL_ITEM 
        add index FKD19984D6F71AFC2F (IG_ITEM_ID), 
        add constraint FKD19984D6F71AFC2F 
        foreign key (IG_ITEM_ID) 
        references EVAL_ITEMGROUP (ID);

    create index eval_itemgroup_owner on EVAL_ITEMGROUP (OWNER);

    create index eval_itemgroup_type on EVAL_ITEMGROUP (type);

    create index eval_itemgroup_expert on EVAL_ITEMGROUP (EXPERT);

    alter table EVAL_ITEMGROUP 
        add index ITEM_GROUP_PARENT_FKC (GROUP_PARENT_FK), 
        add constraint ITEM_GROUP_PARENT_FKC 
        foreign key (GROUP_PARENT_FK) 
        references EVAL_ITEMGROUP (ID);

    create index eval_lock_name on EVAL_LOCK (NAME);

    create index eval_response_groupid on EVAL_RESPONSE (GROUP_ID);

    create index eval_response_owner on EVAL_RESPONSE (OWNER);

    alter table EVAL_RESPONSE 
        add index RESPONSE_EVALUATION_FKC (EVALUATION_FK), 
        add constraint RESPONSE_EVALUATION_FKC 
        foreign key (EVALUATION_FK) 
        references EVAL_EVALUATION (ID);

    create index eval_scale_owner on EVAL_SCALE (OWNER);

    create index eval_scale_mode on EVAL_SCALE (SCALE_MODE);

    create index eval_scale_sharing on EVAL_SCALE (SHARING);

    create index eval_scale_eid on EVAL_SCALE (EID);

    alter table EVAL_SCALE_OPTIONS 
        add index FKE8093A06DEE55A42 (ID), 
        add constraint FKE8093A06DEE55A42 
        foreign key (ID) 
        references EVAL_SCALE (ID);

    create index eval_tags_tag on EVAL_TAGS (TAG);

    create index eval_tagsmeta_owner on EVAL_TAGS_META (OWNER);

    create index eval_tagsmeta_tag on EVAL_TAGS_META (TAG);

    create index eval_template_sharing on EVAL_TEMPLATE (SHARING);

    create index eval_template_eid on EVAL_TEMPLATE (EID);

    create index eval_template_owner on EVAL_TEMPLATE (OWNER);

    create index eval_templateitem_blockid on EVAL_TEMPLATEITEM (BLOCK_ID);

    create index eval_templateitem_eid on EVAL_TEMPLATEITEM (EID);

    create index eval_templateitem_owner on EVAL_TEMPLATEITEM (OWNER);

    alter table EVAL_TEMPLATEITEM 
        add index FK35F30150B6DB815D (item_id), 
        add constraint FK35F30150B6DB815D 
        foreign key (item_id) 
        references EVAL_ITEM (ID);

    alter table EVAL_TEMPLATEITEM 
        add index FK35F30150EDECBA7D (template_id), 
        add constraint FK35F30150EDECBA7D 
        foreign key (template_id) 
        references EVAL_TEMPLATE (ID);

    create index eval_trans_field on EVAL_TRANSLATION (FIELD_NAME);

    create index eval_trans_langcode on EVAL_TRANSLATION (LANGUAGE_CODE);

    create index eval_trans_class on EVAL_TRANSLATION (OBJECT_CLASS);

    create index eval_trans_objectid on EVAL_TRANSLATION (OBJECT_ID);
