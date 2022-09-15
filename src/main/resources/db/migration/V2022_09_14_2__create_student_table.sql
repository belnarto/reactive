CREATE TABLE IF NOT EXISTS PUBLIC.STUDENT_ENTITY (
                                    ID BIGINT NOT NULL DEFAULT nextval('public.default_seq'::regclass),
                                    NAME CHARACTER VARYING(255),
                                    ADDRESS CHARACTER VARYING(255),
                                    CONSTRAINT CONSTRAINT_F PRIMARY KEY (ID)
);