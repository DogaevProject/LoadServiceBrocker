CREATE TABLE service_binding (
    service_binding_id character varying(512) NOT NULL,
    service_instance_id character varying(512) NOT NULL,
    application_id character varying(512) NOT NULL,
    user_name character varying(255) NOT NULL,
    user_password character varying(255) NOT NULL,
    uri character varying(255) NOT NULL
);

CREATE TABLE service_instance (
    service_instance_id character varying(512) NOT NULL,
    plan_id character varying(512) NOT NULL,
    service_id character varying(512) NOT NULL
);

ALTER TABLE ONLY service_binding
    ADD CONSTRAINT service_binding_pkey PRIMARY KEY (service_binding_id);

ALTER TABLE ONLY service_instance
    ADD CONSTRAINT service_instance_pkey PRIMARY KEY (service_instance_id);

ALTER TABLE service_binding
    ADD CONSTRAINT service_binding_fk FOREIGN KEY (service_instance_id)
	REFERENCES service_instance(service_instance_id);
--rollback drop table service_binding;
--rollback drop table service_instance;